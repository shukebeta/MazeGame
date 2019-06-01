package com.shukebeta.zhong.mazegame;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {EyeBallProgress.class}, version = 2, exportSchema = false)
public abstract class EyeBallDatabase extends RoomDatabase {
    private static EyeBallDatabase INSTANCE;

    private static final String DATABASE_NAME = "eyeball_db";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE eyeball_progress ADD COLUMN gameCostTime TEXT");
        }
    };

    public static EyeBallDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (EyeBallDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            EyeBallDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract EyeBallDao eyeBallDao();
}