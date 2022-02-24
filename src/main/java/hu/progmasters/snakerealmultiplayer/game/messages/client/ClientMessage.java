package hu.progmasters.snakerealmultiplayer.game.messages.client;

import hu.progmasters.snakerealmultiplayer.game.messages.Message;

public class ClientMessage implements Message {
    protected String command;
    private String userNick;
    private String whereFrom;
    private String color;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getWhereFrom() {
        return whereFrom;
    }

    public void setWhereFrom(String whereFrom) {
        this.whereFrom = whereFrom;
    }

}
