package com.example.auth;

import android.content.ContentResolver;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class RealTimeData extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;


    Button button_image_browse;
    Button button_image_upload;
    Button button_image_show;
    EditText edit_file_text;
    Uri imageURI;
    EditText editTextName1;
    EditText editTextName2;
    Button btn_post;
    ListView listViewArtists;
    List<Artist> artistList;

    DatabaseReference databaseArtists;
    DatabaseReference databaseStorage;
    StorageReference storage;

    BaseActivity update;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_data);


        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();



        databaseArtists = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());
        storage = FirebaseStorage.getInstance().getReference("Notes");
        databaseStorage = FirebaseDatabase.getInstance().getReference("Images");




        //Log.d("TAG", friends.toString());


        editTextName1 = findViewById(R.id.edit_title);
        btn_post = findViewById(R.id.btn_post);
        editTextName2 = findViewById(R.id.edit_content);
        button_image_browse = findViewById(R.id.button_image_browse);
        button_image_upload = findViewById(R.id.button_image_upload);
        edit_file_text = findViewById(R.id.edit_file_text);
        button_image_show = findViewById(R.id.button_image_show);


        listViewArtists = findViewById(R.id.listViewArtists);

        artistList = new ArrayList<>();





        btn_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addTitle();
            }
        });









listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Artist artist = artistList.get(position);
        showUpdateDialog(artist.personID, artist.personTitle, artist.personContent);

        return false;
    }
});



    }





    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artistList.clear();

                for (DataSnapshot artistSnapshot: dataSnapshot.getChildren())
                {
                    Artist artist = artistSnapshot.getValue(Artist.class);



                    artistList.add(artist);



                }

                ListOfLists adapter = new ListOfLists(RealTimeData.this, artistList);
                listViewArtists.setAdapter(adapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }









    public void showUpdateDialog(final String id, final String names, final String names2) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText textView5 = dialogView.findViewById(R.id.textView5);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDelete);
        final Button button_image_browse = dialogView.findViewById(R.id.button_image_browse);
        final Button button_image_upload = dialogView.findViewById(R.id.button_image_upload);
        final EditText edit_file_text = dialogView.findViewById(R.id.edit_file_text);
        final Button button_image_show = dialogView.findViewById(R.id.button_image_show);

        dialogBuilder.setTitle("Updating ID: " + id);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();



        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline = editTextName.getText().toString().trim();
                String info = textView5.getText().toString().trim();

                if (TextUtils.isEmpty(headline))
                {
                    editTextName.setError("Headline Req!");
                    return;
                }
                updateArt(id, info, headline);
                alertDialog.dismiss();
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(id);
                alertDialog.dismiss();
            }
        });

        button_image_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        button_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile(edit_file_text.getText().toString(), id, names);
            alertDialog.dismiss();
            }
        });

        button_image_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageActivity();
            alertDialog.dismiss();
            }
        });


    }


    private void openImageActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);

        startActivity(intent);
    }


    private void uploadFile(final String fileName, final String id, final String Title) {
        if (imageURI != null)
        {
            final StorageReference fileRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageURI));
            fileRef.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 500);
                    Toast.makeText(RealTimeData.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String photoLink = uri.toString();
                                ImagesUpload image = new ImagesUpload(fileName, photoLink, id);
                                String uploadID = databaseStorage.push().getKey();
                                databaseStorage.child(id).setValue(image);

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RealTimeData.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });


        }
        else {
            Toast.makeText(this,"No File Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageURI = data.getData();

        }
    }

    private void deleteArtist(String id) {

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid()).child(id);
        drArtist.removeValue();

        Toast.makeText(this, "This Article Has Been Deleted!", Toast.LENGTH_LONG).show();

    }

    private boolean updateArt(String id, String headline, String info)
    {

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid()).child(id);
        Artist artist = new Artist(id, headline, info, user.getUid());

        databaseReference.setValue(artist);
        Toast.makeText(this, "Fields Updated Successfully!", Toast.LENGTH_LONG).show();
        return true;

    }


    private void addTitle() {
        String name = editTextName1.getText().toString().trim();
        String name2 = editTextName2.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name2))
        {



            String id = databaseArtists.push().getKey();
            String name3 = user.getUid();

            Artist artist = new Artist(id, name, name2, name3);
            databaseArtists.child(id).setValue(artist);

            Toast.makeText(this, "Field Added...!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "You Should Enter A Name!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RealTimeData.this, EmailPasswordActivity.class);
        startActivity(intent);
    }
}

