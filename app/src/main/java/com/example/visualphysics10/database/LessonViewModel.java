package com.example.visualphysics10.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//TODO: class which communicate with activity ( or fragments!) and repository
public class LessonViewModel extends AndroidViewModel {
    private LiveData<List<LessonData>> lessonLiveData;

    private Repository repository;
    //constructor
    public LessonViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        lessonLiveData = repository.getLessonData();
    }
    //methods interface dataDao using in repository multithreading
    public void insert(LessonData lessonData){
        repository.insert(lessonData);
    }
    public void update(LessonData lessonData){
        repository.update(lessonData);
    }
    public void delete(LessonData lessonData){
        repository.delete(lessonData);
    }
    public void deleteAllData(){
        repository.deleteAllData();
    }

    //returns the entire table
    public LiveData<List<LessonData>> getLessonLiveData() {
        return lessonLiveData;
    }
}
