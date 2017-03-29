package com.example.felix.wearableforensics;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felix on 10/09/2016.
 */
public class PermissionMgr {

    private List<Permission> permissions;
    private Context context;
    private Activity activity;

    public PermissionMgr(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        permissions = new ArrayList<>();
    }

    private Context getContext() {
        return context;
    }

    private Activity getActivity() {
        return activity;
    }

    public void set(Permission permission) {
        permissions.add(permission);
    }

    private List<Permission> getPermissions() {
        return this.permissions;
    }

    public boolean check() {
        for (Permission permission : this.getPermissions()
                ) {

            if (ActivityCompat.checkSelfPermission(this.getContext(), permission.getName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

        }

        return true;

    }

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 100;

    public boolean request() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        for (Permission permission : this.getPermissions()
                ) {

            if (!addPermission(this.getContext(), this.getActivity(), permissionsList, permission.getName()))
                permissionsNeeded.add(permission.getDescription());
        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
            }

            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            System.out.println("Permissions required...");

            return false;
        }
        return true;
    }

    private boolean addPermission(Context context, Activity activity, List<String> permissionsList, String permission) {

        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return false;
        }

        return true;
    }

}