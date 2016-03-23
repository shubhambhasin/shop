package com.example.shubhambhasin.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class itemDetails extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    ImageView itemimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_item_details);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            Intent fromitemselected=getIntent();
            itemId=fromitemselected.getStringExtra("itemId");


            ParseObject itemobject=ParseObject.createWithoutData(ItemTable.TABLE_NAME,itemId);

            getSupportActionBar().setTitle(itemobject.getString(ItemTable.NAME));



            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);





        }catch(Exception create_error){
            Log.d("user", "error in item details activity: " + create_error.getMessage());
            Toast.makeText(itemDetails.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }


}



