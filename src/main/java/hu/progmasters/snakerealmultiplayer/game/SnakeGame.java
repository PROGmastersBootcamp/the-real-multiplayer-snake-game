package hu.progmasters.snakerealmultiplayer.game;

import hu.progmasters.snakerealmultiplayer.game.domain.Coord;
import hu.progmasters.snakerealmultiplayer.game.domain.Direction;
import hu.progmasters.snakerealmultiplayer.game.domain.Snake;
import hu.progmasters.snakerealmultiplayer.game.messages.client.ClientMessage;
import hu.progmasters.snakerealmultiplayer.game.messages.server.NewFruitMessage;
import hu.progmasters.snakerealmultiplayer.game.messages.server.SnakeCoordsMessage;
import hu.progmasters.snakerealmultiplayer.websocket.WsMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SnakeGame {
    private static final Logger logger = LoggerFactory.getLogger(SnakeGame.class);

    /**
     * WsMessageHandler is for communicating with the client throug websocket
     */
    @Autowired
    private WsMessageHandler wsMessageHandler;

    private Map<String, Snake> snakes = new ConcurrentHashMap<>();

    private Coord fruit = Coord.getRandomCoord();

    /**
     * Incoming user commands:
     * Join - join game with a name
     * Leave - leave the game
     * U, D, L, R - directions
     */
    public void userCommand(String sessionId, ClientMessage clientMessage) {

        if (clientMessage.getCommand().startsWith("Join")) {
            joinGame(sessionId, clientMessage);

        } else if (clientMessage.getCommand().startsWith("Leave")) {
            leaveGame(sessionId);

        } else if (Arrays.asList("U", "D", "L", "R").contains(clientMessage.getCommand())) {
            changeDirection(sessionId, clientMessage.getCommand());

        } else {
            logger.warn("Unknow command {} from {}", clientMessage, sessionId);
        }
    }

    private Snake joinGame(String sessionId, ClientMessage clientMessage) {
        Snake snake = new Snake(clientMessage.getUserNick(), clientMessage.getColor(), clientMessage.getWhereFrom());
        snakes.put(sessionId, snake);

        //send init information:
        wsMessageHandler.sendTextMessage(sessionId, new NewFruitMessage(fruit));
        logger.info("User joined: {}", clientMessage.getUserNick());
        return snake;
    }

    public void leaveGame(String sessionId) {
        Snake snake = snakes.get(sessionId);
        if (snake != null) {
            logger.info("User left: {}", snake.getUserNick());
            snakes.remove(sessionId);
        }
    }

    private void changeDirection(String sessionId, String command) {
        Snake snake = snakes.get(sessionId);
        if (snake != null) {
            snake.setDirection(Direction.valueOf(command));
        }
    }

    /**
     * Running the game, generate moves and send snake coordinates to users
     */
    @Scheduled(fixedRate = 50)
    public void move() {
        boolean newFruit = false;
        for (Snake snake : snakes.values()) {
            Coord newHead = snake.move();
            if (newHead.equals(fruit)) {
                snake.eat(fruit);
                fruit = Coord.getRandomCoord();
                newFruit = true;
            }
        }

        if (newFruit) {
            NewFruitMessage newFruitMessage = new NewFruitMessage(fruit);
            wsMessageHandler.sendTextMessageToAll(newFruitMessage);
        }
        SnakeCoordsMessage snakeCoordsMessage = new SnakeCoordsMessage(snakes.values());
        wsMessageHandler.sendTextMessageToAll(snakeCoordsMessage);
    }

    //    @Scheduled(fixedRate = 10000)
    public void removeInactiveSnakes() {
        List<String> sessIdsToRemove = new ArrayList<>();
        for (String sessionId : snakes.keySet()) {
            Snake snake = snakes.get(sessionId);
            if (snake.getLastCommandTime().isBefore(LocalDateTime.now().minus(10, ChronoUnit.SECONDS))) {
                sessIdsToRemove.add(sessionId);
            }
        }
        sessIdsToRemove.forEach(this::leaveGame);
    }
}


