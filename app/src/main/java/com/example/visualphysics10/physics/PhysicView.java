package com.example.visualphysics10.physics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.engine.PhysicsSprite;
import com.example.visualphysics10.engine.Sprite;
import com.example.visualphysics10.engine.Vector2;
import com.example.visualphysics10.lessonsFragment.L1Fragment;
import com.example.visualphysics10.lessonsFragment.L2Fragment;
import com.example.visualphysics10.lessonsFragment.L3Fragment;
import com.example.visualphysics10.lessonsFragment.L4Fragment;
import com.example.visualphysics10.lessonsFragment.L5Fragment;
import com.example.visualphysics10.objects.PhysicsModel;

import java.util.LinkedList;

public class PhysicView extends SurfaceView implements SurfaceHolder.Callback {
    private final LinkedList<PhysicsModel> sprites = new LinkedList<>();
    private Thread drawThread;
    private boolean drawOk;

    public PhysicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        initSprites();
    }


    public PhysicView(Context context) {
        this(context, null);
    }
    //размер surfaceView
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!PhysicsModel.L2start) {
            PhysicsData.setX0(w);
            PhysicsData.setY0(h);
        }
    }
    //создание сущности модели
    public void addModelGV() {
        synchronized (sprites) {
            sprites.add(new PhysicsModel(getContext(), 0, PhysicsData.getY0() - PhysicsModel.l - 5, 0, 0, 0));
        }
    }
    //создание сущности модели в четвертом уроке
    public void addModelGV4(){
        synchronized (sprites){
            sprites.add(new PhysicsModel(getContext(), 0, PhysicsData.getY0() / 2, 0, 0, 0));
        }
    }
    //создание сущности модели в пятом уроке
    public void addModelGV5(int index){
        synchronized (sprites){
            if (index == 0)
                sprites.add(new PhysicsModel(getContext(), 50, PhysicsData.getY0() - PhysicsModel.l - 5, 0, 0, index));
            else
                sprites.add(new PhysicsModel(getContext(), PhysicsData.getX0() - PhysicsModel.l - 50, PhysicsData.getY0() - PhysicsModel.h - 5, 0, 0, index));
        }
    }

    private void initSprites() {
    }
    //начало потока
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        drawOk = true;
        drawThread = new MoveThread();
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        PhysicsData.setY0(height);
    }

    //уничтожение surfaceView
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Canvas canvas = new Canvas();
        for (Sprite sprite : sprites) {
            sprite.destroy(canvas);
        }
        PhysicsModel.L2start = false;
        PhysicsModel.onEarth = false;
        PhysicsModel.onBoard = false;
        PhysicsModel.isTouchedI = false;
        PhysicsModel.isL2Ended = true;
        PhysicsModel.isTouchedNEP = false;
        PhysicsModel.beginning = false;
        PhysicsModel.firstDraw = false;
        L1Fragment.isMoving = false;
        L2Fragment.isMoving = false;
        L3Fragment.isMoving = false;
        L4Fragment.isMoving = false;
        L5Fragment.isMoving = false;
        drawOk = false;
        stopThread();
    }


    public void stopThread() {
        drawThread.interrupt();
    }

    //движение
    public void updateMoving(double vectorX, double vectorY, int index) {
        synchronized (sprites) {
            sprites.get(index).updateVector(vectorX, vectorY);
        }
    }
    //1 и 3 уроки
    public void updateAA(double aX, double aY, int index) {
        synchronized (sprites) {
            sprites.get(index).updateA(aX, aY);
        }
    }
    //2 урок
    public void updateAAC(double radius, double angleV, int index) {
        synchronized (sprites) {
            sprites.get(index).updateAC(radius, angleV);
        }
    }
    //5 урок
    public void updateElasticP(int index1, int index2) {
        synchronized (sprites) {
            sprites.get(index1).updateEP(PhysicsData.getMass1(), PhysicsData.getMass2(), sprites.get(index1).getVectorX(), sprites.get(index2).getVectorX());
            sprites.get(index2).updateEP(PhysicsData.getMass1(), PhysicsData.getMass2(), sprites.get(index2).getVectorX(), sprites.get(index1).getVectorX());
        }
    }
    // 5 урок
    public void updateNoElasticP(int index1, int index2) {
        synchronized (sprites) {
            sprites.get(index1).updateNEP(PhysicsData.getMass1(), PhysicsData.getMass2(), sprites.get(index1).getVectorX(), sprites.get(index2).getVectorX());
            sprites.get(index2).updateNEP(PhysicsData.getMass1(), PhysicsData.getMass2(), sprites.get(index2).getVectorX(), sprites.get(index1).getVectorX());
        }
    }
    //4урок
    public void updateGG(double vel, double ang, int index) {
        synchronized (sprites) {
            sprites.get(index).updateGravity(vel, ang);
        }
    }

    public void stopDraw(int index) {
        synchronized (sprites){
            sprites.get(index).onStopClick();
        }
        stopThreadFlags();
    }


    private void stopThreadFlags() {
        L1Fragment.isMoving = false;
        L2Fragment.isMoving = false;
        L3Fragment.isMoving = false;
        L4Fragment.isMoving = false;
        L5Fragment.isMoving = false;
    }

    public void restartClick(int index) {
        synchronized (sprites){
            sprites.get(index).onRestartClick(index);
        }
        stopThreadFlags();
    }

    //поток
    class MoveThread extends Thread {
        @Override
        public void run() {
            Canvas canvas;
            while (!isInterrupted()) {
                if (drawOk) {
                    canvas = getHolder().lockCanvas();
                    if (canvas == null) continue;
                    onDrawAsync(canvas);
                    PhysicsModel.x0 = canvas.getWidth() / 2;
                    PhysicsModel.y0 = canvas.getHeight() / 2;
                    getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }

        //рисование
        private void onDrawAsync(Canvas canvas) {

            canvas.drawColor(Color.WHITE);
            synchronized (sprites) {
                if (L1Fragment.isMoving) {
                    updateAA(PhysicsData.getAcc(), 0, 0);
                }
                if (L2Fragment.isMoving) {
                    PhysicsModel.firstDraw = false;
                    PhysicsModel.isL2Ended = false;
                    updateAAC(PhysicsData.getRadius(), PhysicsData.getSpeed(), 0);
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(10);
                    canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (float) PhysicsData.getRadius(), paint);
                }
                if (L3Fragment.isMoving) {
                    updateAA(PhysicsData.getAcc(), 0, 0);
                }
                if (L4Fragment.isMoving) {
                    if (PhysicsModel.beginning) {
                        updateGG(PhysicsData.getSpeed(), PhysicsData.getAngle(), 0);
                        PhysicsModel.beginning = false;
                    }
                    updateAA(0, 1, 0);
                }
                if (L5Fragment.isMoving) {
                    updateAA(0, 0, 0);
                    updateAA(0, 0, 1);
                    if (PhysicsData.getElasticImpulse()) {
                        updateElasticP(0, 1);
                    } else {
                        updateNoElasticP(0, 1);
                    }
                }
                //проверка каждой модели
                for (Sprite sprite : sprites) {
                    sprite.update(canvas);
                    if (sprite instanceof PhysicsSprite) {
                        Rect rect = ((PhysicsSprite) sprite).getSize();
                        Vector2 velocity = ((PhysicsSprite) sprite).getVelocity();
                        for (Sprite collision : sprites) {
                            if (collision instanceof PhysicsSprite
                                    && sprite != collision) {
                                ((PhysicsSprite) collision).checkCollation(rect, velocity);
                            }
                        }
                    }
                }
            }
        }
    }
}