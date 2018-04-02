package com.xyc.mypermission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xyc.gupermission.GuPermissionManager;
import com.xyc.okutils.utils.DialogUtils;

import static android.os.Looper.prepare;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        String cameras = Manifest.permission.CAMERA;
        String storages1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String storages2 = Manifest.permission.READ_EXTERNAL_STORAGE;
        String[] loca = new String[]{cameras, storages1, storages2};
        GuPermissionManager.getInstance().addPermissions(loca);
        GuPermissionManager.getInstance().requestPermissions(100, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                GuPermissionManager.getInstance().dealWithRefusePermission(this, grantResults, permissions);
                break;
        }
    }

}
