const CONTEXT_PATH = /*[[@{/}]]*/ '';


/*fetch document*/
fetch(CONTEXT_PATH + 'fetchSecureDoc')
    .then(res => {
        if (!res.ok) throw new Error("Not logged in");
        return res.json();
    })
    .then(data => {
        const tbody = document.getElementById("viewSecureDocTableBody");
        tbody.innerHTML = "";

		data.content.forEach(secureDoc => {

		    const row = document.createElement("tr");
			
			// Status colour logic
				                  let statusClass = "bg-secondary";

				                  if (secureDoc.status === "ACTIVE") statusClass = "bg-success";
				                  else if (secureDoc.status === "REVOKED") statusClass = "bg-danger";
				                  else if (secureDoc.status === "EXPIRED") statusClass = "bg-warning";

		    row.innerHTML = `
		        <td>${secureDoc.documentName}</td>
		        <td>${secureDoc.shareType}</td>
		        <td>${secureDoc.accessType}</td>
		        <td>${secureDoc.expiryTime}</td>
		        <td>
						<span class="badge ${statusClass}">
					            ${secureDoc.status}
						</span>
				</td>
		        <td class="text-end">
		            <button class="btn btn-sm btn-outline-info me-2 view-btn"
		                    data-bs-toggle="modal"
		                    data-bs-target="#viewSecureDocumentModal"
							data-filepath="${secureDoc.filePath}"
							data-filename="${secureDoc.documentName}"
							 ${secureDoc.status !== "ACTIVE" ? "disabled" : ""}>
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
				
				console.log(filePath);
				console.log(fileName);

		        viewDocument(filePath, fileName);
		    }
		});


		// view Document
		
		function viewDocument(filePath, fileName) {

		    document.getElementById("secureDocName").innerText = fileName;

		    const img = document.getElementById("secureDocImage");
		    const pdf = document.getElementById("secureDocPdf");
		    const text = document.getElementById("secureDocText");
		    const noPreview = document.getElementById("secureNoPreview");
		    const downloadBtn = document.getElementById("secureDownloadBtn");

		    img.classList.add("d-none");
		    pdf.classList.add("d-none");
		    text.classList.add("d-none");
		    noPreview.classList.add("d-none");

		    const safePath = encodeURI(filePath);
		    const fullUrl = "/Secure_Document_Sharing" + safePath;

		    const ext = fileName.split('.').pop().toLowerCase();

		    downloadBtn.onclick = function () {
		        window.location.href =
		            "/Secure_Document_Sharing/download?path=" + safePath;
		    };

		    if (["jpg","jpeg","png","gif","webp"].includes(ext)) {
		        img.src = fullUrl;
		        img.classList.remove("d-none");
		    }
		    else if (ext === "pdf") {
		        pdf.src = fullUrl;
		        pdf.classList.remove("d-none");
		    }
		    else if (ext === "txt") {
		        fetch(fullUrl)
		            .then(res => res.text())
		            .then(data => {
		                text.textContent = data;
		                text.classList.remove("d-none");
		            });
		    }
		    else {
		        noPreview.classList.remove("d-none");
		    }
		}
