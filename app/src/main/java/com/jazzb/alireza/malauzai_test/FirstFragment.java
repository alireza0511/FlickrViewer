package com.jazzb.alireza.malauzai_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.facebook.drawee.backends.pipeline.Fresco;
import com.jazzb.alireza.malauzai_test.Model.Flickr.FlickrPhoto;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_URL = "url";
    private static final String ARG_TITLE = "title";
    private static final String ARG_INDEX = "index";

    private View view;
    private TextView tv;
    private ImageView iv;

    // TODO: Rename and change types of parameters
    private String mUrl;
    private String mTitle;
    private Integer mIndex;

   // private OnFragmentInteractionListener mListener;

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
    // TODO: Rename and change types and number of parameters
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

//        Fresco.initialize(getContext());

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
        // Inflate the layout for this fragment
        tv.setText(mTitle);
//        Uri uri = Uri.parse(mUrl);
//        iv.setImageURI(uri);
        Picasso.get().load(mUrl).into(iv);

        return view;
    }

//    public youveGotMail(Boolean setImage){
//        if (setImage) {
//
//        }
//    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }



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



