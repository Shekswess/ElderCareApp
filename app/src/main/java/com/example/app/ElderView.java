package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ElderView extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter2 adapter2;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_view);

        recyclerView = (RecyclerView) findViewById(R.id.activities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Activity>options =
                new FirebaseRecyclerOptions.Builder<Activity>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Activities").orderByChild("own").equalTo(FirebaseAuth.getInstance().getUid()),Activity.class)
                .build();


        adapter2 = new Adapter2(options);
        recyclerView.setAdapter(adapter2);

        adapter2.setOnItemClickListener(new Adapter2.OnItemClickListener() {
            @Override
            public void onItemClick(DataSnapshot documentSnapshot, int position) {
                FirebaseDatabase.getInstance().getReference().child("Activities").child(documentSnapshot.getKey()).removeValue();
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBack();
            }
        });
    }

    private void getBack() {
        startActivity(new Intent(this,ElderMain.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter2.stopListening();
    }
}