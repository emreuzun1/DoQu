package com.example.doqu.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doqu.Post.Post;
import com.example.doqu.R;

import java.util.ArrayList;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ViewHolder>{

    private ArrayList<Post> list;

    public ProfileRecyclerAdapter (ArrayList<Post> list){
        this.list = list;
    }

    public ProfileRecyclerAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_list,parent,false);
        return new ProfileRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ProfileRecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.username.setText(list.get(position).getUsername());
        holder.desc.setText(list.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,username,desc;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            username = itemView.findViewById(R.id.username);
            desc = itemView.findViewById(R.id.desc);
            view = itemView;
        }
    }
}
