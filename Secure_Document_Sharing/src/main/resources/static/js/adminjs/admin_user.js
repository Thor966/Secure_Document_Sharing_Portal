

// get the total users
fetch(CONTEXT_PATH + 'registerUserCount')
								            .then(res => {
								                if (!res.ok) throw new Error("Not logged in");
								                return res.json();
								            })
								            .then(response => {
								                document.getElementById("totalUserCount").innerText = response.userCount;
								            })
								            .catch(err => console.error(err));
											
											
											
	// get the active users 	
			fetch(CONTEXT_PATH + 'onlineUsers')
													            .then(res => {
													                if (!res.ok) throw new Error("Not logged in");
													                return res.json();
													            })
													            .then(count => {
													                document.getElementById("manageActiveUsers").innerText = count;
													            })
													            .catch(err => console.error(err));
																
																
																
				
				
	// get the disabled user count
	fetch(CONTEXT_PATH + 'disabledUserCount')
									            .then(res => {
									                if (!res.ok) throw new Error("Not logged in");
									                return res.json();
									            })
									            .then(userCount => {
									                document.getElementById("disabledUsers").innerText = userCount;
									            })
									            .catch(err => console.error(err));
				
				
				
				


// get the user data
let permissionCurrentPage = 0;
	  const permissionPageSize = 10;

	  function loadManageDocuments(page = 0) {

	      fetch(`${CONTEXT_PATH}fetchManageUserData?page=${page}&size=${permissionPageSize}`)
	          .then(res => res.json())
	          .then(data => {
				

	              permissionCurrentPage = data.currentPage;

	              renderPermissionTable(data.content);
	              renderPermissionPagination(data);

	          })
	          .catch(err =>
	              console.error("Error loading Manage Admin User Data:", err)
	          );
	  }
	  
	  
	  document.addEventListener("DOMContentLoaded", function () {
	      loadManageDocuments(0);
	  });


	  
	  
	  // Render Table
	  function renderPermissionTable(user) {

	      const tbody = document.getElementById("manageAdminUserTableBody");
	      tbody.innerHTML = "";

	      user.forEach(user => {

			// status js
	          let statusClass = "bg-secondary";

	          if (user.status === "ACTIVE") statusClass = "bg-success";
	          else if (user.status === "DISABLED") statusClass = "bg-danger";
	          
			  
			  // action button js
			  let actionButton = "";

			  if (user.status === "ACTIVE") {

			      actionButton = `
			          <button class="btn btn-sm btn-outline-danger"
			                  onclick="toggleUserStatus(${user.uid}, 'DISABLED')">
			              Disable
			          </button>
			      `;

			  } else if (user.status === "DISABLED") {

			      actionButton = `
			          <button class="btn btn-sm btn-outline-success"
			                  onclick="toggleUserStatus(${user.uid}, 'ENABLE')">
			              Enable
			          </button>
			      `;
			  }

			  
			  //  Safe Storage Calculation
			  let storageMB = Number(user.storageUsed || 0).toFixed(2);
			        


			          //  Safe Date Formatting
			          let lastLogin = user.lastLogin 
			              ? new Date(user.lastLogin).toLocaleString() 
			              : "—";

			          let joinedOn = user.joinOn 
			              ? new Date(user.joinOn).toLocaleDateString() 
			              : "—";
					
					


	          const row = document.createElement("tr");

	          row.innerHTML = `
	              <td>${user.firstName} ${user.lastName}</td>
	              <td>${user.email}</td>
	              <td>${lastLogin}</td>
	              <td>${joinedOn}</td>
	              <td>${storageMB} MB</td>
	              <td>
	                  <span class="badge ${statusClass}">
	                      ${user.status}
	                  </span>
	              </td>
	              <td class="text-end">
					  ${actionButton}
	              </td>
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
	                      onclick="loadManageDocuments(${pageData.currentPage - 1})">
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
	                  onclick="loadManageDocuments(${i})">
	                  ${i + 1}
	              </button>
	          `;
	      }

	      // Next
	      if (!pageData.last) {
	          container.innerHTML += `
	              <button class="btn btn-outline-primary mx-1"
	                      onclick="loadManageDocuments(${pageData.currentPage + 1})">
	                  Next
	              </button>
	          `;
	      }
	  }
	  
	  
	  
	  
	  
	  
	  
	  // toggle function
	  function toggleUserStatus(uid, action) {

	      console.log("UID:", uid);
	      console.log("Action:", action);

	      if (!confirm(`Are you sure you want to ${action.toLowerCase()} this user?`)) {
	          return;
	      }

	      fetch(`${CONTEXT_PATH}toggleUserStatus/${uid}?action=${action}`, {
	          method: "POST"
	      })
	      .then(res => {
	          if (!res.ok) throw new Error("Failed to update status");
	          return res.text();
	      })
	      .then(() => {
	          loadManageDocuments(permissionCurrentPage);
	      })
	      .catch(err => {
	          console.error("Toggle error:", err);
	          alert("Error updating user status");
	      });
	  }

