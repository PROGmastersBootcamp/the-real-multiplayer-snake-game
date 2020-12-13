# Multiplayer snake game 
### with Spring Boot websocket and pure Javascript/HTML5

Simple application to learn websocket capabilities. 

#### Documentations
- Server: Spring Websocket support: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket
- Client side: https://developer.mozilla.org/en-US/docs/Web/API/WebSocket

#### Features
- Join/Leave game
- Desktop: control with cursor/arrow keys
- Mobile support with html buttons
- only one game at the same time
- no game over, only scores

try at: http://snake.progmasters.hu/

#### Architecture

The game is implemented at server side with Java. Client is only displaying the game state and handles users' command.

Client:
- index.html: html sceleton for
- websocket.js: simple javsacript websocket client event handlers
- game.js: snake game
- colors.js: random color codes

Server:
- Maven based project with one dependency: spring-boot-starter-websocket
- websocket package: basic server side websocket configuration and a message handler (for getting and sending messages to browser)
- Everything is only in memory
