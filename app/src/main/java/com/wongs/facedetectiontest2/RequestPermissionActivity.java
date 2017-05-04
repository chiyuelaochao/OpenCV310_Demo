package com.wongs.facedetectiontest2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestPermissionActivity extends Activity {
    private final String TAG = RequestPermissionActivity.class.getSimpleName();
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private final List<PEntity> requestList = new ArrayList<>();
    private final List<String> permissionsList = new ArrayList<>();
    private final Class nextPage = MainActivity.class;

    private void initRequestPermissionList() {
        requestList.add(new PEntity(Manifest.permission.WRITE_EXTERNAL_STORAGE, "write external storage"));
        requestList.add(new PEntity(Manifest.permission.READ_EXTERNAL_STORAGE, "read external storage"));
        requestList.add(new PEntity(Manifest.permission.CAMERA, "camera"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 23) {
            initRequestPermissionList();
            requestPermissions();
        } else {
            goToNextActivity();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (requestList.size() < 1) {
            return;
        }
        for (PEntity pEntity : requestList) {
            String permission = pEntity.name;
            if (super.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, permission + " has been granted to the given package");
            } else {
                permissionsList.add(permission);
            }
        }
        Log.i(TAG, "permissionsList " + permissionsList.toString());
        if (permissionsList.size() > 0) {
            String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
            super.requestPermissions(permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            goToNextActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> resultMap = new HashMap<>();
                for (int i = 0; i < permissions.length; i++) {
                    resultMap.put(permissions[i], grantResults[i]);
                }

                Boolean flag = true;
                for (String permission : permissionsList) {
                    flag &= resultMap.get(permission) == PackageManager.PERMISSION_GRANTED;
                }
                if (flag) {
                    goToNextActivity();
                } else {
                    Toast.makeText(this, "Some Permissions is Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void goToNextActivity() {
        startActivity(new Intent(this, nextPage));
        finish();
    }

    private class PEntity {
        String name;
        String rationale;

        PEntity(String name, String rationale) {
            this.name = name;
            this.rationale = rationale;
        }
    }
}
