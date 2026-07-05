const loginForm = document.getElementById("loginForm");
const statusMessage = document.getElementById("formStatus");
const submitButton = document.getElementById("submitButton");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");

const tokenStorageKey = "campusJwtToken";
const apiBaseUrl = window.location.port === "5500" ? "http://127.0.0.1:8080" : "";

function setStatus(message, type = "") {
    statusMessage.textContent = message;
    statusMessage.className = `status-message ${type}`.trim();
}

function setLoading(isLoading) {
    submitButton.disabled = isLoading;
    submitButton.textContent = isLoading ? "Logging in..." : "Login";
}

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = emailInput.value.trim();
    const password = passwordInput.value;

    if (!email || !password) {
        setStatus("Enter both email and password.", "error");
        return;
    }

    setLoading(true);
    setStatus("Checking credentials...");

    try {
        const response = await fetch(`${apiBaseUrl}/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        let payload = {};
        try {
            payload = await response.json();
        } catch (error) {
            payload = {};
        }

        if (!response.ok) {
            throw new Error(payload.error || "Login failed. Check your credentials.");
        }

        if (!payload.token) {
            throw new Error("Login succeeded but no token was returned.");
        }

        localStorage.setItem(tokenStorageKey, payload.token);
        setStatus("Login successful. Token saved in this browser.", "success");
        passwordInput.value = "";
    } catch (error) {
        setStatus(error.message || "Unable to log in right now.", "error");
    } finally {
        setLoading(false);
    }
});