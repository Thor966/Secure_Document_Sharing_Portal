const CONTEXT_PATH = /*[[@{/}]]*/ '';


/*fetch document*/
fetch(CONTEXT_PATH + 'fetchDocumentDetails')
    .then(res => {
        if (!res.ok) throw new Error("Not logged in");
        return res.json();
    })
    .then(data => {
        const tbody = document.getElementById("docTableBody");
        tbody.innerHTML = "";

		data.content.forEach(doc => {

		    const row = document.createElement("tr");

		    row.innerHTML = `
		        <td>${doc.storedName}</td>
		        <td>${doc.insertedOn}</td>
		        <td class="text-end">
		            <button class="btn btn-sm btn-outline-info me-2 view-btn"
		                    data-bs-toggle="modal"
		                    data-bs-target="#viewdocumentModal"
							data-filepath="${doc.filePath}"
							data-filename="${doc.storedName}">
		                View
		            </button>

		            <button class="btn btn-sm btn-outline-secondary share-btn"
		                    data-bs-toggle="modal"
		                    data-bs-target="#shareModal"
							data-docid="${doc.docid}"
							data-originalname="${doc.storedName}">
		                Share
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

	
	
	
	

	/*share doc*/
	
document.addEventListener("click", function (e) {

    const btn = e.target.closest(".share-btn");

    if (btn) {

        const docid = btn.getAttribute("data-docid");
        const originName = btn.getAttribute("data-originalname");

        console.log("Sharing document id:", originName);

        document.getElementById("shareDocumentId").value = docid;
         document.getElementById("originalName").innerText = originName;
    }
});




// Generate Secure link (Hide Receiver mail)

document.addEventListener("DOMContentLoaded", function () {

    const emailField = document.getElementById("emailField");
    const shareEmail = document.getElementById("share_email");
    const generateLink = document.getElementById("generate_link");
    const secureDoc = document.getElementById("secure_doc");
    const shareBtn = document.getElementById("shareBtn2");

    function toggleEmailField() {
        if (generateLink.checked) {
            emailField.style.display = "none";
        } else {
            emailField.style.display = "block";
        }
    }

    function updateButtonText() {
        if (generateLink.checked) {
            shareBtn.textContent = "Generate Link";
        } else {
            shareBtn.textContent = "Share Securely";
        }
    }

    // Initial state
    toggleEmailField();
    updateButtonText();

    // Event listeners
    shareEmail.addEventListener("change", () => {
        toggleEmailField();
        updateButtonText();
    });

    generateLink.addEventListener("change", () => {
        toggleEmailField();
        updateButtonText();
    });

    secureDoc.addEventListener("change", () => {
        toggleEmailField();
        updateButtonText();
    });

});



	// show Secure Link URl for Copy and Toast message

   function handleUpload(event) {
       event.preventDefault();

       const form = event.target;
       const formData = new FormData(form);

       const generateLink = document.getElementById("generate_link");

	   fetch(form.action, {
	       method: 'POST',
	       body: formData
	   })
	   .then(async response => {

	       const contentType = response.headers.get("content-type");

		   let data;

		        if (contentType && contentType.includes("application/json")) {
		            data = await response.json();
		        } else {
		            data = await response.text();
		        }

		        // If response is not OK → show error
		        if (!response.ok) {
		            if (typeof data === "object" && data.error) {
		                throw new Error(data.error);
		            } else {
		                throw new Error(data || "Something went wrong");
		            }
		        }

		        return data;

		   
	   })
	   .then(data => {

	       if (generateLink.checked && typeof data === "object") {

	           document.getElementById("generatedLink").value = data.link;

	           const securityInfo = document.getElementById("securityInfo");
	           securityInfo.innerHTML = "";

	           if (data.accessType === "OTP") {
	               securityInfo.innerHTML =
	                   `OTP: <strong>${data.otp}</strong>`;
	           }

	           if (data.accessType === "PASSWORD") {
	               securityInfo.innerHTML =
	                   ` Password: <strong>${data.password}</strong>`;
	           }

	           const linkModal = new bootstrap.Modal(
	               document.getElementById("linkModal")
	           );
	           linkModal.show();

			   bootstrap.Modal.getInstance(
			              document.getElementById('shareModal')
			          )?.hide();

	           form.reset();

	       } else {

		
			showSuccessToast("Document Shared successfully!");

			        bootstrap.Modal.getInstance(
			            document.getElementById('shareModal')
			        )?.hide();

	           form.reset();
	       }

	   })
	   .catch(error => {
	          console.error("Share Error:", error);
	          showErrorToast(error.message);
	      });
   }
   
   
   
   
   // Show Toast messages
   function showSuccessToast(msg) {
       const toastEl = document.getElementById("successToast");
       const msgEl = document.getElementById("successMsg");

       msgEl.innerText = msg;

       const toast = new bootstrap.Toast(toastEl);
       toast.show();
   }

   function showErrorToast(error) {
       const toastEl = document.getElementById("errorToast");
       const msgEl = document.getElementById("errorMsg");

       msgEl.innerText = error;

       const toast = new bootstrap.Toast(toastEl);
       toast.show();
   }

   
   
      

   // Copy the link
   function copyLink() {
       const input = document.getElementById("generatedLink");
       const button = document.getElementById("copyBtn");

       navigator.clipboard.writeText(input.value)
           .then(() => {

               // Change button text
               button.innerHTML =
                   `<i class="fa-solid fa-check me-2"></i> Copied!`;

               button.style.background =
                   "linear-gradient(135deg, #1cc88a, #17a673)";

               // Reset after 2 seconds
               setTimeout(() => {
                   button.innerHTML =
                       `<i class="fa-solid fa-copy me-2"></i> Copy Link`;

                   button.style.background =
                       "linear-gradient(135deg, #4e73df, #1cc88a)";
               }, 2000);
           })
           .catch(() => {
               alert("Failed to copy link");
           });
   }


   
   
  
	
	  /* manage Document */

	  function loadManageDocuments() {

	      fetch(CONTEXT_PATH + 'docPermission')
	          .then(res => {
	              if (!res.ok) throw new Error("Not logged in");
	              return res.json();
	          })
	          .then(data => {

	              console.log("Permission Data:", data);

	              const tbody = document.getElementById("manageDocTableBody");
	              tbody.innerHTML = "";

	              data.forEach(doc => {

	                  const row = document.createElement("tr");

	                  // Status colour logic
	                  let statusClass = "bg-secondary";

	                  if (doc.status === "ACTIVE") statusClass = "bg-success";
	                  else if (doc.status === "REVOKED") statusClass = "bg-danger";
	                  else if (doc.status === "EXPIRED") statusClass = "bg-warning";

	                  row.innerHTML = `
	                      <td>${doc.documentName}</td>
	                      <td>${doc.accessType}</td>
	                      <td>${doc.grantedToUser}</td>
	                      <td>${doc.remainingTime}</td>
	                      <td>
	                          <span class="badge ${statusClass}">
	                              ${doc.status}
	                          </span>
	                      </td>
	                      <td class="text-end">
	                          <button class="btn btn-sm btn-outline-danger"
	                                  onclick="revokePermission(${doc.dpid})"
	                                  ${doc.status !== "ACTIVE" ? "disabled" : ""}>
	                              Revoke
	                          </button>
	                      </td>
	                  `;

	                  tbody.appendChild(row);
	              });
	          })
	          .catch(err => console.error("Error loading permissions:", err));
	  }

	  
	  
	  document.addEventListener("DOMContentLoaded", function () {
	      loadManageDocuments();
	  });
  
	
	  
	    
	  
	  
	/*revokePermissions*/

	function revokePermission(dpid) {

	    if (!confirm("Are you sure you want to revoke this permission?")) {
	        return;
	    }

	    fetch(CONTEXT_PATH + "revokePermission/" + dpid, {
	        method: "POST"
	    })
	    .then(res => {
	        if (!res.ok) throw new Error("Failed to revoke permission");
	        return res.text();
	    })
	    .then(msg => {
	        alert("Permission revoked successfully");

	        // Reload manage table after revoke
	        loadManageDocuments();   
	    })
	    .catch(err => {
	        console.error("Revoke error:", err);
	        alert("Failed to revoke permission");
	    });
	}

