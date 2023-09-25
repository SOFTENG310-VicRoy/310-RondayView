package com.example.a310_rondayview.ui.createevent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.example.a310_rondayview.model.Event;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEventFragment extends Fragment {

    private static final String DATE_DIALOG = "dateDialog";
    ActivityResultLauncher<String> selectPhoto;
    private Uri localImageUri;
    private Uri downloadImageUri;
    Date date;
    DatePickerDialog datePickerDialog;


    private static class ViewHolder {
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

    CreateEventFragment.ViewHolder vh;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StorageReference mStorageRef;
        FirebaseStorage storage;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        vh = new ViewHolder(view);

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        initDatePicker();
        vh.date.setInputType(InputType.TYPE_NULL);

        vh.date.setText(getTodaysDate());
        vh.date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showDialog(v, DATE_DIALOG);
                }
                return false;
            }
        });

        // getting and setting selected image from users camera roll to the event image image view
        selectPhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    localImageUri = result;
                    vh.eventImage.setImageURI(result);
                }
        );

        vh.chooseImageBtn.setOnClickListener(chooseImageView -> selectPhoto.launch("image/*"));

        vh.postBtn.setOnClickListener(postView -> {
            if (!validateForm()) return;

            try {
                date = new SimpleDateFormat("dd/MM/yyyy").parse(vh.date.getText().toString());
            } catch (ParseException e) {
                vh.date.setError("Invalid date");
                return;
            }

            final StorageReference ref = mStorageRef.child("eventImages/");
            ref.putFile(localImageUri)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        downloadImageUri = uri;
                        // all data needed to create event is ready
                        // placeholder image for profile picture (should fill from account)
                        Event event = new Event(
                                vh.clubName.getText().toString(),
                                vh.eventTitle.getText().toString(),
                                vh.description.getText().toString(),
                                vh.location.getText().toString(),
                                date,
                                downloadImageUri.toString(),
                                "https://cdn.discordapp.com/attachments/1144469565179433131/1144469584573906964/image.png"
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
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Could not create event", Toast.LENGTH_LONG).show());
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

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = dateFormat.format(calendar.getTime());
                vh.date.setText(dateString);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
    }
    private String getTodaysDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(calendar.getTime());
        return dateString;
    }
    public void showDialog(View view, String dialog)
    {
        if (dialog == DATE_DIALOG) {
            datePickerDialog.show();
        }
    }
    public void dismissDialog(View view, String dialog) {
        if (dialog == DATE_DIALOG) {
            datePickerDialog.dismiss();
        }
    }
}
