package hu.progmasters.snakerealmultiplayer.game.messages.server;

import hu.progmasters.snakerealmultiplayer.game.domain.Snake;
import hu.progmasters.snakerealmultiplayer.game.messages.Message;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class SnakeCoordsMessage implements Message {

    private MessageType type = MessageType.SNAKE_COORDS;
    private Collection<Snake> snakes;

    public SnakeCoordsMessage(Collection<Snake> snakes) {
        this.snakes = snakes.stream()
                            .sorted(Collections.reverseOrder(Comparator.comparingInt(Snake::getScore)))
                            .collect(Collectors.toList());
    }

    public Collection<Snake> getSnakes() {
        return snakes;
    }

    public MessageType getType() {
        return type;
    }
}
