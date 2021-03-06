package com.example.shubhambhasin.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    GridView categories;
    searchBar search_bar;
    popularhorizontallist popular;
    RelativeLayout layoutLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            categories=(GridView)findViewById(R.id.gridview);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Sections");

            layoutLoading=(RelativeLayout)findViewById(R.id.loadingPanel);
            //layoutLoading.setVisibility(View.GONE);
            context=this;

            search_bar = (searchBar)getSupportFragmentManager().findFragmentById(R.id.searchfragment);
            search_bar.setUserName(ParseUser.getCurrentUser().getUsername());

            popular=(popularhorizontallist)getSupportFragmentManager().findFragmentById(R.id.popularfragment);

            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);



            final HashMap<Integer,String> item_name=new HashMap<>();
            final HashMap<Integer,Bitmap> item_image=new HashMap<>();
            final HashMap<String,String> item_map=new HashMap<>();
            final ParseQuery<ParseObject> itemquery= new ParseQuery(ItemTable.TABLE_NAME);
            itemquery.addDescendingOrder(ItemTable.COUNT);
            itemquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> categoryobjects, ParseException e) {
                    if (e == null) {
                        if (categoryobjects.size() != 0) {

                            for (int x = 0; x < 7; x++) {
                                ParseObject categoryobject = categoryobjects.get(x);

                                String name = categoryobject.getString(ItemTable.NAME);
                                item_name.put(x, name);

                                item_map.put(name, categoryobject.getObjectId());
                                ParseFile file = (ParseFile) categoryobjects.get(x).get(ItemTable.IMAGE);

                                final int finalX = x;

                                byte[] data = null;

                                try {
                                    data = file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                ImageResizer ir = new ImageResizer();
                                Bitmap bitmap = ir.resizeImage(data, 200, 180);
                                //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                item_image.put(finalX, bitmap);


                            }


                            final String[] name = new String[item_name.size()];

                            for (int y = 0; y < name.length; y++) {
                                name[y] = item_name.get(y);

                            }

                           popular.setData(name,item_image,item_map);




                        } else {
                            Toast.makeText(getApplicationContext(), "No categories found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("category", "exceptional error " + e);
                    }
                }
            });








            final HashMap<Integer,String> category_name=new HashMap<>();
            final HashMap<Integer,Bitmap> category_image=new HashMap<>();
            final HashMap<String,String> category_map=new HashMap<>();
            final ParseQuery<ParseObject> categoryquery= new ParseQuery(CategoryTable.TABLE_NAME);
            categoryquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> categoryobjects, ParseException e) {
                    if (e == null) {
                        if (categoryobjects.size() != 0) {

                            for (int x = 0; x < categoryobjects.size(); x++) {
                                ParseObject categoryobject = categoryobjects.get(x);

                                String name=categoryobject.getString(CategoryTable.NAME);
                                category_name.put(x,name);

                                category_map.put(name,categoryobject.getObjectId());
                                ParseFile file = (ParseFile) categoryobjects.get(x).get(CategoryTable.IMAGE);

                                final int finalX = x;

                                byte[] data=null;

                                try {
                                data=file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                ImageResizer ir=new ImageResizer();
                                Bitmap bitmap = ir.resizeImage(data,200,180);
                                //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            category_image.put(finalX, bitmap);


                            }


                            final String[] name=new String[category_name.size()];

                            for(int y=0;y<name.length;y++){
                                name[y]=category_name.get(y);

                            }

                            CustomGrid adapter = new CustomGrid(MainActivity.this, name, category_image);
                            categories.setAdapter(adapter);
                            new LoadingSyncList(context,layoutLoading,null).execute();


                            categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    String item=name[position];
                                  // Toast.makeText(getApplicationContext(),category_map.get(item),Toast.LENGTH_LONG).show();
                                    Intent categorySelectedIntent=new Intent(MainActivity.this,SubcategoryActivity.class);
                                    categorySelectedIntent.putExtra("categoryId",category_map.get(item));
                                    startActivity(categorySelectedIntent);
                                }
                            });


                        } else {
                            layoutLoading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "No categories found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("category", "exceptional error " + e);
                    }
                }
            });






        }catch(Exception create_error){
            Log.d("user", "error in create main activity: " + create_error.getMessage());
            Toast.makeText(MainActivity.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ParseUser.getCurrentUser()==null){
            Intent tologin=new Intent(MainActivity.this,login.class);
            startActivity(tologin);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



