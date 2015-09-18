package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList<BarcodeFormat>();
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        ALL_FORMATS.add(BarcodeFormat.EAN_13);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();


        mScannerView.setResultHandler(this);
        mScannerView.setFormats(ALL_FORMATS);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", rawResult.getText());
        setResult(RESULT_OK, returnIntent);
        finish();

    }
}