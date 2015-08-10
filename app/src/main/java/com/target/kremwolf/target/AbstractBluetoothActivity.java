package com.target.kremwolf.target;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by kremwolf on 28.07.2015.
 */
abstract class AbstractBluetoothActivity extends ActionBarActivity {

    private TargetBtService btService;
    private ServiceConnection serviceConnection;
    private BroadcastReceiver serviceMessageReceiver;

    protected void onServiceConnected() {
    }

    protected void onServiceDisconnected() {

    }

    protected void onDeviceDiscovered(Context context, Intent intent) {
    }

    protected void onNewMessage(String data) {

    }

    protected AbstractBluetoothActivity() {
        super();

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                TargetBtService.LocalBinder myBinder = (TargetBtService.LocalBinder) binder;
                btService = myBinder.getService();

                IntentFilter serviceMessageFilter = new IntentFilter();
                serviceMessageFilter.addAction(TargetBtService.MESSAGE_DISCOVERED_DEVICE);
                serviceMessageFilter.addAction(TargetBtService.MESSAGE_INCOMING);

                registerReceiver(serviceMessageReceiver, serviceMessageFilter);

                // Call hook
                AbstractBluetoothActivity.this.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                btService = null;
                unregisterReceiver(serviceMessageReceiver);

                // Call hook
                AbstractBluetoothActivity.this.onServiceDisconnected();

            }
        };


        serviceMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

                if(TargetBtService.MESSAGE_DISCOVERED_DEVICE.equals(action)) {
                    AbstractBluetoothActivity.this.onDeviceDiscovered(context, intent);
                }
                else if(TargetBtService.MESSAGE_INCOMING.equals(action)) {

                    String data = intent.getStringExtra(TargetBtService.EXTRA_INCOMING);
                    // Call hook
                    onNewMessage(data);
                }
            }
        };
    }

    protected TargetBtService getBtService() {
        return btService;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Now try to bind to service
        if(! bindService(new Intent(this, TargetBtService.class), serviceConnection, BIND_AUTO_CREATE)) {
            throw new RuntimeException("Unable to bind to bt service");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);
    }
}
