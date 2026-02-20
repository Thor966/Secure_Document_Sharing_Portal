

// get the document stats
fetch(CONTEXT_PATH + 'documentStats')
								            .then(res => {
								                if (!res.ok) throw new Error("Not logged in");
								                return res.json();
								            })
								            .then(response => {
								                document.getElementById("totalDocs").innerText = response.docCount;
								                document.getElementById("activeDocs").innerText = response.activeDocCount;
								                document.getElementById("expiredDocs").innerText = response.expiredDocCount;
								                document.getElementById("revokedDocs").innerText = response.revokedDocCount;
								            })
								            .catch(err => console.error(err));
											








// get the user data
let permissionCurrentPage = 0;
	  const permissionPageSize = 10;
	  
	  let currentKeyword = "";
	  let currentStatus = "";
	  
	  document.addEventListener("DOMContentLoaded", function () {

	      // Load initial data
	      loadManageDocuments(0);

	      // Filter form submit
	      const filterForm = document.getElementById("docFilterForm");
	      if (filterForm) {
	          filterForm.addEventListener("submit", function (e) {

	              e.preventDefault();

	              currentKeyword = this.keyword.value.trim();
	              currentStatus = this.status.value;

	              loadManageDocuments(0);
	          });
	      }

	      // Clear button
	      const clearBtn = document.getElementById("clearFilter");
	      if (clearBtn) {
	          clearBtn.addEventListener("click", function () {

	              filterForm.reset();
	              currentKeyword = "";
	              currentStatus = "";

	              loadManageDocuments(0);
	          });
	      }

	  });



	  function loadManageDocuments(page = 0) {

	      let url = `${CONTEXT_PATH}fetchAdminManageDocData?page=${page}&size=${permissionPageSize}`;

	      if (currentKeyword) {
	          url += `&keyword=${encodeURIComponent(currentKeyword)}`;
	      }

	      if (currentStatus) {
	          url += `&status=${encodeURIComponent(currentStatus)}`;
	      }

	      fetch(url)
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
	  function renderPermissionTable(doc) {

	      const tbody = document.getElementById("manageAdminDocumentTableBody");
	      tbody.innerHTML = "";

	      doc.forEach(doc => {

			// access type js
			
			let accessTypeClass = "bg-secondary";
			if (doc.accessType === "OTP") accessTypeClass = "badge-otp";
			          else if (doc.accessType === "PASSWORD") accessTypeClass = "badge-pass";
			          else if (doc.status === "NO ACCESS") accessTypeClass = "badge-public";
			
			
			// status js
	          let statusClass = "bg-secondary";

	          if (doc.status === "ACTIVE") statusClass = "bg-success";
	          else if (doc.status === "DISABLED") statusClass = "bg-danger";
	          else if (doc.status === "EXPIRED") statusClass = "bg-warning";
	          
			  
			  // action button js
			  let actionButton = "";

			  if (doc.status === "ACTIVE") {

			      actionButton = `
			          <button class="btn btn-sm btn-outline-danger"
			                  onclick="toggleDocStatus(${doc.dpid}, 'REVOKE')">
			              Force Revoke
			          </button>
			      `;

			  }

			  
			  //  Safe Storage Calculation
			  let storageMB = Number(doc.size || 0).toFixed(2);
			        


			          //  Safe Date Formatting
			         
			          let joinedOn = doc.uploaded 
			              ? new Date(doc.uploaded).toLocaleDateString() 
			              : "—";
					
					


	          const row = document.createElement("tr");

	          row.innerHTML = `
	              <td>${doc.documentName}</td>
	              <td>${doc.owner}</td>
	              <td>
				  <span class="badge ${accessTypeClass}">
				  		${doc.accessType}
				  </span>
				  </td>
	              <td>${storageMB} MB</td>
	              <td>${formatTime(doc.expiry)}</td>
	              <td>${doc.shares}</td>
	              <td>${joinedOn}</td>
	              
	              <td>
	                  <span class="badge ${statusClass}">
	                      ${doc.status}
	                  </span>
	              </td>
	              <td class="text-end">
					  ${actionButton}
	              </td>
	          `;

	          tbody.appendChild(row);
	      });
	  }
	  
	  
	  // date format
	  function formatTime(dateStr) {

	  	    const date = new Date(dateStr);
	  	    const diff = Math.floor((Date.now() - date.getTime()) / 1000);

	  	    if (diff < 60) return "Just now";
	  	    if (diff < 3600) return `${Math.floor(diff / 60)} minutes ago`;
	  	    if (diff < 86400) return `${Math.floor(diff / 3600)} hours ago`;
	  	    if (diff < 172800) return "Yesterday";

	  	    return date.toLocaleDateString() + " " + date.toLocaleTimeString();
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
	  function toggleDocStatus(dpid, action) {

	      console.log("DPID:", dpid);
	      console.log("Action:", action);

	      if (!confirm(`Are you sure you want to ${action.toLowerCase()} this Document?`)) {
	          return;
	      }

	      fetch(`${CONTEXT_PATH}forceRevoke/${dpid}?action=${action}`, {
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
	          alert("Error updating Document status");
	      });
	  }