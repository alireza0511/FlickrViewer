package com.jazzb.alireza.malauzai_test.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.facebook.drawee.backends.pipeline.Fresco;
import com.jazzb.alireza.malauzai_test.Controller.MainActivity;
import com.jazzb.alireza.malauzai_test.Model.Flickr.FlickrPhoto;
import com.jazzb.alireza.malauzai_test.R;
import com.squareup.picasso.Picasso;

import java.util.List;

//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link FirstFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link FirstFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FirstFragment extends Fragment implements MainActivity.DataUpdateListener {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "url";
    private static final String ARG_TITLE = "title";
    private static final String ARG_INDEX = "index";

    private View view;
    private TextView tv;
    private ImageView iv;

    private String mUrl;
    private String mTitle;
    private Integer mIndex;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */

    public static FirstFragment newInstance(String url, String title, Integer index) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_URL);
            mTitle = getArguments().getString(ARG_TITLE);
            mIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_first, container, false);

        tv = view.findViewById(R.id.tv_first);
        iv = view.findViewById(R.id.iv_first);
        tv.setText(mTitle);
        Picasso.get().load(mUrl).into(iv);

        return view;
    }

    @Override
    public void onDataUpdate(List<FlickrPhoto> flickrPhoto) {
        mUrl = flickrPhoto.get(mIndex).getUrl();
        mTitle = flickrPhoto.get(mIndex).getTitle();
        tv.setText(mTitle);
        Picasso.get().load(mUrl).into(iv);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).registerDataUpdateListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).unregisterDataUpdateListener(this);
    }
}



