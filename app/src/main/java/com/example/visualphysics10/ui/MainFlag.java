package com.example.visualphysics10.ui;

public abstract class MainFlag {
    public static boolean visibleInfo;
    public static boolean threadStop;
    public static int position;

    public boolean isVisibleInfo() {
        return visibleInfo;
    }

    public void setVisibleInfo(boolean visibleInfo) {
        MainFlag.visibleInfo = visibleInfo;
    }
    public static boolean getThreadStop() {
        return threadStop;
    }

    public static void setThreadStop(boolean threadStop) {
        MainFlag.threadStop = threadStop;
    }


    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        MainFlag.position = position;
    }
}
