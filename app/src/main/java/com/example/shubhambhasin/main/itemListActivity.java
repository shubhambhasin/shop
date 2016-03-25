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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class itemListActivity extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    GridView categories;
String subcategoryName;
    searchBar search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_item_list);
            categories=(GridView)findViewById(R.id.gridview);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            search_bar = (searchBar)getSupportFragmentManager().findFragmentById(R.id.searchfragment);
            search_bar.setUserName(ParseUser.getCurrentUser().getUsername());

            Intent subcategorySelected=getIntent();
            subcategoryId=subcategorySelected.getStringExtra("subcategoryId");

            subcategoryName=subcategorySelected.getStringExtra("subcategoryName");


            getSupportActionBar().setTitle(subcategoryName);



            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);



            final HashMap<Integer,String> item_name=new HashMap<>();
            final HashMap<Integer,Bitmap> item_image=new HashMap<>();
            final HashMap<String,String> item_map=new HashMap<>();
            final ParseQuery<ParseObject> itemquery= new ParseQuery(ItemTable.TABLE_NAME);
            itemquery.whereEqualTo(ItemTable.SUB_CATEGORY,ParseObject.createWithoutData(SubCategoryTable.TABLE_NAME,subcategoryId));
            itemquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> itemobjects, ParseException e) {
                    if (e == null) {
                        if (itemobjects.size() != 0) {

                            for (int x = 0; x <itemobjects.size(); x++) {
                                ParseObject itemobject = itemobjects.get(x);

                                String name=itemobject.getString(ItemTable.NAME);
                                item_name.put(x,name);

                                item_map.put(name,itemobject.getObjectId());
                                ParseFile file = (ParseFile)itemobjects.get(x).get(ItemTable.IMAGE);

                                final int finalX = x;

                                byte[] data=null;

                                try {
                                    data=file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                item_image.put(finalX, bitmap);


                            }


                            final String[] name=new String[item_name.size()];

                            for(int y=0;y<name.length;y++){
                                name[y]=item_name.get(y);

                            }

                            CustomGrid adapter = new CustomGrid(itemListActivity.this, name, item_image);
                            categories.setAdapter(adapter);


                            categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String item=name[position];
                                  //  Toast.makeText(getApplicationContext(),item_map.get(item),Toast.LENGTH_LONG).show();
                                    Intent categorySelectedIntent=new Intent(itemListActivity.this,itemDetails.class);
                                    categorySelectedIntent.putExtra("itemId",item_map.get(item));
                                    startActivity(categorySelectedIntent);
                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), "No items found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("item", "exceptional error " + e);
                    }
                }
            });






        }catch(Exception create_error){
            Log.d("user", "error in create main activity: " + create_error.getMessage());
            Toast.makeText(itemListActivity.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }


}



