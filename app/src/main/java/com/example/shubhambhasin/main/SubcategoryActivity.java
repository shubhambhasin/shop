package com.example.shubhambhasin.main;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class SubcategoryActivity extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    ListView subcategories;
    searchBar search_bar;
<<<<<<< HEAD
    popularhorizontallist popular;
    RelativeLayout layoutLoading;
    Activity context;
=======

>>>>>>> a78e5baede71bcb01b8297cd8b7acb11f840d076

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_subcategory);
            subcategories=(ListView)findViewById(R.id.listview);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Categories");

<<<<<<< HEAD
            context= this;

            popular=(popularhorizontallist)getSupportFragmentManager().findFragmentById(R.id.popularfragment);
=======
>>>>>>> a78e5baede71bcb01b8297cd8b7acb11f840d076
            search_bar = (searchBar)getSupportFragmentManager().findFragmentById(R.id.searchfragment);
            search_bar.setUserName(ParseUser.getCurrentUser().getUsername());

            layoutLoading=(RelativeLayout)findViewById(R.id.loadingPanel);
           // layoutLoading.setVisibility(View.GONE);


            Intent categorySelectedIntent=getIntent();
            categoryId=categorySelectedIntent.getStringExtra("categoryId");


            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);






            final HashMap<Integer,String> subcategory_name=new HashMap<>();
            final HashMap<Integer,Bitmap> subcategory_image=new HashMap<>();
            final HashMap<String,String> subcategory_map=new HashMap<>();
            final ParseQuery<ParseObject> subcategoryquery= new ParseQuery(SubCategoryTable.TABLE_NAME);
            subcategoryquery.whereEqualTo(SubCategoryTable.CATEGORY,ParseObject.createWithoutData(CategoryTable.TABLE_NAME,categoryId));
            subcategoryquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> subcategoryobjects, ParseException e) {
                    if (e == null) {
                        if (subcategoryobjects.size() != 0) {

                            for (int x = 0; x < subcategoryobjects.size(); x++) {
                                ParseObject categoryobject = subcategoryobjects.get(x);

                                String name = categoryobject.getString(CategoryTable.NAME);
                                subcategory_name.put(x, name);

                                subcategory_map.put(name, categoryobject.getObjectId());
                                ParseFile file = (ParseFile) subcategoryobjects.get(x).get(CategoryTable.IMAGE);

                                final int finalX = x;

                                byte[] data = null;

                                try {
                                    data = file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                ImageResizer ir = new ImageResizer();
                                Bitmap bitmap = ir.resizeImage(data, 150, 150);
                                subcategory_image.put(finalX, bitmap);


                            }


                            final String[] name = new String[subcategory_name.size()];

                            for (int y = 0; y < name.length; y++) {
                                name[y] = subcategory_name.get(y);

                            }

                            CustomList adapter = new CustomList(SubcategoryActivity.this, name, subcategory_image, "heading");
                            subcategories.setAdapter(adapter);
                            new LoadingSyncList(context,layoutLoading,null).execute();


                            subcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String item = name[position];

                                    //Toast.makeText(getApplicationContext(),subcategory_map.get(item),Toast.LENGTH_LONG).show();
                                    Intent subcategorySelectedIntent = new Intent(SubcategoryActivity.this, itemListActivity.class);
                                    subcategorySelectedIntent.putExtra("subcategoryId", subcategory_map.get(item));
                                    subcategorySelectedIntent.putExtra("subcategoryName", item);
                                    subcategorySelectedIntent.putExtra("from", "subcategories");
                                    startActivity(subcategorySelectedIntent);
                                }
                            });


                        } else {
                            layoutLoading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "No subcategories found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("subcategory", "exceptional error " + e);
                    }
                }
            });



        }catch(Exception create_error){
            Log.d("user", "error in create main activity: " + create_error.getMessage());
            Toast.makeText(SubcategoryActivity.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ParseUser.getCurrentUser()==null){
            Intent tologin=new Intent(SubcategoryActivity.this,login.class);
            startActivity(tologin);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent tomain=new Intent(SubcategoryActivity.this,MainActivity.class);
        startActivity(tomain);
    }
}



