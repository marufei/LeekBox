/**
 *
 */
package com.dytj.leekbox.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 *
 * @author hongming.wang
 * @创建时间：2016年5月27日 下午3:01:31
 * @项目名称： AMapLocationDemo
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 * @since 2.5.0
 */
public class CheckPermissionsActivity extends FragmentActivity {
    private String TAG = "CheckPermissionsActivity";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions;

    private static final int PERMISSON_REQUESTCODE = 0X13;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setPermission(String[] strings) {
        needPermissions=new String[strings.length];
        for(int i=0;i<strings.length;i++){
            needPermissions[i]=strings[i];
            Log.e(TAG,"Permissions:"+needPermissions[i]);
        }

        Log.e(TAG, "进入----setPermission方法");
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }
    }


    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String[] permissions) {
        Log.e(TAG, "进入------checkPermissions方法");
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
//                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
//                if (null != needRequestPermissonList
//                        && needRequestPermissonList.size() > 0) {
//                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
//                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
//                            int.class});
//
//                    method.invoke(this, array, PERMISSON_REQUESTCODE);
//                }
                //获取权限列表
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    //list.toarray将集合转化为数组
                    ActivityCompat.requestPermissions(this,
                            needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]),
                            PERMISSON_REQUESTCODE);
                    Log.e(TAG, "requestPermissions方法执行-------");
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        //for (循环变量类型 循环变量名称 : 要被遍历的对象)
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "进入权限回调方法");
        if (requestCode == PERMISSON_REQUESTCODE) {
            Log.e(TAG, "进入权限回调方法1");
            if (!verifyPermissions(grantResults)) {
                Log.e(TAG, "进入权限回调方法2");
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否允许打开权限");

        // 拒绝, 退出应用
        builder.setNegativeButton("拒绝",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("允许",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
