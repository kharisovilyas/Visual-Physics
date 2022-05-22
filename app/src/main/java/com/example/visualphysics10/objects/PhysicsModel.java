package com.example.visualphysics10.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;

import com.example.visualphysics10.database.PhysicsData;
import com.example.visualphysics10.engine.PhysicsSprite;
import com.example.visualphysics10.engine.Vector2;

//TODO: класс сущности физ. моделек наследуется от PhysicsSprite где у нас описаны основные параметры моделек наследуемые от класса Sprites
// в этом классе у нас так же описивается изменение координаты моделек (движение) и их взаимодействие
// класс можно назвать Физическим Движком с нуля написанный нами
public class PhysicsModel extends PhysicsSprite {
    //координаты моделек
    double x;
    double y;
    public static boolean beginning = false;
    public static boolean firstDraw = true;
    public static double x0;
    public static double y0;
    //размеры моделек
    public static int l = 150;
    public static int h = 150;
    int index;
    //переменные задающие движение
    double vectorX;
    double vectorY;
    double vectorXR;
    double vectorYR;
    public static boolean L2start = false;
    public static boolean isTouchedNEP = false;
    public static boolean isL2Ended = false;
    public static boolean isTouchedI = false;
    //флаги-слушатели для остановки моделек чтобы они не вылетали за пределы экрана (при ускорении {в потоке выполнятся})
    public static boolean onEarth = false;
    public static boolean onBoard = false;
    //флаги необходимые для корректной работы движка под разные уроки и разные модели
    public static boolean L1 = false;
    public static boolean L2 = false;
    public static boolean L3 = false;
    public static boolean L4 = false;
    public static boolean L5 = false;
    //
    //угол броска
    int angle = 0;
    //число оборотов
    int n = 1;
    double F;
    //переменные необходимые для задания параметров моделек(цвет)
    private final Paint paint = new Paint();
    private final Paint paint2 = new Paint();
    private final Paint paint4 = new Paint();
    private final Paint paint3 = new Paint();
    //звук в уроках
    public static MediaPlayer end;
    public static MediaPlayer collision;
    public static MediaPlayer rotation;
    public static MediaPlayer landing;


    //конструктор вызваемый в addModelGV добавляет модельки в SurfaceView отображает их
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

    //работа со звуком
    public static void addSound1(MediaPlayer end) {
        PhysicsModel.end = end;
    }

    public static void addSound2(MediaPlayer rotation) {
        PhysicsModel.rotation = rotation;
    }

    public static void addSound4(MediaPlayer landing) {
        PhysicsModel.landing = landing;
    }

    public static void addSound5(MediaPlayer collision) {
        PhysicsModel.collision = collision;
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
                collision.start();
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
        //задание цвета
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
        //задание движения и координат
        updateVector(vectorX, vectorY);
        if (L1 || L3 || L4) {
            x = x + vectorX;
            y = y + vectorY;
            rect = new Rect((int) x, (int) y, (int) x + l, (int) y + h);
            checkBoard(canvas.getWidth(), canvas.getHeight());
        } else if (L5) {
            x = x + vectorX;
            y = y + vectorY;
            rect = new Rect((int) x, (int) y, (int) x + l, (int) y + h);
            checkBoardForL5(canvas.getWidth(), canvas.getHeight());
        } else if (L2) {
            if (!isL2Ended) {
                canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (float) PhysicsData.getRadius(), paint2);
            } else {
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
    //для 2 урока - движение по окружности
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
        rotation.start();
        angle += angleV;
    }

    //проверка чтобы модельки не выходили за пределы экрана для 5 урока
    private void checkBoardForL5(int width, int height) {
        if ((x < 0 && vectorX - l < 0) || (x > width - l && vectorX > 0)) {
            onBoard = true;
            vectorX = 0;
        }
        if (y < 0 && vectorY - h < 0 || (y > height - h && vectorY > 0)) {
            onEarth = true;
            vectorY = 0;
        }
    }
    //проверка чтобы модельки не выходили за пределы экрана для остальных кроме 2ч
    private void checkBoard(int width, int height) {
        if ((x < 0 && vectorX - l < 0) || (x > width - l && vectorX > 0)) {
            onBoard = true;
            PhysicsData.setSpeedEnd(vectorX);
            if (x > width - l && vectorX > 0)
                if(!L4) end.start();
            x = PhysicsData.getX0() - h;
            vectorX = 0;
            vectorY = 0;

        }
        if ((y > height - h && vectorY > 0)) {
            onEarth = true;
            if (y > height - h && vectorY > 0)
                landing.start();
            y = PhysicsData.getY0() - l;
            vectorX = 0;
            vectorY = 0;

        }
    }

    //логика прорисовки движения под углом к горизонту для 4 урока
    public void updateGravity(double vel, double ang) {
        vectorX = vel * Math.cos((Math.PI / 180) * ang);
        vectorY = -vel * Math.sin((Math.PI / 180) * ang);
    }

    public double getVectorX() {
        return vectorX;
    }

    //логика взаимодействия двух моделек при столкновении - имульс 5 урок
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

    //логика работы ускорения тел в 1, 3 уроках
    public void updateA(double ax, double ay) {
        if (!L5) {
            if (onEarth) {
                ay = 0;
                y = PhysicsData.getY0() - h;
                vectorY = 0;
            }
            if (onBoard) {
                ax = 0;
                x = PhysicsData.getX0() - l;
                vectorX = 0;
            }
        }
        vectorX += ax;
        vectorY += ay;
    }

    @Override
    public void destroy(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    //логика остановки визуализации
    public void onStopClick() {
        updateVector(0, 0);
        onBoard = false;
        PhysicsData.setSpeedEnd(vectorX);
        onEarth = false;
        stopSound();
    }
    //остановка звука в каждом отдельном уроке (фрагменте)
    private void stopSound() {
        if(L1 || L3) end.stop();
        if(L4) landing.stop();
        if(L2 && !rotation.isPlaying()) {
            rotation.stop();
        }
        if(L5) collision.stop();
        if(L2 && !rotation.isPlaying()) updateAC(PhysicsData.getRadius(), 0);
        else if(!L2) updateA(0, 0);
    }

    //логика перезапуска визуализации
    public void onRestartClick(int index) {
        if(L5) {
            isTouchedI = false;
            isTouchedNEP = false;
        }
        if(index == 0) x = 0;
        else x = PhysicsData.getX0() - h;
        if(L2) updateAC(PhysicsData.getRadius(), 0);
        else updateVector(0, 0);
        if (L4) y = (PhysicsData.getY0() - h) / 2;
        else y = PhysicsData.getY0() - h;
        onBoard = false;
        onEarth = false;
        vectorX = 0;
    }
}
