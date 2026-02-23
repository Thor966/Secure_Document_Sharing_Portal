   /*==========Dashboard JavaScript========== */




// dashboard refresh js
function refreshDashboard() {
            location.reload();
        }

// auto refresh
        setInterval(function () {
            location.reload();
        }, 60000);
		
		
		const CONTEXT_PATH = /*[[@{/}]]*/ '';
		
		
		// fetch the total User stats
		fetch(CONTEXT_PATH + 'adminDashboardUserStats')
								            .then(res => {
								                if (!res.ok) throw new Error("Not logged in");
								                return res.json();
								            })
								            .then(response => {
								                document.getElementById("totalRegisterUserCount").innerText = response.userCount;
								                document.getElementById("totalDaywiseUserCount").innerText = response.todaysUserCount;
								                document.getElementById("todaysRegisterUser").innerText = response.todaysUserCount;
								                document.getElementById("activeUsers").innerText = response.onlineCount;
								                document.getElementById("disabledUser").innerText = response.disableUserCount;
								            })
								            .catch(err => console.error(err));
											
											
											
											
		// fetch the total uploaded documents and daywise uploaded doc count
		fetch(CONTEXT_PATH + 'adminDashboardDocStats')
										            .then(res => {
										                if (!res.ok) throw new Error("Not logged in");
										                return res.json();
										            })
										            .then(response => {
										                document.getElementById("totalUploadedDocCount").innerText = response.uploadedDocCount;
										                document.getElementById("totalDaywiseDocCount").innerText = response.daywiseCount;
										                document.getElementById("docUploadedToday").innerText = response.daywiseCount;
										                document.getElementById("expiredCount").innerText = response.expiredDocCount;
										                document.getElementById("forceRevoke").innerText = response.forceRevokeDocCount;
										            })
										            .catch(err => console.error(err));
													
										
													
																															
																
																
																
			// get the Share Stats
			fetch(CONTEXT_PATH + 'adminSharesStats')
																            .then(res => {
																                if (!res.ok) throw new Error("Not logged in");
																                return res.json();
																            })
																            .then(response => {
																				document.getElementById("totalActiveShares").innerText = response.activeSharesCount;
																                document.getElementById("otpLinks").innerText = response.otpCount;
																                document.getElementById("passwordLinks").innerText = response.passCount;
																                document.getElementById("publicLinks").innerText = response.publicCount;
																                
																            })
																            .catch(err => console.error(err));
			
																			
																			
																			

			// get the storage usage	
			fetch(CONTEXT_PATH + "storageUsage")
			        .then(res => res.json())
			        .then(data => {

			            document.getElementById("storagePercent")
			                .innerText = data.percentage.toFixed(1) + "%";

			            document.getElementById("storageUsed")
			                .innerText = data.usedGB;

			            document.getElementById("storageMax")
			                .innerText = data.maxGB;

			            document.getElementById("storageProgress")
			                .style.width = data.percentage + "%";
			        });
					
					
					
					
					
					
					
				// get the system health
				
				fetch(CONTEXT_PATH + 'actuator/health')
				  .then(res => res.json())
				  .then(data => {

					  const email = data.components.email || data.components.mail;


				      const statusDiv = document.getElementById("emailServiceStatus");
				      const badge = document.getElementById("emailServiceBadge");
				      const time = document.getElementById("emailServiceTime");

				      const now = new Date().toLocaleTimeString();
				      time.innerText = "Last checked: " + now;

				      if (email && email.status === "UP") {

				          statusDiv.innerText = "Operational";
				          statusDiv.className = "stat-value text-success";

				          badge.innerText = "UP";
				          badge.className = "badge bg-success";

				      } else {

				          statusDiv.innerText = "Service Down";
				          statusDiv.className = "stat-value text-danger";

				          badge.innerText = "DOWN";
				          badge.className = "badge bg-danger";
				      }
				  })
				  .catch(() => {

				      const statusDiv = document.getElementById("emailServiceStatus");
				      const badge = document.getElementById("emailServiceBadge");

				      statusDiv.innerText = "Unavailable";
				      statusDiv.className = "stat-value text-danger";

				      badge.innerText = "ERROR";
				      badge.className = "badge bg-danger";
				  });
				  
				  
				  
				  
				  
				  // checking the health
				  fetch(CONTEXT_PATH + 'actuator/health')
				         .then(res => res.json())
				         .then(data => {

				             const statusText =
				                 document.getElementById("systemStatusText");

				             const statusSub =
				                 document.getElementById("systemStatusSub");

				             const overallStatus = data.status;

				             if (overallStatus === "UP") {

				                 statusText.innerText = "Healthy";
				                 statusText.className = "stat-value text-success";

				                 statusSub.innerText = "All services running";

				             } else if (overallStatus === "DOWN") {

				                 statusText.innerText = "System Down";
				                 statusText.className = "stat-value text-danger";

				                 statusSub.innerText = "One or more services failed";

				             } else {

				                 statusText.innerText = "Degraded";
				                 statusText.className = "stat-value text-warning";

				                 statusSub.innerText = "Partial service disruption";
				             }
				         })
				         .catch(() => {

				             const statusText =
				                 document.getElementById("systemStatusText");

				             const statusSub =
				                 document.getElementById("systemStatusSub");

				             statusText.innerText = "Unavailable";
				             statusText.className = "stat-value text-danger";

				             statusSub.innerText = "Cannot say server";
				         });
						 
						 
						 
						 
			// storage usage per user
			
			fetch(CONTEXT_PATH + 'storageUsagePerUser')
			    .then(res => res.json())
			    .then(data => {
					
					console.log(data);

			        const tbody =
			            document.getElementById("storageTableBody");

			        tbody.innerHTML = "";

			        data.forEach(row => {

			            const mb =
			                (row.totalBytes / 1024 / 1024).toFixed(2);

			            const tr =
			                document.createElement("tr");

			            tr.innerHTML = `
			                <td>${row.email}</td>
			                <td>${mb} MB</td>
			            `;

			            tbody.appendChild(tr);
			        });
			    });


				
				
				
				
				// get the Admin recent action 
					
					let currentPage = 0;
					const pageSize = 5;

					function loadLogs(page = 0) {

					    fetch(`${CONTEXT_PATH}recentAdminAction?page=${page}&size=${pageSize}`)
					        .then(res => res.json())
					        .then(data => {

								console.log("recent admin activity:", data);
								currentPage = data.currentPage;

					            renderLogs(data.content);
					            renderPagination(data);
					        });
					}

					
					// render audit logs to html
					function renderLogs(logs) {

					    const container = document.getElementById("activityContainer");
					    container.innerHTML = "";

					    const ul = document.createElement("ul");
					    ul.className = "list-group list-group-flush";

					    logs.forEach(log => {

					        const li = document.createElement("li");
					        li.className = "list-group-item";

					        li.innerHTML = `
					            ${getMessage(log)}
					            <div class="activity-time mt-1">
					                ${formatTime(log.time)}
					            </div>
					        `;

					        ul.appendChild(li);
					    });

					    container.appendChild(ul);
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


		
					
					// message format
					function getMessage(log) {

					    const doc = `<strong>${log.documentName}</strong>`;
						const performTo = `<strong>${log.performTo}</strong>`

					    switch (log.action) {
					        case "FORCED_REVOKED":
					            return `Admin revoked access to  ${doc}.`;

					        case "USER_DISABLED":
					            return `User ${performTo} disabled.`;
								
							case "USER_ENABLED":
								return `User ${performTo} enabled.`;

					        default:
					            return `${log.action} on ${doc}`;
					    }
					}

					
					
					// time format
					
					function formatTime(dateStr) {

					    const date = new Date(dateStr);
					    const now = new Date();

					    const diffSeconds = Math.floor((now - date) / 1000);
					    const diffMinutes = Math.floor(diffSeconds / 60);
					    const diffHours = Math.floor(diffMinutes / 60);
					    const diffDays = Math.floor(diffHours / 24);

					    if (diffSeconds < 60) return "Just now";
					    if (diffMinutes < 60) return `${diffMinutes} min ago`;
					    if (diffHours < 24) return `${diffHours} hrs ago`;
					    if (diffDays === 1) return "Yesterday";
					    if (diffDays < 7) return `${diffDays} days ago`;

					    return date.toLocaleString("en-IN", {
					        day: "2-digit",
					        month: "short",
					        year: "numeric",
					        hour: "2-digit",
					        minute: "2-digit"
					    });
					}