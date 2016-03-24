package com.example.shubhambhasin.main;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cart extends BaseActivity{

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    ListView cartList;
    Button placeOrder;
    TextView totalcost;

    TextView selecteditemprice;
    Spinner quantitySpinner;
    ImageView selecteditemimage;
    TextView selecteditemdetail;
    TextView selecteditembrand;
    Button removefromcart;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_cart);

            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Cart");

           drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);//pass role
            drawerFragment.setDrawerListener(this);

            cartList=(ListView)findViewById(R.id.cartList);
            placeOrder=(Button)findViewById(R.id.placeorderbutton);
            totalcost=(TextView)findViewById(R.id.total);

            final HashMap<Integer,Bitmap> cart_image=new HashMap<>();
            final HashMap<Integer,String> cart_name=new HashMap<>();
            final HashMap<String,String> cart_map=new HashMap<>();
            final HashMap<Integer,Integer> quantity_map=new HashMap<>();
            final HashMap<Integer,Integer> price_map=new HashMap<>();
            final ParseQuery<ParseObject> cartquery= new ParseQuery(CartTable.TABLE_NAME);
            cartquery.whereEqualTo(CartTable.USER, ParseUser.getCurrentUser());
            cartquery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> cartobjects, ParseException e) {
                    if (e == null) {
                        if (cartobjects.size() != 0) {

                            for (int x = 0; x < cartobjects.size(); x++) {

                                ParseObject cartobject = cartobjects.get(x);
                                ParseObject itemordered = cartobject.getParseObject(CartTable.ITEM);
                                String itemname = null;

                                String itembrand =null;
                                Integer itemprice=null;
                                Integer itemquantity=null;


                                try {
                                    itemname = itemordered.fetchIfNeeded().getString(ItemTable.NAME);

                                  itembrand = itemordered.fetchIfNeeded().getString(ItemTable.BRAND);
                                  itemprice = itemordered.fetchIfNeeded().getInt(ItemTable.PRICE);

                                 itemquantity = cartobject.fetchIfNeeded().getInt(CartTable.QUANTITY);

                                    String itemincartdetails = itemname + "\n" + itembrand + "\nRs. " + itemprice + "\nQuantity ordered = " + String.valueOf(itemquantity);
                                price_map.put(x,itemprice);
                                    quantity_map.put(x,itemquantity);
                                    cart_name.put(x, itemincartdetails);
                                cart_map.put(itemname, cartobject.getObjectId());
                                ParseFile file = itemordered.getParseFile(ItemTable.IMAGE);

                                final int finalX = x;

                                byte[] data = null;

                                try {
                                    data = file.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                cart_image.put(finalX, bitmap);
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                            }


                            final String[] name = new String[cart_name.size()];
                            Integer total=0;
                            for (int y = 0; y < name.length; y++) {
                                name[y] = cart_name.get(y);
                                total+=(price_map.get(y)*quantity_map.get(y));
                            }

                            CustomList adapter = new CustomList(cart.this, name, cart_image);
                            cartList.setAdapter(adapter);


                            totalcost.setText(String.valueOf(total));

                            placeOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(),"Order Placed",Toast.LENGTH_LONG).show();
                                }
                            });

                            cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                   String item=name[position];
                                    Toast.makeText(getApplicationContext(),cart_map.get(item),Toast.LENGTH_LONG).show();
                                    final Dialog itemincartselected=new Dialog(cart.this);
                                    itemincartselected.setContentView(R.layout.item_details);
                                    setDialogSize(itemincartselected);

                                    done=(Button)itemincartselected.findViewById(R.id.done);
                                    removefromcart=(Button)itemincartselected.findViewById(R.id.RemoveFromCart);
                                    selecteditemimage=(ImageView)itemincartselected.findViewById(R.id.itemimage);
                                    selecteditembrand=(TextView)itemincartselected.findViewById(R.id.brand);
                                    selecteditemprice=(TextView)itemincartselected.findViewById(R.id.price);
                                    selecteditemdetail=(TextView)itemincartselected.findViewById(R.id.details);
                                    quantitySpinner=(Spinner)itemincartselected.findViewById(R.id.quantitySpinner);

                                    String[] quant=new String[12];
                                    for (int i = 0; i < 12; i++) {
                                        quant[i] = String.valueOf(i+1);
                                    }
                                    ArrayAdapter<String> adapter_end = new ArrayAdapter<String>(cart.this,android.R.layout.simple_spinner_item,quant);
                                    quantitySpinner.setAdapter(adapter_end);

                                    final ParseObject selectedcartitemobject=ParseObject.createWithoutData(CartTable.TABLE_NAME, cart_map.get(item));
                                    ParseObject selecteditemobject= null;
                                    try {
                                        selecteditemobject = selectedcartitemobject.fetchIfNeeded().getParseObject(CartTable.ITEM);


                                    selecteditemprice.setText(String.valueOf(selecteditemobject.getNumber(ItemTable.PRICE)));
                                    selecteditemdetail.setText(selecteditemobject.getString(ItemTable.DETAILS));
                                    selecteditembrand.setText(selecteditemobject.getString(ItemTable.BRAND));
                                    ParseFile file = selecteditemobject.getParseFile(ItemTable.IMAGE);
                                    byte[] data = null;

                                    try {
                                        data = file.getData();
                                    } catch (ParseException e2) {
                                        e2.printStackTrace();
                                    }

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    selecteditemimage.setImageBitmap(bitmap);
                                    quantitySpinner.setSelection(selectedcartitemobject.getInt(CartTable.QUANTITY) - 1);



                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    itemincartselected.show();
                                    done.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            itemincartselected.dismiss();
                                        }
                                    });

                                    removefromcart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selectedcartitemobject.deleteEventually();
                                            itemincartselected.dismiss();
                                        }
                                    });


                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "No item added to cart", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("cart", "exceptional error " + e);
                    }
                }
            });


                        }catch(Exception create_error){
                            Log.d("user", "error in create cart activity: " + create_error.getMessage());
                            Toast.makeText(cart.this, "error " + create_error, Toast.LENGTH_LONG).show();
        }
    }







}
