package com.example.shubhambhasin.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;


public class itemDetails extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    ImageView itemimage;
    TextView price;
    TextView details;
    Button tocart;
    TextView brand;

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


            final ParseObject itemobject=ParseObject.createWithoutData(ItemTable.TABLE_NAME,itemId);

            getSupportActionBar().setTitle(itemobject.fetchIfNeeded().getString(ItemTable.NAME));
            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);


            itemimage=(ImageView)findViewById(R.id.itemimage);
            tocart=(Button)findViewById(R.id.addToCart);
            brand=(TextView)findViewById(R.id.Brand);
            price=(TextView)findViewById(R.id.price);
            details=(TextView)findViewById(R.id.details);



            brand.setText(itemobject.fetchIfNeeded().getString(ItemTable.BRAND));
            price.setText(String.valueOf(itemobject.fetchIfNeeded().getNumber(ItemTable.PRICE)));
            details.setText(itemobject.fetchIfNeeded().getString(ItemTable.DETAILS));


            ParseFile imageFile = itemobject.getParseFile(ItemTable.IMAGE);

            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {

                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        itemimage.setImageBitmap(bmp);
                    } else {
                        Log.d("test",
                                "There was a problem downloading the data.");
                    }
                }

            });

            tocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ParseQuery<ParseObject> alreadyincart=ParseQuery.getQuery(CartTable.TABLE_NAME);
                    alreadyincart.whereEqualTo(CartTable.USER, ParseUser.getCurrentUser());
                    alreadyincart.whereEqualTo(CartTable.ITEM, itemobject);
                    alreadyincart.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> cartobjects, ParseException e) {
                            if (e == null) {
                                if (cartobjects.size() != 0) {
                                    Toast.makeText(getApplicationContext(), "Item already in cart", Toast.LENGTH_LONG).show();
                                } else {
                                    ParseObject itemtocart = new ParseObject(CartTable.TABLE_NAME);
                                    itemtocart.put(CartTable.ITEM, itemobject);
                                    itemtocart.put(CartTable.USER, ParseUser.getCurrentUser());
                                    itemtocart.put(CartTable.QUANTITY, 1);
                                    itemtocart.saveEventually();
                                    Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                    Log.d("cart","Exceptional error " + e);
                            }
                        }
                    });


                }
            });

        }catch(Exception create_error){
            Log.d("user", "error in item details activity: " + create_error.getMessage());
            Toast.makeText(itemDetails.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }


}



