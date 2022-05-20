package com.example.visualphysics10.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
//TODO: Database of our application
// the logic of its work is implemented in the entities that are in this package
// in this class we create a database using the Room methods
@Database(entities = {LessonData.class}, version = 7, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract DataDao dataDao();

    private static AppDataBase instance;

    //created new AsyncTask using .CallBack methods
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopularDbAsyncTask(instance).execute();
        }
    };

    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), AppDataBase.class, "lessonData"
            ).fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //our thread which works with data without interfering with the main thread
    private static class PopularDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private final DataDao dataDao;

        //constructor
        private PopularDbAsyncTask(AppDataBase db){
            dataDao = db.dataDao();
        }
        //thread
        @Override
        protected Void doInBackground(Void... voids) {
            dataDao.insert(new LessonData());
            return null;
        }
    }
}
