package com.example.doqu.Main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.doqu.DBHelper;
import com.example.doqu.Post.Post;
import com.example.doqu.R;
import com.example.doqu.Post.RecyclerAdapter;
import com.example.doqu.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainPageFrag extends Fragment {

    FloatingActionButton addButton;
    DBHelper db;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.settings:
                Intent intent = new Intent(getContext(), Settings.class);
                intent.putExtra("USERNAME",username);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        username = getActivity().getIntent().getStringExtra("USERNAME");
        RecyclerView listView = view.findViewById(R.id.list);
        MyAsyncTask task = new MyAsyncTask(listView, progressBar, getContext());
        task.execute();
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageFragDirections.ActionMainPageFragToShareQuestionPage action = MainPageFragDirections.actionMainPageFragToShareQuestionPage();
                action.setUsername(username);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    protected class MyAsyncTask extends AsyncTask<Void, Post, ArrayList> {

        private Cursor res;
        private ArrayList<Post> list = new ArrayList<>();
        private ProgressBar progressBar;
        private RecyclerView recyclerView;
        private Context context;
        private RecyclerAdapter adapter;

        public MyAsyncTask(RecyclerView recyclerView, ProgressBar progressBar, Context context) {
            this.recyclerView = recyclerView;
            this.progressBar = progressBar;
            this.context = context;
        }

        public RecyclerAdapter getAdapter() {
            return adapter;
        }

        @Override
        protected void onPreExecute() {
            db = new DBHelper(context);
            res = db.getPosts();
            adapter = new RecyclerAdapter(list, username);
            System.out.println(username);
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

