package com.updesigns.moneycircle;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.util.List;

//This is another body of room database in this class we declare all the variables we need in our database table
// and define getters and setters for it.. This is Called the entity class.. This particular one is for Circles...


//***You will need to create another column with name points in order to add gamification

@Entity
public class User {

    @NonNull
    @PrimaryKey
    private String fb_user_id = "";

    @ColumnInfo
    private String name_user;

    @ColumnInfo
    private String user_name;

    @ColumnInfo
    private String password;

    @ColumnInfo
    private String bank_account;

    @ColumnInfo
    private long balance;

    //The below 2 Lists cannot be stored directly in room so we need type convertors for them.. those type converters are defined in
    //  GithubTyeConverters.class

    @ColumnInfo
    private List<Circles> joinCircleList;            //List of circles that are joined

    @ColumnInfo
    private List<Transaction> transactionList;      //List of transactions occured

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @ColumnInfo
    private int joinedCircles;


    public List<Circles> getJoinCircleList() {
        return joinCircleList;
    }

    public void setJoinCircleList(List<Circles> joinCircleList) {
        this.joinCircleList = joinCircleList;
    }

    public int getJoinedCircles() {
        return joinedCircles;
    }

    public void setJoinedCircles(int joinedCircles) {
        this.joinedCircles = joinedCircles;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getName_user() {
        if(this.name_user == null) return "";
        return this.name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFb_user_id() {
        return this.fb_user_id;
    }

    public void setFb_user_id(@NonNull String fb_user_id) {
        this.fb_user_id = fb_user_id;
    }
}
