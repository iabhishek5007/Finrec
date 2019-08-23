package com.bangalore.bosankus.finrec.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bangalore.bosankus.finrec.R;
import com.bangalore.bosankus.finrec.ViewModel.ToDoListModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.TaskViewHolder> {

    List<ToDoListModel> toDoListModelList;

    Context context;

    Task<Void> mRefCompletedListRemove;

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskSolverName, taskCompletionDate, itemName, itemAmount;

        ConstraintLayout clExpandedCompletedTask;


        public TaskViewHolder(@NonNull View itemView) {

            super(itemView);

            taskSolverName = itemView.findViewById(R.id.activity_completed_task_list_solver_name);

            taskCompletionDate = itemView.findViewById(R.id.activity_completed_task_list_solving_date);

            itemName = itemView.findViewById(R.id.activity_completed_task_list_item_name);

            itemAmount = itemView.findViewById(R.id.activity_completed_task_list_item_amount);

            clExpandedCompletedTask = itemView.findViewById(R.id.activity_completed_task_list_item_holder);
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


        holder.clExpandedCompletedTask.setOnClickListener(v -> {

            ViewGroup viewGroup = v.findViewById(android.R.id.content);

            View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_completed_task_expanded_item_layout, viewGroup, false);

            TextView tvCreationId = dialogView.findViewById(R.id.activity_completed_task_expanded_creationId);

            TextView tvResolvingDate = dialogView.findViewById(R.id.activity_completed_task_expanded_resolve_date);

            TextView tvCreationDate = dialogView.findViewById(R.id.activity_completed_task_expanded_creation_date);

            TextView tvResolverName = dialogView.findViewById(R.id.activity_completed_task_expanded_resolver_name);

            TextView tvCreatorName = dialogView.findViewById(R.id.activity_completed_task_expanded_creator_name);

            TextView tvItemName = dialogView.findViewById(R.id.activity_completed_task_expanded_item_name);

            TextView tvAmount = dialogView.findViewById(R.id.activity_completed_task_expanded_total_amount);


            tvCreationId.setText(toDoListModel.getCreationId());

            tvResolvingDate.setText(toDoListModel.getTaskCompletionDate());

            tvCreationDate.setText(toDoListModel.getCreationDate());

            tvResolverName.setText(toDoListModel.getTaskSolverName());

            tvCreatorName.setText(toDoListModel.getCreatorName());

            tvItemName.setText(toDoListModel.getItemName());

            tvAmount.setText(toDoListModel.getItemAmount());


            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            alertDialog.show();

        });


        holder.clExpandedCompletedTask.setOnLongClickListener(v -> {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);

            builder.setMessage("Are you sure you want to delete the task?")

                    .setPositiveButton("Yes", (dialog, which) -> {
                        mRefCompletedListRemove = FirebaseDatabase.getInstance().getReference().child("completed_task_list").child(toDoListModel.getCreationId()).removeValue();

                    })
                    .setNegativeButton("No", (dialog, which) -> {

                    });

            androidx.appcompat.app.AlertDialog dialog = builder.create();

            dialog.show();

            return true;
        });
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try {

            if (toDoListModelList.size() == 0) {

            } else {
                arr = toDoListModelList.size();
            }

        } catch (Exception e) {

        }

        return arr;
    }
}
