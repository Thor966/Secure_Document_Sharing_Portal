
// get the security audit logs
const CONTEXT_PATH = '';

/*fetch(CONTEXT_PATH + 'securityLogs')
    .then(res => res.json())
    .then(data => {

        const container = document.getElementById("activityContainer");
        container.innerHTML = "";

        data.content.forEach(log => {

            const { icon, color, badge } = getActionUI(log.action);

            const div = document.createElement("div");
            div.className = "activity-item d-flex justify-content-between align-items-start";

            div.innerHTML = `
                <div>
                    <i class="fa-solid ${icon} me-2 ${color}"></i>
                    ${getMessage(log)}
                    <div class="activity-time mt-1">
                        ${formatTime(log.time)}
                    </div>
                </div>
                <span class="badge badge-security">${badge}</span>
            `;

            container.appendChild(div);
        });
    });*/
	
	
	// get the data from endpoint
	let currentPage = 0;
	const pageSize = 10;

	function loadLogs(page = 0) {

	    fetch(`${CONTEXT_PATH}securityLogs?page=${page}&size=${pageSize}`)
	        .then(res => res.json())
	        .then(data => {

				currentPage = data.currentPage;

	            renderLogs(data.content);
	            renderPagination(data);
	        });
	}

	
	// render audit logs to html
	function renderLogs(logs) {

	    const container =
	        document.getElementById("activityContainer");

	    container.innerHTML = "";

	    logs.forEach(log => {

	        const { icon, color, badge } =
	            getActionUI(log.action);

	        const div = document.createElement("div");

	        div.className =
	            "activity-item d-flex justify-content-between align-items-start";

	        div.innerHTML = `
	            <div>
	                <i class="fa-solid ${icon} me-2 ${color}"></i>
	                ${getMessage(log)}
	                <div class="activity-time mt-1">
	                    ${formatTime(log.time)}
	                </div>
	            </div>
	            <span class="badge badge-security">${badge}</span>
	        `;

	        container.appendChild(div);
	    });
	}
	
	
	// pagination controls and data
	function renderPagination(pageData) {

	    const container = document.getElementById("paginationContainer");
	    container.innerHTML = "";

	    const totalPages = pageData.totalPages;
	    const current = pageData.currentPage;

	    if (totalPages <= 1) return;

	    // Previous
	    if (!pageData.first) {
	        container.innerHTML += `
	            <button class="btn btn-outline-primary mx-1"
	                onclick="loadLogs(${current - 1})">
	                Previous
	            </button>`;
	    }

	    

	    for (let i = 0; i < pageData.totalPages; i++) {

	        container.innerHTML += `
	            <button class="btn mx-1
	                ${i === current ? 'btn-primary' : 'btn-outline-primary'}"
	                onclick="loadLogs(${i})">
	                ${i + 1}
	            </button>`;
	    }

	    // Next
	    if (!pageData.last) {
	        container.innerHTML += `
	            <button class="btn btn-outline-primary mx-1"
	                onclick="loadLogs(${current + 1})">
	                Next
	            </button>`;
	    }
	}

	
	
	document.addEventListener("DOMContentLoaded", function () {
	    loadLogs(0);
	});



		
	
	// Action JS
	
	function getActionUI(action) {

	    switch (action) {
	        case "UPLOAD":
	            return { icon: "fa-upload", color: "text-success", badge: "Upload" };

	        case "DOWNLOAD":
	            return { icon: "fa-download", color: "text-info", badge: "Download" };

	        case "VIEW":
	            return { icon: "fa-eye", color: "text-primary", badge: "View" };

	        case "SHARE":
	            return { icon: "fa-share-nodes", color: "text-primary", badge: "Share" };

	        case "REVOKE":
	            return { icon: "fa-user-slash", color: "text-danger", badge: "Revoke" };

	        case "EXPIRED":
	            return { icon: "fa-clock", color: "text-warning", badge: "Expired" };

	        case "OTP":
	            return { icon: "fa-key", color: "text-success", badge: "OTP" };

	        case "PASSWORD":
	            return { icon: "fa-lock", color: "text-secondary", badge: "Password" };

	        default:
	            return { icon: "fa-circle-info", color: "text-muted", badge: action };
	    }
	}

	
	
	
	// message format
	function getMessage(log) {

	    const doc = `<strong>${log.documentName}</strong>`;
		const performBy = `<strong>${log.performedBy}</strong>`
		const expiredTime = `<strong>${formatTime(log.time)}</strong>`

	    switch (log.action) {
	        case "UPLOAD":
	            return `Uploaded document ${doc}`;

	        case "DOWNLOAD":
	            return `Downloaded ${doc}`;

	        case "VIEW":
	            return `Document Accessed: ${doc}`;

	        case "SHARE":
	            return `Shared ${doc} to ${performBy}`;

	        case "REVOKE":
	            return `Access revoked for ${performBy} on ${doc}`;

	        case "EXPIRED":
	            return `Document expired: ${doc} by ${expiredTime}`;

	        case "OTP":
	            return `OTP verified for ${doc} by ${performBy}`;

	        case "PASSWORD":
	            return `Password verified for ${doc} by ${performBy}`;

	        default:
	            return `${log.action} on ${doc}`;
	    }
	}

	
	
	// time format
	
	function formatTime(dateStr) {

	    const date = new Date(dateStr);
	    const diff = Math.floor((Date.now() - date.getTime()) / 1000);

	    if (diff < 60) return "Just now";
	    if (diff < 3600) return `${Math.floor(diff / 60)} minutes ago`;
	    if (diff < 86400) return `${Math.floor(diff / 3600)} hours ago`;
	    if (diff < 172800) return "Yesterday";

	    return date.toLocaleDateString() + " " + date.toLocaleTimeString();
	}
