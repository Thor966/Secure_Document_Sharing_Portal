   /*==========Dashboard JavaScript==========*/




// dashboard refresh js
function refreshDashboard() {
            location.reload();
        }

// auto refresh
        setInterval(function () {
            location.reload();
        }, 60000);
		
		
		const CONTEXT_PATH = /*[[@{/}]]*/ '';
		
		
		// fetch the total Registred User and daywise register user
		fetch(CONTEXT_PATH + 'registerUserCount')
								            .then(res => {
								                if (!res.ok) throw new Error("Not logged in");
								                return res.json();
								            })
								            .then(response => {
								                document.getElementById("totalRegisterUserCount").innerText = response.userCount;
								                document.getElementById("totalDaywiseUserCount").innerText = response.todaysUserCount;
								                document.getElementById("todaysRegisterUser").innerText = response.todaysUserCount;
								            })
								            .catch(err => console.error(err));
											
											
											
											
		// fetch the total uploaded documents and daywise uploaded doc count
		fetch(CONTEXT_PATH + 'uploadedDocument')
										            .then(res => {
										                if (!res.ok) throw new Error("Not logged in");
										                return res.json();
										            })
										            .then(response => {
										                document.getElementById("totalUploadedDocCount").innerText = response.uploadedDocCount;
										                document.getElementById("totalDaywiseDocCount").innerText = response.daywiseCount;
										                document.getElementById("docUploadedToday").innerText = response.daywiseCount;
										            })
										            .catch(err => console.error(err));
													
										
													
													
		// fetch active shares
		fetch(CONTEXT_PATH + 'fetchActiveShares')
												            .then(res => {
												                if (!res.ok) throw new Error("Not logged in");
												                return res.json();
												            })
												            .then(activeSharesCount => {
												                document.getElementById("totalActiveShares").innerText = activeSharesCount;
												                
												            })
												            .catch(err => console.error(err));
					
													
				
			// fetch the total active users 
			function loadOnlineUsers() {

			    fetch('onlineUsers')
			        .then(res => res.json())
			        .then(count => {
			            document.getElementById("activeUsers").innerText = count;
			        });
			}

			// Refresh every 10 seconds
			setInterval(loadOnlineUsers, 60000);

			document.addEventListener("DOMContentLoaded", loadOnlineUsers);
			
			
			
			
			// get the daywise expired document count
			fetch(CONTEXT_PATH + 'daywiseExpiredCount')
													            .then(res => {
													                if (!res.ok) throw new Error("Not logged in");
													                return res.json();
													            })
													            .then(expiredDocCount => {
													                document.getElementById("expiredCount").innerText = expiredDocCount;
													                
													            })
													            .catch(err => console.error(err));
			
																
																
																
																
			// get the access type count
			fetch(CONTEXT_PATH + 'accessTypeCount')
																            .then(res => {
																                if (!res.ok) throw new Error("Not logged in");
																                return res.json();
																            })
																            .then(response => {
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

