package com.example.shubhambhasin.main;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class searchBar extends Fragment {

    public static TextView user;
    public static ImageButton searchButton;
    public static EditText searchText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search_bar, container, false);
        user=(TextView)layout.findViewById(R.id.user);
        searchButton=(ImageButton)layout.findViewById(R.id.searchButton);
        searchText=(EditText)layout.findViewById(R.id.searchText);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searched=searchText.getText().toString();
                if(searched.equals(""))
                {
                    Toast.makeText(getActivity(),"Nothing to be searched for",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),searched, Toast.LENGTH_LONG).show();
                }
            }
        });
        return layout;


    }

    public void setUserName(String username)
    {
        user.setText("Hi," + username);
    }
}
