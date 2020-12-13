package hu.progmasters.snakerealmultiplayer.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.progmasters.snakerealmultiplayer.game.SnakeGame;
import hu.progmasters.snakerealmultiplayer.game.messages.Message;
import hu.progmasters.snakerealmultiplayer.game.messages.client.ClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsMessageHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WsMessageHandler.class);
    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * objectMapper is for JSON <==> JAVA object auto mapping.
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SnakeGame snakeGame;

    /**
     * Incoming messages.
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        snakeGame.userCommand(session.getId(), objectMapper.readValue(message.getPayload(), ClientMessage.class));
    }

    public void sendTextMessage(String sessionId, Message message) {
        try {
            sessions.get(sessionId).sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (Exception e) {
            logger.error("Could not send message: {}. Remove websocket session.", message, e);
            sessions.remove(sessionId);
        }
    }

    public void sendTextMessageToAll(Message message) {
        List<String> sessionsToRemove = new ArrayList<>();
        for (WebSocketSession session : sessions.values()) {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (Exception e) {
                logger.error("Could not send message: {} Remove websocket session.", message, e);
                sessionsToRemove.add(session.getId());
            }
        }
        for (String sessId : sessionsToRemove) {
            sessions.remove(sessId);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Websocket connection session closed: {}", session.getId());
        snakeGame.leaveGame(session.getId());
        sessions.remove(session.getId());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("New websocket connection: {}", session.getId());
        WebSocketSession threadSafeSession = new ConcurrentWebSocketSessionDecorator(session, 100, 1000000);
        sessions.put(session.getId(), threadSafeSession);
    }

}