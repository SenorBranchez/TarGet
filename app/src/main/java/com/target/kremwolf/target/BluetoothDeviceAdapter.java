package com.target.kremwolf.target;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by kremwolf on 28.07.2015.
 */
public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    private final Context context;

    public BluetoothDeviceAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BluetoothDevice device = getItem(position);

        View row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView label = (TextView) row.findViewById(android.R.id.text1);
        label.setText(device.getName());

        return row;
    }
}
