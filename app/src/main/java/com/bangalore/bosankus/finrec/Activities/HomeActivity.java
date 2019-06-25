package com.bangalore.bosankus.finrec.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bangalore.bosankus.finrec.Adapters.ToDoListAdapater;
import com.bangalore.bosankus.finrec.R;
import com.bangalore.bosankus.finrec.ViewModel.ToDoListModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.activity_home)
    ConstraintLayout clHome;

    @BindView(R.id.activity_home_todo_empty_state)
    ConstraintLayout clEmptyState;

    @BindView(R.id.activity_home_todo_list_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.activity_home_todo_list_header)
    TextView tvTodoListHeader;

    @BindView(R.id.activity_home_todo_list_header_underline)
    View tvTodoListHeaderunderline;

    @BindView(R.id.activity_home_profile_name)
    TextView profileName;

    @BindView(R.id.activity_home_completed_list_btn)
    ImageView imgCompletedList;

    @BindView(R.id.activity_home_chart_btn)
    ImageView imgChartView;

    @BindView(R.id.activity_home_create)
    ImageView imgCreate;

    @BindView(R.id.activity_home_todo_list_loader)
    ProgressBar progressBar;

    private FirebaseAuth mFirebaseAuth;

    private FirebaseUser mFirebaseUser;

    private Trace toDoListDataInsertionTrace, todoListDataFetchTrace;

    DatabaseReference mRef;

    List<ToDoListModel> toDoListModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference("todo_list");

        toDoListDataInsertionTrace = FirebasePerformance.getInstance().newTrace("toDoListDataInsertionTrace");

        todoListDataFetchTrace = FirebasePerformance.getInstance().newTrace("todoListDataFetchTrace");

        loadProfile();

        loadScreen();

        loadTodoListData();
        todoListDataFetchTrace.start();

        setListener();
    }

    public void loadScreen() {
        progressBar.setVisibility(View.VISIBLE);
        tvTodoListHeader.setVisibility(View.GONE);
        tvTodoListHeaderunderline.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void setListener() {

        profileName.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        imgCreate.setOnClickListener(v -> createTodoListItem());

        imgCompletedList.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CompletedTaskActivity.class)));

        imgChartView.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show());

    }

    public void loadProfile() {
        profileName.setText(String.format("Hi, %s", mFirebaseUser.getDisplayName()));
    }

    public void loadTodoListData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    recyclerView.setVisibility(View.VISIBLE);
                    tvTodoListHeader.setVisibility(View.VISIBLE);
                    tvTodoListHeaderunderline.setVisibility(View.VISIBLE);
                    clEmptyState.setVisibility(View.GONE);

                    toDoListModelList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ToDoListModel value = dataSnapshot1.getValue(ToDoListModel.class);
                        ToDoListModel toDo = new ToDoListModel();
                        assert value != null;
                        String creationId = value.getCreationId();
                        String creationDate = value.getCreationDate();
                        String creatorName = value.getCreatorName();
                        String itemNames = value.getItemName();
                        toDo.setCreationId(creationId);
                        toDo.setCreatorName(creatorName);
                        toDo.setCreationDate(creationDate);
                        toDo.setItemName(itemNames);
                        toDoListModelList.add(toDo);

                        ToDoListAdapater toDoListAdapater = new ToDoListAdapater(toDoListModelList, HomeActivity.this);
                        RecyclerView.LayoutManager recyce = new LinearLayoutManager(HomeActivity.this);
                        toDoListAdapater.notifyDataSetChanged();
                        recyclerView.setLayoutManager(recyce);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(toDoListAdapater);
                        //recyclerviewAnimation();

                        // Performance trace
                        todoListDataFetchTrace.stop();

                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvTodoListHeader.setVisibility(View.GONE);
                    tvTodoListHeaderunderline.setVisibility(View.GONE);
                    clEmptyState.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Could not load list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createTodoListItem() {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_home_create_todo_list_layout, viewGroup, false);

        ImageView imgCreateList = dialogView.findViewById(R.id.activity_home_create_list_item_btn);

        TextInputEditText etItem = dialogView.findViewById(R.id.activity_home_create_list_item_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        imgCreateList.setOnClickListener(v -> {

            toDoListDataInsertionTrace.start();

            String creationId = UUID.randomUUID().toString().substring(0, 7);
            String creationDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            String creatorName = mFirebaseUser.getDisplayName();
            String itemNames = etItem.getText().toString();

            if (!TextUtils.isEmpty(creationId)) {
                mRef.child(creationId).child("creationId").setValue(creationId);
            }
            if (!TextUtils.isEmpty(creationDate)) {
                mRef.child(creationId).child("creationDate").setValue(creationDate);
            }
            if (!TextUtils.isEmpty(creatorName)) {
                mRef.child(creationId).child("creatorName").setValue(creatorName);
            }
            if (!TextUtils.isEmpty(itemNames)) {
                mRef.child(creationId).child("itemName").setValue(itemNames);
            }
            alertDialog.dismiss();

            Snackbar.make(clHome, "Item added", Snackbar.LENGTH_SHORT)
                    .setAction("Done", null).show();

            toDoListDataInsertionTrace.stop();
        });
    }
}
