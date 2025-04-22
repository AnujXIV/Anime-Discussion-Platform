document.addEventListener('DOMContentLoaded', () => {
  const registerForm = document.getElementById('registerForm');
  const registerBtn = document.getElementById('registerBtn');

  registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('usernameInput').value.trim();
    const email = document.getElementById('emailInput').value.trim();
    const password = document.getElementById('passwordInput').value.trim();

    if (!username || !email || !password) {
      showToast('⚠️ Please fill in all fields', 'danger');
      return;
    }

    try {
      // Loading UI
      registerBtn.disabled = true;
      registerBtn.innerHTML = `<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Registering...`;

      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password })
      });

      const result = await response.json();

      if (response.ok) {
        showToast('✅ Registered successfully! Redirecting...', 'success');
        setTimeout(() => {
          window.location.href = '/login';
        }, 2000);
      } else {
        showToast(`❌ ${result.error || 'Registration failed'}`, 'danger');
      }
    } catch (err) {
      console.error('🚨 Registration error:', err);
      showToast('🚫 Server error. Please try again later.', 'danger');
    } finally {
      registerBtn.disabled = false;
      registerBtn.innerHTML = 'Register';
    }
  });

  // Ensure a toast container exists
  if (!document.getElementById('toast-container')) {
    const toastContainer = document.createElement('div');
    toastContainer.id = 'toast-container';
    toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    document.body.appendChild(toastContainer);
  }
});

function showToast(message, type = 'info') {
  const toastId = `toast-${Date.now()}`;
  const toast = document.createElement('div');
  toast.id = toastId;
  toast.className = `toast align-items-center text-bg-${type} border-0 show mb-2`;
  toast.setAttribute('role', 'alert');
  toast.setAttribute('aria-live', 'assertive');
  toast.setAttribute('aria-atomic', 'true');

  toast.innerHTML = `
    <div class="d-flex">
      <div class="toast-body">${message}</div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  `;

  const container = document.getElementById('toast-container');
  container.appendChild(toast);

  setTimeout(() => {
    const bsToast = bootstrap.Toast.getOrCreateInstance(toast);
    bsToast.hide();
    toast.addEventListener('hidden.bs.toast', () => toast.remove());
  }, 3500);
}
