let socket;

document.addEventListener("DOMContentLoaded", () => {
    socket = new WebSocket('ws://localhost:8080/snakeWebsocketServer');
    // socket = new WebSocket('ws://snake.progmasters.hu/snakeWebsocketServer');
    // socket = new WebSocket('ws://192.168.1.18:8080/snakeWebsocketServer');

    // Connection opened
    socket.addEventListener('open', function (event) {
        document.getElementById("status").innerHTML = "Websocket connected";
    });

    // Connection closed
    socket.addEventListener('close', function (event) {
        document.getElementById("status").innerHTML = "Websocket disconnected";
    });

    // Listen for messages
    socket.addEventListener('message', function (event) {
        messageFromServer(event);
    });
});


