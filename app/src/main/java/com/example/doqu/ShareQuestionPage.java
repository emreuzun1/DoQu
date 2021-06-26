package com.example.doqu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ShareQuestionPage extends Fragment {

    EditText title, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_question_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBHelper db = new DBHelper(getActivity().getApplicationContext());
        String username = ShareQuestionPageArgs.fromBundle(getArguments()).getUsername();
        Button button = (Button) view.findViewById(R.id.submitButton);
        title = (EditText) view.findViewById(R.id.title);
        description = (EditText) view.findViewById(R.id.description);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res = false;
                if (!title.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                    res = db.addPost(db.getWritableDatabase(), username, title.getText().toString(), description.getText().toString());
                }

                if (res) {
                    getActivity().onBackPressed();
                    Snackbar snackbar = Snackbar.make(view, R.string.question_created, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setDuration(3500);
                    snackbar.show();
                } else {
                    Toast.makeText(getActivity(), R.string.question_not_created, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}