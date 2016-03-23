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

public class MainActivity extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    GridView categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            categories=(GridView)findViewById(R.id.gridview);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Categories");



            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);



            final HashMap<Integer,String> category_name=new HashMap<>();
            final HashMap<Integer,Bitmap> category_image=new HashMap<>();
            final ParseQuery<ParseObject> categoryquery= new ParseQuery<ParseObject>(CategoryTable.TABLE_NAME);
            categoryquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> categoryobjects, ParseException e) {
                    if (e == null) {
                        if (categoryobjects.size() != 0) {

                            for (int x = 0; x < categoryobjects.size(); x++) {
                                ParseObject categoryobject = categoryobjects.get(x);
                                category_name.put(x, categoryobject.getString(CategoryTable.NAME));
                                ParseFile file = (ParseFile) categoryobjects.get(x).get(CategoryTable.IMAGE);

                                final int finalX = x;
                                file.getDataInBackground(new GetDataCallback() {

                                    public void done(byte[] data, ParseException e) {
                                        if (e == null) {

                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            category_image.put(finalX, bitmap);
                                        }
                                    }
                                });
                            }


                            String[] name=new String[category_name.size()];

                            for(int y=0;y<name.length;y++){
                                name[y]=category_name.get(y);

                            }

                            CustomGrid adapter = new CustomGrid(MainActivity.this, name, category_image);
                            categories.setAdapter(adapter);

                        } else {
                            Toast.makeText(getApplicationContext(), "No categoriees found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("ategory", "exceptional error");
                    }
                }
            });






        }catch(Exception create_error){
            Log.d("user", "error in create main activity: " + create_error.getMessage());
            Toast.makeText(MainActivity.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }

}
