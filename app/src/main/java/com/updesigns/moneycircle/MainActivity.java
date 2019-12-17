package com.updesigns.moneycircle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//---WHERE EVER WE ARE ACCESSING BALANCE OR PERFORMING A TRANSACTION WE WILL NEED TO SEND THE REQUEST TO BANK..
//--- THIS IS A SLOW PROCESS.. CONSIDER THREADING WHILE EXECUTING THEM..

//This class is used to Display a splash screen, build database and set transaction intervals

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 2500;
    public static database database;
    public static circleDatabase circleDatabase;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init2(getApplicationContext());     //Build the database
        init();                             //Declare the background routine
        new Handler().postDelayed(new Runnable() {
            //Splash Screen is declared
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

    private void init2(Context context){
        //Building the databases
        //2 Database used. One For circles the other for user data
        circleDatabase = Room.databaseBuilder(context, circleDatabase.class, "Circles").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        database = Room.databaseBuilder(context, database.class, "User").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    private void init() {
        //Inorder to perform transactions we need background service at intervals..
        // This declares AlarmManager which runs Transfer class at set intervals even when the app is closed
        Intent alarmIntent = new Intent(this, Transfer.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        int interval = 300000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }



    //The following Method will be called for transactions from Transfer Class when called by the above AlarmManager

    //***You can add logic to the transaction method which will increase user's points when they complete a circle or perform a transaction
    //***You can maybe add points where we have cleared the getTime of circle to '0'.. Since that code runs only when a cycle is complete
    //---If linked with bankaccount you will need to request the transfer everytime from bankaccount or if you have transferred the money somewhere
    //---then you will access it from there.. whenever we are accessing database for balance, there bankaccount will be accessed
    public void performTransac(final Context context) {
        Log.d("cirir", "ITS STILL WORKING");
        init2(context);     //build the database
        List<Circles> circles = null;
        try {
            circles = circleDatabase.circleDao().getAll();  //getting list of all circles
        } catch (Exception e) {
            Log.d("cirir", "No Circles");
        }
        if (circles != null) {
            long bal, cash;
            for (int i = circles.size() - 1; i >= 0; i--) {         //loop to traverse through all circles
                List<String> mem = circles.get(i).getMembers();     //getting list of all members of a single circle
                if (mem != null && mem.size() > 2) {                //Transactions will be performed when there are minimum 3 members
                    for (int j = mem.size() - 1; j >= 0; j--) {     //loop to traverse through all members
                        String s = mem.get(j);
                        User user = database.MyDao().getUser(s);
//                        Log.d("cirir", "Name: " + s);
                        Transaction trans = new Transaction();
                        if (j == circles.get(i).getTime()) {
                            //getTime returns how many times transaction have been done on a circle
                            //so at every value of Time any one of the members is paid in full of the circle
                            bal = user.getBalance();
//                            Log.d("cirir", String.valueOf(circles.get(i).getTime()) + "  After:  " + String.valueOf(bal));
                            cash = circles.get(i).getCash();
                            bal += cash;
                            trans.setAmount(cash);
                            trans.setCircle(circles.get(i).getCircleName());
                            trans.setType(1);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                            Date date = new Date();
                            trans.setDate(dateFormat.format(date));
                            user.setBalance(bal);                               //update the balance
                            List<Transaction> x = user.getTransactionList();
                            x.add(trans);                                       //add the transaction performed to the list
                            user.setTransactionList(x);
//                            Log.d("cirir", String.valueOf(circles.get(i).getTime()) + "  After:  " + String.valueOf(bal));
                        } else {
                            bal = user.getBalance();
                            cash = circles.get(i).getCash();
                            cash = cash / (mem.size() - 1);     //calculate how much money each member need to pay
                            bal = bal - cash;
                            user.setBalance(bal);
                            trans.setAmount(cash);
                            trans.setCircle(circles.get(i).getCircleName());
                            trans.setType(2);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                            Date date = new Date();
                            trans.setDate(dateFormat.format(date));
                            List<Transaction> x = user.getTransactionList();
                            x.add(trans);
                            user.setTransactionList(x);
//                            Log.d("cirir", String.valueOf(circles.get(i).getTime()) + "  elseAfter:  " + String.valueOf(bal));
                        }
                        database.MyDao().deleteUser(s);
                        database.MyDao().addUser(user);     //update the user data
                    }
                    int c = 0;
                    if (circles.get(i).getTime() != (mem.size() - 1)) {
                        //Increment The value of time
                        c = circles.get(i).getTime();
                        c++;
                        circleDatabase.circleDao().updateTime(c, circles.get(i).getID());
                    } else {
                        //if the getTime is equal to number of members then it means the cycle of the circle is complete
                        //So clear the value of time
                        circleDatabase.circleDao().updateTime(0, circles.get(i).getID());
                    }
                }
            }
        }
    }
}