package com.example.a310_rondayview.ui.createevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.a310_rondayview.R;
import com.example.a310_rondayview.data.event.EventsFirestoreManager;
import com.example.a310_rondayview.data.group.GroupDatabaseService;
import com.example.a310_rondayview.data.group.GroupFirestoreManager;
import com.example.a310_rondayview.data.user.FireBaseUserDataManager;
import com.example.a310_rondayview.model.Event;
import com.example.a310_rondayview.model.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateEventFragment extends Fragment {

    private static final String DATE_DIALOG = "dateDialog";
    private static final String TIME_DIALOG = "timeDialog";
    private Uri localImageUri;
    private Uri downloadImageUri;
    private int hour;
    private int minute;
    private Date date;
    private DatePickerDialog datePickerDialog;
    private Boolean isPrivate;
    ActivityResultLauncher<String> selectPhoto;



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
        CheckBox privacyCheckBox;
        EditText groupNameEditText;


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
            privacyCheckBox = view.findViewById(R.id.privacyCheckBox);
            groupNameEditText = view.findViewById(R.id.groupNameEditText);
        }
    }

    CreateEventFragment.ViewHolder vh;

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        vh = new ViewHolder(view);

        // Set up the Date Dialog Picker
        initDatePicker();
        vh.date.setInputType(InputType.TYPE_NULL);
        vh.date.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showDialog(DATE_DIALOG);
            }
            return false;
        });

        // Set up the Time Dialog Picker
        vh.time.setInputType(InputType.TYPE_NULL);
        vh.time.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showDialog(TIME_DIALOG);
            }
            return false;
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

        vh.privacyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vh.groupNameEditText.setVisibility(View.VISIBLE); // Show the text field
                } else {
                    vh.groupNameEditText.setVisibility(View.GONE); // Hide the text field
                    vh.groupNameEditText.setText(""); // Clear the text
                }
            }
        });

        vh.postBtn.setOnClickListener(postView -> {
            if (!validateForm()) return;
            // Take the Date and Time and convert it to a single Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            try {
                Date parsedDate = dateFormat.parse(vh.date.getText().toString());
                Date parsedTime = timeFormat.parse(vh.time.getText().toString());

                // Combine the parsed date and time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                Calendar timeCalendar = Calendar.getInstance();
                timeCalendar.setTime(parsedTime);
                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
                date = calendar.getTime();
            } catch (ParseException e) {
                vh.date.setError("Invalid date");
                return;
            }

            //Check if set to private event
            String groupNameTag;
            GroupDatabaseService groupDatabaseService = new GroupDatabaseService();
            if(vh.privacyCheckBox.isChecked()){
                if(!hasText(vh.groupNameEditText)){
                    Toast.makeText(getActivity(), "Group name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                groupNameTag = vh.groupNameEditText.getText().toString();
                //Check if group exist
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                groupDatabaseService.getGroupByName(groupNameTag).thenAccept(group -> {
                    if(group!=null){
                        //If group exist and user not part of group, show toast and drop task
                        if(!group.getUserIdList().contains(user.getUid())){
                            Log.e("Unauthorised user", "Current user "+ user.getUid()+" has no access to group "+ groupNameTag);
                            Toast.makeText(getActivity(), "You are not member of the group: "+groupNameTag, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Else proceed to event creation with group tag
                        Toast.makeText(getActivity(), "Added event to existing group "+groupNameTag, Toast.LENGTH_SHORT).show();
                        FireBaseUserDataManager.getInstance().addParticipatedGroupName(groupNameTag);
                        createEvent(groupNameTag);
                    } else {
                        ArrayList<String> userIdList = new ArrayList<>();
                        userIdList.add(user.getUid());
                        Group newGroup = new Group(groupNameTag, userIdList, new ArrayList<String>());
                        //Automatically create new group if group doesn't exist
                        GroupFirestoreManager.getInstance().addGroup(newGroup, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Created new group "+groupNameTag, Toast.LENGTH_SHORT).show();
                                FireBaseUserDataManager.getInstance().addParticipatedGroupName(groupNameTag);
                                createEvent(groupNameTag);
                            } else {
                                Toast.makeText(getActivity(), "Could not create new group", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            } else {
                groupNameTag = "";
                createEvent(groupNameTag);
            }
        });
        return view;
    }

    private void createEvent(String groupNameTag){
        StorageReference mStorageRef;
        FirebaseStorage storage;

        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
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
                            "https://cdn.discordapp.com/attachments/1144469565179433131/1144469584573906964/image.png",
                            0,
                            new ArrayList<>(),
                            groupNameTag
                    );
                    EventsFirestoreManager.getInstance().addEvent(event, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Event created", Toast.LENGTH_SHORT).show();
                            //Update the group:
                            if(event.getGroupNameTag()!=null){
                                GroupDatabaseService groupDatabaseService = new GroupDatabaseService();
                                groupDatabaseService.getGroupByName(event.getGroupNameTag()).thenAccept(group -> {
                                    group.getEventIdList().add(event.getEventId());
                                    GroupFirestoreManager.getInstance().updateGroup(group);
                                });

                            }
                            // not good practice to switch within fragments but for now:
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new CreateEventFragment()).commit();
                        } else {
                            Toast.makeText(getActivity(), "Could not create event", Toast.LENGTH_SHORT).show();
                        }
                    });
                }))
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Could not create event", Toast.LENGTH_LONG).show());
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

    /**
     * This method initialise the Date Dialog Picker by setting the current day to display first.
     */
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = dateFormat.format(calendar.getTime());
            vh.date.setText(dateString);
        };
        // Show current day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getContext(), 0, dateSetListener, year, month, day);
    }

    /**
     * This method displays a dialog picker depending on the given parameter
     * @param dialog The string to determine which dialog to show
     */
    public void showDialog(String dialog)
    {
        if (dialog.equals(DATE_DIALOG)) {
            datePickerDialog.show();
        }
        else if (dialog.equals(TIME_DIALOG)) {
            // Set up Time Dialog Picker to show current time
            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
                hour = selectedHour;
                minute = selectedMinute;
                vh.time.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), 0, onTimeSetListener, hour, minute, true);
            timePickerDialog.setTitle("Select Time");
            timePickerDialog.show();
        }
    }

    /**
     * This method dismiss a dialog picker depending on the given paramter.
     * @param dialog The string to detemrine which dialog to dismiss.
     */
    public void dismissDialog(String dialog) {
        if (dialog.equals(DATE_DIALOG)) {
            datePickerDialog.dismiss();
        }
    }
}
