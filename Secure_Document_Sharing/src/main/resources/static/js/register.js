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
	   
	   
	   
	   
	   document.getElementById("registerForm").addEventListener("submit", function (e) {
	       e.preventDefault();

	       const user = {
	           firstName: document.querySelector("input[name='firstName']").value,
	           lastName: document.querySelector("input[name='lastName']").value,
	           email: document.querySelector("input[name='email']").value,
	           password: document.querySelector("input[name='password']").value
	       };

	       fetch("/Secure_Document_Sharing/userOperations/register", {
	           method: "POST",
	           headers: {
	               "Content-Type": "application/json"
	           },
	           body: JSON.stringify(user)
	       })
	       .then(response => {
	           if (!response.ok) {
	               throw new Error("Registration failed");
	           }
	           return response.text();
	       })
	       .then(data => {
	           alert(data);
	           window.location.href = "/Secure_Document_Sharing/login";
	       })
	       .catch(error => {
	           alert(error.message);
	       });
	   });
