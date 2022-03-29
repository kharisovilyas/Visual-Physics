package com.example.visualphysics10.engine;

import android.content.Context;
import android.graphics.Canvas;

public abstract class Sprite {
    public Sprite(Context context) { }

    public abstract void update(Canvas canvas);

    public abstract void destroy(Canvas canvas);
}
