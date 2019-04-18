package com.gymtrainer.trainerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gymtrainer.trainerapp.Activities.HomeActivity;
import com.gymtrainer.trainerapp.Models.Hire;
import com.gymtrainer.trainerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haroonpc on 3/27/2019.
 */

public class HireAdapter extends RecyclerView.Adapter<HireAdapter.HireAdapterViewHolder>
{

    Context context;
    ArrayList<Hire> hireList;

    public HireAdapter(Context context,ArrayList<Hire> hireList)
    {
        this.context = context;
        this.hireList = hireList;
    }

    @NonNull
    @Override
    public HireAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hired_trainers,viewGroup,false);
        return new HireAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HireAdapterViewHolder holder, int i) {
        Hire hire = hireList.get(i);
        holder.textViewName.setText(hire.getUserName());
        holder.textViewCategory.setText(hire.getCategoryName());
        holder.textViewRate.setText(hire.getRate()+"$");
        Picasso.get().load(hire.getImageUrl()).placeholder(R.drawable.ic_launcher_man).into(holder.hiredImg);
    }

    @Override
    public int getItemCount() {
        return hireList.size();
    }

    public class HireAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName,textViewRate,textViewCategory;
        CircleImageView hiredImg;

        public HireAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.hiredUserName);
            hiredImg = (CircleImageView)itemView.findViewById(R.id.hiredUsersImg);
            textViewRate = (TextView)itemView.findViewById(R.id.hiredUserRate);
            textViewCategory = (TextView)itemView.findViewById(R.id.hiredUsersCategoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Hire hire = hireList.get(getAdapterPosition());
                    ((HomeActivity)context).openNewActivity(hire.getHourList(),hire.getCategoryName(),hire.getUserId(),hire.getDate());

                }
            });
        }
    }
}
