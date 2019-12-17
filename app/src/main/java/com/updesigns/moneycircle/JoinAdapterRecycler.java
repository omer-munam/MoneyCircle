package com.updesigns.moneycircle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

//This class is used to display circles that one has already joined
//all functions used are for RecyclerView. Standard for every RecyclerView.

public class JoinAdapterRecycler  extends RecyclerView.Adapter<JoinAdapterRecycler.ViewHolder>{

    List<Circles> items;


    public JoinAdapterRecycler(List<Circles> items) {
        this.items = items;
    }

    @Override
    public JoinAdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.join_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JoinAdapterRecycler.ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getCircleName());
        holder.ID.setText(items.get(position).getID());
        holder.creator.setText(items.get(position).getCreator());
        holder.amnt.setText(String.valueOf(items.get(position).getCash()));
        holder.mem.setText(String.valueOf(MainActivity.circleDatabase.circleDao().getjoined(items.get(position).getID()))); //inorder to get updated number of members
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView ID;
        public TextView amnt;
        public TextView creator;
        public TextView mem;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ID = itemView.findViewById(R.id.ID);
            amnt = itemView.findViewById(R.id.amnt);
            creator = itemView.findViewById(R.id.creator);
            mem = itemView.findViewById(R.id.members);
        }
    }

}
