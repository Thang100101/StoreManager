package com.example.yourshopapplication.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;

public class GrantPermission {
    public static void scanQRCode(ActivityResultLauncher<ScanOptions> launcher, ScanOptions options, Context context){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
                launcher.launch(options);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };
        createTedPermission(permissionListener);
    }

    public static void openGallery(ActivityResultLauncher<Intent> activityResultLauncher, Context context){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        };

        createTedPermission(permissionListener);
    }

    private static void createTedPermission(PermissionListener permissionListener){
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }


}
