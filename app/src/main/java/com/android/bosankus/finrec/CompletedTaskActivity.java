package com.android.bosankus.finrec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;

public class CompletedTaskActivity extends AppCompatActivity {

    @BindView(R.id.activity_completed_task_back)
    ImageView imgBack;

    @BindView(R.id.activity_completed_task_back_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
    }
}
