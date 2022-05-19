package com.example.visualphysics10.ui;

public abstract class MainFlag {
    public static boolean threadStop;
    public static int position;

    public static boolean isNotLesson() {
        return notLesson;
    }

    public static void setNotLesson(boolean notLesson) {
        MainFlag.notLesson = notLesson;
    }

    public static boolean notLesson;


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
