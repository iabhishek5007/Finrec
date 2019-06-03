package com.android.bosankus.finrec.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bosankus.finrec.R;
import com.android.bosankus.finrec.ViewModel.ToDoListModel;

import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.TaskViewHolder> {

    List<ToDoListModel> toDoListModelList;
    Context context;

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskSolverName, taskCompletionDate, itemName, itemAmount ;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskSolverName = itemView.findViewById(R.id.activity_completed_task_list_solver_name);
            taskCompletionDate = itemView.findViewById(R.id.activity_completed_task_list_solving_date);
            itemName = itemView.findViewById(R.id.activity_completed_task_list_item_name);
            itemAmount = itemView.findViewById(R.id.activity_completed_task_list_item_amount);
        }
    }

    public CompletedTaskAdapter(List<ToDoListModel> toDoListModelList, Context context) {
        this.toDoListModelList = toDoListModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_completed_task_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        ToDoListModel toDoListModel = toDoListModelList.get(position);

        holder.taskSolverName.setText(toDoListModel.getTaskSolverName());
        holder.taskCompletionDate.setText(toDoListModel.getTaskCompletionDate());
        holder.itemName.setText(toDoListModel.getItemName());
        holder.itemAmount.setText(toDoListModel.getItemAmount());
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
