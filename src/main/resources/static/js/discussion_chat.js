let stompClient = null;
const discussionId = document.querySelector('input[name="discussionId"]').value;
let currentUserId = null;
let currentUsername = null;

// 🔐 Fetch current logged-in user
fetch('/api/auth/me')
    .then(res => {
        if (!res.ok) throw new Error('Not authenticated or session expired');
        return res.json();
    })
    .then(user => {
        currentUserId = user.userId;
        currentUsername = user.username;
        console.log("✅ Logged-in user:", currentUsername);
        connectWebSocket();
    })
    .catch(err => {
        console.error("❌ Failed to fetch current user", err);
    });

// 🕓 Load previous messages from REST
fetch(`/api/messages/discussion/${discussionId}`)
    .then(res => res.json())
    .then(messages => {
        messages.forEach(displayMessage);
    })
    .catch(err => {
        console.error("❌ Failed to load messages", err);
    });


function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/discussion/${discussionId}`, (message) => {
            const msg = JSON.parse(message.body);
            displayMessage(msg);
        });
    });
}

function displayMessage(msg) {
    const chatBox = document.getElementById('chat-box');
    const wrapper = document.createElement('div');
    wrapper.className = 'mb-4';
    wrapper.innerHTML = `
        <div class="d-flex align-items-start">
            <img src="/images/avatar-placeholder.png" class="rounded-circle me-3" width="42" height="42">
            <div>
                <div class="fw-bold text-light mb-1">${msg.user.username}</div>
                <div class="bg-secondary text-light p-3 rounded-3">${msg.message}</div>
                <div class="small text-muted mt-1">${new Date(msg.timestamp).toLocaleString()}</div>
            </div>
        </div>
    `;
    chatBox.appendChild(wrapper);
    chatBox.scrollTop = chatBox.scrollHeight;
}


document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('messageForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const input = form.querySelector('input[name="message"]');
        const messageText = input.value.trim();
        if (!messageText || !stompClient) return;

        const payload = {
            message: messageText,
            user: { id: currentUserId, username: currentUsername },
            discussion: { id: parseInt(discussionId) }
        };

        stompClient.send(`/app/chat.send/${discussionId}`, {}, JSON.stringify(payload));
        input.value = '';
    });
});
