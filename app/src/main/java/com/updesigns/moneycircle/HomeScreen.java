package com.updesigns.moneycircle;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import im.getsocial.sdk.ui.GetSocialUi;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import java.util.List;

//Parent activity for all fragments

public class HomeScreen extends AppCompatActivity{
    Fragment frag =null;
    Bundle b;
    NavigationTabStrip navigationTabStrip;
    ViewPager mPager;
    User use;
    String user;
    PagerAdapter pagerAdapter;
    int NUM_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Builds the fragments
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        //Builds the navigation bar on top of fragments.
        navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts);
        navigationTabStrip.setViewPager(mPager);
        frag = new HomeFragment();
        b = getIntent().getExtras();
        if(b!= null){
            user = b.getString("usern");                //get username from previous activity
            use  = MainActivity.database.MyDao().getUser(user); //get whole user class data
        }
    }

    //Following code sets the fragment on left or right swipe of screen
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private Fragment[] childFragments;
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            childFragments = new Fragment[] {
                    new HomeFragment(), //0
                    new CircleFragment(), //1
                    new ProfileFragment() //2
            };
        }

        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {           //left empty so that back button cannot work
    }

    public void inviteFriend(View v){
        boolean wasShown = GetSocialUi.createInvitesView().show();          //This is called when invite Friends icon is clicked
    }

    public String getData(){
        if(user == null || user.equals("")){
            Toast.makeText(HomeScreen.this,"FOUND THE ERROR",Toast.LENGTH_SHORT).show();
        }
        return user;            //returns the username to its fragments.
    }


                //Fragment Circle
    //Opens JoinCircle Activity upon clicking Join Circle Button
    public void joinCircle(View v){
        Intent intent = new Intent(HomeScreen.this,JoinCircleActivity.class);
        Bundle b = new Bundle();
        b.putString("usern",user);
        intent.putExtras(b);
        startActivity(intent);
    }

    //Opens Create Circle Activity upon clicking Create Circle Circle Button
    public void creatCircleBut(View v){
        Intent intent = new Intent(HomeScreen.this,CreateCircleActivity.class);
        Bundle b = new Bundle();
        b.putString("usern",user);
        intent.putExtras(b);
        startActivity(intent);
    }

    //Fragment Profile
    //Moves to login screen when logout is pressed
    public void logOut(View v){
        Intent intent = new Intent(HomeScreen.this,LoginActivity.class);
        startActivity(intent);
    }

    //Deletes Account Button
    public void deleteAccount(View v){
        //Following code builds a yes no dialog for Deletion of account
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:       //Yes Selected
                        User usr = MainActivity.database.MyDao().getUser(user);
                        List<Circles> x = usr.getJoinCircleList();
                        for (int j = x.size() -1;j>=0;j--){             //To remove the user from every circle a loop is made
                            Circles c = MainActivity.circleDatabase.circleDao().getCircle(x.get(j).getID());
                            if (c.getCreator().equals(usr.getName_user())){         //If the user is the creator then the circle is deleted
                                MainActivity.circleDatabase.circleDao().deleteCircle(c.getID());
                                List<String> z = c.getMembers();
                                for (int i = z.size() -1;i>=0;i--){             //To update data of every member of the circle user was a creator of
                                    User s = MainActivity.database.MyDao().getUser(z.get(i));
                                    s.setJoinedCircles(s.getJoinedCircles()-1);
                                    List<Circles> q = s.getJoinCircleList();
                                    for (int y = q.size() -1 ; y>=0;y--){       //Remove that circle from their list of circles
                                        if (q.get(y).getID().equals(c.getID())){
                                            q.remove(y);
                                            break;
                                        }
                                    }
                                    s.setJoinCircleList(q);
                                    MainActivity.database.MyDao().deleteUser(z.get(i));
                                    MainActivity.database.MyDao().addUser(s);
                                }
                            }
                            else {          //if the user was just a member of the circle
                                c.getMembers().remove(user);
                                c.setJoined(c.getJoined()-1);
                                MainActivity.circleDatabase.circleDao().deleteCircle(c.getID());
                                MainActivity.circleDatabase.circleDao().addCircle(c);
                            }
                        }
                        MainActivity.database.MyDao().deleteUser(user);         //finally delete user data
                        Intent intent = new Intent(HomeScreen.this,LoginActivity.class);
                        startActivity(intent);          //go back to login page
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to delete your account?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
