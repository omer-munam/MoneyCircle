package com.updesigns.moneycircle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//This class is used to display open circles that are available to join
//all functions used are for RecyclerView. Standard for every RecyclerView.

public class CircleAdapterRecycler extends RecyclerView.Adapter<CircleAdapterRecycler.ViewHolder>{

    List<Circles> items;
    private static ClickListener clickListener;


    public void setOnItemClickListener(ClickListener clickListener) {
        CircleAdapterRecycler.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public CircleAdapterRecycler(List<Circles> items) {
        this.items = items;
    }

    @Override
    public CircleAdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CircleAdapterRecycler.ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getCircleName());
        holder.ID.setText(items.get(position).getID());
        holder.creator.setText(items.get(position).getCreator());
        holder.amnt.setText(String.valueOf(items.get(position).getCash()));
        holder.mem.setText(String.valueOf(items.get(position).getJoined()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView ID;
        public TextView amnt;
        public TextView creator;
        public TextView mem;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.circleName);
            ID = itemView.findViewById(R.id.circleID);
            amnt = itemView.findViewById(R.id.circleAmnt);
            creator = itemView.findViewById(R.id.circleCreator);
            mem = itemView.findViewById(R.id.circleMembers);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

}
