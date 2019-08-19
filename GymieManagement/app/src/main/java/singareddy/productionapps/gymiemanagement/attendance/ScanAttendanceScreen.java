package singareddy.productionapps.gymiemanagement.attendance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import singareddy.productionapps.gymiemanagement.R;

public class ScanAttendanceScreen extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener{

    private static final String TAG = "ScanAttendanceScreen";
    private static final int CAMERA_PERMISSION_REQ = 34;
    private String scannedQrString;
    private ImageView scanSuccessIndicator;
    private TextView scannedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_attendance);
        initialiseUI();
    }

    private void initialiseUI() {
        scannedTextView = findViewById(R.id.activity_scan_attendance_tv_scan_output);
        scanSuccessIndicator = findViewById(R.id.activity_scan_attendance_iv_scan_output);
        scannedTextView.setText("");
        scanSuccessIndicator.setImageDrawable(null);
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String [] {Manifest.permission.CAMERA},CAMERA_PERMISSION_REQ);
        }
    }

    /**
     * This is where the scanned barcode is returned.
     * Here we extract the string value in the barcode
     * and use it to update the UI.
     * Preferred check in format: username__timestamp__in
     * Preferred check out format: username__timestamp__out
     * @param barcode
     */
    @Override
    public void onScanned(Barcode barcode) {
        Log.i(TAG, "onScanned: Scanned Barcode: "+barcode.displayValue);
        Log.i(TAG, "onScanned: Thread: "+Thread.currentThread().getName());
        scannedQrString = barcode.displayValue;
        scannedTextView.setText(scannedQrString);
        scanSuccessIndicator.setImageDrawable(getDrawable(R.drawable.correct));
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.i(TAG, "onScannedMultiple: "+Thread.currentThread().getName());
//        scannedTextView.setText("");
        scanSuccessIndicator.setImageDrawable(getDrawable(R.drawable.incorrect));
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.i(TAG, "onScanError: Barcode Error: "+errorMessage);
        scannedTextView.setText("");
        scanSuccessIndicator.setImageDrawable(getDrawable(R.drawable.incorrect));
    }

    @Override
    public void onCameraPermissionDenied() {
        Log.i(TAG, "onCameraPermissionDenied: Camera Permission Denied");
    }
}
