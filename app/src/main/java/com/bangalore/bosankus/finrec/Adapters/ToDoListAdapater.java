package com.bangalore.bosankus.finrec.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bangalore.bosankus.finrec.R;
import com.bangalore.bosankus.finrec.ViewModel.ToDoListModel;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ToDoListAdapater extends RecyclerView.Adapter<ToDoListAdapater.ViewHolder> {

    private List<ToDoListModel> toDoListModelList;
    Context context;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    Task<Void> mRefTodoList;
    DatabaseReference mRefCompletedList  = FirebaseDatabase.getInstance().getReference("completed_task_list");

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView creatorName, createdItems, creationId;
        Button btUpdatePrice;
        ConstraintLayout clParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clParent = itemView.findViewById(R.id.activity_home_todo_list);
            creatorName = itemView.findViewById(R.id.activity_home_todo_list_creator_name);
            createdItems = itemView.findViewById(R.id.activity_home_todo_list_items);
            creationId = itemView.findViewById(R.id.activity_home_todo_list_items_creation_id);
            btUpdatePrice = itemView.findViewById(R.id.activity_home_completed_list_btn);
        }
    }

    public ToDoListAdapater(List<ToDoListModel> toDoListModelList, Context context) {
        this.toDoListModelList = toDoListModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_todo_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoListModel toDoListModel = toDoListModelList.get(position);

        holder.creatorName.setText(toDoListModel.getCreatorName());
        holder.createdItems.setText(toDoListModel.getItemName());

        holder.btUpdatePrice.setOnClickListener(v -> {

            ViewGroup viewGroup = v.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_home_expanded_todo_item_layout, viewGroup, false);
            TextInputEditText etPopupitemNames = dialogView.findViewById(R.id.activity_home_expanded_todo_item_name);
            etPopupitemNames.setText(toDoListModel.getItemName());
            Button btPopupUpdateAmount = dialogView.findViewById(R.id.activity_home_completed_list_btn);
            TextInputEditText etPopupItemAmount = dialogView.findViewById(R.id.activity_home_expanded_todo_item_amount);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            btPopupUpdateAmount.setOnClickListener(v1 -> {
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                mRefCompletedList = FirebaseDatabase.getInstance().getReference("completed_task_list");

                String creatorName = toDoListModel.getCreatorName();
                String creationDate = toDoListModel.getCreationDate();
                String creationId = toDoListModel.getCreationId();
                String taskSolverName = mFirebaseUser.getDisplayName();
                String taskCompletionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                String itemName = etPopupitemNames.getText().toString().trim();
                String itemAmount = etPopupItemAmount.getText().toString().trim();

                if (!TextUtils.isEmpty(creationDate)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("creationDate").setValue(creationDate);
                }
                if (!TextUtils.isEmpty(creatorName)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("creatorName").setValue(creatorName);
                }
                if (!TextUtils.isEmpty(creationId)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("creationId").setValue(creationId);
                }
                if (!TextUtils.isEmpty(itemName)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("itemName").setValue(itemName);
                }
                if (!TextUtils.isEmpty(itemAmount)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("itemAmount").setValue(itemAmount);
                }
                if (!TextUtils.isEmpty(taskCompletionDate)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("taskCompletionDate").setValue(taskCompletionDate);
                }
                if (!TextUtils.isEmpty(taskSolverName)) {
                    mRefCompletedList.child(toDoListModel.getCreationId()).child("taskSolverName").setValue(taskSolverName);
                }

                alertDialog.dismiss();

                mRefTodoList = FirebaseDatabase.getInstance().getReference().child("todo_list").child(toDoListModel.getCreationId()).removeValue();
            });
        });
        holder.clParent.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete the task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        mRefCompletedList = FirebaseDatabase.getInstance().getReference("completed_task_list");
                        mRefTodoList = FirebaseDatabase.getInstance().getReference().child("todo_list").child(toDoListModel.getCreationId()).removeValue();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // user cancelled the dialog
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
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
