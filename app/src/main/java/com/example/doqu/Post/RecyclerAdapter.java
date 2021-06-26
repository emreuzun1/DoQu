package com.example.doqu.Post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doqu.Main.MainPageFragDirections;
import com.example.doqu.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Post> posts;
    private String user;

    public RecyclerAdapter(ArrayList<Post> posts,String user){
        this.posts = posts;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.username.setText(posts.get(position).getUsername());
        holder.desc.setText(posts.get(position).getDesc());
        View view = holder.view;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageFragDirections.ActionMainPageFragToPostFrag action = MainPageFragDirections.actionMainPageFragToPostFrag();
                action.setId(posts.get(position).getId());
                action.setDescription(posts.get(position).getDesc());
                action.setTitle(posts.get(position).getTitle());
                action.setUsername(posts.get(position).getUsername());
                action.setLikeCount(posts.get(position).getLike());
                action.setUser(user);
                Navigation.findNavController(view).navigate(action);
            }
        });


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
