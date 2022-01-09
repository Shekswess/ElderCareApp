package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ElderAdd extends AppCompatActivity implements View.OnClickListener{

    private EditText  editDescActivity, editTime, editRepetitive, editUrgency, editLocation, editDate, editTypeActivity;
    ImageView back;
    private Button addButton;
    private TextView banner;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userID;
    List<Address> listGeoCoder;
    double latitude, longtitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_add);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        addButton = (Button) findViewById(R.id.addActivity);
        addButton.setOnClickListener(this);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        editTypeActivity = (EditText) findViewById(R.id.typeActivity);
        editDescActivity = (EditText) findViewById(R.id.descActivity);
        editTime = (EditText) findViewById(R.id.time);
        editRepetitive = (EditText) findViewById(R.id.repetitive);
        editUrgency = (EditText) findViewById(R.id.urgency);
        editLocation = (EditText) findViewById(R.id.location);
        editDate = (EditText) findViewById(R.id.date);



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int dayy = calendar.get(Calendar.DAY_OF_MONTH);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ElderAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        editDate.setText(date);
                    }
                },year,month,dayy);
                datePickerDialog.show();
            }
        });


        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    final Intent intent = new Intent(view.getContext(), SettingLocation.class);
                    startActivityForResult(intent,1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                latitude = data.getDoubleExtra("lat", 0);
                longtitude= data.getDoubleExtra("lon", 0);
                try {
                    listGeoCoder = new Geocoder(this).getFromLocation(latitude,longtitude,1);
                    editLocation.setText(listGeoCoder.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == 0) {
                Toast.makeText(ElderAdd.this, "Error !", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.addActivity:
                addActivity();
                startActivity(new Intent(this, ElderMain.class));
                break;
            case R.id.back:
                startActivity(new Intent(this,ElderMain.class));
                break;
        }

    }



    private void addActivity() {

        String type = editTypeActivity.getText().toString().trim();
        String desc = editDescActivity.getText().toString().trim();
        String time = editTime.getText().toString().trim();
        String rep = editRepetitive.getText().toString().trim();
        String urg = editUrgency.getText().toString().trim();
        String loc = editLocation.getText().toString().trim();
        String date = editDate.getText().toString().trim();

        if (type.isEmpty()) {
          editTypeActivity.setError("Type of the Activity is required !");
          editTypeActivity.requestFocus();
          return;
       }

        if (desc.isEmpty()) {
            editDescActivity.setError("Description of the Activity is required !");
            editDescActivity.requestFocus();
            return;
        }

        if (time.isEmpty()) {
            editTime.setError("Time for the Activity is required !");
            editTime.requestFocus();
            return;
        }

        if (rep.isEmpty()) {
            editRepetitive.setError("Repetition information for the Activity is required !");
            editRepetitive.requestFocus();
            return;
        }

        if(!rep.equals("repetitive")&&!rep.equals("one-time"))
        {
            editUrgency.setError("Valid Urgency information for the Activity is required !");
            editUrgency.requestFocus();
            return;
        }

        if (urg.isEmpty()) {
            editUrgency.setError("Urgency information for the Activity is required !");
            editUrgency.requestFocus();
            return;
        }

        if(!urg.equals("yes")&&!urg.equals("no"))
        {
            editUrgency.setError("Valid Urgency information for the Activity is required !");
            editUrgency.requestFocus();
            return;
        }

        if (loc.isEmpty()) {
            editLocation.setError("Location for the Activity is required !");
            editLocation.requestFocus();
            return;
        }

        if(date.isEmpty())
        {
            editDate.setError("Date for the Activity is required !");
            editDate.requestFocus();
            return;
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user  = snapshot.getValue(User.class);

                if(user != null)
                {
                    Activity activity = new Activity(type,desc,time,rep,urg,loc, userID, date);
                    activity.setOwnName(user.getFullName());
                    activity.setRatingEld(user.getRating());
                    activity.setOwnTel(user.getNumber());
                    activity.setOwnMail(user.getEmail());
                    mDatabase.child("Activities").push().setValue(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ElderAdd.this, "Activity has been added successfully!", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(ElderAdd.this, "Something went wrong. Try again !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}