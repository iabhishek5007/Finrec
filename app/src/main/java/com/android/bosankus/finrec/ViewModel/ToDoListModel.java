package com.android.bosankus.finrec.ViewModel;

public class ToDoListModel {

    public String creationId;
    public String creationDate;
    public String creatorName;
    public String itemName;
    public String taskSolverName;
    public String taskCompletionDate;
    public String itemAmount;

    public ToDoListModel() {
    }

    public String getCreationId() {
        return creationId;
    }

    public void setCreationId(String creationId) {
        this.creationId = creationId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getTaskCompletionDate() {
        return taskCompletionDate;
    }

    public void setTaskCompletionDate(String taskCompletionDate) {
        this.taskCompletionDate = taskCompletionDate;
    }

    public String getTaskSolverName() {
        return taskSolverName;
    }

    public void setTaskSolverName(String taskSolverName) {
        this.taskSolverName = taskSolverName;
    }
}
