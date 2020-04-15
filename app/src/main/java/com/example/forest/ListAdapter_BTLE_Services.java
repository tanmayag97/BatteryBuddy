package com.example.forest;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter_BTLE_Services extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<BluetoothGattService> services_ArrayList;
    private HashMap<String, ArrayList<BluetoothGattCharacteristic>> characteristics_HashMap;

    public ListAdapter_BTLE_Services(Activity activity, ArrayList<BluetoothGattService> listDataHeader,
                                     HashMap<String, ArrayList<BluetoothGattCharacteristic>> listChildData) {

        this.activity = activity;
        this.services_ArrayList = listDataHeader;
        this.characteristics_HashMap = listChildData;
    }

    @Override
    public int getGroupCount() {
        return services_ArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return characteristics_HashMap.get(
                services_ArrayList.get(groupPosition).getUuid().toString()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return services_ArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return characteristics_HashMap.get(
                services_ArrayList.get(groupPosition).getUuid().toString()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        BluetoothGattService bluetoothGattService = (BluetoothGattService) getGroup(groupPosition);
        String serviceName;

        String serviceUUID = bluetoothGattService.getUuid().toString();
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.btle_service_list_item, null);
        }

        TextView tv_service = convertView.findViewById(R.id.tv_service_uuid);

        if(serviceUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f75cba")){
            serviceName = "Data service: ";
        }
        else if(serviceUUID.matches("00001801-0000-1000-8000-00805f9b34fb")){ //
            serviceName = "GATT service: ";
        }
        else if(serviceUUID.matches("00001800-0000-1000-8000-00805f9b34fb")){
            serviceName = "GAPP service: ";
        }
        else if(serviceUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f85cba")){
            serviceName = "Diagnostic: ";
        }
        else {
            serviceName = "Unknown: ";
        }

        tv_service.setText(serviceName + serviceUUID);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        BluetoothGattCharacteristic bluetoothGattCharacteristic = (BluetoothGattCharacteristic) getChild(groupPosition, childPosition);
        String charName;
        String characteristicUUID =  bluetoothGattCharacteristic.getUuid().toString();
        byte[] data = bluetoothGattCharacteristic.getValue();

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.btle_characteristics_list_item, null);
        }

        TextView tv_service = convertView.findViewById(R.id.tv_characteristic_uuid);

        if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f75dba")){
            charName = "Main: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f75eba")){ //
            charName = "Battery Voltage: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f75fba")){
            charName = "Cell/Mosfet: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f76aba")){
            charName = "Range: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f76bba")){
            charName = "Battery ID: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f76cba")){
            charName = "BMS config: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f76dba")){
            charName = "Firmware ver: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f76eba")){
            charName = "Auth: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f85dba")){
            charName = "Main: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f85eba")){
            charName = "DCCCW: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f85fba")){
            charName = "CTC: ";
        }
        else if(characteristicUUID.matches("ed0ef62e-9b0d-11e4-89d3-123b93f85aba")){
            charName = "Voltage: ";
        }
        else {
            charName = "Unknown: ";
        }

        tv_service.setText(charName + characteristicUUID);

        int properties = bluetoothGattCharacteristic.getProperties();

        TextView tv_property = convertView.findViewById(R.id.tv_properties);
        StringBuilder sb = new StringBuilder();

        if (Utils.hasReadProperty(properties) != 0) {
            sb.append("Read ");
        }

        if (Utils.hasWriteProperty(properties) != 0) {
            sb.append("Write ");
        }

        if (Utils.hasNotifyProperty(properties) != 0) {
            sb.append("Notify ");
        }

        tv_property.setText("Properties: " + sb.toString());

        TextView tv_value = convertView.findViewById(R.id.tv_value);


        if (data != null) {
            tv_value.setText("Value: " + Utils.hexToString(data));
        }
        else {
            tv_value.setText("Value: ---");
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
