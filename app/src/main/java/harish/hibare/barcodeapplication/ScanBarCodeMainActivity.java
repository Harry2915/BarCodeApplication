package harish.hibare.barcodeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanBarCodeMainActivity extends AppCompatActivity {
    SurfaceView QR_Preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code_main);
        QR_Preview = findViewById(R.id.camera_preview);
        createQRCaptureFromSource();
    }

    private void createQRCaptureFromSource() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(ScanBarCodeMainActivity.this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024).build();
        QR_Preview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(ScanBarCodeMainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cameraSource.start(QR_Preview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size()>0){
                    Intent intent=new Intent();
                    intent.putExtra("barcode",barcodes.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();
                    Toast.makeText(ScanBarCodeMainActivity.this, "Value :: 00 \n"+barcodes.valueAt(0), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}