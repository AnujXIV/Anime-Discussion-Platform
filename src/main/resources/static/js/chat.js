document.addEventListener('DOMContentLoaded', () => {
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    const chatBox = document.getElementById('chatBox');
    const form = document.getElementById('chatForm');
    const input = document.getElementById('chatInput');

    stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/discussions/${discussionId}`, (msg) => {
            const message = JSON.parse(msg.body);
            appendMessage(message.username, message.content);
        });
    });

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const message = input.value.trim();
        if (message) {
            stompClient.send(`/app/discussions/${discussionId}`, {}, JSON.stringify({ username, content: message }));
            input.value = '';
        }
    });

    function appendMessage(sender, content) {
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + (sender === username ? 'user' : 'other');
        msgDiv.innerHTML = `<strong>${sender}</strong>: ${content}`;
        chatBox.appendChild(msgDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    }
});
