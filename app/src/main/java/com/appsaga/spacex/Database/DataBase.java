package com.appsaga.spacex.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {CrewDBModel.class},version=2,exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    public abstract CrewDao crewDao();
}
