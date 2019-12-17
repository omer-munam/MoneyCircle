package com.updesigns.moneycircle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//Class for fragment of circle on homescreen

public class CircleFragment extends Fragment {
    String user;
    private RecyclerView recyclerView2;
    private JoinAdapterRecycler mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_circle,container,false);
        init(view);
        return view;
    }

    private void init(final View view){
        HomeScreen activity = (HomeScreen) getActivity();
        try{
            user = activity.getData();          //This gets the username that is present in parent activity into the fragment
        }catch (Exception e){}

        //The following code uses the recyclerview in the layout to display the list of circles joined by the user
        User x = MainActivity.database.MyDao().getUser(user);
        recyclerView2= (RecyclerView)view.findViewById(R.id.recyclerJoined);
        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        mAdapter= new JoinAdapterRecycler(x.getJoinCircleList());
        mAdapter.notifyDataSetChanged();
        recyclerView2.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        init(getView());        //init method is ran again so that recyclerview can be repopulated when we return to this screen
        super.onResume();
    }
}
