
const API = "http://localhost:8081/api";

const table = document.getElementById("requestsTable");
const trackingResult = document.getElementById("trackingResult");

async function loadRequests() {
    table.replaceChildren();

    try {
        // Demo technician ID = 2
        const response = await fetch(`${API}/requests?technicianId=2`);

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const requests = await response.json();

        if (!Array.isArray(requests)) {
            throw new Error("Unexpected API response");
        }

        for (const request of requests) {
            const row = document.createElement("tr");

            const idCell = document.createElement("td");
            idCell.textContent = request.requestId;

            const issueCell = document.createElement("td");
            issueCell.textContent = request.description;

            const statusCell = document.createElement("td");
            statusCell.textContent = request.status;

            const actionCell = document.createElement("td");
            const select = document.createElement("select");

            ["ASSIGNED", "IN_PROGRESS", "COMPLETED"].forEach(status => {
                const option = document.createElement("option");
                option.value = status;
                option.textContent = status.replace("_", " ");
                select.appendChild(option);
            });

            select.value = request.status;

            const button = document.createElement("button");
            button.textContent = "Update";
            button.addEventListener("click", () =>
                updateStatus(request.requestId, select.value)
            );

            actionCell.append(select, button);
            row.append(idCell, issueCell, statusCell, actionCell);
            table.appendChild(row);
        }
    } catch (error) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.colSpan = 4;
        cell.textContent = `Unable to load requests: ${error.message}`;
        row.appendChild(cell);
        table.appendChild(row);
    }
}

async function updateStatus(requestId, status) {
    try {
        const response = await fetch(
            `${API}/requests/${requestId}/status`,
            {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ status })
            }
        );

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        alert("Request status updated successfully!");
        await loadRequests();
    } catch (error) {
        alert(`Update failed: ${error.message}`);
    }
}

async function trackRequest() {
    const requestId = document.getElementById("trackId").value;

    if (!requestId || Number(requestId) < 1) {
        trackingResult.textContent = "Enter a valid request ID.";
        return;
    }

    try {
        const response = await fetch(`${API}/requests/${requestId}`);

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        const request = await response.json();

        trackingResult.textContent =
            `Request #${request.requestId} | ` +
            `Issue: ${request.description} | ` +
            `Status: ${request.status}`;
    } catch (error) {
        trackingResult.textContent =
            `Unable to track request: ${error.message}`;
    }
}

document.getElementById("loadRequests")
    .addEventListener("click", loadRequests);

document.getElementById("trackButton")
    .addEventListener("click", trackRequest);
