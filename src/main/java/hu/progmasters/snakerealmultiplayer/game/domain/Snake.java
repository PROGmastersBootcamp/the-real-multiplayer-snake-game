/*
 * Copyright © Progmasters (QTC Kft.), 2018.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */
package hu.progmasters.snakerealmultiplayer.game.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private String userNick;
    private String color;
    private Integer score;
    private List<Coord> coordinates = new ArrayList<>();
    private Direction direction = Direction.R;
    private LocalDateTime lastCommandTime;

    public Snake() {

    }

    public Snake(String userNick, String color) {
        this.userNick = userNick;
        this.color = color;
        this.score = 0;
        this.lastCommandTime = LocalDateTime.now();
        //add random init coords:

        Coord rndCoord = Coord.getRandomCoord();
        coordinates.add(rndCoord);
        coordinates.add(new Coord(rndCoord.x + 1, rndCoord.y));
        coordinates.add(new Coord(rndCoord.x + 2, rndCoord.y));
        coordinates.add(new Coord(rndCoord.x + 3, rndCoord.y));
        coordinates.add(new Coord(rndCoord.x + 4, rndCoord.y));
    }


    public Coord move() {
        Coord oldHead = coordinates.get(coordinates.size() - 1);
        Coord newHead = new Coord(oldHead.x + direction.dx, oldHead.y + direction.dy);

        //handle borders:
        if (newHead.x >= Coord.mx) newHead.x = 0;
        if (newHead.y >= Coord.my) newHead.y = 0;
        if (newHead.x < 0) newHead.x = Coord.mx - 1;
        if (newHead.y < 0) newHead.y = Coord.my - 1;

        coordinates.add(newHead);
        coordinates.remove(0);

        return newHead;
    }

    public void eat(Coord head) {
        coordinates.add(head);
        score++;
    }

    public String getUserNick() {
        return userNick;
    }

    public List<Coord> getCoordinates() {
        return coordinates;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.lastCommandTime = LocalDateTime.now();
        this.direction = direction;
    }

    public LocalDateTime getLastCommandTime() {
        return lastCommandTime;
    }

    public String getColor() {
        return color;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Snake{" +
                "userNick:'" + userNick + '\'' +
                ", coordinates:" + coordinates +
                '}';
    }
}
