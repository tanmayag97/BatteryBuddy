package com.example.forest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog_BTLE_Characteristic extends DialogFragment implements DialogInterface.OnClickListener {

    private String title;
    private Service_BTLE_GATT service;
    private BluetoothGattCharacteristic characteristic;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_btle_characteristic, null))
                /*.setNegativeButton("Cancel", this)*/
                .setPositiveButton("Send", this);
        builder.setTitle(title);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        // Find a way to check which button as pressed cancel or ok
//            Utils.toast(activity.getApplicationContext(), "Button " + Integer.toString(which) + " Pressed");

        EditText edit_in_text = ((AlertDialog) dialog).findViewById(R.id.et_submit);
        int edit_in_number = Integer.parseInt(edit_in_text.getText().toString());

        switch (which) {
            case -1:
                // okay button pressed
                if (service != null) {
                    characteristic.setValue(edit_in_number, BluetoothGattCharacteristic.FORMAT_UINT32, 0);
                    service.writeCharacteristic(characteristic);
                }
                break;
            default:
                break;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setService(Service_BTLE_GATT service) {
        this.service = service;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }
}
