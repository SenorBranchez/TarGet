package com.target.kremwolf.target;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.zxing.integration.android.IntentIntegrator;


public class ScanQrCode extends ActionBarActivity {

    public static final String EXTRA_SCANNED_UUID = "SCANNED_UUID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        IntentIntegrator.initiateScan(this, IntentIntegrator.QR_CODE_TYPES);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == IntentIntegrator.REQUEST_CODE) {

            String uuid = data.getStringExtra("SCAN_RESULT");

            if(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {

                Intent nextActivity = new Intent(this, BluetoothConnectActivity.class);
                nextActivity.putExtra(EXTRA_SCANNED_UUID, uuid);
                startActivity(nextActivity);
            }
        }
    }
}
