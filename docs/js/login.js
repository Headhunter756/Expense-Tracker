function handleLogin() {
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();
  const errorMsg = document.getElementById("errorMsg");

  if (!email || !password) {
    errorMsg.textContent = "Please enter both email and password.";
    return;
  }

  // Example: Send login data to backend
  fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, password }),
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("Invalid credentials");
      }
    })
    .then((data) => {
      errorMsg.textContent = "";
      sessionStorage.setItem("username", data.username);
      sessionStorage.setItem("token", data.token);
      window.location.href = "main.html";
    })
    .catch((error) => {
      console.error("Login failed:", error);
      errorMsg.textContent = "Login failed. Please check your credentials.";
    });
}
