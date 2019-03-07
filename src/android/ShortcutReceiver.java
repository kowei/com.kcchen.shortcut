package com.kcchen.shortcut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kowei on 2018/5/29.
 */

public class ShortcutReceiver extends BroadcastReceiver{

    private static final String TAG = ShortcutReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "> shortcut onReceive: ");
    }
}
