package com.mosambee.notification_sdk.backup_data;


import com.mosambee.notification_sdk.enumeration.ResultAction;

import org.json.JSONObject;

public interface NotificationListenerOld {
    void onMessageArrived(JSONObject response);
    void onFailed(ResultAction resultAction, String message);
    void onSuccess(String message);
    void onSoundPayingStatus(boolean isComplete);
}
