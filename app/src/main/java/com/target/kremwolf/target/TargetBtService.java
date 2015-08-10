package com.target.kremwolf.target;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class TargetBtService extends Service {

    public final static String EXTRA_DEVICE = "com.target.kremwolf.btservice.extra.device";
    public final static String EXTRA_INCOMING = "com.target.kremwolf.btservice.extra.incoming";

    public final static String MESSAGE_DISCOVERED_DEVICE = "com.target.kremwolf.btservice.discovered_device";
    public final static String MESSAGE_INCOMING = "com.target.kremwolf.btservice.incoming";

    private static final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private static final DeviceCollection foundDevices = new DeviceCollection();

    private final LocalBinder mBinder = new LocalBinder();
    private String uuid = "";
    private BluetoothSocket socket = null;

    /**
     * Local binder class
     */
    public class LocalBinder extends Binder {
        TargetBtService getService() {
            Log.i("service", "getservice");
            return TargetBtService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i("service", "bound");

        if(socket != null) {
            try {
                btConnect(socket.getRemoteDevice().getAddress());
            }
            catch(Exception e) {
                Log.e("onBind", "Error connecting on bind!");
            }
        }

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Service", "service started");

        //start device discovery
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        registerReceiver(btReceiver, filter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(btReceiver);
        super.onDestroy();
    }

    public void btDiscovery(String uuid) {

        this.uuid = uuid;
        adapter.startDiscovery();
    }


    public void btConnect(String addr) throws IOException{

        adapter.cancelDiscovery();

        BluetoothDevice device = foundDevices.get(addr);

        // Check bonding state of the connection (bonding dialogue should have popped up at this point)
        if(device.getBondState() != BluetoothDevice.BOND_BONDED) {

            while(device.getBondState() == BluetoothDevice.BOND_BONDING) {
                // In bonding process
            }

            if(device.getBondState() == BluetoothDevice.BOND_NONE) {
                // No Bonding -> abort
            }
        }

        Log.i("btService", "trying to connect to " + device.getName());

        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
        socket.connect();

        Log.i("btService", "connected to " + device.getName());

        startListening();
    }

    private void startListening() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {

                    Log.i("message listener", "starting to listen");

                    InputStream in = socket.getInputStream();

                    while (true) {

                        if (in.available() == 0) {
                            sleep(1000);
                        }
                        else {

                            byte[] msg = new byte[100];
                            in.read(msg);

                            Intent msgIntent = new Intent();
                            msgIntent.setAction(MESSAGE_INCOMING);
                            msgIntent.putExtra(EXTRA_INCOMING, TargetUtility.bytesToString(msg));
                            sendBroadcast(msgIntent);
                        }
                    }
                }
                catch (Exception e) {
                    Log.e("message listener", e.getMessage());
                }
            }
        };

        t.start();
    }

    /**
     * BroadcastReceiver for bluetooth actions
     */
    private final BroadcastReceiver btReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            //Discovery found new device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Get the Bluetooth Device
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(foundDevices.add(device)) {
                    Log.i("btService", "\t Found: " + device.getName());
                }

            }
            // Discovery finished without finding suitable device
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                adapter.cancelDiscovery();

                if(foundDevices.size() == 0) {
                    adapter.startDiscovery();
                    return;
                }

                for(BluetoothDevice device : foundDevices) {

                    device.fetchUuidsWithSdp();
                }
            }
            // Request for UUIDS of given device returns
            /**
             * @TODO Returns twice: first time for actual return and second time cached UUIDs
             */
            else if(BluetoothDevice.ACTION_UUID.equals(action)) {

                Parcelable addr = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);

                if(uuids == null) {
                    return;
                }

                Log.i("btService", "Getting uids from " + addr);

                BluetoothDevice device = foundDevices.get(addr.toString());

                //Loop through every UUID of the device
                for(Parcelable entry : uuids) {

                    String ent = entry.toString();

                    //Check if device supports desired service, if so Broadcast its name
                    if(ent.equals(uuid)) {

                        Intent msgIntent = new Intent();
                        msgIntent.setAction(MESSAGE_DISCOVERED_DEVICE);
                        msgIntent.putExtra(EXTRA_DEVICE, device);

                        sendBroadcast(msgIntent);
                    }
                }
            }
        }
    };

    public void sendMessage(String data) throws IOException {

        socket.getOutputStream().write(data.getBytes());
    }




}
