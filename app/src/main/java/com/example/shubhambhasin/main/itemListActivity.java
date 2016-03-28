package com.example.shubhambhasin.main;


import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class itemListActivity extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    GridView categories;
String subcategoryName;
    searchBar search_bar;
    Button filterbutton;
    SeekBar pricerange;
    CheckBox statuspricefilter;
    RadioButton mostpopularcheck;
    RadioButton pricelth;
    RadioButton pricehtl;
    GridView filtergrid;
    Model[] modelItems;
    Button filterproductbutton;
    static int size;

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



            filterbutton=(Button)findViewById(R.id.filterbutton);
            final Intent subcategorySelected=getIntent();
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
            itemquery.whereEqualTo(ItemTable.SUB_CATEGORY, ParseObject.createWithoutData(SubCategoryTable.TABLE_NAME, subcategoryId));


            List<ParseObject> itemobjects= null;
            try {
                itemobjects = itemquery.find();

            if (itemobjects.size() != 0) {

                                for (int x = 0; x < itemobjects.size(); x++) {
                                    ParseObject itemobject = itemobjects.get(x);

                                    String name = itemobject.getString(ItemTable.NAME);
                                    String brand = itemobject.getString(ItemTable.BRAND);
                                    String itemdetails = name + "\nBy " + brand;
                                    item_name.put(x, itemdetails);

                                    item_map.put(itemdetails, itemobject.getObjectId());
                                    ParseFile file = (ParseFile) itemobjects.get(x).get(ItemTable.IMAGE);



                                    byte[] data = null;

                                    try {
                                        data = file.getData();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                    ImageResizer ir = new ImageResizer();
                                    Bitmap bitmap = ir.resizeImage(data, 200, 180);
                                    item_image.put(x, bitmap);


                                }


                                final String[] name = new String[item_name.size()];

                                for (int y = 0; y < name.length; y++) {
                                    name[y] = item_name.get(y);

                                }

                                CustomGrid adapter = new CustomGrid(itemListActivity.this, name, item_image);
                                categories.setAdapter(adapter);


                                filterbutton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final Dialog filterdialog=new Dialog(itemListActivity.this);
                                        filterdialog.setContentView(R.layout.filter_layout);

                                        filterdialog.setTitle("Filters");

                                        filtergrid=(GridView)filterdialog.findViewById(R.id.filtergrid);
                                        filterproductbutton=(Button)filterdialog.findViewById(R.id.done);
                                        mostpopularcheck=(RadioButton)filterdialog.findViewById(R.id.mostpopular);
                                        pricehtl=(RadioButton)filterdialog.findViewById(R.id.orderpricehtl);
                                        pricelth=(RadioButton)filterdialog.findViewById(R.id.orderpricelth);
                                        pricerange=(SeekBar)filterdialog.findViewById(R.id.priceseekbar);
                                        pricerange.setVisibility(View.INVISIBLE);
                                        statuspricefilter=(CheckBox)filterdialog.findViewById(R.id.pricerangestatus);

                                        statuspricefilter.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if( statuspricefilter.isChecked()) {
                                                    statuspricefilter.setChecked(true);
                                                    pricerange.setVisibility(View.VISIBLE);
                                                }else
                                                {
                                                    statuspricefilter.setChecked(false);
                                                    pricerange.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });


                                        pricerange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                            Dialog scrollvaluedialog=new Dialog(itemListActivity.this);

                                            TextView scrollvalue;
                                            @Override
                                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                scrollvaluedialog.setContentView(R.layout.scrollvalue);
                                                scrollvalue=(TextView)scrollvaluedialog.findViewById(R.id.scrollvalue);

                                                scrollvalue.setText(String.valueOf(seekBar.getProgress()));

                                            }

                                            @Override
                                            public void onStartTrackingTouch(SeekBar seekBar) {
                                                scrollvaluedialog.show();

                                            }

                                            @Override
                                            public void onStopTrackingTouch(SeekBar seekBar) {
                                                scrollvaluedialog.dismiss();
                                            }
                                        });

                                        final HashMap<String,String> brand_map=new HashMap<>();
                                        final ParseQuery<ParseObject> brandlistingquery=ParseQuery.getQuery(ItemTable.TABLE_NAME);
                                        brandlistingquery.whereEqualTo(ItemTable.SUB_CATEGORY,ParseObject.createWithoutData(SubCategoryTable.TABLE_NAME,subcategoryId));
                                        List<ParseObject> itemobjectsinfilter= null;
                                        try {
                                            itemobjectsinfilter = brandlistingquery.find();

                                        int i = 0;
                                                    if (itemobjectsinfilter.size() != 0) {

                                                        for (int x = 0; x < itemobjectsinfilter.size(); x++) {
                                                            ParseObject itemobject = itemobjectsinfilter.get(x);
                                                            String brand = itemobject.getString(ItemTable.BRAND).toUpperCase();
                                                            String itemid = itemobject.getObjectId();

                                                            if (brand_map.get(brand) == null) {
                                                                brand_map.put(brand, itemid);
                                                            }
                                                        }

                                                        modelItems = new Model[brand_map.size()];
                                                        Iterator it = brand_map.entrySet().iterator();
                                                        while (it.hasNext()) {
                                                            Map.Entry pair = (Map.Entry)it.next();
                                                            modelItems[i] = new Model(pair.getKey().toString(), 0);
                                                            i++;
                                                            it.remove(); // avoids a ConcurrentModificationException
                                                        }
                                                        CustomAdapter adapter = new CustomAdapter(itemListActivity.this, modelItems);
                                                        filtergrid.setAdapter(adapter);
                                                        filterdialog.show();


                                                        filterproductbutton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                filterdialog.dismiss();
                                                                size=modelItems.length;

                                                                boolean brandfiltering=false;

                                                                int flag = 0;
                                                                for (int l = 0; l < size; l++) {
                                                                    if (modelItems[l].isChecked()) {
                                                                        flag = 1;
                                                                        brandfiltering=true;
                                                                        break;
                                                                    }
                                                                }
                                                                if(statuspricefilter.isChecked()){
                                                                    flag=1;
                                                                }
                                                                if(mostpopularcheck.isChecked()){
                                                                    flag=1;
                                                                }
                                                                if(pricelth.isChecked()){
                                                                    flag=1;
                                                                }
                                                                if(pricehtl.isChecked()){
                                                                    flag=1;
                                                                }


                                                                if (flag != 0)
                                                                    applyfilter(size,brandfiltering,statuspricefilter.isChecked(),pricerange.getProgress(),mostpopularcheck.isChecked(),pricehtl.isChecked(),pricelth.isChecked());



                                                            }
                                                        });




                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "No filters found", Toast.LENGTH_SHORT).show();
                                                    }


                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }


                                    }
                                });

                                categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String item = name[position];
                                        //  Toast.makeText(getApplicationContext(),item_map.get(item),Toast.LENGTH_LONG).show();
                                        Intent categorySelectedIntent = new Intent(itemListActivity.this, itemDetails.class);
                                        categorySelectedIntent.putExtra("itemId", item_map.get(item));
                                        categorySelectedIntent.putExtra("subcategoryId", subcategoryId);
                                        categorySelectedIntent.putExtra("subcategoryName", subcategoryName);
                                        startActivity(categorySelectedIntent);
                                    }
                                });


                            } else {
                filterbutton.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "No items found", Toast.LENGTH_LONG).show();
                            }
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }catch(Exception create_error){
            Log.d("user", "error in create main activity: " + create_error.getMessage());
            Toast.makeText(itemListActivity.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }



    protected void applyfilter(final int size, final boolean brandfiltering,boolean ifpricefilter,int priceRange,boolean mostpopular,boolean pricehtl,boolean pricelth)
    {

            categories.removeAllViewsInLayout();


        final HashMap<Integer, String> item_name = new HashMap<>();
        final HashMap<Integer, Bitmap> item_image = new HashMap<>();
        final HashMap<String, String> item_map = new HashMap<>();
        final ParseQuery<ParseObject> itemquery = new ParseQuery(ItemTable.TABLE_NAME);
        if(ifpricefilter){
            itemquery.whereLessThan(ItemTable.PRICE,priceRange);
        }
        if(mostpopular){
            itemquery.addDescendingOrder(ItemTable.COUNT);
        }
        if(pricehtl){
            itemquery.addDescendingOrder(ItemTable.PRICE);
        }
        if(pricelth){
            itemquery.addAscendingOrder(ItemTable.PRICE);

        }
        itemquery.whereEqualTo(ItemTable.SUB_CATEGORY, ParseObject.createWithoutData(SubCategoryTable.TABLE_NAME, subcategoryId));

        itemquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> itemobjects, ParseException e) {
                if (e == null) {
                    int c = 0;
                    if (itemobjects.size() != 0) {

                        for (int x = 0; x < itemobjects.size(); x++) {
                            ParseObject itemobject = itemobjects.get(x);
                            String brand = itemobject.getString(ItemTable.BRAND);

                            // Log.d("user","here");
                            if (brandfiltering) {
                                for (int l = 0; l < size; l++) {
                                    Log.d("model", modelItems[l].getName() + " " + modelItems[l].isChecked());
                                    if ((modelItems[l].isChecked()) && (modelItems[l].getName().equalsIgnoreCase(brand))) {

                                        String name = itemobject.getString(ItemTable.NAME);
                                        //String brand = itemobject.getString(ItemTable.BRAND);
                                        String itemdetails = name + "\nBy " + brand;
                                        item_name.put(c, itemdetails);

                                        item_map.put(itemdetails, itemobject.getObjectId());
                                        ParseFile file = (ParseFile) itemobjects.get(x).get(ItemTable.IMAGE);

                                        byte[] data = null;

                                        try {
                                            data = file.getData();
                                        } catch (ParseException e1) {
                                            e1.printStackTrace();
                                        }

                                        ImageResizer ir = new ImageResizer();
                                        Bitmap bitmap = ir.resizeImage(data, 200, 180);
                                        item_image.put(c, bitmap);
                                        c++;
                                    }

                                }
                            } else {


                                String name = itemobject.getString(ItemTable.NAME);
                                //String brand = itemobject.getString(ItemTable.BRAND);
                                String itemdetails = name + "\nBy " + brand;
                                item_name.put(c, itemdetails);

                                item_map.put(itemdetails, itemobject.getObjectId());
                                ParseFile file = (ParseFile) itemobjects.get(x).get(ItemTable.IMAGE);

                                byte[] data = null;

                                try {
                                    data = file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                ImageResizer ir = new ImageResizer();
                                Bitmap bitmap = ir.resizeImage(data, 200, 180);
                                item_image.put(c, bitmap);
                                c++;


                            }

                        }


                        final String[] name = new String[item_name.size()];

                        for (int y = 0; y < name.length; y++) {
                            name[y] = item_name.get(y);

                        }

                        CustomGrid adapter = new CustomGrid(itemListActivity.this, name, item_image);
                        categories.setAdapter(adapter);
                        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String item = name[position];
                                //  Toast.makeText(getApplicationContext(),item_map.get(item),Toast.LENGTH_LONG).show();
                                Intent categorySelectedIntent = new Intent(itemListActivity.this, itemDetails.class);
                                categorySelectedIntent.putExtra("itemId", item_map.get(item));
                                categorySelectedIntent.putExtra("subcategoryId", subcategoryId);
                                categorySelectedIntent.putExtra("subcategoryName", subcategoryName);
                                startActivity(categorySelectedIntent);
                            }
                        });

                    } else {

                        Toast.makeText(getApplicationContext(), "No products according to filter", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Log.d("item", "exceptional error " + e);
                }
            }
        });




    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ParseUser.getCurrentUser()==null){
            Intent tologin=new Intent(itemListActivity.this,login.class);
            startActivity(tologin);
        }

    }


    protected void sleep(int x){
        for(int y=0;y<x;y++){}
    }
}



