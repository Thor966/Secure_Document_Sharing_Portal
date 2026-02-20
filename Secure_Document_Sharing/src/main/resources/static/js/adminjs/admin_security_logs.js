



// get the global audit logs data
let permissionCurrentPage = 0;
	  const permissionPageSize = 10;
	  
	  let currentKeyword = "";
	  let currentStatus = "";
	  
	  document.addEventListener("DOMContentLoaded", function () {

	      // Load initial data
	      loadGlobalAuditLogsData(0);

	      // Filter form submit
	      const filterForm = document.getElementById("auditFilterForm");
	      if (filterForm) {
	          filterForm.addEventListener("submit", function (e) {

	              e.preventDefault();

	              currentKeyword = this.keyword.value.trim();
	              currentStatus = this.status.value;

	               loadGlobalAuditLogsData(0);
	          });
	      }

	      // Clear button
	      const clearBtn = document.getElementById("clearFilter");
	      if (clearBtn) {
	          clearBtn.addEventListener("click", function () {

	              filterForm.reset();
	              currentKeyword = "";
	              currentStatus = "";

	               loadGlobalAuditLogsData(0);
	          });
	      }

	  });



	  function  loadGlobalAuditLogsData(page = 0) {

	      let url = `${CONTEXT_PATH}globalAuditLogs?page=${page}&size=${permissionPageSize}`;

	      if (currentKeyword) {
	          url += `&keyword=${encodeURIComponent(currentKeyword)}`;
	      }

	      if (currentStatus) {
	          url += `&status=${encodeURIComponent(currentStatus)}`;
	      }

	      fetch(url)
	          .then(res => res.json())
	          .then(data => {

				console.log("API Response:", data);
				
	              permissionCurrentPage = data.currentPage;

	              renderPermissionTable(data.content);
	              renderPermissionPagination(data);

	          })
	          .catch(err =>
	              console.error("Error loading Manage Admin User Data:", err)
	          );
	  }



	  
	  
	  // Render Table
	  function renderPermissionTable(logs) {

	      const tbody = document.getElementById("globalAuditLogsTableBody");
	      tbody.innerHTML = "";

	      logs.forEach(logs => {

			// access type js
			
			let actionTypeClass = "bg-secondary";

			if (logs.action === "UPLOAD") actionTypeClass = "badge-upload";
			else if (logs.action === "DOWNLOAD") actionTypeClass = "badge-download";
			else if (logs.action === "SHARE") actionTypeClass = "badge-share";
			else if (logs.action === "REVOKE") actionTypeClass = "badge-revoke";
			else if (logs.action === "EXPIRED") actionTypeClass = "badge-expired";
			else if (logs.action === "VIEW") actionTypeClass = "badge-view";
			else if (logs.action === "OTP") actionTypeClass = "badge-otp";
			else if (logs.action === "PASSWORD") actionTypeClass = "badge-pass";
			else if (logs.action === "LOGIN") actionTypeClass = "badge-login";
			else if (logs.action === "FORCE_REVOKE") actionTypeClass = "badge-force";
			else if (logs.action === "USER_DISABLED") actionTypeClass = "badge-disabled";
			else if (logs.action === "USER_ENABLED") actionTypeClass = "badge-enabled";
			else if (logs.action === "ADMIN_REQUEST") actionTypeClass = "badge-admin";
			
			
			// status js
			let statusClass = "bg-secondary";

			if (logs.status === "SUCCESS") statusClass = "bg-success";
			else if (logs.status === "FAILED") statusClass = "bg-danger";
			else if (logs.status === "ALLOW") statusClass = "bg-primary";
			else if (logs.status === "LIMIT_REACHED") statusClass = "bg-warning";
			else if (logs.status === "ADMIN") statusClass = "bg-dark";
			else if (logs.status === "EXPIRED") statusClass = "bg-warning";
	          

			  
			 

			          //  Safe Date Formatting
			         
			          let time = logs.timestamp
			              ? new Date(logs.timestamp).toLocaleDateString() 
			              : "—";
					
					


	          const row = document.createElement("tr");

	          row.innerHTML = `
	              <td>${time}</td>
	              <td>${logs.username}</td>
	              <td>
				  <span class="badge ${actionTypeClass}">
				  		${logs.action}
				  </span>
				  </td>
	              <td>${logs.documentName}</td>
	              <td>
	                  <span class="badge ${statusClass}">
	                      ${logs.status}
	                  </span>
	              </td>
				  <td>${logs.source}</td>
	          `;

	          tbody.appendChild(row);
	      });
	  }
	  
	  
	  
	  // pagination controls
	  function renderPermissionPagination(pageData) {

	      const container =
	          document.getElementById("permissionPagination");

	      container.innerHTML = "";

	      // Previous
	      if (!pageData.first) {
	          container.innerHTML += `
	              <button class="btn btn-outline-primary mx-1"
	                      onclick="loadGlobalAuditLogsData(${pageData.currentPage - 1})">
	                  Previous
	              </button>
	          `;
	      }

	      // Page Numbers
	      for (let i = 0; i < pageData.totalPages; i++) {

	          container.innerHTML += `
	              <button class="btn mx-1 
	                  ${i === pageData.currentPage
	                      ? 'btn-primary'
	                      : 'btn-outline-primary'}"
	                  onclick="loadGlobalAuditLogsData(${i})">
	                  ${i + 1}
	              </button>
	          `;
	      }

	      // Next
	      if (!pageData.last) {
	          container.innerHTML += `
	              <button class="btn btn-outline-primary mx-1"
	                      onclick="loadGlobalAuditLogsData(${pageData.currentPage + 1})">
	                  Next
	              </button>
	          `;
	      }
	  }