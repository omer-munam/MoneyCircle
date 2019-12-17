package com.updesigns.moneycircle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class CreateCircleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String members=null, type=null, pas, nam, ID,amont,usern;
    EditText pass, name, id, amnt;
    long amount;
    Circles circles;
    ArrayList<String> mem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_circle);
        Bundle b = getIntent().getExtras();
        usern = "";
        if(b!=null) usern=b.getString("usern");     //gets username of the logged in user
        init();                                          //initializes all the variables and views
    }

    //On pressing the create button
    public void circleCreate(View v){
        //get all fields values
        pas = pass.getText().toString();
        nam = name.getText().toString();
        ID = id.getText().toString();
        amont = amnt.getText().toString();
        mem.add(usern);                     //creating user added as a member to list of members.
        if (amont.equals("")){}
        else{ amount = Long.parseLong(amont);}      //convert string amount to long
        if (members != null && type != null){       //check dropdown menus
            if ((type.equals("Closed") && pas.equals("")) || ID.equals("") || nam.equals("") || amont.equals("")){      //if type is 'open' all fields mandatory otherwise all except password
                Toast.makeText(CreateCircleActivity.this,"All Fields are Mandatory",Toast.LENGTH_LONG).show();
            }
            else{
                //***You can add another check here to see if the person has enough points to create a circle... Or how big a circle he can create.
                if(amount < 1000){          //Amount should be at least 1000 of a circle
                    Toast.makeText(CreateCircleActivity.this,"Amount Should Be Greater Than 1000 PKR",Toast.LENGTH_LONG).show();
                }
                else{
                    if (usern.equals("")){
                        Toast.makeText(CreateCircleActivity.this,"ERROR IN GETTING USER", Toast.LENGTH_LONG).show();
                    }
                    else {
                         if(ID.equals(MainActivity.circleDatabase.circleDao().checkID(ID))) {  //check if entered ID is unique
                             Toast.makeText(CreateCircleActivity.this,"Circle ID in Use",Toast.LENGTH_LONG).show();
                         }
                         else if(MainActivity.database.MyDao().getBalance(usern) <= amount){  //Get users balance and compare with amount entered
                             Toast.makeText(CreateCircleActivity.this, "Your Account Balance Must Be Greater Than Circle Amount!",Toast.LENGTH_LONG).show();
                         }
                         else {
                             //Set all entered values to circle entity
                             circles = new Circles();
                             circles.setCircleName(nam);
                             circles.setCash(amount);
                             circles.setCreator(MainActivity.database.MyDao().getNameuser(usern));
                             circles.setID(ID);
                             circles.setJoined(1);
                             circles.setType(type);
                             circles.setPass(pas);
                             circles.setMembers(mem);
                             circles.setTime(0);
                             circles.setMax_users(Integer.parseInt(members));
                             try {
                                 MainActivity.circleDatabase.circleDao().addCircle(circles);        //add the circle entity to database
                                 User x = MainActivity.database.MyDao().getUser(usern);
                                 x.setJoinedCircles(x.getJoinedCircles()+1);                        //Increase number of joined circles
                                 List<Circles> y = x.getJoinCircleList();
                                 y.add(circles);                                                    //Add this circle to list of joined circles
                                 x.setJoinCircleList(y);
                                 MainActivity.database.MyDao().deleteUser(usern);
                                 MainActivity.database.MyDao().addUser(x);
                                 Toast.makeText(CreateCircleActivity.this,"Circle Created!",Toast.LENGTH_LONG).show();
                                 Intent intent = new Intent(CreateCircleActivity.this,HomeScreen.class);
                                 Bundle b = new Bundle();
                                 b.putString("usern",usern);                                        //Passes username to next activity
                                 intent.putExtras(b);
                                 startActivity(intent);                                             //Get back to home screen
                             } catch (Exception e) {
                                 Toast.makeText(CreateCircleActivity.this, "Error in adding circle", Toast.LENGTH_LONG).show();
                             }
                         }
                    }
                }
            }
        }
        else {
            Toast.makeText(CreateCircleActivity.this,"Pick Something From Dropdown Menus",Toast.LENGTH_LONG).show();
        }
    }





    public  void init(){
        amnt = (EditText) findViewById(R.id.circleAmnt);
        pass = (EditText) findViewById(R.id.circlePass);
        id = (EditText) findViewById(R.id.circleId);
        name = (EditText) findViewById(R.id.circleName);
        mem = new ArrayList<String>();

        //initializing dropdown menus.. Also known as spinners for Max_members and Type
        Spinner spinMember = (Spinner) findViewById(R.id.spinMember);
        Spinner spinPass = (Spinner) findViewById(R.id.spinPass);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spin_member, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMember.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.spin_pass, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPass.setAdapter(adapter);
        spinMember.setOnItemSelectedListener(CreateCircleActivity.this);
        spinPass.setOnItemSelectedListener(CreateCircleActivity.this);
    }

    //gets the value of selected item into a string variable members
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(parent.getId() == R.id.spinMember)
        {
            if(pos != 0){
                members = parent.getItemAtPosition(pos).toString();
            }
            else{
                members = null;
            }
        }
        else if(parent.getId() == R.id.spinPass)
        {
            if(pos != 0){
                type = parent.getItemAtPosition(pos).toString();
            }
            else{
                type = null;
            }
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
