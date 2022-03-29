package com.example.visualphysics10.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataDao {
    @Query("SELECT * FROM LessonData")
    LiveData<List<LessonData>> getAll();


    @Query("SELECT * FROM LessonData WHERE id =:id")
    List<LessonData> getById(long id);

    @Query("SELECT * FROM LessonData")
    LiveData<List<LessonData>> getAllLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LessonData lessonData);

    @Update
    void update(LessonData lessonData);

    @Delete
    void delete(LessonData lessonData);
}
