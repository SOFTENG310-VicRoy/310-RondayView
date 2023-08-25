package com.example.a310_rondayview;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    ActivityResultLauncher<String> selectPhoto;
    private Uri localImageUri;
    private Uri downloadImageUri;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    Date date;

    private class ViewHolder {
        EditText clubName;
        EditText eventTitle;
        EditText location;
        EditText date;
        EditText time;
        EditText description;
        ImageView eventImage;
        Button chooseImageBtn;

        Button postBtn;


        public ViewHolder(View view) {
            clubName = view.findViewById(R.id.club_name);
            eventTitle = view.findViewById(R.id.event_title);
            location = view.findViewById(R.id.location);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            description = view.findViewById(R.id.description);
            eventImage = view.findViewById(R.id.event_image);
            chooseImageBtn = view.findViewById(R.id.choose_image_btn);
            postBtn = view.findViewById(R.id.post_btn);
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

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        // getting and setting selected image from users camera roll to the event image image view
        selectPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        localImageUri = result;
                        vh.eventImage.setImageURI(result);
                    }
                }
        );

        vh.chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto.launch("image/*");
            }
        });

        vh.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) return;

                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(vh.date.getText().toString());
                } catch (ParseException e) {
                    vh.date.setError("Invalid date");
                    return;
                }

                final StorageReference ref = mStorageRef.child("eventImages/");
                ref.putFile(localImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                    downloadImageUri = uri;
                                    // all data needed to create event is ready
                                    Event event = new Event(
                                            vh.clubName.getText().toString(),
                                            vh.eventTitle.getText().toString(),
                                            vh.description.getText().toString(),
                                            vh.location.getText().toString(),
                                            date,
                                            downloadImageUri.toString(),
                                            "https://firebasestorage.googleapis.com/v0/b/rondayview-872b4.appspot.com/o/placeholders%2Fprofile.png?alt=media&token=3e507e8e-6d77-4b43-b7bb-944bd0e1d60a"
                                    );
                                    EventsFirestoreManager.getInstance().addEvent(event, task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Event created", Toast.LENGTH_SHORT).show();
                                            // not good practice to switch within fragments but for now:
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CreateEventFragment()).commit();
                                        } else {
                                            Toast.makeText(getActivity(), "Could not create event", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                });

                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Could not create event", Toast.LENGTH_LONG).show());
            }
        });

        return view;
    }

    private boolean hasText(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setError("Required");
            return false;
        }
        return true;
    }

    /**
     * Check if each text field is filled
     * @return true if all fields are filled, false otherwise
     */
    private boolean validateForm() {
        boolean valid = true;

        if (!hasText(vh.clubName)) valid = false;
        if (!hasText(vh.eventTitle)) valid = false;
        if (!hasText(vh.location)) valid = false;
        if (!hasText(vh.date)) valid = false;
        if (!hasText(vh.time)) valid = false;
        if (!hasText(vh.description)) valid = false;
        if (localImageUri == null) {
            Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}
