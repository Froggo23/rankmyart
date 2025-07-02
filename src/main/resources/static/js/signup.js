const container = document.querySelector('.container');
const registerBtn = document.querySelector('.register-btn');
const loginBtn = document.querySelector('.login-btn');

// Form elements
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const loginMessage = document.getElementById('loginMessage');
const registerMessage = document.getElementById('registerMessage');

// Toggle between login and register forms
registerBtn.addEventListener('click', () => {
  container.classList.add('active');
  // Clear messages when switching forms
  loginMessage.textContent = '';
  registerMessage.textContent = '';
});

loginBtn.addEventListener('click', () => {
  container.classList.remove('active');
  // Clear messages when switching forms
  loginMessage.textContent = '';
  registerMessage.textContent = '';
});

// --- Handle Login Form Submission ---
loginForm.addEventListener('submit', async (e) => {
  e.preventDefault(); // Prevent default form submission (page reload)

  loginMessage.textContent = ''; // Clear previous messages

  const formData = new FormData(loginForm);
  // FormData automatically handles 'name' attributes of inputs

  try {
    const response = await fetch('/login', {
      method: 'POST',
      body: new URLSearchParams(formData) // Send as URL-encoded form data
      // No 'Content-Type' header needed for URLSearchParams, fetch sets it automatically
    });

    const result = await response.text(); // Read response text

    if (response.ok) {


      document.cookie = `username=${loginForm.username.value}; expires=${new Date(Date.now() + 300 * 60 * 1000).toUTCString()}; path=/`;
      alert('Login succeeded!');
      // Login successful
      loginMessage.style.color = 'green';
      loginMessage.textContent = result + " Redirecting...";
      // Redirect to gallery or dashboard on successful login
      window.location.href = '/gallery';
    } else {
      // Login failed (e.g., 401 Unauthorized)
      loginMessage.style.color = 'red';
      loginMessage.textContent = result || "Login failed.";
    }
  } catch (error) {
    console.error('Error during login:', error);
    loginMessage.style.color = 'red';
    loginMessage.textContent = 'An error occurred during login. Please try again.';
  }
});


// --- Handle Registration Form Submission ---
registerForm.addEventListener('submit', async (e) => {
  e.preventDefault(); // Prevent default form submission (page reload)

  registerMessage.textContent = ''; // Clear previous messages

  const formData = new FormData(registerForm);

  try {
    const response = await fetch('/register', {
      method: 'POST',
      body: new URLSearchParams(formData) // Send as URL-encoded form data
    });

    const result = await response.text(); // Read response text

    if (response.ok) {
      // Registration successful (e.g., 200 OK)
      registerMessage.style.color = 'green';
      registerMessage.textContent = result + " You can now log in!";
      // Optionally, switch to the login form after successful registration
      container.classList.remove('active');
      loginMessage.textContent = ''; // Clear login message just in case
    } else {
      // Registration failed (e.g., 409 Conflict)
      registerMessage.style.color = 'red';
      registerMessage.textContent = result || "Registration failed.";
    }
  } catch (error) {
    console.error('Error during registration:', error);
    registerMessage.style.color = 'red';
    registerMessage.textContent = 'An error occurred during registration. Please try again.';
  }
});


// --- Preloader Script (from your HTML, moved here for consolidation) ---
window.addEventListener("load", () => {
  const preloader = document.getElementById("preloader");
  if (preloader) { // Check if preloader exists
    preloader.style.opacity = "0";
    setTimeout(() => {
      preloader.style.display = "none";
    }, 600); // matches the CSS transition
  }
});