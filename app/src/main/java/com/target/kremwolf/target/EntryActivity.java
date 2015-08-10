package com.target.kremwolf.target;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class EntryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Set QR Button
        Button buttonQr = (Button) findViewById(R.id.start_scanqr);
        buttonQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntryActivity.this, ScanQrCode.class));
            }
        });

        /*
        // Set history Button
        Button buttonHistory = (Button) findViewById(R.id.start_showhistory);
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EntryActivity.this, ProfileActivity.class);
            }
        });
        */

    }

}
