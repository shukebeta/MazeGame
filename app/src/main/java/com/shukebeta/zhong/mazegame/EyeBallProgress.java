package com.shukebeta.zhong.mazegame;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "eyeball_progress")
public class EyeBallProgress {


    @PrimaryKey(autoGenerate = true)
    private int progressId;

    private String progressName;

    private String gameStage;

    private String gameCostTime;

    private String walkedPieceList;

    public EyeBallProgress(int progressId, String progressName, String gameStage, String gameCostTime, String walkedPieceList) {
        this.progressId = progressId;
        this.progressName = progressName;
        this.gameStage = gameStage;
        this.gameCostTime = gameCostTime;
        this.walkedPieceList = walkedPieceList;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public String getProgressName() {
        return progressName;
    }

    public void setProgressName(String progressName) {
        this.progressName = progressName;
    }

    public String getGameStage() {
        return gameStage;
    }

    public void setGameStage(String gameStage) {
        this.gameStage = gameStage;
    }

    public String getWalkedPieceList() {
        return walkedPieceList;
    }

    public void setWalkedPieceList(String walkedPieceList) {
        this.walkedPieceList = walkedPieceList;
    }


    public String getGameCostTime() {
        return gameCostTime;
    }

    public void setGameCostTime(String gameCostTime) {
        this.gameCostTime = gameCostTime;
    }

}