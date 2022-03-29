package com.example.visualphysics10.engine;

import android.content.Context;
import android.graphics.Rect;

public abstract class PhysicsSprite extends Sprite {
    public PhysicsSprite(Context context) {
        super(context);
    }

    public abstract Rect getSize();

    public abstract boolean checkCollation(Rect rect, Vector2 velocity);

    public abstract double addForce(Vector2 velocity);

    public abstract Vector2 getVelocity();

}
