package com.smartfix.ai.booking.service;

import com.smartfix.ai.booking.controller.BookingController.BookRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.sql.*;
import java.time.*;
import java.util.*;

@Service
public class BookingService {
  private final JdbcTemplate jdbc;
  public BookingService(JdbcTemplate jdbc) { this.jdbc = jdbc; }
  private ResponseStatusException notFound(String what) { return new ResponseStatusException(HttpStatus.NOT_FOUND, what + " not found"); }
  private ResponseStatusException conflict(String message) { return new ResponseStatusException(HttpStatus.CONFLICT, message); }

  public List<Map<String,Object>> recommended(long requestId) {
    List<Map<String,Object>> requests = jdbc.queryForList("SELECT issue_category, location FROM maintenance_requests WHERE request_id=?", requestId);
    if (requests.isEmpty()) throw notFound("Request");
    Map<String,Object> request = requests.get(0);
    String category = Objects.toString(request.get("issue_category"), "").toLowerCase(Locale.ROOT);
    String location = Objects.toString(request.get("location"), "").toLowerCase(Locale.ROOT);
    return jdbc.queryForList("""
      SELECT t.technician_id, u.name, t.specialization, t.skills, t.location, t.rating, t.workload
      FROM technicians t JOIN users u ON u.user_id=t.technician_id
      WHERE (?='' OR LOWER(t.specialization) LIKE CONCAT('%',?,'%') OR LOWER(COALESCE(t.skills,'')) LIKE CONCAT('%',?,'%'))
      ORDER BY CASE WHEN LOWER(COALESCE(t.location,''))= ? THEN 0 ELSE 1 END,
               t.rating DESC, t.workload ASC, t.technician_id ASC
      """, category, category, category, location);
  }

  public List<Map<String,Object>> slots(long technicianId) {
    if (jdbc.queryForObject("SELECT COUNT(*) FROM technicians WHERE technician_id=?", Integer.class, technicianId)==0) throw notFound("Technician");
    return jdbc.queryForList("""
      SELECT slot_id, technician_id, start_time, end_time FROM technician_availability
      WHERE technician_id=? AND is_available=TRUE AND start_time > UTC_TIMESTAMP()
      ORDER BY start_time
      """, technicianId);
  }

  @Transactional
  public Map<String,Object> book(BookRequest input) {
    // Lock the request and slot to prevent concurrent bookings for the same slot/request.
    List<Map<String,Object>> requests = jdbc.queryForList("SELECT request_id, status, technician_id FROM maintenance_requests WHERE request_id=? FOR UPDATE", input.requestId());
    if (requests.isEmpty()) throw notFound("Request");
    String requestStatus = Objects.toString(requests.get(0).get("status"));
    if (!"PENDING".equals(requestStatus)) throw conflict("Request is not pending");
    List<Map<String,Object>> slots = jdbc.queryForList("SELECT slot_id, technician_id, start_time, end_time, is_available FROM technician_availability WHERE slot_id=? FOR UPDATE", input.slotId());
    if (slots.isEmpty()) throw notFound("Slot");
    Map<String,Object> slot = slots.get(0);
    if (((Number)slot.get("technician_id")).longValue()!=input.technicianId()) throw conflict("Slot belongs to a different technician");
    if (!Boolean.TRUE.equals(slot.get("is_available"))) throw conflict("Slot is already reserved");
    LocalDateTime start = ((Timestamp)slot.get("start_time")).toLocalDateTime();
    if (!start.isAfter(LocalDateTime.now(Clock.systemUTC()))) throw conflict("Slot must be in the future");
    if (jdbc.queryForObject("SELECT COUNT(*) FROM appointments WHERE request_id=? AND status <> 'CANCELLED'", Integer.class, input.requestId())>0) throw conflict("Request already has an active booking");
    if (jdbc.queryForObject("SELECT COUNT(*) FROM appointments WHERE technician_id=? AND appointment_date=? AND appointment_time=? AND status <> 'CANCELLED'", Integer.class, input.technicianId(), java.sql.Date.valueOf(start.toLocalDate()), Time.valueOf(start.toLocalTime()))>0) throw conflict("Technician already booked at this time");
    jdbc.update("INSERT INTO appointments (request_id, technician_id, appointment_date, appointment_time, estimated_cost, status) VALUES (?,?,?,?,?,'PENDING')", input.requestId(), input.technicianId(), java.sql.Date.valueOf(start.toLocalDate()), Time.valueOf(start.toLocalTime()), input.estimatedCost());
    Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    jdbc.update("UPDATE technician_availability SET is_available=FALSE WHERE slot_id=?", input.slotId());
    jdbc.update("UPDATE maintenance_requests SET technician_id=?, status='ASSIGNED' WHERE request_id=?", input.technicianId(), input.requestId());
    return Map.of("appointmentId", id, "status", "PENDING", "requestId", input.requestId(), "technicianId", input.technicianId());
  }

  @Transactional
  public Map<String,Object> accept(long id) {
    Map<String,Object> appointment = getLocked(id);
    if (!"PENDING".equals(Objects.toString(appointment.get("status")))) throw conflict("Only pending appointments can be accepted");
    jdbc.update("UPDATE appointments SET status='ACCEPTED' WHERE appointment_id=?", id);
    return Map.of("appointmentId", id, "status", "ACCEPTED");
  }

  @Transactional
  public Map<String,Object> cancel(long id) {
    Map<String,Object> appointment = getLocked(id);
    if ("CANCELLED".equals(Objects.toString(appointment.get("status")))) throw conflict("Appointment is already cancelled");
    long technicianId = ((Number)appointment.get("technician_id")).longValue();
    long requestId = ((Number)appointment.get("request_id")).longValue();
    jdbc.update("UPDATE appointments SET status='CANCELLED' WHERE appointment_id=?", id);
    jdbc.update("UPDATE technician_availability SET is_available=TRUE WHERE technician_id=? AND start_time=?", technicianId,
        Timestamp.valueOf(LocalDateTime.of(((java.sql.Date)appointment.get("appointment_date")).toLocalDate(), ((Time)appointment.get("appointment_time")).toLocalTime())));
    jdbc.update("UPDATE maintenance_requests SET technician_id=NULL, status='PENDING' WHERE request_id=? AND status='ASSIGNED'", requestId);
    return Map.of("appointmentId", id, "status", "CANCELLED");
  }

  private Map<String,Object> getLocked(long id) {
    List<Map<String,Object>> rows = jdbc.queryForList("SELECT * FROM appointments WHERE appointment_id=? FOR UPDATE", id);
    if (rows.isEmpty()) throw notFound("Appointment");
    return rows.get(0);
  }
}
