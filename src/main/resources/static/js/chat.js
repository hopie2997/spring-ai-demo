document.addEventListener('DOMContentLoaded', function () {
    const chatBox = document.getElementById('chat-box');
    const initialMessage = "Hello, I'm MFV AI assistant. How can I help you?";
    const wrapperElement = document.createElement('div');
    wrapperElement.className = 'message-wrapper';

    const responseElement = document.createElement('div');
    responseElement.className = 'alert alert-primary d-inline-flex align-items-center bot-message';

    const botAvatar = document.createElement('img');
    botAvatar.src = '/images/bot_avatar.jpg';
    botAvatar.className = 'avatar bot-avatar';

    const responseText = document.createElement('span');
    responseText.textContent = initialMessage;

    responseElement.appendChild(botAvatar);
    responseElement.appendChild(responseText);
    wrapperElement.appendChild(responseElement);
    chatBox.appendChild(wrapperElement);
});

document.getElementById('chat-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const message = document.getElementById('message').value;
    if (message.trim() !== '') {
        const chatBox = document.getElementById('chat-box');
        const wrapperElement = document.createElement('div');
        wrapperElement.className = 'message-wrapper';

        const messageElement = document.createElement('div');
        messageElement.className = 'alert alert-secondary d-inline-flex align-items-center justify-content-end user-message';

        const avatar = document.createElement('img');
        avatar.src = '/images/user_avatar.jpg';
        avatar.className = 'avatar user-avatar';

        const text = document.createElement('span');
        text.textContent = message;

        messageElement.appendChild(text);
        messageElement.appendChild(avatar);
        wrapperElement.appendChild(messageElement);
        chatBox.appendChild(wrapperElement);

        fetch(`/api/chat/mfv?query=${encodeURIComponent(message)}`)
            .then(response => response.text())
            .then(data => {
                const wrapperElement = document.createElement('div');
                wrapperElement.className = 'message-wrapper';

                const responseElement = document.createElement('div');
                responseElement.className = 'alert alert-primary d-inline-flex align-items-center bot-message';

                const botAvatar = document.createElement('img');
                botAvatar.src = '/images/bot_avatar.jpg';
                botAvatar.className = 'avatar bot-avatar';

                const responseText = document.createElement('span');
                responseText.textContent = data;

                responseElement.appendChild(botAvatar);
                responseElement.appendChild(responseText);
                wrapperElement.appendChild(responseElement);
                chatBox.appendChild(wrapperElement);
            });

        document.getElementById('message').value = '';
    }
});
