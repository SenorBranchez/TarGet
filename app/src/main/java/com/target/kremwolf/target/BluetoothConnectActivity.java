package com.target.kremwolf.target;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;

public class BluetoothConnectActivity extends AbstractBluetoothActivity {

    private final static int REQUEST_ENABLE_BT = 1;
    private final static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static BluetoothDeviceAdapter listAdapter;

    private TargetBtService btService;
    private String uuid;

    @Override
    protected void onServiceConnected() {

        btService = this.getBtService();
        btService.btDiscovery(uuid);
    }

    @Override
    protected void onDeviceDiscovered(Context context, Intent intent)  {

        BluetoothDevice device = intent.getParcelableExtra(TargetBtService.EXTRA_DEVICE);
        listAdapter.add(device);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        listAdapter = new BluetoothDeviceAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        final ListView statList = (ListView) findViewById(R.id.list_devices);
        statList.setAdapter(listAdapter);
        statList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String deviceAddr = listAdapter.getItem(position).getAddress();
                getConnectionDialog(parent.getContext(), deviceAddr).show();
            }
        });

        // Start bt service
        startService(new Intent(this, TargetBtService.class));

        uuid = getResources().getString(R.string.uuid);

        Log.i("statchange", "Enabeling Bluetooth");

        if(bluetoothAdapter == null) {
            // Device not compatible (no BT)
        }

        if(!bluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void establishConnection(String addr) {
        try {
            btService.btConnect(addr);
        }
        catch(IOException e) {
            Log.e("Connecting", "error establishing connection");
            return;
        }

        Intent nextActivity = new Intent(this, ShootingActivity.class);
        startActivity(nextActivity);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_CANCELED) {
                    //Bluetooth was not enabled
                }
            break;
        }
    }

    private AlertDialog getConnectionDialog(Context context, String deviceAddr) {

        final String addr = deviceAddr;

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
                establishConnection(addr);
            }
        });
        builder.setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.alert_connection)
                .setTitle(R.string.alert_connection_title);

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        return dialog;
    }
}
