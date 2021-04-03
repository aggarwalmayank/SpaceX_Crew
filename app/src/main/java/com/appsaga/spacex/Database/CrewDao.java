package com.appsaga.spacex.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.appsaga.spacex.WebModel.Crew;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CrewDao {
    @Insert
    void insertCrew(CrewDBModel crewDBModel);

    @Query("select * from crewDetails")
    List<CrewDBModel> getCrewDetails();

    @Query("Delete from crewDetails")
    void deleteAll();

}
