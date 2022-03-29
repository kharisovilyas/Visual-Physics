package com.example.visualphysics10.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LessonData.class}, version = 5, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract DataDao dataDao();
}
