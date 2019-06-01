package com.shukebeta.zhong.mazegame;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EyeBallDao {
    @Insert
    void insert(EyeBallProgress eyeBallProgress);

    @Query("SELECT progressName FROM eyeball_progress ORDER BY progressId DESC")
    public List<String> getAll();

    @Query("DELETE FROM eyeball_progress")
    public void clear();

    @Query("SELECT COUNT(*) AS TOTAL FROM eyeball_progress")
    public int getTotal();

    @Query("SELECT * FROM eyeball_progress WHERE progressId = :id")
    EyeBallProgress get(int id);
}