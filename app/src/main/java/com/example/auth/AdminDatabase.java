package com.example.auth;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class AdminDatabase extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Notes");
    private DatabaseReference usersRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Users");


    ListView listViewNew;
    ListView listViewNew2;
    private Button btn_pp;
    TextView Hello;
    RealTimeData real = new RealTimeData();
    Object obj;

    List<Users> UsersList;
    List<Artist> ArtistsLst;
    List<String> newList;
    List<String> uidList;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_database);

        listViewNew = findViewById(R.id.listViewNew);
        listViewNew2 = findViewById(R.id.listViewNew2);
        btn_pp = findViewById(R.id.btn_pp);
        Hello = findViewById(R.id.Hello);
        ArtistsLst = new ArrayList<>();
        uidList = new ArrayList<>();
        newList = new ArrayList<>();
        UsersList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        showAllData();
        findViewById(R.id.btn_pp).setVisibility(View.GONE);
        findViewById(R.id.Hello).setVisibility(View.VISIBLE);
        listViewNew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                Log.d("TAG", newList.toString());
               showDetailsDialog(newList.get(position));

                return false;
            }
        });






    }




    private void showDetailsDialog(String userUID) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.activity_admin_database, null);

        dialogBuilder.setView(dialogView);

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        findViewById(R.id.btn_pp).setVisibility(View.VISIBLE);
        findViewById(R.id.Hello).setVisibility(View.GONE);


        btn_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDatabase.this, AdminDatabase.class);
                startActivity(intent);
            }
        });

        rootRef.child(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArtistsLst.clear();
                for (DataSnapshot artistSnapshot: dataSnapshot.getChildren())
                {
                    Artist artist = artistSnapshot.getValue(Artist.class);



                    ArtistsLst.add(artist);



                }

                ListOfLists adapter = new ListOfLists(AdminDatabase.this, ArtistsLst);
                listViewNew.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }


private void showAllData() {

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersList.clear();
                for (DataSnapshot usersSnapshot: dataSnapshot.getChildren()) {
                    Users pay = usersSnapshot.getValue(Users.class);
                    obj = usersSnapshot.getKey();
                    UsersList.add(pay);
                    uidList.add(pay.ultimateID);
                    newList.add(obj.toString());

                    Log.d("TAG", UsersList.toString());
                    Log.d("TAG", obj.toString());
                    Log.d("TAG", uidList.toString());
                }
                ListOf adapter = new ListOf(AdminDatabase.this, UsersList);
                listViewNew.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminDatabase.this, EmailPasswordActivity.class);
        startActivity(intent);
    }
}

