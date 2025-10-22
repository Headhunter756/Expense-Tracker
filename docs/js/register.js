function handleRegister() {
  const name = document.getElementById("name").value.trim();
  const email = document.getElementById("email").value.trim();
  const age = parseInt(document.getElementById("age").value.trim());
  const password = document.getElementById("password").value;
  const confirmPassword = document.getElementById("confirmPassword").value;
  const errorMsg = document.getElementById("errorMsg");
  const image = document.getElementById("image").files[0];

  // Basic validation
  if (!name || !email || !age || !password || !confirmPassword || !image) {
    errorMsg.textContent = "All fields are required.";
    return;
  }

  if (!/^\S+@\S+\.\S+$/.test(email)) {
    errorMsg.textContent = "Invalid email format.";
    return;
  }

  if (age < 1 || isNaN(age)) {
    errorMsg.textContent = "Please enter a valid age.";
    return;
  }

  if (password.length < 6) {
    errorMsg.textContent = "Password must be at least 6 characters.";
    return;
  }

  if (password !== confirmPassword) {
    errorMsg.textContent = "Passwords do not match.";
    return;
  }

  // Send data to backend
  const user = {
    name: name,
    email: email,
    age: age,
    password: confirmPassword,
  };
  const formdate = new FormData();
  formdate.append(
    "user",
    new Blob([JSON.stringify(user)], { type: "application/json" })
  );
  formdate.append("image", image);

  fetch("http://techhunters-expense-tracker.up.railway.app/auth/register", {
    method: "POST",
    body: formdate,
  })
    .then((response) => {
      if (response.ok) {
        errorMsg.textContent = "";
        alert("Registration successful!");
      } else {
        throw new Error("Registration failed");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      errorMsg.textContent = "Registration failed. Try again.";
    });
}
