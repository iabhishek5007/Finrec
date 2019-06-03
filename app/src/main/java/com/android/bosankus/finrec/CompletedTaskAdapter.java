package com.android.bosankus.finrec;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bosankus.finrec.ViewModel.ToDoListModel;

import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.ViewHolder> {

    private List<ToDoListModel> toDoListModelList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView creatorName, createdItems, taskSolverName, taskCompletionDate, itemAmount, creationId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public CompletedTaskAdapter(List<ToDoListModel> toDoListModelList, Context context) {
        this.toDoListModelList = toDoListModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CompletedTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedTaskAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (toDoListModelList.size() == 0) {
                arr = 0;
            } else {
                arr = toDoListModelList.size();
            }
        } catch (Exception e) {
        }
        return arr;
    }
}
