package com.xyc.gupermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.xyc.okutils.utils.DialogUtils;

/**
 * Created by hasee on 2018/4/2.
 */

public class GuPermissionManager {
    private String[] permissions;
    private static GuPermissionManager instance = null;

    private final int REQUEST_PERMISSION_SETTING = 110;

    private GuPermissionManager() {

    }

    public static GuPermissionManager getInstance() {

        if (instance == null) {
            instance = new GuPermissionManager();
        }
        return instance;
    }

    /**
     * 添加权限
     *
     * @param permissions
     */
    public void addPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    /**
     * 检查多个权限是否被授予
     *
     * @param permissions 被检查权限名
     * @return
     */
    public boolean checkMorePermission(Context context, String[] permissions) {
        boolean isAllCheck = false;
        for (int i = 0; i < permissions.length; i++) {
            if (checkSelfPer(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                isAllCheck = true;
                break;
            }
        }
        if (isAllCheck) {
            return false;
        } else {
            return true;
        }
    }

    private int checkSelfPer(Context context, String pers) {
        try {
            return ContextCompat.checkSelfPermission(context, pers);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 检查单个权限是否被授予
     *
     * @param permission 被检查权限名
     * @return
     */
    public boolean checkOnePermission(Context context, String permission) {
        if (checkSelfPer(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 请求权限一个或多个
     *
     * @param
     * @param requestCode
     */
    public void requestPermissions(int requestCode, Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return;
        }
        String[] pers = getPermissions();
        if (pers == null || pers.length == 0) {
            return;
        }
        boolean isAllGrant = checkMorePermission(activity, pers);
        if (isAllGrant) {
            return;
        }
        for (int i = 0; i < permissions.length; i++) {
            if (!checkOnePermission(activity, permissions[i])) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        }

    }

    /**
     * 处理用户拒绝、不再询问权限
     * 有些手机用户选择不再询问后，shouldShowRequestPermissionRationale会一直返回false ，手动处理下
     *
     * @param context
     * @param grantResults
     * @param permissions
     */
    public void dealWithRefusePermission(final Activity context, int[] grantResults, String[] permissions) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {

                    String tips = "您的" + permissions[i].toString() + "权限被拒绝，请手动授予该权限";
                    DialogUtils.showConfirmDialog(context, tips, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                context.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            } catch (Exception e) {

                            }

                        }
                    });
                }
            }
        }
    }

}
