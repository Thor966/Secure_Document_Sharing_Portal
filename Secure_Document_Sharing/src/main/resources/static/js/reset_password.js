function togglePassword(inputId, icon) {
       const input = document.getElementById(inputId);

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
   
   
   
   
   
   
   const CONTEXT_PATH = /*[[@{/}]]*/ '';

   const form = document.getElementById("resetForm");
   const newPasswordInput = document.getElementById("newPassword");
   const confirmPasswordInput = document.getElementById("confirmPassword");
   const submitBtn = document.getElementById("submitBtn");
   const btnText = document.getElementById("btnText");
   const btnSpinner = document.getElementById("btnSpinner");

   form.addEventListener("submit", function (e) {
       e.preventDefault();

       const newPassword = newPasswordInput.value.trim();
       const confirmPassword = confirmPasswordInput.value.trim();

       //  Frontend validation
       if (newPassword.length < 6) {
           alert("Password must be at least 6 characters");
           return;
       }

       if (newPassword !== confirmPassword) {
           alert("Passwords do not match");
           return;
       }
	   
	   // Show loading state
	   		   submitBtn.disabled = true;
	   		   btnSpinner.classList.remove("d-none");
	   		   btnText.innerText = "Updating...";

       // Send to backend
       fetch(CONTEXT_PATH + "reset-password", {
           method: "POST",
           headers: {
               "Content-Type": "application/json"
           },
           body: JSON.stringify({
               password: newPassword
           })
       })
       .then(res => {
           if (!res.ok) {
               return res.text().then(text => { throw new Error(text); });
           }
           return res.text();
       })
       .then(message => {
		const messageDiv = document.createElement("div");
		messageDiv.className = "alert alert-success mt-3";
		messageDiv.innerText = message;
		form.prepend(messageDiv);
		
		// Small delay for UX (1 second)
		       setTimeout(() => {
				// Redirect to login page after success
				window.location.href = CONTEXT_PATH + "login";
		       }, 1000);

         
          
       })
	   .catch(err => {
	           console.error(err);

	           const errorDiv = document.createElement("div");
	           errorDiv.className = "alert alert-danger mt-3";
	           errorDiv.innerText = err.message;
	           form.prepend(errorDiv);
	       })
	       .finally(() => {
	           // Restore button if error happens
	           submitBtn.disabled = false;
	           btnSpinner.classList.add("d-none");
	           btnText.innerText = "Submit";
	       });
   });