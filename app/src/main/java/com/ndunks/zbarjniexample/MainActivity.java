package com.ndunks.zbarjniexample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

public class MainActivity extends Activity {
    TextView scanText;
    FrameLayout mainView;
    private Camera camera;
    private CameraPreview cameraPreview;
    private ImageScanner scanner;
    public Camera.Size previewSize;
    private boolean previewing;
    private Handler autoFocusHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (FrameLayout) findViewById(R.id.activity_main);
        scanText = (TextView) findViewById(R.id.scan_text);
    }
    @Override
    protected void onStop() {
        Log.d("QL", "MainActivity Stop");
        releaseCamera();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("QL", "MainActivity Start");
        startCamera();
    }

    @Override
    public void onBackPressed() {
        releaseCamera();
        super.onBackPressed();
    }
    private boolean startCamera()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(camera == null)
        {
            try {
                camera = getCamera();
                if(camera == null){
                    scanText.setText("Cannot use camera");
                    return false;
                }

                if(cameraPreview == null)
                {
                    autoFocusHandler = new Handler();
                    cameraPreview = new CameraPreview(this, camera, previewCb, autoFocusCB);
                    mainView.addView(cameraPreview);
                    scanText.bringToFront();
                }


            } catch (Exception e){
                e.printStackTrace();
                return false;
            }

        }
        previewSize = camera.getParameters().getPreviewSize();
        if(scanner == null)
        {
            /* Instance barcode scanner */
            scanner = new ImageScanner();
            scanner.setConfig(0, Config.X_DENSITY, 3);
            scanner.setConfig(0, Config.Y_DENSITY, 3);
        }
        previewing = true;
        return true;
    }

    private void releaseCamera() {
        previewing = false;
        if (camera != null) {
            mainView.removeView(cameraPreview);
            camera.setPreviewCallback(null);
            camera.release();
            scanner.destroy();
            camera = null;
            cameraPreview = null;
            scanner = null;
            Log.d("QL", "Camera Released");
        }
    }
    public static Camera getCamera(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback(){
        public void onPreviewFrame(byte[] data, Camera camera) {

            if(!previewing) return;

            Image barcode = new Image(previewSize.width, previewSize.height, "Y800");
            barcode.setData(data);
            int result = scanner.scanImage(barcode);
            //Log.d("QL", "SCANNER GOT: " + result);

            if (result != 0) {
                previewing = false;
                StringBuilder stringBuilder = new StringBuilder();

                for (Symbol sym : scanner.getResults()) {

                    Log.d("QL", "GOT DATA: \n" + sym.getData() + "\n" + "Type: " + sym.getType());
                    stringBuilder.append(sym.getData());
                    stringBuilder.append("\n");
                }

                if (stringBuilder.length() > 0) {
                    releaseCamera();
                    Intent i = new Intent(MainActivity.this, DisplayActivity.class);
                    i.putExtra("text", stringBuilder.toString());
                    startActivity(i);
                }else
                {
                    previewing = true;
                    scanText.setText("Nothing found");
                }
            }
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                camera.autoFocus(autoFocusCB);
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (previewing)
                autoFocusHandler.postDelayed(doAutoFocus, 2000);
        }
    };
}
