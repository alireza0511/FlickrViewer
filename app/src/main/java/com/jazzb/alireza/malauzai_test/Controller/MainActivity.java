package com.jazzb.alireza.malauzai_test.Controller;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jazzb.alireza.malauzai_test.View.FirstFragment;
import com.jazzb.alireza.malauzai_test.Model.Flickr.Flickr;
import com.jazzb.alireza.malauzai_test.Model.Flickr.FlickrPhoto;
import com.jazzb.alireza.malauzai_test.Model.Flickr.VolleyCallBack;
import com.jazzb.alireza.malauzai_test.R;
import com.jazzb.alireza.malauzai_test.Utils.NetworkCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

public class MainActivity extends FragmentActivity implements Observer {

    private Flickr flickr;
    private FlickrPhoto flickrPhoto;
    private NetworkCheck networkCheck;
    private RequestQueue mRequestQueue;
    private Gson gson;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;
    private SimpleFragmentPagerAdapter adapter;

    private float mPrevX;
    int j = 1;

    private FirstFragment firstFragment;

    private List<DataUpdateListener> mListeners;
    private boolean firstSet = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flickr = new Flickr();
        flickrPhoto = new FlickrPhoto();
        networkCheck = new NetworkCheck();
        flickrPhoto.addObserver(this);
        mListeners = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        viewPager = findViewById(R.id.viewpager);

        configurationDisplay();

        mRequestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        requestPhoto();

        swipListenerFunc();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void configurationDisplay() {
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        float width = size. x;
        float height = size. y;
        final Integer touchSlop = (int) (width/ceil(height/width));

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mPrevX = MotionEvent.obtain(event).getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float eventY = event.getY();
                        float xDiff = Math.abs(eventY - mPrevX);
                        if (xDiff > touchSlop) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void requestPhoto() {

        if (networkCheck.isConnect(this)) {
            flickr.getRequestFlickr(mRequestQueue, String.valueOf(j), new VolleyCallBack() {
                @Override
                public void onSuccessResponse(String result) {
                    swipeRefreshLayout.setRefreshing(false);

                    List<FlickrPhoto> photos = Arrays.asList(gson.fromJson(result, FlickrPhoto[].class));

                    for (int i = 0; i < photos.size(); i++) {
                        flickrPhoto.setList(i, photos.get(i));
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
        } else {

            Toast.makeText(this, R.string.msg_no_connection, Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "network problem");
        }

    }

    private void swipListenerFunc() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestPhoto();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {

        dataUpdated();

    }

    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();
        for (int i=0; i < flickrPhoto.getPhotoList().size(); i++) {

                fList.add(firstFragment.newInstance(flickrPhoto.getPhotoList().get(i).getUrl(),
                        flickrPhoto.getPhotoList().get(i).getTitle(), i));
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
