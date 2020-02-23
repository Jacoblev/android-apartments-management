package com.example.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;
import static android.view.View.VISIBLE;

public class EmailPasswordActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "EmailPasswordActivity";

	public EditText mEdtEmail, mEdtPassword;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private ImageView mImageView;
	private TextView mTextViewProfile;
	private TextView detailsTextView;
	private TextInputLayout mLayoutEmail, mLayoutPassword;
	private Button mAdmins;
	private Button mDatabase;
	private Button WholeDB;
	private Button signInBtn;

	private DatabaseReference usersRef = FirebaseDatabase.getInstance("https://fir-auth-android-master-24ac4.firebaseio.com/").getReference().child("Users");


	List<String> UsersList;
	List<String> newList;
	int cnt = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emailpassword);

		UsersList = new ArrayList<>();
		newList = new ArrayList<>();
		mTextViewProfile = findViewById(R.id.profile);
		mEdtEmail = findViewById(R.id.edt_email);
		mEdtPassword = findViewById(R.id.edt_password);
		mImageView = findViewById(R.id.logo);
		mLayoutEmail = findViewById(R.id.layout_email);
		mLayoutPassword = findViewById(R.id.layout_password);
		mAdmins = findViewById(R.id.Admins);
		mDatabase = findViewById(R.id.goToDatabase);
		WholeDB = findViewById(R.id.goToWholeDatabase);
		detailsTextView = findViewById(R.id.detailsTextView);
		signInBtn = findViewById(R.id.email_sign_in_button);
		//usersRef.keepSynced(true);



		findViewById(R.id.email_sign_in_button).setOnClickListener(this);
		findViewById(R.id.email_create_account_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
		findViewById(R.id.verify_button).setOnClickListener(this);
		findViewById(R.id.goToDatabase).setOnClickListener(this);
		findViewById(R.id.goToWholeDatabase).setOnClickListener(this);
		findViewById(R.id.Admins).setOnClickListener(this);

		mAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					verifyUserLevel();

					Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					Log.d(TAG, "onAuthStateChanged:signed_out");
				}
				updateUI(user);

			}


		};


	}

	@Override
	public void onStart() {
		super.onStart();

		mAuth.addAuthStateListener(mAuthListener);

	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.Admins:
				findViewById(R.id.Admins).setEnabled(true);
				Intent ide2 = new Intent(EmailPasswordActivity.this,AdminPage.class);
				ide2.putExtra("hi", "HI");
				ide2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(ide2);
				break;
			case R.id.email_create_account_button:
				createAccount(mEdtEmail.getText().toString(), mEdtPassword.getText().toString());
				break;
			case R.id.email_sign_in_button:
				signIn(mEdtEmail.getText().toString(), mEdtPassword.getText().toString());
				signInBtn.onEditorAction(EditorInfo.IME_ACTION_DONE);
				break;
			case R.id.sign_out_button:
				signOut();
				break;
			case R.id.goToDatabase:
					findViewById(R.id.goToDatabase).setEnabled(true);
					Intent ide = new Intent(EmailPasswordActivity.this,RealTimeData.class);
					ide.putExtra("hi", "HI");
					ide.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(ide);
					break;
			case R.id.goToWholeDatabase:

					findViewById(R.id.goToWholeDatabase).setEnabled(true);
					Intent ide1 = new Intent(EmailPasswordActivity.this,AdminDatabase.class);
					ide1.putExtra("hi", "HI");
					ide1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(ide1);
					break;

			case R.id.verify_button:
				findViewById(R.id.verify_button).setEnabled(false);
				final FirebaseUser firebaseUser = mAuth.getCurrentUser();
				firebaseUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if (task.isSuccessful()) {
							Toast.makeText(
									EmailPasswordActivity.this, "Verification email sent to " + firebaseUser.getEmail(), Toast.LENGTH_LONG
							).show();
						} else {
							Toast.makeText(EmailPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
						}
						findViewById(R.id.verify_button).setEnabled(true);

					}
				});
				break;
		}
	}

	private void createAccount(final String email, String password) {

		if (!validateForm()) {
			return;
		}
		showProgressDialog();
		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (!task.isSuccessful()) {
					mTextViewProfile.setTextColor(Color.RED);
					mTextViewProfile.setText(task.getException().getMessage());

				} else {
					mTextViewProfile.setTextColor(Color.DKGRAY);
				}


				final FirebaseUser user = mAuth.getCurrentUser();
				if (user.getEmail() != null) {
					storeUID(user.getUid(), user.getEmail(), "0");
				}
				hideProgressDialog();
			}

		});

	}



	private void signIn(String email, String password) {
		if (!validateForm()) {
			return;
		}
		showProgressDialog();
		mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
				if (!task.isSuccessful()) {
					mTextViewProfile.setTextColor(Color.RED);
					mTextViewProfile.setText(task.getException().getMessage());


				} else {
					mTextViewProfile.setTextColor(Color.DKGRAY);
					mTextViewProfile.setText(null);

				}
				hideProgressDialog();


			}
		});
	}

	private void signOut() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(R.string.logout);
		alert.setCancelable(false);
		alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				mAuth.signOut();
				updateUI(null);
			}
		});
		alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		alert.show();
	}

	private boolean validateForm() {
		if (TextUtils.isEmpty(mEdtEmail.getText().toString())) {
			mLayoutEmail.setError("Required.");
			return false;
		} else if (TextUtils.isEmpty(mEdtPassword.getText().toString())) {
			mLayoutPassword.setError("Required.");
			return false;
		} else {
			mLayoutEmail.setError(null);
			mLayoutPassword.setError(null);
			return true;
		}
	}

	private void storeUID(String uID, String usEmail, String userLevel) {

		mAuth = FirebaseAuth.getInstance();


		final FirebaseUser user = mAuth.getCurrentUser();

		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
		Users artist = new Users(uID, usEmail, userLevel);

		databaseReference.child(user.getUid()).setValue(artist);


		//Toast.makeText(this, "New UID And Email Added To Database", Toast.LENGTH_LONG).show();


	}

	private void verifyUserLevel() {

		final FirebaseUser user = mAuth.getCurrentUser();
		usersRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				Users activeUser = dataSnapshot.getValue(Users.class);
				String newUser;
				newUser = activeUser.userLevel;
				if (newUser.equals("1"))
					{
							mDatabase.setVisibility(VISIBLE);
							WholeDB.setVisibility(VISIBLE);
							mAdmins.setVisibility(VISIBLE);

					}
					else
					{
						mDatabase.setVisibility(VISIBLE);
						WholeDB.setVisibility(GONE);
						mAdmins.setVisibility(GONE);

					}


			}



			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
			}
		});




	}

	private void updateUI(FirebaseUser user) {
		if (user != null) {
			if (user.getPhotoUrl() != null) {
				new DownloadImageTask().execute(user.getPhotoUrl().toString());
			}


			detailsTextView.setText("Email: " + user.getEmail() + "\n\n" + "UserID: " + user.getUid() + "\n\n" + "Email-Verification: " + user.isEmailVerified() + "\n\n" + "To Access The Database You Must Verify Your Email And Re Login!"
			);





			if (user.isEmailVerified()) {
				findViewById(R.id.verify_button).setVisibility(GONE);




			} else {
				findViewById(R.id.verify_button).setVisibility(VISIBLE);
				findViewById(R.id.goToDatabase).setVisibility(GONE);

			}

			findViewById(R.id.email_password_buttons).setVisibility(GONE);
			findViewById(R.id.email_password_fields).setVisibility(GONE);
			findViewById(R.id.signout_zone).setVisibility(VISIBLE);

		} else {
			mTextViewProfile.setText(null);
			detailsTextView.setText(null);

			findViewById(R.id.email_password_buttons).setVisibility(VISIBLE);
			findViewById(R.id.email_password_fields).setVisibility(VISIBLE);
			findViewById(R.id.signout_zone).setVisibility(GONE);
		}
		hideProgressDialog();
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap mIcon = null;
			try {
				InputStream in = new URL(urls[0]).openStream();
				mIcon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				mImageView.getLayoutParams().width = (getResources().getDisplayMetrics().widthPixels / 100) * 24;
				mImageView.setImageBitmap(result);
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
		//moveTaskToBack(true);


	}
}