package com.android.bosankus.finrec.ViewModel;

public class User {
    public String displayPictureUrl;
    public String displayName;
    public String emailId;
    public String currentMonthInvest;

    public User() {
    }

    public String getDisplayPictureUrl() { return  displayPictureUrl; }

    public void setDisplayPictureUrl(String displayPictureUrl) { this.displayPictureUrl = displayPictureUrl; }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmailId() { return emailId; }

    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getCurrentMonthInvest() {
        return currentMonthInvest;
    }

    public void setCurrentMonthInvest(String currentMonthInvest) {
        this.currentMonthInvest = currentMonthInvest;
    }
}