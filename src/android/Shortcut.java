//Copyright 2013 Jorge Cisneros jorgecis@gmail.com

package com.kcchen.shortcut;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import com.kcchen.nativecanvas.enums.RESULT_TYPE;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class Shortcut extends CordovaPlugin {

    private static final String TAG = Shortcut.class.getSimpleName();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            Log.e(TAG, "execute " + "action:" + action + " args:" + args + " callbackContext:" + callbackContext);

            if (action.equals("createShortcut")) {
                return createShortcut(args.getString(0), callbackContext);
            } else if (action.equals("removeShortcut")) {
                return removeShortcut(args.getString(0), callbackContext);
            }

            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            final String errorMessage = e.getMessage();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, errorMessage));
            return false;
        }
    }

    private boolean createShortcut(final String name, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PluginResult pluginResult;

                Context context = cordova.getActivity().getApplicationContext();
                PackageManager pm = cordova.getActivity().getPackageManager();

                Intent i = new Intent();
                i.setClassName(cordova.getActivity().getPackageName(), cordova.getActivity().getClass().getName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Get Icon
                ResolveInfo ri = pm.resolveActivity(i, 0);
                int iconId = ri.activityInfo.applicationInfo.icon;

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    // https://developer.android.com/about/versions/oreo/android-8.0-changes
                    // https://blog.csdn.net/rentee/article/details/77005547

                    i.setAction("android.intent.action.ACTION_MAIN");
                    ShortcutManager shortcutManager = null;
                    shortcutManager = context.getSystemService(ShortcutManager.class);

                    if (shortcutManager.isRequestPinShortcutSupported()) {
                        //Intent shortcutInfoIntent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //shortcutInfoIntent.setAction(Intent.ACTION_VIEW); //action必須設置，不然報錯

                        ShortcutInfo info = new ShortcutInfo.Builder(context, name + System.currentTimeMillis())
                                .setIcon(Icon.createWithResource(context, iconId))
                                .setShortLabel(name)
                                .setIntent(i)
                                .build();

                        //當添加快捷方式的確認彈框彈出來時，將被回調
                        PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, ShortcutReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

                        shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());
                    }
                } else {
                    Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                    shortcutintent.putExtra("duplicate", false);
                    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

                    Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconId);

                    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
                    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
                    context.sendBroadcast(shortcutintent);
                }


                pluginResult = new PluginResult(PluginResult.Status.OK, RESULT_TYPE.SUCCESS.key());

                if (pluginResult == null) {
                    pluginResult = new PluginResult(PluginResult.Status.ERROR, "openNativeCanvas error!!!");
                }

                pluginResult.setKeepCallback(false);
                callbackContext.sendPluginResult(pluginResult);
            }
        });

        return true;
    }

    private boolean removeShortcut(final String name, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PluginResult pluginResult;
                Context context = cordova.getActivity().getApplicationContext();

                Intent i = new Intent();
                i.setClassName(cordova.getActivity().getPackageName(), cordova.getActivity().getClass().getName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Intent shortcutintent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
                shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
                shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
                context.sendBroadcast(shortcutintent);
                pluginResult = new PluginResult(PluginResult.Status.OK, RESULT_TYPE.SUCCESS.key());


                if (pluginResult == null) {
                    pluginResult = new PluginResult(PluginResult.Status.ERROR, "openNativeCanvas error!!!");
                }

                pluginResult.setKeepCallback(false);
                callbackContext.sendPluginResult(pluginResult);
            }
        });

        return true;
    }

}
