package com.oocl.johngao.smartcr.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oocl.johngao.smartcr.Data.Pictures;
import com.oocl.johngao.smartcr.R;

import java.io.File;


public class PicturesFragment extends Fragment {

    private static final String ARG_PARAM1 = "pictures";
    public static final String TAG = "Pictures Fragment";

    private int mParam1;
    private ImageView mImageView;
    private Pictures mPictures;
    private TextView mTextView;

    public PicturesFragment() {
    }

    public static PicturesFragment newInstance(Pictures pictures) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,pictures);
        PicturesFragment fragment = new PicturesFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPictures = (Pictures) getArguments().getSerializable("pictures");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pictures,container,false);
        mImageView = (ImageView) view.findViewById(R.id.pic_view);
        mTextView = (TextView) view.findViewById(R.id.pic_name);

        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String path = file + File.separator+ mPictures.getTCode()+ File.separator + mPictures.getName();
        Glide.with(getActivity()).load(path).asBitmap().centerCrop().into(mImageView);
        Log.e(TAG, "onCreateView: to be showed in fragment is/are " + mPictures.getName() );
        mTextView.setText(mPictures.getName());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
}
