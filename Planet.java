package com.example.gamebasic;

public class Planet {
    int x, y;
    int planetSpeed = 5;
    int dir;  // 0이면 왼쪽으로 1이면 오른쪽으로 이동

    Planet(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void move() {
        if(dir == 0) {
            x -= planetSpeed;
        }
        else {
            x += planetSpeed;
        }
    }
}
