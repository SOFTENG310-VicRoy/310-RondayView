package com.example.a310_rondayview;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    ActivityResultLauncher<String> selectPhoto;

    private class ViewHolder {
        Button choose_image_btn;
        ImageView event_image_img;

        public ViewHolder(View view) {
            choose_image_btn = view.findViewById(R.id.choose_image_btn);
            event_image_img = view.findViewById(R.id.event_image_img);
        }
    }

    ViewHolder vh;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventFragment newInstance(String param1, String param2) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        vh = new ViewHolder(view);

        // getting and setting selected image from users camera roll to the event image image view
        selectPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        vh.event_image_img.setImageURI(result);
                    }
                }
        );

        vh.choose_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto.launch("image/*");
            }
        });

        return view;
    }
}