document.addEventListener('DOMContentLoaded', () => {
  const loginForm = document.getElementById('loginForm');
  const loginBtn = document.getElementById('loginBtn');

  loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('usernameInput').value.trim();
    const password = document.getElementById('passwordInput').value.trim();

    if (!username || !password) {
      showToast('Please fill in all fields', 'danger');
      return;
    }

    try {
      // Disable button & show spinner
      loginBtn.disabled = true;
      loginBtn.innerHTML = `<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Logging in...`;

      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      const result = await response.json();

      if (response.ok) {
        showToast(`✅ ${result.message}`, 'success');
        setTimeout(() => {
          window.location.href = '/index';
        }, 1500);
      } else {
        showToast(`❌ ${result.error || 'Login failed'}`, 'danger');
      }
    } catch (err) {
      console.error('Login Error:', err);
      showToast('Something went wrong. Try again later.', 'danger');
    } finally {
      loginBtn.disabled = false;
      loginBtn.innerHTML = 'Login';
    }
  });

  // Create toast container if not exists
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
  toast.className = `toast align-items-center text-bg-${type} border-0 show`;
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

  // Auto-dismiss
  setTimeout(() => {
    const bsToast = bootstrap.Toast.getOrCreateInstance(toast);
    bsToast.hide();
    toast.addEventListener('hidden.bs.toast', () => toast.remove());
  }, 3500);
}
