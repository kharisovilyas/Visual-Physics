package com.example.visualphysics10.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;

import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.engine.PhysicsSprite;
import com.example.visualphysics10.engine.Vector2;


public class PhysicsModel extends PhysicsSprite {
    double x;
    public static boolean beginning = false;
    double y;
    public static boolean firstDraw = true;
    public static double x0;
    public static double y0;
    public static int l = 150;
    public static int h = 150;
    int index;
    double mokV = 0;
    double[] vectorXs = new double[2];
    double vectorX;
    double vectorY;
    double vectorI;
    double p;
    double m;
    double vectorXR;
    double vectorYR;
    public static boolean isTouchedNEP = false;
    public static boolean isL2Ended = false;
    public static boolean isTouchedI = false;
    public static boolean onEarth = false;
    public static boolean onBoard = false;
    public static boolean onStopClick = false;
    public static boolean onStopClick2 = false;
    public static boolean onRestartClick = false;
    public static boolean L1 = false;
    public static boolean L2 = false;
    public static boolean L3 = false;
    public static boolean L4 = false;
    public static boolean L5 = false;
    int angle = 0;
    int n = 1;
    final static double g = 9.8;
    double F;
    private final Paint paint = new Paint();
    private final Paint paint2 = new Paint();
    private final Paint paint4 = new Paint();
    private final Paint paint3 = new Paint();


