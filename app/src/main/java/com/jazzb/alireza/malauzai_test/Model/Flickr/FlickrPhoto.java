package com.jazzb.alireza.malauzai_test.Model.Flickr;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class FlickrPhoto extends Observable {

    String id;

    String title;

    @SerializedName("url_m")
    String url;

    private List<FlickrPhoto> mList;

    public FlickrPhoto(){
        mList = new ArrayList<FlickrPhoto>();
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;


    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FlickrPhoto> getPhotoList() {
        return mList;
    }

    public void setList( int index, FlickrPhoto photo){
        mList.add(index, photo);
        setChanged();
        notifyObservers();
    }
}
