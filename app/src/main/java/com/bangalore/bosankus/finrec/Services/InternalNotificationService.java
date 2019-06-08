package com.bangalore.bosankus.finrec.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class InternalNotificationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InternalNotificationService(String name) {
        super("notificationIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        switch (intent.getAction()) {
            case "left":
                android.os.Handler leftHandler = new android.os.Handler(Looper.getMainLooper());
                leftHandler.post(() -> Toast.makeText(getBaseContext(), "You clicked left button", Toast.LENGTH_SHORT).show());
                break;
            case "right":
                android.os.Handler rightHandler = new android.os.Handler(Looper.getMainLooper());
                rightHandler.post(() -> Toast.makeText(getBaseContext(), "You clicked right button", Toast.LENGTH_SHORT).show());
                break;
        }
    }
}
