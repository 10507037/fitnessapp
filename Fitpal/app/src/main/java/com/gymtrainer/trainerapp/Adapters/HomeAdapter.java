package com.gymtrainer.trainerapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gymtrainer.trainerapp.Models.Category;
import com.gymtrainer.trainerapp.R;

import java.util.ArrayList;

/**
 * Created by haroonpc on 3/14/2019.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeAdapterViewHolder>
{
    Context context;
    ArrayList<Category> arrayListUsers;

    public HomeAdapter(Context context, ArrayList<Category> arrayListUsers)
    {
        this.context = context;
        this.arrayListUsers = arrayListUsers;

    }

    @Override
    public HomeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home,parent,false);
        return new HomeAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HomeAdapterViewHolder holder, int position) {
        Category user = arrayListUsers.get(position);
        holder.textViewName.setText(user.getCategoryName());



    }

    @Override
    public int getItemCount() {
        return arrayListUsers.size();
    }

    public class HomeAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName,textViewCategory,textViewTime;

        public HomeAdapterViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.nameTxt);
            textViewCategory = (TextView)itemView.findViewById(R.id.categoryNameTxt);
            textViewTime = (TextView)itemView.findViewById(R.id.hourTxt);
        }
    }
}
