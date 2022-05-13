package com.example.visualphysics10.placeholder;

import androidx.annotation.NonNull;

import com.example.visualphysics10.R;
import com.example.visualphysics10.database.PhysicsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceholderContent {

    public static final List<PlaceHolderItem> ITEMS = new ArrayList<PlaceHolderItem>();

    public static final Map<String, PlaceHolderItem> ITEM_MAP = new HashMap<String, PlaceHolderItem>();

    private static final int COUNT = 5;

    static {
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(PlaceHolderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PlaceHolderItem createPlaceholderItem(int position) {
        return new PlaceHolderItem(String.valueOf(position), switchLesson(position), switchProgress(position), switchDetails(position), switchImageView(position));
    }

    private static String switchProgress(int position) {
        switch (position){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return "Прогресс: " + PhysicsData.getSpeed();
            default:
                return "";
        }
    }


    private static String switchLesson(int position) {
        switch (position){
            case 1:
                return "Ускорение";
            case 2:
                return "Движение по Окружности";
            case 3:
                return "II Закон Ньютона";
            case 4:
                return "Движение под углом к горизонту";
            case 5:
                return "Законы Сохранения Импульса";
            default: return "";
        }
    }

    private static String switchDetails(int position) {
        switch (position){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return "Решено задач: " + PhysicsData.getSpeed();

            default: return "";
        }
    }


     static int switchImageView(int position) {
        switch (position){
            case 1: return R.drawable.lesson_1;
            case 2: return R.drawable.lesson_2;
            case 3: return R.drawable.lesson_3;
            case 4: return R.drawable.lesson_4;
            case 5: return R.drawable.lesson_5;
            default: return 0;
        }
    }

    public static class PlaceHolderItem {
        public final String id;
        public final String title;
        public final String task;
        public final String progress;
        public final int imageView;


        public PlaceHolderItem(String id, String content, String task, String progress, int imageView) {
            this.id = id;
            this.title = content;
            this.task = task;
            this.progress = progress;
            this.imageView = imageView;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }
}