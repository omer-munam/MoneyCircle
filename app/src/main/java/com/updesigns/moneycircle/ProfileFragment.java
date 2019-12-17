package com.updesigns.moneycircle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView img;
    TextView nameTxt,joined;
    String user, ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile,container,false);
        img = view.findViewById(R.id.imgProfile);
        joined = view.findViewById(R.id.circleJoined);
        nameTxt = view.findViewById(R.id.nameProfile);
        HomeScreen activity = (HomeScreen) getActivity();
        try{
            user = activity.getData();      //get username from parent activity
        }catch (Exception e){};
        setDet();
        return view;
    }

    public void setDet(){
        //***Once points are formed you can link to the textView already in the layout or create another one to display points..
        //***Use the database to access users points...
        nameTxt.setText(MainActivity.database.MyDao().getNameuser(user));       //set name
        ID = MainActivity.database.MyDao().getFbIDbyUser(user);
        String profile_pic = "https://graph.facebook.com/"+ID+"/picture?width=250&height=250";          //get profile picture from facebook
        Picasso.with(this.getActivity()).load(profile_pic).fit().centerInside().into(img);              //set it inside the view
        joined.setText(String.valueOf(MainActivity.database.MyDao().getJoined(user)));                  //set the number of circles joined
    }

    @Override
    public void onResume() {
        super.onResume();
        setDet();       //refreshes data when returned on this screen
    }

}
