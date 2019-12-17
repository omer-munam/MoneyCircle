package com.updesigns.moneycircle;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

//functions to access room database called DAO
//this particular one is for circleDatabase

@Dao
public interface circleDao {
    @Insert
    public void addCircle(Circles circles);

    @Query("SELECT ID FROM Circles WHERE ID LIKE :search")
    public String checkID(String search);

    @Query("SELECT * FROM Circles WHERE type LIKE :search")
    public LiveData<List<Circles>> getAllItems(String search);

    @Query("SELECT * FROM Circles")
    public List<Circles> getAll();

    @Query("SELECT ID FROM Circles WHERE pass LIKE :search")
    public String checkPass(String search);

    @Query("SELECT joined FROM Circles WHERE ID LIKE :search")
    public int getjoined(String search);

    @Query("UPDATE Circles SET joined = :joined WHERE ID LIKE :search")
    public void updateJoined(int joined, String search);

    @Query("SELECT members FROM circles WHERE ID LIKE :search")
    public List<String> getMembers(String search);

    @Query("UPDATE Circles SET members = :members WHERE ID LIKE :search")
    public void updateMembers(List<String> members, String search);

    @Query("SELECT Creator FROM Circles WHERE ID LIKE :search")
    public String getCreator(String search);

    @Query("DELETE FROM Circles WHERE ID lIKE :search")
    public void deleteCircle(String search);

    @Query("SELECT time FROM Circles WHERE ID LIKE :search")
    public int getTime(String search);

    @Query("UPDATE Circles SET time = :time WHERE ID LIKE :search")
    public void updateTime(int time, String search);

    @Query("SELECT * FROM Circles WHERE ID LIKE :search")
    public Circles getCircle(String search);
}
