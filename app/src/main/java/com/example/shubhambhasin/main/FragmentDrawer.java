package com.example.shubhambhasin.main;

/**
 * Created by Shubham Bhasin on 23/3/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();


    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private int[] icons;
    private FragmentDrawerListener drawerListener;
    private TextView accountUsername;
    private TextView accountEmail;
    public static final int SHORT_SIDE_TARGET = 450;
    private de.hdodenhof.circleimageview.CircleImageView accountProfile;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);

            data.add(navItem);
        }

        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

        icons= new int[]{
                R.drawable.home,
                R.drawable.cart

        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
       ParseUser User=ParseUser.getCurrentUser();
        accountUsername = (TextView) layout.findViewById(R.id.accountUsername);/////layout. ???
        accountEmail = (TextView) layout.findViewById(R.id.accountEmail);
        accountUsername.setText(User.getUsername());
        accountEmail.setText(User.getEmail());
        accountProfile = (de.hdodenhof.circleimageview.CircleImageView) layout.findViewById(R.id.circleView);
        accountProfile.post(new Runnable() {
            @Override
            public void run() {

                if(ParseUser.getCurrentUser() != null) {
                    ParseUser myParseUser = ParseUser.getCurrentUser();
                    if (ParseUser.getCurrentUser().getParseFile("imageFile") != null) {
                        ParseFile imageFile = (ParseFile) myParseUser.getParseFile("imageFile");
                        imageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    //Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    //Bitmap bmp = ImageResizer.resizeImageMaintainAspectRatio(data, SHORT_SIDE_TARGET);
                                    Bitmap bmp = ImageResizer.resizeImage(data, accountProfile.getMeasuredWidth(),accountProfile.getMeasuredHeight());
                                    accountProfile.setImageBitmap(bmp);
                                } else {
                                    Log.d("test",
                                            "There was a problem downloading the data.");
                                }
                            }

                        });
                    }
                }
            }
        });



        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        recyclerView.setHasFixedSize(true);
        adapter = new NavigationDrawerAdapter(getActivity(), getData(),icons);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;



            titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
            icons= new int[]{
                    R.drawable.home,
                    R.drawable.cart

            };


        adapter = new NavigationDrawerAdapter(getActivity(), getData(),icons);
        recyclerView.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }


}