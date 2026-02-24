const inputs = document.querySelectorAll('.otp-input');

    inputs.forEach((input, index) => {
        input.addEventListener('input', () => {
            if (input.value && index < inputs.length - 1) {
                inputs[index + 1].focus();
            }
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !input.value && index > 0) {
                inputs[index - 1].focus();
            }
        });
    });
	
	
	
	const CONTEXT_PATH = /*[[@{/}]]*/ '';

	const form = document.getElementById("otpForm");
	const submitBtn = document.getElementById("submitBtn");
	const btnText = document.getElementById("btnText");
	const btnSpinner = document.getElementById("btnSpinner");

	/*  Auto move to next input*/
	inputs.forEach((input, index) => {

	    input.addEventListener("input", function () {
	        this.value = this.value.replace(/[^0-9]/g, "");

	        if (this.value.length === 1 && index < inputs.length - 1) {
	            inputs[index + 1].focus();
	        }
	    });

	    input.addEventListener("keydown", function (e) {
	        if (e.key === "Backspace" && !this.value && index > 0) {
	            inputs[index - 1].focus();
	        }
	    });
	});

	/* ================================
	   Submit OTP
	================================ */
	form.addEventListener("submit", function (e) {
	    e.preventDefault();

	    let otp = "";

	    inputs.forEach(input => {
	        otp += input.value;
	    });

	    if (otp.length !== 6) {
	        alert("Please enter complete 6-digit OTP");
	        return;
	    }
		
		// Show loading state
		   submitBtn.disabled = true;
		   btnSpinner.classList.remove("d-none");
		   btnText.innerText = "Verifying...";

	    fetch(CONTEXT_PATH + "verify-otp", {
	        method: "POST",
	        headers: {
	            "Content-Type": "application/json"
	        },
	        body: JSON.stringify({ otp: otp })
	    })
		.then(res => {
		    if (!res.ok) {
		        return res.json().then(err => { throw new Error(err.error); });
		    }
		    return res.json();
		})
		.then(data => {

			const messageDiv = document.createElement("div");
					messageDiv.className = "alert alert-success mt-3";
					messageDiv.innerText = data.message;
					form.prepend(messageDiv);
					
					// Small delay for UX (1 second)
					        setTimeout(() => {
					            if (data.next) {
					                window.location.href = CONTEXT_PATH + data.next;
					            }
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