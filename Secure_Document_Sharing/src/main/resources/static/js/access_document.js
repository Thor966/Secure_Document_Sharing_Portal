
/*password toggle js*/
function togglePassword(icon) {
        const input = icon.previousElementSibling;
        if (input.type === "password") {
            input.type = "text";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            input.type = "password";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    }
	
			
			
			const CONTEXT_PATH = window.location.pathname.split('/')[1]
			    ? '/' + window.location.pathname.split('/')[1]
			    : '';

			
			
			// Get token from URL
			const params = new URLSearchParams(window.location.search);
			const token = params.get("token");

			if (!token) {
			    alert("Invalid access link");
			    window.location.href = "/error.html";
			}

			// Call backend REST API
			fetch(CONTEXT_PATH + "/docaccess/" + token)
			    .then(res => {
			        if (!res.ok) throw new Error("Access denied");
			        return res.json();
			    })
			    .then(data => {

			        document.getElementById("documentName").innerText = data.documentName;

			        if (data.accessType === "NO ACCESS") {
			            window.location.href = data.next;
			        }

			        if (data.accessType === "OTP") {
			            document.getElementById("otpSection").style.display = "block";
			            document.getElementById("passwordSection").style.display = "none";
			        }

			        if (data.accessType === "PASSWORD") {
			            document.getElementById("otpSection").style.display = "none";
			            document.getElementById("passwordSection").style.display = "block";
			        }
			    })
			    .catch(err => {
			        console.error(err);
			        alert("This link is invalid or expired");
			    });

			
			
			
			
			
			// verify access
			
			function verifyAccess() {

			    const value =
			        document.getElementById("otpSection").style.display === "block"
			        ? document.getElementById("otpInput").value
			        : document.getElementById("passwordInput").value;

					fetch(CONTEXT_PATH + "/docaccess/verify", {
					    method: "POST",
					    headers: { "Content-Type": "application/json" },
					    body: JSON.stringify({ token, value })
					})

			    .then(res => {
			        if (!res.ok) throw new Error("Invalid credentials");
			        return res.json();
			    })
			    .then(data => {
			        // Redirect to preview page
			        window.location.href = data.previewUrl;
			    })
			    .catch(() => {
			        alert("Invalid OTP or password");
			    });
			}