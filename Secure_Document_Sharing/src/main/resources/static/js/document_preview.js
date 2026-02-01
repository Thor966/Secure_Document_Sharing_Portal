function confirmRecipientRevoke() {
    return confirm(
        "Revoke access for this recipient?\n\nThey will no longer be able to view this document."
    );
}



	// view doc Info
	
	document.addEventListener("DOMContentLoaded", () => {

		// Get token from query parameter
		const urlParams = new URLSearchParams(window.location.search);
		const token = urlParams.get("token");

		if (!token) {
		    alert("Invalid preview link. Token missing.");
		    return;
		}
		
		const contextPath = window.location.pathname.split("/")[1];


	    //  Call preview API
	    fetch(`/${contextPath}/api/preview/${token}`)
	        .then(response => {
	            if (!response.ok) {
	                return response.text().then(err => {
	                    throw new Error(err || "Access denied");
	                });
	            }
	            return response.json();
	        })
	        .then(data => {

	            //  Populate header data
	            document.getElementById("docName").innerText = data.documentName;
	            document.getElementById("sharedBy").innerText =
	                `Shared by ${data.grantedBy}`;

	            if (data.expiry) {
	                document.getElementById("expiryInfo").innerText =
	                    `Expires at ${new Date(data.expiry).toLocaleString()}`;
	            } else {
	                document.getElementById("expiryInfo").innerText = "No expiry";
	            }

	            //  Setup download button
				document.getElementById("downloadBtn").href =
				    `/${contextPath}/download/${token}`;


	            //  File preview logic
	            showPreview(data.filePath);
	        })
	        .catch(err => {
	            console.error(err);
	            alert(err.message || "Preview not available");
	        });
	});

	/**
	 * Handles preview based on file extension
	 */
	function showPreview(filePath) {

	    const imageEl = document.getElementById("docImage");
	    const pdfEl = document.getElementById("docPdf");
	    const textEl = document.getElementById("docText");
	    const fallbackEl = document.getElementById("noPreview");

	    // Reset visibility
	    imageEl.classList.add("d-none");
	    pdfEl.classList.add("d-none");
	    textEl.classList.add("d-none");
	    fallbackEl.classList.add("d-none");

	    const extension = filePath.split(".").pop().toLowerCase();
		
		const contextPath = window.location.pathname.split("/")[1];

	    // Image preview
	    if (["png", "jpg", "jpeg", "gif", "webp"].includes(extension)) {
			imageEl.src = `/${contextPath}${filePath}`;
	        imageEl.classList.remove("d-none");
	        return;
	    }

	    // PDF preview
	    if (extension === "pdf") {
			pdfEl.src = `/${contextPath}${filePath}`;
	        pdfEl.classList.remove("d-none");
	        return;
	    }

	    //  Text preview
	    if (["txt", "log", "csv"].includes(extension)) {
	        fetch(filePath)
	            .then(res => res.text())
	            .then(text => {
	                textEl.innerText = text;
	                textEl.classList.remove("d-none");
	            })
	            .catch(() => fallbackEl.classList.remove("d-none"));
	        return;
	    }

	    //  Unsupported
	    fallbackEl.classList.remove("d-none");
	}
