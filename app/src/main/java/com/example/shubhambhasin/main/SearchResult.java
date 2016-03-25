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
import android.widget.ListView;
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

public class SearchResult extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    ListView searchList;
    searchBar search_bar;
    final HashMap<Integer,String> search_name=new HashMap<>();
    final HashMap<Integer,Bitmap> search_image=new HashMap<>();
    final HashMap<String,String> search_map=new HashMap<>();
    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_search_result);
            searchList=(ListView)findViewById(R.id.gridview);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Intent searchIntent=getIntent();
            String searchedString=searchIntent.getStringExtra("searchedstring");
            getSupportActionBar().setTitle("Searched : " + searchedString);

            search_bar = (searchBar)getSupportFragmentManager().findFragmentById(R.id.searchfragment);
            search_bar.setUserName(ParseUser.getCurrentUser().getUsername());

            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);

            final String[] searchstringcomponents=searchedString.split(" ");

            checkByItem(searchstringcomponents,searchstringcomponents.length);
            checkBySubCategory(searchstringcomponents,searchstringcomponents.length);

            final String[] name=new String[search_name.size()];

            for(int y=0;y<name.length;y++){
                name[y]=search_name.get(y);

            }

            CustomList adapter = new CustomList(SearchResult.this, name,search_image);
            searchList.setAdapter(adapter);


        }catch(Exception create_error){
            Log.d("user", "error in create search activity: " + create_error.getMessage());
            Toast.makeText(SearchResult.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ParseUser.getCurrentUser()==null){
            Intent tologin=new Intent(SearchResult.this,login.class);
            startActivity(tologin);
        }

    }




    protected void checkByItem(String[] searchedstringcomponents,int size) throws ParseException {
        ParseQuery<ParseObject> searchquery=ParseQuery.getQuery(ItemTable.TABLE_NAME);
        List<ParseObject> itemobjects=searchquery.find();
        int c=0;
        if(itemobjects.size()!=0){
            for(int y=0;y<itemobjects.size();y++) {
                ParseObject itemobject = itemobjects.get(y);
                String brand = itemobject.getString(ItemTable.BRAND);
                String name = itemobject.getString(ItemTable.NAME);

                for (int x = 0; x < size; x++){                 //for each component

                    if (brand.equalsIgnoreCase(searchedstringcomponents[x])) {                  //check brand
                        c++;
                        String itemname = itemobject.fetchIfNeeded().getString(ItemTable.NAME);
                        String itembrand = itemobject.fetchIfNeeded().getString(ItemTable.BRAND);
                        String itemdetails = itemname + "\n" + itembrand;

                        search_name.put(index, itemdetails);

                        search_map.put(itemdetails, itemobject.getObjectId());

                        ParseFile file = (ParseFile) itemobject.get(ItemTable.IMAGE);

                        byte[] data = null;

                        try {
                            data = file.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        ImageResizer ir = new ImageResizer();
                        Bitmap bitmap = ir.resizeImage(data, 100, 100);
                        search_image.put(index, bitmap);
                        index++;
                    }


            }



                String[] namecomponents=name.split(" ");

                for(int x=0;x<namecomponents.length;x++)
                {
                    for(int z=0;z<size;z++){
                        if(namecomponents[x].equalsIgnoreCase(searchedstringcomponents[z])){
                            c++;
                            String itemname = itemobject.fetchIfNeeded().getString(ItemTable.NAME);
                            String itembrand = itemobject.fetchIfNeeded().getString(ItemTable.BRAND);
                            String itemdetails = itemname + "\n" + itembrand;


                            if(search_map.get(itemdetails)==null) {
                                search_name.put(index, itemdetails);

                                search_map.put(itemdetails, itemobject.getObjectId());

                                ParseFile file = (ParseFile) itemobject.get(ItemTable.IMAGE);

                                byte[] data = null;

                                try {
                                    data = file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                ImageResizer ir = new ImageResizer();
                                Bitmap bitmap = ir.resizeImage(data, 100, 100);
                                search_image.put(index, bitmap);
                                index++;
                            }
                        }
                    }
                }
            }

            Log.d("count from brand", String.valueOf(c));

        }else
        {
            Log.d("search", " not item available ");
        }

    }








    protected void checkBySubCategory(String[] searchedstringcomponents,int size) throws ParseException {
        ParseQuery<ParseObject> searchquery=ParseQuery.getQuery(SubCategoryTable.TABLE_NAME);
        List<ParseObject> subcategoryobjects=searchquery.find();
        int c=0;
        if(subcategoryobjects.size()!=0){
            for(int y=0;y<subcategoryobjects.size();y++) {
                ParseObject subcategoryobject = subcategoryobjects.get(y);
                String name = subcategoryobject.getString(SubCategoryTable.NAME);

                for (int x = 0; x < size; x++){                 //for each component

                    if (name.equalsIgnoreCase(searchedstringcomponents[x])) {                  //check brand
                        c++;

                        ParseQuery<ParseObject> itemsfromsubcategoryquery=ParseQuery.getQuery(ItemTable.TABLE_NAME);

                        List<ParseObject> itemobjects=itemsfromsubcategoryquery.find();
                        if(itemobjects.size()!=0){
                            for(int z=0;z<itemobjects.size();z++) {
                                ParseObject itemobject = itemobjects.get(z);

                                String itemname = itemobject.fetchIfNeeded().getString(ItemTable.NAME);
                                String itembrand = itemobject.fetchIfNeeded().getString(ItemTable.BRAND);
                                String itemdetails = itemname + "\n" + itembrand;

                                if (search_map.get(itemdetails) == null) {
                                    search_name.put(index, itemdetails);

                                    search_map.put(itemdetails, itemobject.getObjectId());

                                    ParseFile file = (ParseFile) itemobject.get(ItemTable.IMAGE);

                                    byte[] data = null;

                                    try {
                                        data = file.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    ImageResizer ir = new ImageResizer();
                                    Bitmap bitmap = ir.resizeImage(data, 100, 100);
                                    search_image.put(index, bitmap);
                                    index++;
                                }

                            }

                        }

                    }else
                    {
                            Log.d("items from subcategory","no item");
                    }

                }

            }

            Log.d("count from brand", String.valueOf(c));

        }else
        {
            Log.d("search"," not item available ");
        }

    }




}



