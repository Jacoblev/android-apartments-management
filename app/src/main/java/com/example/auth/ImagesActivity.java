package com.example.auth;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends RealTimeData {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<ImagesUpload> mUploads;
    private List<Users> activeUser;
    private List<String> notes;
    private DatabaseReference notesRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Notes");
    private DatabaseReference usersRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Users");
    private DatabaseReference imageRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Images");
    String str;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
        activeUser = new ArrayList<>();
        notes = new ArrayList<>();

        imageFunc();









    }








        public void imageFunc() {
            imageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ImagesUpload uploads = postSnapshot.getValue(ImagesUpload.class);
                        mUploads.add(uploads);

                    }


                    mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
                   mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


}
