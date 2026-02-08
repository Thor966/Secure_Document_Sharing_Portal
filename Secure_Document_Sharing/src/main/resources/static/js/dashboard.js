// Thymeleaf gives correct context path automatically
   const CONTEXT_PATH = /*[[@{/}]]*/ '';

   fetch(CONTEXT_PATH + 'userOperations/get-username')
       .then(res => {
           if (!res.ok) throw new Error("Not logged in");
           return res.json();
       })
       .then(user => {
           document.getElementById("username").innerText = user.firstName;
       })
       .catch(err => console.error(err));
	   
	   
	   
	   
	 // Toast message
	   function handleUpload(event) {
	       event.preventDefault();

	       const form = event.target;
	       const formData = new FormData(form);

	       fetch(form.action, {
	           method: 'POST',
	           body: formData
	       })
	       .then(response => {
	           if (response.ok) {
	               showSuccessToast("Document uploaded successfully!");
	               bootstrap.Modal.getInstance(
	                   document.getElementById('documentModal')
	               ).hide();
	               form.reset();
	           } else {
	               showErrorToast("Upload failed. Try again.");
	           }
	       })
	       .catch(() => {
	           showErrorToast("Server error occurred.");
	       });
	   }

	   function showSuccessToast(msg) {
	       document.getElementById("successMsg").innerText = msg;
	       new bootstrap.Toast(document.getElementById("successToast")).show();
	   }

	   function showErrorToast(msg) {
	       document.getElementById("errorMsg").innerText = msg;
	       new bootstrap.Toast(document.getElementById("errorToast")).show();
	   }
	 
	   
	   
	   // get the total Documents
	   
	   fetch(CONTEXT_PATH + 'documentCount')
	          .then(res => {
	              if (!res.ok) throw new Error("Not logged in");
	              return res.json();
	          })
	          .then(count => {
	              document.getElementById("totalDocumentCount").innerText = count;
	          })
	          .catch(err => console.error(err));
			  
			  
			  
			  // get the shared document count
			  
			  fetch(CONTEXT_PATH + 'sharedDocCount')
			            .then(res => {
			                if (!res.ok) throw new Error("Not logged in");
			                return res.json();
			            })
			            .then(sharedCount => {
			                document.getElementById("totalSharedDocCount").innerText = sharedCount;
			            })
			            .catch(err => console.error(err));
						
						
						
			// get the active link count
			
				fetch(CONTEXT_PATH + 'activeLinkCount')
						            .then(res => {
						                if (!res.ok) throw new Error("Not logged in");
						                return res.json();
						            })
						            .then(linkCount => {
						                document.getElementById("activeLinkCount").innerText = linkCount;
						            })
						            .catch(err => console.error(err));
									
									
									
			
			// get the OTP Based Document Count
			
							fetch(CONTEXT_PATH + 'docOtpCount')
									            .then(res => {
									                if (!res.ok) throw new Error("Not logged in");
									                return res.json();
									            })
									            .then(documentOtpCount => {
									                document.getElementById("otpDocCount").innerText = documentOtpCount;
									            })
									            .catch(err => console.error(err));

												
												
												
			// get the Password Based Document Count
			
								fetch(CONTEXT_PATH + 'docPassCount')
												            .then(res => {
												                if (!res.ok) throw new Error("Not logged in");
												                return res.json();
												            })
												            .then(documentPassCount => {
												                document.getElementById("passDocCount").innerText = documentPassCount;
												            })
												            .catch(err => console.error(err));
															
															
															
															
			// get the Expired Document Count
			
												fetch(CONTEXT_PATH + 'docExpiredCount')
															            .then(res => {
															                if (!res.ok) throw new Error("Not logged in");
															                return res.json();
															            })
															            .then(documentExpiredCount => {
															                document.getElementById("expiredDocCount").innerText = documentExpiredCount;
															            })
															            .catch(err => console.error(err));
																		
																		
																		
																		
																		
																		
			// get the recent Document Data


			/*fetch recentDocument*/
			fetch(CONTEXT_PATH + 'recentDocuments')
			    .then(res => {
			        if (!res.ok) throw new Error("Not logged in");
			        return res.json();
			    })
			    .then(data => {
			        const tbody = document.getElementById("recentDocTableBody");
			        tbody.innerHTML = "";

					data.content.forEach(doc => {

					    const row = document.createElement("tr");
						
						// Status colour logic
						                  let statusClass = "bg-secondary";

						                  if (doc.status === "ACTIVE") statusClass = "bg-success";
						                  else if (doc.status === "REVOKED") statusClass = "bg-danger";
						                  else if (doc.status === "EXPIRED") statusClass = "bg-warning";

					    row.innerHTML = `
					        <td>${doc.documentName}</td>
					        <td>${doc.accessType}</td>
					        <td>${doc.expiryTime}</td>
					        <td>
									<span class="badge ${statusClass}">
										 ${doc.status}
									</span>
							</td>
					        <td class="text-end">
					            <button class="btn btn-sm btn-outline-info me-2 view-btn"
					                    data-bs-toggle="modal"
					                    data-bs-target="#viewdocumentModal"
										data-filepath="${doc.filePath}"
										data-filename="${doc.documentName}">
					                View
					            </button>
								

					        </td>
					    `;

					    tbody.appendChild(row);
					});
					
			    })
				
			    .catch(err => console.error(err));
				

				// View btn click event
				
				document.addEventListener("click", function (e) {
				    if (e.target.classList.contains("view-btn")) {

				        const filePath = e.target.getAttribute("data-filepath");
				        const fileName = e.target.getAttribute("data-filename");

				        viewDocument(filePath, fileName);
				    }
				});


				// view Document
				
				function viewDocument(filePath, fileName) {

					document.getElementById("docName").innerText = fileName;
					
				    const img = document.getElementById("docImage");
				    const pdf = document.getElementById("docPdf");
				    const text = document.getElementById("docText");
				    const noPreview = document.getElementById("noPreview");
					const downloadBtn = document.getElementById("downloadBtn");

				    // Hide all first
				    img.classList.add("d-none");
				    pdf.classList.add("d-none");
				    text.classList.add("d-none");
				    noPreview.classList.add("d-none");

				    const safePath = encodeURI(filePath);
				    const fullUrl = "/Secure_Document_Sharing" + safePath;

				    // Extract extension
				    const ext = fileName.split('.').pop().toLowerCase();
					
					// Download link
					  downloadBtn.onclick = function () {
					      window.location.href = "/Secure_Document_Sharing/download?path=" + safePath;
					  };

				    // image files
				    if (["jpg", "jpeg", "png", "gif", "webp"].includes(ext)) {
				        img.src = fullUrl;
				        img.classList.remove("d-none");
				    }

				    // pdf files
				    else if (ext === "pdf") {
				        pdf.src = fullUrl;
				        pdf.classList.remove("d-none");
				    }

				    // text files
				    else if (ext === "txt") {
				        fetch(fullUrl)
				            .then(res => res.text())
				            .then(data => {
				                text.textContent = data;
				                text.classList.remove("d-none");
				            });
				    }

				    // No Preview
				    else {
				        noPreview.classList.remove("d-none");
				    }
					
					

					
				}
				
				
				
				
				
				
				
	// Recent Audit Activity logs JS
	
	
	
	
	// get the security audit logs

	fetch(CONTEXT_PATH + 'recentAuditLogs')
	    .then(res => res.json())
	    .then(data => {

	        const container = document.getElementById("recentAuditActivityLogs");
	        container.innerHTML = "";

	        data.content.forEach(recentLogs => {

	            const { icon, color} = getActionUI(recentLogs.action);

	            const li = document.createElement("li");
	            li.className = "list-group-item d-flex justify-content-between";

	            li.innerHTML = `
	               
						<span>
						 <i class="fa-solid ${icon} ${color} me-2"></i>
							${getMessage(recentLogs)}
							</span>
						<small class="text-muted">${formatTime(recentLogs.time)}</small>
					
					
	            `;

	            container.appendChild(li);
	        });
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
		function getMessage(recentLogs) {

		    const doc = `<strong>${recentLogs.documentName}</strong>`;
			const performBy = `<strong>${recentLogs.performedBy}</strong>`
			const expiredTime = `<strong>${formatTime(recentLogs.time)}</strong>`

		    switch (recentLogs.action) {
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
		            return `${recentLogs.action} on ${doc}`;
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

			