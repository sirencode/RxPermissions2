package diablo.permission.com;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by yonghe.shen on 16/8/16.
 */
public class PermissionDenyAlert {
    private Activity activity;
    /**
     * 跳转到权限设置的码
     */
    public static final int SETTINGS_REQ_CODE = 16061;

    /**
     * 构造函数
     * @param activity activity
     */
    public PermissionDenyAlert(Activity activity) {
        this.activity = activity;
    }

    /**
     *不再提示时候的提示处理
     * @param permissions 权限申请列表
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void showPermissionDeny(String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        for (String permission : permissions) {
            if (!activity.shouldShowRequestPermissionRationale(permission)) {
                showNeverAskDialog("对应权限已经被禁止了,去设置打开对应权限!");
                break;
            }
        }
    }

    private void showNeverAskDialog(String alert) {
        AlertDialog dialog = new AlertDialog.Builder(activity).setMessage(alert)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, SETTINGS_REQ_CODE);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
