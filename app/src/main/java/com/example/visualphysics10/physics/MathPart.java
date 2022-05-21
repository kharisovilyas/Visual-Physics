package com.example.visualphysics10.physics;

public class MathPart {
    public static double frequency;
    public static double imp1;
    public static double imp2;
    public static double time;
    public static int count1;
    public static int count2;
    public static int count3;
    public static int count4;
    public static int count5;

    public static double getTime(double speed, double angle) {
        return (2 * Math.sin((angle * 180) / Math.PI) * speed) / 10;
    }

    public static void setTime(double time) {
        MathPart.time = time;
    }

    public static double getImp1(double speed, double mass) {
        return speed * mass;
    }

    public static void setImp1(double imp1) {
        MathPart.imp1 = imp1;
    }

    public static double getImp2(double speed, double mass) {
        return speed * mass;
    }

    public static void setImp2(double imp2) {
        MathPart.imp2 = imp2;
    }

    public static int getCount2() {
        return count2;
    }

    public static void setCount2(int count2) {
        MathPart.count2 = count2;
    }

    public static int getCount3() {
        return count3;
    }

    public static void setCount3(int count3) {
        MathPart.count3 = count3;
    }

    public static int getCount4() {
        return count4;
    }

    public static void setCount4(int count4) {
        MathPart.count4 = count4;
    }

    public static int getCount5() {
        return count5;
    }

    public static void setCount5(int count5) {
        MathPart.count5 = count5;
    }


    public static int getCount1() {
        return count1;
    }

    public static void setCount1(int count1) {
        MathPart.count1 = count1;
    }


    public static double getFrequency() {
        return frequency;
    }

    public static void setFrequency(double frequency) {
        MathPart.frequency = frequency;
    }

}
