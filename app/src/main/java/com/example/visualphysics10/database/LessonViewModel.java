package com.example.visualphysics10.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class LessonViewModel extends ViewModel {
    private LiveData<List<LessonData>> lessonLiveData = App.getInstance()
            .getDataDao().getAllLiveData();

    public LiveData<List<LessonData>> getLessonLiveData() {
        return lessonLiveData;
    }
}
