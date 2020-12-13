package hu.progmasters.snakerealmultiplayer.game.messages.server;

import hu.progmasters.snakerealmultiplayer.game.domain.Snake;
import hu.progmasters.snakerealmultiplayer.game.messages.Message;

import java.util.Collection;

public class SnakeCoordsMessage implements Message {

    private MessageType type = MessageType.SNAKE_COORDS;
    private Collection<Snake> snakes;

    public SnakeCoordsMessage(Collection<Snake> snakes) {
        this.snakes = snakes;
    }

    public Collection<Snake> getSnakes() {
        return snakes;
    }

    public MessageType getType() {
        return type;
    }
}
