package com.example.shubhambhasin.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.HashMap;

public class popularhorizontallist extends Fragment {

    public static TextView user;
    public static ImageButton searchButton;
    public static EditText searchText;

    Gallery myHorizontalListView;
    AnHorizontalListViewFragment.MyAdapter myAdapter;
    private Context mContext;
    public String[] names;
    public HashMap<Integer,Bitmap> Images;


    public void setData(String[] names,HashMap<Integer,Bitmap> Images) {
        this.names=names;
        this.Images=Images;
        CustomGrid adapter = new CustomGrid(getActivity(), names, Images);
        myHorizontalListView.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_popularhorizontallist, container, false);

        myHorizontalListView=(Gallery)layout.findViewById(R.id.horizontallistview);



        return layout;


    }


}
