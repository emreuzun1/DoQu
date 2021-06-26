package com.example.doqu.Profile;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doqu.DBHelper;
import com.example.doqu.Post.Post;
import com.example.doqu.R;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    DBHelper db;
    String username;
    TextView profileUsernameText,profileNameText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolBar = view.findViewById(R.id.profileToolBar);

        db = new DBHelper(getActivity());

        username = getActivity().getIntent().getStringExtra("USERNAME");

        profileNameText = view.findViewById(R.id.profileNameText);
        profileUsernameText = view.findViewById(R.id.profileUsernameText);

        Cursor res = db.getProfile(username);
        Profile profile = null;

        if(res != null){
            while(res.moveToNext()){
                profile = new Profile(res.getString(1), res.getString(0));
            }
        }

        profileNameText.setText(profile.getName());
        profileUsernameText.setText("@" + profile.getUsername());

        ProgressBar progressBar = view.findViewById(R.id.profileProgressBar);
        RecyclerView recyclerView = view.findViewById(R.id.profileList);
        ProfileFragment.MyAsyncTask task = new ProfileFragment.MyAsyncTask(recyclerView, progressBar, getActivity());
        task.execute();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    protected class MyAsyncTask extends AsyncTask<Void, Post, ArrayList> {

        private Cursor res;
        private ArrayList<Post> list = new ArrayList<>();
        private ProgressBar progressBar;
        private RecyclerView recyclerView;
        private Context context;
        private ProfileRecyclerAdapter adapter;

        public MyAsyncTask(RecyclerView recyclerView, ProgressBar progressBar, Context context) {
            this.recyclerView = recyclerView;
            this.progressBar = progressBar;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            db = new DBHelper(context);
            res = db.getProfilePost(username);
            adapter = new ProfileRecyclerAdapter(list);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList doInBackground(Void... voids) {
            if (res != null) {
                while (res.moveToNext()) {
                    Post p = new Post(res.getInt(0),
                            res.getString(1),
                            res.getString(2),
                            res.getString(3),
                            res.getInt(4));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    publishProgress(p);
                }
                res.close();
            }
            db.close();
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList list) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Post... values) {
            list.add(values[0]);

        }
    }
}