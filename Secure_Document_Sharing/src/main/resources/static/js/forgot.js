

/*
const CONTEXT_PATH = [[@{/}]] '';

// Call backend REST API

document.querySelector("form").addEventListener("submit", function (e) {
    e.preventDefault();

    const email = document.querySelector("input[name='email']").value;

    fetch(CONTEXT_PATH + "forgot-password", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email })
    })
    .then(res => res.json())
    .then(data => {
				const messageDiv = document.createElement("div");
				messageDiv.className = "alert alert-success mt-3";
				messageDiv.innerText = message;
				form.prepend(messageDiv);

        if (data.next) {
            window.location.href = data.next;
        }
    })
    .catch(err => {
        console.error(err);
        alert("Something went wrong");
    });
});

*/


const CONTEXT_PATH = /*[[@{/}]]*/ '';

const form = document.querySelector("form");
const submitBtn = document.getElementById("submitBtn");
const btnText = document.getElementById("btnText");
const btnSpinner = document.getElementById("btnSpinner");

form.addEventListener("submit", function (e) {
    e.preventDefault();

    const email = document.querySelector("input[name='email']").value;

    // Show loading state
    submitBtn.disabled = true;
    btnSpinner.classList.remove("d-none");
    btnText.innerText = "Sending...";

    fetch(CONTEXT_PATH + "forgot-password", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email })
    })
    .then(res => {
        if (!res.ok) {
            return res.json().then(err => { throw new Error(err.error || "Error"); });
        }
        return res.json();
    })
    .then(data => {

        // Show success message
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