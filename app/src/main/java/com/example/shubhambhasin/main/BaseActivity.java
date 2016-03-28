package com.example.shubhambhasin.main;

/**
 * Created by Shubham Bhasin on 23-Mar-16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final int RESULT_LOAD_IMAGE = 1;
      int densityX;
    int densityY;
    String categoryId;
    String subcategoryId;
    String itemId;
    Activity context;
    RelativeLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //Parse.enableLocalDatastore(getApplicationContext());
        //Parse.initialize(getApplicationContext(), "5pPTGNabAK5TyJDfxKMuhzATUnMXS3GvjOS98IGD", "TRPqa2TRC5JmF2NUJLKvcdlH7j9c4saF4TODVwlG");


        //found width of Screen for Gridview
        WindowManager windowManager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
        Display display = windowManager.getDefaultDisplay();
        densityX = display.getWidth();
        densityY = display.getHeight();
    }



    public void selectPicture(View v) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();

            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
           // BitmapFactory.decodeFile(picturePath, options);

            // Locate the image in res > drawable-hdpi
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath,options);

            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            */
            final Bitmap[] bitmap = new Bitmap[1];
            final CircleImageView imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.circleView);
            imageView.post(new Runnable() {
                @Override
                public void run() {


                    bitmap[0] = decodeSampledBitmapFromFile(picturePath, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
                    imageView.setImageBitmap(bitmap[0]);

                    // bitmap[0]= decodeSampledBitmapFromFile(picturePath,imageView.getWidth(),imageView.getHeight());


                    // Convert it to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100
                    bitmap[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();


                    final ParseUser currentUser = ParseUser.getCurrentUser();
                    // Create the ParseFile
                    final ParseFile file = new ParseFile(image);


                    file.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(BaseActivity.this, "Image Uploaded to Parse", Toast.LENGTH_SHORT).show();
                                currentUser.put("imageFile", file);
                            } else {
                                Log.d("test",
                                        "There was a problem uploading the data.");
                            }
                        }
                    });


            /*ParseQuery<ParseUser> query = ParseQuery.getQuery("User");

// Retrieve the object by id
            query.getInBackground(String.valueOf(ParseUser.getCurrentUser()), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject imgupload, com.parse.ParseException e) {
                    if (e == null) {
                        // Now let's update it with some new data. In this case, only cheatMode and score
                        // will get sent to the Parse Cloud. playerName hasn't changed.
                        imgupload.put("imageFile", file);
                        imgupload.saveInBackground();
                    }
                }
            });*/
                    // Show a simple toast message
                    Toast.makeText(BaseActivity.this, "Image Uploaded",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    public static Bitmap decodeSampledBitmapFromFile(String picturePath,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        // Calculate inSampleSize
        //options.inSampleSize =2;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        Log.d("memory ",
                "height " + height + " reqd " + reqHeight + " width " + width + " reqd " + reqWidth);
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }

    private void displayView(int position) {

        if (position == 0) {//home
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }else
            if (position == 1) { //cart
              Intent task_intent = new Intent(BaseActivity.this, cart.class);
                startActivity(task_intent);
            }else
        if (position == 2) { //logout
            ParseUser.logOut();
            Intent task_intent = new Intent(BaseActivity.this, login.class);
            startActivity(task_intent);
        }


    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    void checkDate(int Day, int Month, int Year, long milliseconds[]) {

        Date d;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(new Date(calendar.getTimeInMillis()));
        d = null;
        try {
            d = format.parse(date);
        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        milliseconds[0] = d.getTime();

        String string_date = String.valueOf(Day) + "-" + String.valueOf(Month) + "-" + String.valueOf(Year);

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        d = null;
        try {
            d = f.parse(string_date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        milliseconds[1] = d.getTime();

        Log.d("date test base", milliseconds[0] + " selected:" + milliseconds[1]);


    }

    void setDialogSize(Dialog dialogcal) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogcal.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        dialogcal.getWindow().setAttributes(lp);

    }
}