package com.updesigns.moneycircle;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.List;

//This is another body of room database in this class we declare all the variables we need in our database table
// and define getters and setters for it.. This is Called the entity class.. This particular one is for Circles...

@Entity
public class Circles{

    @NonNull
    @PrimaryKey
    private String ID = "";

    @ColumnInfo
    private String circleName;

    @ColumnInfo
    private String Creator;

    @ColumnInfo
    private long Cash;

    @ColumnInfo
    private String type;                    //OPEN or CLOSED

    @ColumnInfo
    private String pass;

    @ColumnInfo
    private int max_users;                  //Maximum members that can join a circle

    @ColumnInfo
    private int joined;                     //Number of joined members

    //List of string requires a typeconverter also. It is defined in Converters.class
    @ColumnInfo
    private List<String> members;           //List of username of members

    @ColumnInfo
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @NonNull
    public String getID() {
        return ID;
    }

    public void setID(@NonNull String ID) {
        this.ID = ID;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String name) {
        circleName = name;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public long getCash() {
        return Cash;
    }

    public void setCash(long cash) {
        Cash = cash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getMax_users() {
        return max_users;
    }

    public void setMax_users(int max_users) {
        this.max_users = max_users;
    }

    public int getJoined() {
        return joined;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }
}
