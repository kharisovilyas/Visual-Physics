package com.example.visualphysics10.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {
    //
    //TODO: Our Repository here the logic of multithreading is basically implemented
    // the main methods are in DataDao, here - multithreading
    // repository transmits data to ViewModel
    //
    private DataDao dataDao;
    private LiveData<List<LessonData>> lessonData;

    //constructor
    public Repository(Application application){
        AppDataBase appDataBase = AppDataBase.getInstance(application);
        dataDao = appDataBase.dataDao();
        lessonData = dataDao.getAllLiveData();
    }
    //methods which in thread refers to db
    public void insert(LessonData lessonData){
        new InsertDataAsyncTask(dataDao).execute(lessonData);
    }
    public void update(LessonData lessonData){
        new UpdateDataAsyncTask(dataDao).execute(lessonData);
    }
    public void delete(LessonData lessonData){
        new DeleteDataAsyncTask(dataDao).execute(lessonData);
    }
    public void deleteAllData(){
        new DeleteAllDataAsyncTask(dataDao).execute();
    }
    public LiveData<List<LessonData>> getLessonData(){
        return lessonData;
    }
    private static class DeleteDataAsyncTask extends AsyncTask<LessonData, Void, Void>{
        private DataDao dataDao;

        private DeleteDataAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }
        @Override
        protected Void doInBackground(LessonData... lessonData) {
            dataDao.delete(lessonData[0]);
            return null;
        }
    }


    //logic of multithreading
    private static class DeleteAllDataAsyncTask extends AsyncTask<Void, Void, Void>{
        private DataDao dataDao;
        //constructor called in the repository
        private DeleteAllDataAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }
        //and in thread deleteAllData for example
        @Override
        protected Void doInBackground(Void... voids) {
            dataDao.deleteAllData();
            return null;
        }
    }

    private static class UpdateDataAsyncTask extends AsyncTask<LessonData, Void, Void>{
        private DataDao dataDao;

        private UpdateDataAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }
        @Override
        protected Void doInBackground(LessonData... lessonData) {
            dataDao.update(lessonData[0]);
            return null;
        }
    }

    private static class InsertDataAsyncTask extends AsyncTask<LessonData, Void, Void>{
        private DataDao dataDao;

        private InsertDataAsyncTask(DataDao dataDao){
            this.dataDao = dataDao;
        }
        @Override
        protected Void doInBackground(LessonData... lessonData) {
            dataDao.insert(lessonData[0]);
            return null;
        }
    }
}
