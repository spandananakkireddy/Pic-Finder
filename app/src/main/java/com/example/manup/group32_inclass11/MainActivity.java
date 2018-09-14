package com.example.manup.group32_inclass11;

// Priyanka Manusanipally - 801017222
// Sai Spandana Nakkireddy - 801023658

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  {

    EditText editText;
    Button button;
    ImageView imageView;
    String im = null;
     String icon;
     int index=0;
     ArrayList<Parser.images> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pic Finder");
        editText= findViewById(R.id.editText);
        button= findViewById(R.id.button);
        imageView= findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected())
                {
                    if(editText.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Please enter a keyword", Toast.LENGTH_SHORT).show();
                    }
                    addMessages();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*imageView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // this will make sure event is not propagated to others, nesting same view area
                return false;
            }

        });*/

        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {


            public void onSwipeTop() {
              //  Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                if (isConnected()) {
                    if (list != null) {
                        if (index <= list.size() - 1) {
                            index++;
                            if (index == list.size()) {
                                index = 0;
                            }
                            icon = String.valueOf(list.get(index).display_sizes.get(0).getUri());
                            Log.d("icon", icon + "");
                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Picasso.with(MainActivity.this)
                                            .load(icon)
                                            .placeholder(R.drawable.loading)
                                            .error(R.drawable.ic_delete)
                                            .into(imageView);

                                }
                            });

                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeLeft() {

                if (isConnected()) {
                    if (list != null) {
                        if (index >= 0) {
                            if (index == 0) {
                                index = list.size();
                            }
                            index--;
                            icon = String.valueOf(list.get(index).display_sizes.get(0).getUri());
                            Log.d("icon", icon + "");
                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Picasso.with(MainActivity.this)
                                            .load(icon)
                                            .placeholder(R.drawable.loading)
                                            .error(R.drawable.ic_delete)
                                            .into(imageView);

                                }
                            });
                        }

                    }


                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

            public void onSwipeBottom() {
               // Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }


        });




    }

    private void addMessages() {
        OkHttpClient okHttpClient = new OkHttpClient();
        if (isConnected()) {
            final Request request = new Request.Builder()
                    .url("https://api.gettyimages.com/v3/search/images?phrase=" + editText.getText().toString())
                    .header("Api-Key", "hqkx4rgjxh4wq9bumrrk2hsp")
                    .build();
            Log.d("keyword", editText.getText().toString());
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.getMessage();
                    Log.d("error", e.getMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.d("loop", "entered");

                        String responsestring = response.body().string();
                        try {
                            Gson gson = new Gson();
                            Parser parser = gson.fromJson(responsestring, Parser.class);
                            list = parser.getImages();
                            final ArrayList<Parser.display_sizes> imagelist;
                            Parser.images dis = gson.fromJson(responsestring, Parser.images.class);
                            imagelist = dis.getSizesList();
                            Log.d("demochat", list + "");

                            if (list.size() > 0) {

                                // Log.d("firstimageoutsideloop", im);
                                icon = String.valueOf(list.get(0).display_sizes.get(0).getUri());
                                Log.d("file", "string" + icon);

                                Handler uiHandler = new Handler(Looper.getMainLooper());
                                uiHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(MainActivity.this)
                                                .load(icon)
                                                .placeholder(R.drawable.loading)
                                                .error(R.drawable.ic_delete)
                                                .into(imageView);
                                    }
                                });
                            } else {

                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageResource(0);
                                    /*imageView.setOnTouchListener(new View.OnTouchListener(){

                                        @Override
                                        public boolean onTouch(View arg0, MotionEvent arg1) {
                                            // this will make sure event is not propagated to others, nesting same view area
                                            return true;
                                        }

                                    });*/
                                        Toast.makeText(MainActivity.this, "No Images found for the given keyword", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }
        else
        {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.clear){
            clear(item);
        }
        return true;
    }

    public void clear(MenuItem item) {


        try {
                editText.setText("");
                list.clear();
                imageView.setImageResource(0);
            }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "No Items to clear", Toast.LENGTH_SHORT).show();
        }
    }



    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

    }



    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
}
