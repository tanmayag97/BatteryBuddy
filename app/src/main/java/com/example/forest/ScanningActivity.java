package com.example.forest;

import com.skyfishjy.library.RippleBackground;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class ScanningActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int REQUEST_ENABLE_BT  = 1;
    public static final int BTLE_SERVICES = 2;
    private HashMap<String, BTLE_Device> mBTDevicesHashMap; // maps String --> BTLE_Device
    private ArrayList<BTLE_Device> mBTDevicesArrayList;  // dynamic array for BTLE devices
    private ListAdapter_BTLE_Devices adapter;
    private ListView listView;
    private Button scan_button;
    private BroadcastReceiver_BTState mBTStateUpdateReceiver;
    private Scanner_BTLE mBTLeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        //set nav bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        //animation for scanning
        final Animation outer = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,  0.5f);
        outer.setDuration(1500);
        outer.setFillAfter(true);
        outer.setRepeatCount(Animation.INFINITE);
        outer.setRepeatMode(Animation.REVERSE);
        scan_button = findViewById(R.id.scanButton);
        final RippleBackground rippleBackground= findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        scan_button.startAnimation(outer);
        //ble check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Log.i("Info" ,"MainActivity/onCreate/'BLE not supported'");
            Toast.makeText(this, "BLE not supported by this device", Toast.LENGTH_SHORT).show();
            finish();
        }

        mBTStateUpdateReceiver = new BroadcastReceiver_BTState(getApplicationContext());
        mBTLeScanner = new Scanner_BTLE(this, 15000, -75);
        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();
        adapter = new ListAdapter_BTLE_Devices(this, R.layout.btle_device_list_item, mBTDevicesArrayList);
        listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        scan_button.setOnClickListener(this);
        startScan();

        final Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ScanningActivity.this, FeedbackActivity.class);
                startActivity(i);
            }
        });
        final Button scanButton = findViewById(R.id.scanButton1);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ScanningActivity.this, ScanningActivity.class);
                startActivity(i);
            }
        });
        final Button qrButton = findViewById(R.id.qrButton);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ScanningActivity.this, QRActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        //Log.i("Info", "Main Activity is started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("Info", "Main Activity is resumed");
        /*
        try {
            registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
        catch (Exception e) {
            Log.i(TAG, "Can't unregister onResume");
        }
         */
        //startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i("Info", "Main Activity is paused");
        /*
        try {
            unregisterReceiver(mBTStateUpdateReceiver);
        }
        catch (Exception e) {
            Log.i(TAG, "Can't unregister onPause");
        }
         */
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //      unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        try {
            unregisterReceiver(mBTStateUpdateReceiver);
        }
        catch (Exception e){
            Log.i(TAG, "Can't unregister onDestroy");
        }
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // CHECK WHICH REQUEST WE'RE RESPONDING TO
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth turned on manually successfully", Toast.LENGTH_SHORT).show();
                startScan();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please enable your bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BTLE_SERVICES) {
            // Do something
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

        // do something with the text views and start the next activity.

        String name = mBTDevicesArrayList.get(position).getName();
        String address = mBTDevicesArrayList.get(position).getAddress();

        Intent intent = new Intent(this, BTLEServicesActivity.class);
        intent.putExtra(BTLEServicesActivity.EXTRA_NAME, name);
        intent.putExtra(BTLEServicesActivity.EXTRA_ADDRESS, address);
        startActivityForResult(intent, BTLE_SERVICES);
        stopScan();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanButton:
                if (!mBTLeScanner.isScanning()) {
                    startScan();
                }
                else {
                    stopScan();
                }
                break;
            default:
                break;
        }
    }

    public void addDevice(BluetoothDevice device, int rssi) {
        String address = device.getAddress();
        if (!mBTDevicesHashMap.containsKey(address)) {
            BTLE_Device btleDevice = new BTLE_Device(device);
            btleDevice.setRSSI(rssi);

            mBTDevicesHashMap.put(address, btleDevice);
            mBTDevicesArrayList.add(btleDevice);

        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan(){

        /*
        PROGRESS BAR THAT STAYS ON THE SCREEN LIKE LOADING
        ---------------------------------------------------
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
         */
        final RippleBackground rippleBackground= findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        scan_button.setText("Stop Scan");
        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();
        mBTLeScanner.start();
        //Toast.makeText(this, "Started scanning", Toast.LENGTH_SHORT).show();
    }

    public void stopScan() {
        RippleBackground rippleBackground= findViewById(R.id.content);
        rippleBackground.stopRippleAnimation();
        scan_button.clearAnimation();
        scan_button.setText("Scan");
        mBTLeScanner.stop();
        //Log.i("Info", "Scanning stopped");
    }
}
