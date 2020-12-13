package hu.progmasters.snakerealmultiplayer.game.domain;

public enum Direction {
    U(0, -1),
    D(0, 1),
    L(-1, 0),
    R(1, 0);

    int dx;
    int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
