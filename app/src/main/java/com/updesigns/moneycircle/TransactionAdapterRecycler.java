package com.updesigns.moneycircle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

//This class is used to display Transactions that are performed on one's account
//all functions used are for RecyclerView. Standard for every RecyclerView.

public class TransactionAdapterRecycler   extends RecyclerView.Adapter<TransactionAdapterRecycler.ViewHolder>{

    List<Transaction> items;


    public TransactionAdapterRecycler(List<Transaction> items) {
        this.items = items;
    }

    @Override
    public TransactionAdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_row,parent,false);
        return new TransactionAdapterRecycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionAdapterRecycler.ViewHolder holder, int position) {
//        Log.d("chk",items.get(position).getCircle());
        holder.name.setText(items.get(position).getCircle());
        String s;
        if (items.get(position).getType() == 1){    //type one indicates inflow of cash and type 2 indicates outflow
            s = "Debit ";
            holder.type.setText(s);
        }
        else if (items.get(position).getType() == 2){
            s = "Credit ";
            holder.type.setText(s);
        }
        s= String.valueOf(items.get(position).getAmount()) + " PKR";
        holder.amnt.setText(s);
        holder.date.setText(items.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView type;
        public TextView amnt;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCircle);
            type = itemView.findViewById(R.id.typeTrans);
            amnt = itemView.findViewById(R.id.transAmount);
            date = itemView.findViewById(R.id.dateTrans);
        }
    }

}
