package com.example.visualphysics10.engine;

public class Vector2 {
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private final double x;
    private final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
