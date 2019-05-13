package com.jazzb.alireza.malauzai_test;

import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jazzb.alireza.malauzai_test.Model.Flickr.Flickr;
import com.jazzb.alireza.malauzai_test.Model.Flickr.FlickrConstants;
import com.jazzb.alireza.malauzai_test.Model.Flickr.FlickrPhoto;
import com.jazzb.alireza.malauzai_test.Model.Flickr.VolleyCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends FragmentActivity implements Observer {

    private FlickrConstants flickrConstants;
    private Flickr flickr;
    private FlickrPhoto flickrPhoto;

//    List<FlickrPhoto> photos;

    private RequestQueue mRequestQueue;
    private Gson gson;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;
    SimpleFragmentPagerAdapter adapter;
    // new

    private int mTouchSlop;
    private float mPrevX;
    int j = 1;

    private FirstFragment firstFragment;

    private List<DataUpdateListener> mListeners;
    private Boolean firstSet = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flickr = new Flickr();
        flickrConstants = new FlickrConstants();
        flickrPhoto = new FlickrPhoto();
        flickrPhoto.addObserver(this);
        mListeners = new ArrayList<>();


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        viewPager = findViewById(R.id.viewpager);

        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;
        int height = size. y;
        mTouchSlop = height/6;
        Log. e("Width", "" + width);
        Log. e("height", "" + height);
        Log. e("height", "" + mTouchSlop);

        mRequestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        requestPhoto();



        swipListenerFunc();


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPrevX = MotionEvent.obtain(event).getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float eventY = event.getY();
                        Log. v("mPrevX", "" + mPrevX);
                        Log. v("eventY", "" + eventY);

                        float xDiff = Math.abs(eventY - mPrevX);
                        if (xDiff > mTouchSlop) {
                            swipeRefreshLayout.setEnabled(true);
//                            Toast.makeText(MainActivity.this, "down", Toast.LENGTH_SHORT).show();


                        } else {
//                            Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setEnabled(false);


                        }

                        break;

                }
                return false;
            }
        });
//

    }

    private void requestPhoto() {

        flickr.getRequestFlickr(mRequestQueue, String.valueOf(j), new VolleyCallBack() {
            @Override
            public void onSuccessResponse(String result) {
                swipeRefreshLayout.setRefreshing(false);

                List<FlickrPhoto> photos = Arrays.asList(gson.fromJson(result, FlickrPhoto[].class));
                Log.i("PostActivity", photos.size() + " photo loaded.");
                Log.i("PostActivity", photos.get(photos.size()-1).getUrl());
//                int it = 0;
//                for (FlickrPhoto photo : photos) {
//                    flickrPhoto.setList(it,photo);
//                    it += 1 ;
//                }

                for (int i=0; i< photos.size(); i++) {

                    flickrPhoto.setList(i , photos.get(i));
                }


                if (firstSet) {

                    List<Fragment> fragments = getFragments();
                    // Create an adapter that knows which fragment should be shown on each page
                    adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragments);

                    // Set the adapter onto the view pager
                    viewPager.setAdapter(adapter);
                }
                firstSet = false;
            }

            @Override
            public void onFailureResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());

            }
        });
        j += 1;

    }

    private void swipListenerFunc() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestPhoto();
//                Toast.makeText(MainActivity.this, "swip", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {

        Log.i("Test", String.valueOf(flickrPhoto.getPhotoList().size()));
//        Toast.makeText(this, flickrPhoto.getPhotoList().get(1).getId(), Toast.LENGTH_SHORT).show();
dataUpdated();

    }

    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();



        for (int i=0; i < flickrPhoto.getPhotoList().size(); i++) {

                fList.add(firstFragment.newInstance(flickrPhoto.getPhotoList().get(i).getUrl(), flickrPhoto.getPhotoList().get(i).getTitle(), i));


        }

        return fList;
    }

    public interface DataUpdateListener {
        void onDataUpdate(List<FlickrPhoto> flickrPhotoList);
    }

    public synchronized void registerDataUpdateListener(DataUpdateListener listener) {
        mListeners.add(listener);
    }

    public synchronized void unregisterDataUpdateListener(DataUpdateListener listener) {
        mListeners.remove(listener);
    }

    public synchronized void dataUpdated() {
        for (DataUpdateListener listener : mListeners) {
            listener.onDataUpdate((List<FlickrPhoto>) flickrPhoto.getPhotoList());
        }
    }
}


//        StringRequest request = new StringRequest(Request.Method.GET,uriBuilder.toString(), onPostsLoaded, onPostsError);
//
//        mRequestQueue.add(request);


//    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            Log.i("PostActivity", response);
//
//            /** improvment **/
//            try {
//            JSONObject baseJsonResponse = new JSONObject(response);
//            JSONObject rootObject = baseJsonResponse.getJSONObject("photos");
//            JSONArray mainRootArray = rootObject.getJSONArray("photo");
//
//            photos = Arrays.asList(gson.fromJson(String.valueOf(mainRootArray), FlickrPhoto[].class));
//            Log.i("PostActivity", photos.size() + " photo loaded.");
//                Log.i("PostActivity", photos.get(0).getUrl());
//
//            } catch (JSONException e) {
//                /** 1- imperovment */
//
//                // If an error is thrown when executing any of the above statements in the "try" block,
//                // catch the exception here, so the app doesn't crash. Print a log message
//                // with the message from the exception.
//                Log.e("QueryUtils", "Problem parsing the  JSON results", e);
//
//                Toast.makeText(MainActivity.this, "Error 701: \n"+e.toString(), Toast.LENGTH_LONG ).show();
//
//            }
//
//        }
//    };
//
//    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.e("PostActivity", error.toString());
//
//        }
//    };