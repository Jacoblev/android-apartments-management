package com.example.auth;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class AdminPage extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Notes");
    private DatabaseReference usersRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Users");

    ListView listViewNew;
    List<Users> UsersList;
    List<String> newList;
    List<String> uidList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        listViewNew = findViewById(R.id.listViewNew);
        uidList = new ArrayList<>();
        newList = new ArrayList<>();
        UsersList = new ArrayList<>();
        showAllData();

        listViewNew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Users artist = UsersList.get(position);
                showUpdateDialog(artist.ultimateEmail, artist.ultimateID, artist.userLevel);

                return false;
            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    private void showAllData() {

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersList.clear();
                for (DataSnapshot usersSnapshot : dataSnapshot.getChildren()) {
                    Users pay = usersSnapshot.getValue(Users.class);

                    UsersList.add(pay);
                    uidList.add(pay.ultimateID);


                    Log.d("TAG", UsersList.toString());

                    Log.d("TAG", uidList.toString());
                }
                ListOf adapter = new ListOf(AdminPage.this, UsersList);
                listViewNew.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void showUpdateDialog(final String email, final String id, final String level) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_admin_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText textView11 = dialogView.findViewById(R.id.textView11);

        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Updating userLevel: " + level);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userLvl = textView11.getText().toString().trim();


                if (TextUtils.isEmpty(userLvl)) {
                    textView11.setError("Headline Req!");
                    return;
                }
                updateArt(email, id, userLvl);
                alertDialog.dismiss();
            }
        });


    }

    private boolean updateArt(String id, String email, String level)
    {

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(email);
        Users artist = new Users(email, id, level);

        databaseReference.setValue(artist);
        Toast.makeText(this, "Fields Updated Successfully!", Toast.LENGTH_LONG).show();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminPage.this, EmailPasswordActivity.class);
        startActivity(intent);
    }
}