let canvas;
let ctx;
const mx = 50;
const my = 50;

let fruit = {x: -1, y: -1};

let snakes = {
    "wsSessionId1": {
        'userNick': 'Donkey1',
        'whereFrom': 'home',
        'color': '#000000',
        'coordinates': [],
    },
    "wsSessionId2": {
        'userNick': 'Donkey2',
        'whereFrom': 'personal',
        'color': '#FF0000',
        'coordinates': [],
    }
};

let messageFromServer = function (event) {
    let message = JSON.parse(event.data);
    switch (message.type) {
        case "SNAKE_COORDS":
            snakes = message.snakes;
            draw();
            break;
        case "NEW_FRUIT":
            fruit = message.fruit;
            break;
    }
};

const draw = () => {
    ctx.clearRect(0, 0, mx * 10, my * 10);
    const players = document.getElementById("players");
    players.innerHTML = "";

    for (let key in snakes) {
        const snakeData = snakes[key];
        drawSnake(snakeData);
        refreshPlayersTable(snakeData, players);
    }

    ctx.fillStyle = "#ff59e3";
    ctx.fillRect(fruit.x * 10, fruit.y * 10, 10, 10);
};

let drawSnake = function (snakeData) {
    ctx.fillStyle = snakeData.color;
    snakeData.coordinates.forEach((coord) => {
        ctx.fillRect(coord.x * 10, coord.y * 10, 10, 10);
    });
};

const refreshPlayersTable = function (snakeData, players) {
    let div = document.createElement('div');
    div.innerHTML = snakeData.userNick + ', score: ' + snakeData.score;
    div.style.color = snakeData.color;
    if (snakeData.whereFrom === 'home') {
        div.style.border = 'thin solid' + '#f80313';
    } else {
        div.style.border = 'thin solid' + '#0954df';
    }
    div.style.padding = '2px';
    div.style.margin = '2px';
    div.style.textAlign = 'center';
    players.appendChild(div);
};

document.addEventListener("keydown", (e) => {
    if (e.code === "ArrowUp") {
        socket.send('{"command": "U"}');
        e.preventDefault();
    }
    if (e.code === "ArrowDown") {
        socket.send('{"command": "D"}');
        e.preventDefault();
    }
    if (e.code === "ArrowRight") {
        socket.send('{"command": "R"}');
        e.preventDefault();
    }
    if (e.code === "ArrowLeft") {
        socket.send('{"command": "L"}');
        e.preventDefault();
    }
});

document.addEventListener("DOMContentLoaded", () => {
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');
    let joinButton = document.getElementById('join');
    let leaveButton = document.getElementById('leave');
    let userNick = document.getElementById('userNick');
    let snakeColor = document.getElementById('snakeColor');

    userNick.value = 'Donkey' + Math.floor(Math.random() * 1000) + 1;
    snakeColor.value = COLORS[Math.floor(Math.random() * COLORS.length)];

    document.getElementById('home').onchange = () => {
        if (leaveButton.disabled === true) {
            joinButton.disabled = false;
        }
    };
    document.getElementById('personal').onchange = () => {
        if (leaveButton.disabled === true) {
            joinButton.disabled = false;
        }
    };
    document.getElementById('join').onclick = () => {
        document.getElementById('status').innerHTML = 'Joined to game.';
        const joinMessage = {
            command: 'Join',
            userNick: userNick.value,
            whereFrom: document.querySelector('input[name="whereFrom"]:checked').value,
            color: snakeColor.value,
        };
        socket.send(JSON.stringify(joinMessage));
        joinButton.disabled = true;
        leaveButton.disabled = false;
    };

    document.getElementById("leave").onclick = () => {
        document.getElementById("status").innerHTML = "Left from game.";
        socket.send(JSON.stringify({command: 'Leave'}));
        joinButton.disabled = false;
        leaveButton.disabled = true;
    }

    document.getElementById("UP").ontouchstart = (e) => {
        e.preventDefault();
        socket.send('{"command": "U"}');
    }
    document.getElementById("DOWN").ontouchstart = (e) => {
        e.preventDefault();
        socket.send('{"command": "D"}');
    }
    document.getElementById("RIGHT").ontouchstart = (e) => {
        e.preventDefault();
        socket.send('{"command": "R"}');
    }
    document.getElementById("LEFT").ontouchstart = (e) => {
        e.preventDefault();
        socket.send('{"command": "L"}');
    }
});


