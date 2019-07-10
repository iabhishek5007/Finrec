package com.bangalore.bosankus.finrec.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bangalore.bosankus.finrec.R;
import com.bangalore.bosankus.finrec.ViewModel.User;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE";

    @BindView(R.id.activity_profile_view_mode)
    ConstraintLayout clViewMode;

    @BindView(R.id.activity_profile_edit_mode)
    ConstraintLayout clEditMode;

    @BindView(R.id.activity_profile_back)
    ImageView btBack;

    @BindView(R.id.activity_profile_logout)
    TextView txtLogOut;

    @BindView(R.id.activity_profile_img)
    CircularImageView profileImg;

    @BindView(R.id.activity_profile_name)
    TextView profileName;

    @BindView(R.id.activity_profile_email)
    TextView profileEmailId;

    @BindView(R.id.activity_profile_edit_mode_current_month_investment_amount)
    TextInputEditText etCurrentMonthInvest;

    @BindView(R.id.activity_profile_edit_mode_name_save)
    Button btSave;

    @BindView(R.id.activity_profile_edit_mode_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.activity_profile_edit_mode_current_month_header)
    TextView tvCurrentMonthInvest;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    DatabaseReference mFirebaseDatabase, mRef;
    FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("user_details");

        loadProfileDetails();

        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, SigninActivity.class));
    }

    private void setListener() {
        txtLogOut.setOnClickListener(v -> logOut());

        btBack.setOnClickListener(v -> onBackPressed());

        btSave.setOnClickListener(v -> saveToFirebaseDb());
    }

    public void loadProfileDetails() {
        if (mFirebaseUser.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(mFirebaseUser.getPhotoUrl())
                    .into(profileImg);
            profileName.setText(mFirebaseUser.getDisplayName());
            profileEmailId.setText(mFirebaseUser.getEmail());
            readFromFirebaseDb();
        } else {
            profileImg.setImageResource(R.drawable.ic_user);
        }
    }

    public void saveToFirebaseDb() {
        String userId = mFirebaseUser.getUid();
        String name = mFirebaseUser.getDisplayName();
        String email = mFirebaseUser.getEmail();
        String photoUrl = Objects.requireNonNull(mFirebaseUser.getPhotoUrl()).toString();
        String currentMonthInvest = Objects.requireNonNull(etCurrentMonthInvest.getText()).toString();

        if (!TextUtils.isEmpty(name)) {
            mFirebaseDatabase.child(userId).child("name").setValue(name);
        }
        if (!TextUtils.isEmpty(email)) {
            mFirebaseDatabase.child(userId).child("email").setValue(email);
        }
        if (!TextUtils.isEmpty(currentMonthInvest)) {
            mFirebaseDatabase.child(userId).child("currentMonthInvest").setValue(currentMonthInvest);
        }
        if (!TextUtils.isEmpty(photoUrl)) {
            mFirebaseDatabase.child(userId).child("photoUrl").setValue(photoUrl);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {

            if (!task.isSuccessful()) {

                Log.w(TAG, "getInstanceId failed", task.getException());

                return;
            }

            String userToke = Objects.requireNonNull(task.getResult()).getToken();

            if (!TextUtils.isEmpty(userToke)) {
                mFirebaseDatabase.child(userId).child("userToken").setValue(userToke);
            }

        });

        readFromFirebaseDb();
    }

    public void readFromFirebaseDb() {
        clViewMode.setVisibility(View.VISIBLE);
        clEditMode.setVisibility(View.GONE);
        mRef = FirebaseDatabase.getInstance().getReference("user_details").child(mFirebaseUser.getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showData(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (user != null) {
            tvCurrentMonthInvest.setText(String.format("₹%s", user.getCurrentMonthInvest()));
        } else {
            Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            clViewMode.setVisibility(View.GONE);
            clEditMode.setVisibility(View.VISIBLE);
        }
    }

    public void logOut() {
        mFirebaseAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, SigninActivity.class));
    }
}
