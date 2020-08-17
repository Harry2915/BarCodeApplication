package harish.hibare.barcodeapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {
    TextView textViewBarcodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewBarcodeResult = findViewById(R.id.barcode_Content);
        isCameraPermissionGiven();
    }

    private boolean isCameraPermissionGiven() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Permission RE Grant", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else {
            Toast.makeText(this, "Android OS is not Supported", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public void scanBarCode(View view) {
        startActivityForResult(new Intent(this, ScanBarCodeMainActivity.class), 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Requested : " + permissions[0] + "\nResult\n" + grantResults[0], Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode=data.getParcelableExtra("barcode");
                    textViewBarcodeResult.setText(barcode.displayValue);
                }
                else{
                    textViewBarcodeResult.setText("No Bar Code Found");

                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}