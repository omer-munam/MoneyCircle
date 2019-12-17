package com.updesigns.moneycircle;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

public class JoinCircleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CircleAdapterRecycler mAdapter;
    EditText ID, pass;
    String Id, pas, usern;
    Circles circles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);
        init();
    }

    //Ran when user clicks Join Circle Button
    public void circleJoin(View v){
        Id = ID.getText().toString();
        pas = pass.getText().toString();
        Circles circles = MainActivity.circleDatabase.circleDao().getCircle(Id);
        if (Id.equals("") || pas.equals("")){       //checks to see if fields are empty
            Toast.makeText(JoinCircleActivity.this,"ALL FIELDS ARE MANDATORY",Toast.LENGTH_LONG).show();
        }
        else {
            if(Id.equals(MainActivity.circleDatabase.circleDao().checkID(Id))){         //checks if circle of that id exists
                if (Id.equals(MainActivity.circleDatabase.circleDao().checkPass(pas))){ //checks if pass is correct
                    if(circles.getMembers().contains(usern)){                           //checks if user is already a member
                        Toast.makeText(JoinCircleActivity.this, "Already In Circle!", Toast.LENGTH_LONG).show();
                    }
                    else if(MainActivity.database.MyDao().getBalance(usern) <= circles.getCash()){  //checks if user has enough cash
                        Toast.makeText(JoinCircleActivity.this, "Your Account Balance Must Be Greater Than Circle Amount!",Toast.LENGTH_LONG).show();
                    }
                    //***You can add another check here to see if the user has enough points to join a crtain circle.. According to amount or something else
                    else if (circles.getJoined() == circles.getMax_users()){            //checks if circle is full
                        Toast.makeText(JoinCircleActivity.this, "Circle is Full!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        int x = circles.getJoined();
                        x++;
                        circles.setJoined(x);               //updates number of members
                        User z = MainActivity.database.MyDao().getUser(usern);
                        z.setJoinedCircles(z.getJoinedCircles()+1);    //updates number of joined circles
                        List<Circles> y = z.getJoinCircleList();
                        y.add(circles);
                        z.setJoinCircleList(y);             //updates list of circles
                        MainActivity.database.MyDao().deleteUser(usern);
                        MainActivity.database.MyDao().addUser(z);
                        List<String> temp = circles.getMembers();
                        temp.add(usern);
                        circles.setMembers(temp);               //update members list
                        MainActivity.circleDatabase.circleDao().deleteCircle(Id);
                        MainActivity.circleDatabase.circleDao().addCircle(circles);
                        Toast.makeText(JoinCircleActivity.this, "Joined Circle!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(JoinCircleActivity.this,"Incorrect Password!",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(JoinCircleActivity.this,"Circle Not Found!",Toast.LENGTH_LONG).show();
            }
        }
    }


    public void init(){
        ID = (EditText) findViewById(R.id.uniqueID);
        pass = (EditText) findViewById(R.id.chkPass);
        Bundle b = getIntent().getExtras();
        usern = "";
        if(b!=null) usern = b.getString("usern");           //gets username from previous activity
        Runnable r = new Runnable(){
            @Override
            public void run() {
                MainActivity.circleDatabase.circleDao().getAllItems("Open").observe(JoinCircleActivity.this, new Observer<List<Circles>>() {
                    @Override
                    public void onChanged(@Nullable final List<Circles> circles) {
                        //LiveData is used to populate the open circles list...
                        //Same recyclerview is used inside
                        recyclerView= (RecyclerView)findViewById(R.id.recycler);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                        mAdapter= new CircleAdapterRecycler(circles);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListener(
                                new CircleAdapterRecycler.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        openJoin(circles.get(position).getID());            //Calls this method when a circle is clicked upon
                                    }
                                }
                        );
                    }
                });
            }
        };
        Thread newThread= new Thread(r);
        newThread.start();
    }

    //When a circle is clicked from open circle list..
    public void openJoin(final String id){
        Circles circles = MainActivity.circleDatabase.circleDao().getCircle(id);
        if(circles.getMembers().contains(usern)){       //check if user is already part of circle
            Toast.makeText(JoinCircleActivity.this, "Already A Part of This Circle!", Toast.LENGTH_LONG).show();
        }
        else if(MainActivity.database.MyDao().getBalance(usern) <= circles.getCash()){      //check if user has enough cash
            Toast.makeText(JoinCircleActivity.this, "Your Account Balance Must Be Greater Than Circle Amount!",Toast.LENGTH_LONG).show();
        }
        else if (circles.getJoined() == circles.getMax_users()){        //check if circle is full
            Toast.makeText(JoinCircleActivity.this, "Circle is Full!",Toast.LENGTH_LONG).show();
        }
        //***You can add another check here to see if the user has enough points to join a certain circle.. According to amount or something else
        else{
            //same working as in closed join
            int x = circles.getJoined();
            x++;
            circles.setJoined(x);
            List<String> temp = circles.getMembers();
            temp.add(usern);
            User z = MainActivity.database.MyDao().getUser(usern);
            z.setJoinedCircles(z.getJoinedCircles()+1);
            List<Circles> y = z.getJoinCircleList();
            y.add(circles);
            z.setJoinCircleList(y);
            MainActivity.database.MyDao().deleteUser(usern);
            MainActivity.database.MyDao().addUser(z);
            circles.setMembers(temp);
            MainActivity.circleDatabase.circleDao().deleteCircle(id);
            MainActivity.circleDatabase.circleDao().addCircle(circles);
            Toast.makeText(JoinCircleActivity.this, "Joined Circle!", Toast.LENGTH_LONG).show();
        }
    }
}
