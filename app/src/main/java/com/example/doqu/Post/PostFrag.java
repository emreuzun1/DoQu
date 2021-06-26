package com.example.doqu.Post;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doqu.Comment;
import com.example.doqu.DBHelper;
import com.example.doqu.R;
import com.example.doqu.Settings;

import java.util.ArrayList;


public class PostFrag extends Fragment {

    TextView title, username, description, likeCountText;
    String user;
    EditText commentText;
    DBHelper db;
    ArrayList<Comment> comments;

    int id, likeCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
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
                intent.putExtra("USERNAME",username.getText().toString());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = (TextView) view.findViewById(R.id.title);
        username = (TextView) view.findViewById(R.id.username);
        description = (TextView) view.findViewById(R.id.description);
        likeCountText = (TextView) view.findViewById(R.id.likeCount);
        commentText = view.findViewById(R.id.commentText);
        db = new DBHelper(getActivity().getApplicationContext());

        user = PostFragArgs.fromBundle(getArguments()).getUser();
        title.setText(PostFragArgs.fromBundle(getArguments()).getTitle());
        username.setText(PostFragArgs.fromBundle(getArguments()).getUsername());
        description.setText(PostFragArgs.fromBundle(getArguments()).getDescription());
        id = PostFragArgs.fromBundle(getArguments()).getId();
        likeCount = PostFragArgs.fromBundle(getArguments()).getLikeCount();
        likeCountText.setText(String.valueOf(likeCount));

        comments = new ArrayList<>();
        getComments();


        ListView listView = view.findViewById(R.id.commentList);
        MyAdapter myAdapter = new MyAdapter(getContext(), R.layout.post_comment_list, comments);

        listView.setAdapter(myAdapter);

        view.findViewById(R.id.likeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCount += 1;
                db.likePost(db.getWritableDatabase(), id, likeCount);
                likeCountText.setText(String.valueOf(likeCount));
            }
        });

        view.findViewById(R.id.commentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentText.getText().toString().isEmpty()) {
                    db.insertComment(db.getWritableDatabase(), id, user, commentText.getText().toString());
                    onViewCreated(view,savedInstanceState);
                    closeKeyboard();
                    commentText.setText("");
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void getComments(){
        Cursor res = db.getComments(db.getWritableDatabase(), id);
        if (res != null) {
            if (res.getCount() != 0) {
                while (res.moveToNext()) {
                    comments.add(new Comment(res.getInt(0), res.getString(2), res.getString(3)));
                }
            }
        }
    }

    class MyAdapter extends ArrayAdapter<Comment> {

        Context context;
        int resource;
        ArrayList<Comment> comments;


        public MyAdapter(Context context, int resource, ArrayList<Comment> comments) {
            super(context, resource, comments);

            this.context = context;
            this.resource = resource;
            this.comments = comments;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.post_comment_list, null);

            TextView username = view.findViewById(R.id.commentUsername);
            TextView description = view.findViewById(R.id.commentDescription);

            Comment c = comments.get(position);
            username.setText(c.getUsername());
            description.setText(c.getDescription());

            return view;
        }
    }

    protected class MyAsyncTask extends AsyncTask<Integer,Comment,Void>{
        private ContentValues postValues;
        private ProgressBar progressBar;

        public MyAsyncTask(ProgressBar progressBar){
            this.progressBar = progressBar;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int postId = integers[0];
            SQLiteOpenHelper dbHelper = new DBHelper(getActivity());
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.update("POSTS",postValues, "POSTID = ?",new String[] {Integer.toString(postId)});
                Thread.sleep(500);
                db.close();
            }catch(SQLiteException | InterruptedException e){

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            postValues = new ContentValues();
            postValues.put("LIKES",PostFragArgs.fromBundle(getArguments()).getLikeCount() + 1);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Comment... values) {
            super.onProgressUpdate(values);
        }
    }


}