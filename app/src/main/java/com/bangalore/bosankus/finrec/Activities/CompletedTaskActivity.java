package com.bangalore.bosankus.finrec.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bangalore.bosankus.finrec.Adapters.CompletedTaskAdapter;
import com.bangalore.bosankus.finrec.R;
import com.bangalore.bosankus.finrec.ViewModel.ToDoListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedTaskActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.activity_completed_task_empty_state)
    ConstraintLayout clEmptyState;

    @BindView(R.id.activity_completed_task_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.activity_completed_task_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.activity_completed_task_loader)
    ProgressBar progressBar;

    @BindView(R.id.activity_completed_task_back)
    ImageView btback;

    private FirebaseAuth mFirebaseAuth;

    private FirebaseUser mFirebaseUser;

    private Trace completedTaskListFetchTrace;

    DatabaseReference mRef;

    List<ToDoListModel> toDoListModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference("completed_task_list");

        completedTaskListFetchTrace = FirebasePerformance.getInstance().newTrace("completedTaskListFetchTrace");

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);

        loadScreen();

        loadData();
        completedTaskListFetchTrace.start();

        setListener();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            loadData();
            swipeRefreshLayout.setRefreshing(false);
        }, 1000);
    }

    private void setListener() {
        btback.setOnClickListener(v -> startActivity(new Intent(CompletedTaskActivity.this, HomeActivity.class)));
    }

    public void loadScreen() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        clEmptyState.setVisibility(View.GONE);
    }

    public void loadData() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toDoListModelList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    clEmptyState.setVisibility(View.GONE);

                    toDoListModelList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        ToDoListModel value = dataSnapshot2.getValue(ToDoListModel.class);
                        ToDoListModel toDo = new ToDoListModel();
                        assert value != null;
                        String creationId = value.getCreationId();
                        String creationDate = value.getCreationDate();
                        String creatorName = value.getCreatorName();
                        String taskSolverName = value.getTaskSolverName();
                        String taskCompletionDate = value.getTaskCompletionDate();
                        String itemName = value.getItemName();
                        String itemAmount = value.getItemAmount();
                        toDo.setCreationId(creationId);
                        toDo.setCreationDate(creationDate);
                        toDo.setCreatorName(creatorName);
                        toDo.setTaskSolverName(taskSolverName);
                        toDo.setTaskCompletionDate(taskCompletionDate);
                        toDo.setItemName(itemName);
                        toDo.setItemAmount("₹"+itemAmount);
                        toDoListModelList.add(toDo);

                        CompletedTaskAdapter completedTaskAdapter = new CompletedTaskAdapter(toDoListModelList, CompletedTaskActivity.this);
                        RecyclerView.LayoutManager recyce = new LinearLayoutManager(CompletedTaskActivity.this);
                        completedTaskAdapter.notifyDataSetChanged();
                        recyclerView.setLayoutManager(recyce);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(completedTaskAdapter);
                        recyclerviewAnimation();

                        // Performance trace
                        completedTaskListFetchTrace.stop();

                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    clEmptyState.setVisibility(View.VISIBLE);

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recyclerviewAnimation() {
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);
    }

}