    public PhysicsModel(Context context, double x, double y, double vectorX, double vectorY) {
        super(context);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 250, 50, 50));
        paint.setStrokeWidth(15);
        paint3.setStyle(Paint.Style.FILL_AND_STROKE);
        paint3.setStrokeWidth(15);
        paint3.setColor(Color.argb(100, 250, 50, 250));
        paint2.setColor(Color.BLACK);
        paint4.setColor(Color.WHITE);
        paint2.setStyle(Paint.Style.STROKE);
        paint4.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(10);
        paint4.setStrokeWidth(15);
        this.x = x;
        this.y = y;
        x0 = x;
        y0 = y;
        this.vectorX = vectorX;
        this.vectorY = vectorY;

    }

    public PhysicsModel(Context context, double x, double y, double vectorX, double vectorY, int index) {
        super(context);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(100, 250, 50, 50));
        paint.setStrokeWidth(15);
        paint3.setStyle(Paint.Style.FILL_AND_STROKE);
        paint3.setStrokeWidth(15);
        paint3.setColor(Color.argb(100, 250, 50, 250));
        paint2.setColor(Color.BLACK);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(10);
        this.x = x;
        this.y = y;
        x0 = x;
        y0 = y;
        this.vectorX = vectorX;
        this.vectorY = vectorY;
        this.index = index;

    }

    private DrawerArrowDrawable getResources() {
        return null;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public Rect getSize() {
        return new Rect((int) x, (int) y, (int) x + l, (int) y + h);
    }


    @Override
    public boolean checkCollation(Rect rect, Vector2 velocity) {
        boolean result = rect.intersect((int) x, (int) y, (int) x + l, (int) y + h);
        if (result) {
            isTouchedI = true;
            if (!isTouchedNEP) {
                vectorX = -vectorX;
                vectorY = -vectorY;
            }
        }

        return result;
    }

    @Override
    public double addForce(Vector2 velocity) {
        return F;
    }

    @Override
    public Vector2 getVelocity() {
        return new Vector2(vectorX, vectorY);
    }

    @Override
    public void update(Canvas canvas) {
        if (L1) {
            paint3.setColor(Color.argb(255, 255, 240, 0));
            paint.setColor(Color.argb(255, 255, 200, 0));
        }
        if (L2) {
            paint.setColor(Color.argb(255, 0, 255, 255));
            paint3.setColor(Color.argb(255, 0, 206, 209));
        }
        if (L3) {
            paint3.setColor(Color.argb(255, 65, 105, 255));
            paint.setColor(Color.argb(255, 125, 190, 255));
        }
        if (L4) {
            paint.setColor(Color.argb(255, 176, 224, 230));
            paint3.setColor(Color.argb(255, 135, 206, 250));

        }
        if (L5) {
            if (index == 0) {
                paint3.setColor(Color.argb(255, 255, 95, 55));
                paint.setColor(Color.argb(255, 255, 180, 115));
            } else if (index == 1) {
                paint3.setColor(Color.argb(255, 65, 105, 255));
                paint.setColor(Color.argb(255, 125, 190, 255));
            }
        }
        Rect rect = new Rect();
        updateVector(vectorX, vectorY);
        if (L1 || L3 || L4 || L5) {
            x = x + vectorX;
            y = y + vectorY;
            rect = new Rect((int) x, (int) y, (int) x + l, (int) y + h);
            checkBoard(canvas.getWidth(), canvas.getHeight());
        } else if (L2) {
            if (!isL2Ended) {
                canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (float) PhysicsData.getRadius(), paint2);
            }
            else{
                canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (float) PhysicsData.getRadius(), paint4);

            }
            x = x0 + vectorX;
            y = y0 + vectorY;
            rect = new Rect((int) x, (int) y, (int) x + l, (int) y + h);
        } else {
            rect = new Rect(0, 0, 0, 0);
        }
        if (L2) {
            canvas.drawCircle((float) x, (float) y, l / 2, paint3);
            canvas.drawCircle((float) x, (float) y, l / 2, paint);
        } else {
            canvas.drawRect(rect, paint3);
            canvas.drawRect(rect, paint);
        }

    }

    public void updateVector(double vX, double vY) {
        vectorX = vX;
        vectorY = vY;
    }

    public void updateAC(double radius, double angleV) {
        vectorXR = radius * Math.cos((Math.PI / 180) * angle);
        vectorYR = radius * Math.sin((Math.PI / 180) * angle);
        if (angle >= 360 * (n - 1) && angle < 90 + 360 * (n - 1)) {
            vectorX = Math.sqrt((radius * radius) - (vectorYR * vectorYR));
            vectorY = Math.sqrt((radius * radius) - (vectorXR * vectorXR));
        } else if (angle >= 90 + 360 * (n - 1) && angle < 180 + 360 * (n - 1)) {
            vectorX = -Math.sqrt((radius * radius) - (vectorYR * vectorYR));
            vectorY = Math.sqrt((radius * radius) - (vectorXR * vectorXR));
        } else if (angle >= 180 + 360 * (n - 1) && angle < 270 + 360 * (n - 1)) {
            vectorX = -Math.sqrt((radius * radius) - (vectorYR * vectorYR));
            vectorY = -Math.sqrt((radius * radius) - (vectorXR * vectorXR));
        } else if (angle >= 270 + 360 * (n - 1) && angle < 360 + 360 * (n - 1)) {
            vectorX = Math.sqrt((radius * radius) - (vectorYR * vectorYR));
            vectorY = -Math.sqrt((radius * radius) - (vectorXR * vectorXR));
        }
        if (angle >= 360 * n) {
            n++;
        }
        if(onStopClick){
            mokV = angleV;
            angleV = 0;
        }
        if(onStopClick2){
            angleV = mokV;
        }
        angle += angleV;
    }


    private void checkBoard(int width, int height) {
        //TODO: доделать в этом моменте когда у нас L5
        if(L5 && x < 0 && vectorX - l < 0 || (x > width - l && vectorX > 0)){
            onBoard = true;
            onStopClick = true;
            vectorX = 0;
        }
        if(L4  && y >= 1000){
            vectorY = 0;
            onStopClick = true;
        }
        if (x < 0 && vectorX - l < 0 || (x > width - l && vectorX > 0)) {
            onBoard = true;
            onStopClick = true;
            x = PhysicsData.getX0() - h;
            vectorX = 0;
        }
        if (y < 0 && vectorY - h < 0 || (y > height - h && vectorY > 0)) {
            onEarth = true;
            onStopClick = true;
            y = PhysicsData.getY0() - l;
            vectorY = 0;
        }
        if(onStopClick){
            vectorX = 0;
            vectorY = 0;
        }
    }

    public void updateGravity(double vel, double ang) {
        vectorX = vel * Math.cos((Math.PI / 180) * ang);
        vectorY = -vel * Math.sin((Math.PI / 180) * ang);
    }

    public double getVectorX() {
        return vectorX;
    }

    public void updateEP(double m1, double m2, double vectorX1, double vectorX2) {
        if (isTouchedI) {
            isTouchedI = false;
        }
    }

    public void updateNEP(double m1, double m2, double vectorX1, double vectorX2) {
        if (isTouchedI) {
            vectorX = (m1 * vectorX1 + m2 * vectorX2) / (m1 + m2);
            isTouchedNEP = true;
        }
    }

    public void updateA(double ax, double ay) {
        if (onEarth) {
            ay = 0;
            y = PhysicsData.getY0() - h;
            vectorY = 0;
        }
        if(onBoard){
            ax  = 0;
            x = PhysicsData.getX0() - l;
            vectorX = 0;
        }
        if(onStopClick){
            ax = 0;
            ay = 0;
        }
        vectorX += ax;
        vectorY += ay;
    }

    @Override
    public void destroy(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

}
