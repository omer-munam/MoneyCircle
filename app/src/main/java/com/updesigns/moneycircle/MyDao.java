package com.updesigns.moneycircle;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

//functions to access room database called DAO
//this particular one is for User Database

@Dao
public interface MyDao {

    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM User WHERE user_name LIKE :search")
    public User getUser(String search);

    @Query("SELECT password FROM User WHERE user_name LIKE :search")
    public String getPassword(String search);

    @Query("SELECT user_name FROM User WHERE user_name LIKE :search")
    public String getUsername(String search);

    @Query("SELECT name_user FROM User WHERE user_name LIKE :search")
    public String getNameuser(String search);

    @Query("SELECT fb_user_id FROM User WHERE fb_user_id LIKE :search")
    public String getFbID(String search);

    @Query("SELECT fb_user_id FROM User WHERE user_name LIKE :search")
    public String getFbIDbyUser(String search);

    @Query("SELECT joinedCircles FROM User WHERE user_name LIKE :search")
    public int getJoined(String search);

    @Query("UPDATE User SET joinedCircles = :joined WHERE user_name LIKE :search")
    public void updateJoined(int joined, String search);

    @Query("SELECT balance FROM User WHERE user_name LIKE :search")
    public int getBalance(String search);

    @Query("UPDATE User SET balance = :balance WHERE user_name LIKE :search")
    public void updateBalance(long balance, String search);

    @Query("DELETE FROM User WHERE user_name LIKE :search")
    public void deleteUser(String search);

}
