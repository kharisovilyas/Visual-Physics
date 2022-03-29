package com.example.visualphysics10.database;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {


    private AppDataBase database;
    private DataDao dataDao;
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(
                getApplicationContext(),
                AppDataBase.class,
                "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        dataDao = database.dataDao();
    }

    public void setDatabase(AppDataBase database) {
        this.database = database;
    }

    public AppDataBase getDatabase() {
        return database;
    }

    public DataDao getDataDao() {
        return dataDao;
    }
}
