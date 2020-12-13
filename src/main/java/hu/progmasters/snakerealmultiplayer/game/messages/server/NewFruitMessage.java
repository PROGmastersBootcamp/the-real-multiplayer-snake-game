package hu.progmasters.snakerealmultiplayer.game.messages.server;

import hu.progmasters.snakerealmultiplayer.game.domain.Coord;
import hu.progmasters.snakerealmultiplayer.game.messages.Message;

public class NewFruitMessage implements Message {

    private MessageType type = MessageType.NEW_FRUIT;
    private Coord fruit;

    public NewFruitMessage( Coord fruit) {
        this.fruit = fruit;
    }

    public MessageType getType() {
        return type;
    }
    public Coord getFruit() {
        return fruit;
    }
}
