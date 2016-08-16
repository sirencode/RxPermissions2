package diablo.permission.com;

import android.Manifest;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import com.permission.diablo.rxpermissionlib.RxPermissions;
import java.io.IOException;
import rx.functions.Action1;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "RxPermissionsSample";

    private final String[] permissions =
            new String[] { Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION };

    private Camera camera;
    private SurfaceView surfaceView;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = RxPermissions.getInstance(this);
        rxPermissions.setLogging(true);

        setContentView(R.layout.act_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        Button grant = (Button) findViewById(R.id.enableCamera);
        grant.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        RxPermissions.getInstance(this)
                .request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean grant) {
                        if (grant){
                            openCamera();
                        }else {
                            new PermissionDenyAlert(MainActivity.this).showPermissionDeny(permissions);
                        }
                    }
                });
    }

    private void openCamera() {
        releaseCamera();
        camera = Camera.open(0);
        try {
            camera.setPreviewDisplay(surfaceView.getHolder());
            camera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Error while trying to display the camera preview", e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
