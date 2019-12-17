package com.updesigns.moneycircle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransactionAdapterRecycler mAdapter;
    TextView balance,nam;
    String user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View vew =  inflater.inflate(R.layout.fragment_home,container,false);
        init(vew);
        return vew;
    }

    private void init(final View vew){
        final HomeScreen activity = (HomeScreen) getActivity();
        try{
            user = activity.getData();      //gets username of logged in from parent activity
        }catch (Exception e){};
        balance = vew.findViewById(R.id.balHome);
        nam = vew.findViewById(R.id.namHome);
        nam.setText(MainActivity.database.MyDao().getNameuser(user));
        //---If linked with bankaccount you will get the balance by the method specified by the bank
        String bal = String.valueOf(MainActivity.database.MyDao().getBalance(user)) + " PKR";
        balance.setText(bal);                   //Set value of Balance on Textview at home
        User y = MainActivity.database.MyDao().getUser(user);

        //Following code is used to display list of transactions by help of recyclerview and TransactionAdapterRecycler.class
        recyclerView = (RecyclerView) vew.findViewById(R.id.transactionLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(vew.getContext().getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TransactionAdapterRecycler(y.getTransactionList());
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        init(getView());        //repopulate the transaction list upon returning
        super.onResume();
    }
}
