package com.target.kremwolf.target;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by kremwolf on 24.07.2015.
 */
public class DeviceCollection extends HashSet<BluetoothDevice> {

    @Override
    public boolean add(BluetoothDevice object) {

        Iterator<BluetoothDevice> it = this.iterator();

        while(it.hasNext()) {
            if(it.next().getAddress().equals(object.getAddress())) {
                return false;
            }
        }
        return super.add(object);
    }

    public BluetoothDevice get(String addr) {

        Iterator<BluetoothDevice> it = this.iterator();

        while(it.hasNext()) {

            BluetoothDevice device = it.next();

            if(device.getAddress().equals(addr)) {
                return device;
            }
        }

        return null;
    }

    public BluetoothDevice get(int index) {

        if(index < size() && index >= 0) {

            BluetoothDevice[] ret = new BluetoothDevice[size()];

            toArray(ret);

            Log.i("inGet", ret[index].getName());

            return ret[index];
        }
        else {
            return null;
        }
    }
}
