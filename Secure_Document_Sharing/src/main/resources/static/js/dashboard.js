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
	 
