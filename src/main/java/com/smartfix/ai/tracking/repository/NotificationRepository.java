package com.smartfix.ai.tracking.repository;


import com.smartfix.ai.tracking.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NotificationRepository extends JpaRepository<Notification,Long>{
 List<Notification> findByRecipientIdOrderBySentAtDesc(Long recipientId);
}
