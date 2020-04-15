package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;

public class QRActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT  = 1;
    public static final int BTLE_SERVICES = 2;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private String LOGTAG = "QR info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        //set nav bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        //ble check
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.i("Info" ,"MainActivity/onCreate/'BLE not supported'");
            Toast.makeText(this, "BLE not supported by this device", Toast.LENGTH_SHORT).show();
            finish();
        }
        //QR code scan button
        Button btn = findViewById(R.id.scan_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QRActivity.this, QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // CHECK WHICH REQUEST WE'RE RESPONDING TO
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth turned on manually successfully", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please enable your bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == BTLE_SERVICES) {
            // Do something
        }

        //QR code
        if(resultCode != Activity.RESULT_OK)
        {
            Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(LOGTAG,"Have scan result in your app activity :"+ result);

            String name = "Unknown";
            String address = result;

            Intent intent = new Intent(QRActivity.this, BTLEServicesActivity.class);
            intent.putExtra(BTLEServicesActivity.EXTRA_NAME, name);
            intent.putExtra(BTLEServicesActivity.EXTRA_ADDRESS, address);
            startActivityForResult(intent, BTLE_SERVICES);

            /*
            AlertDialog alertDialog = new AlertDialog.Builder(QRActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
             */

        }
    }

    public void clickQRFunction(View view) {
        // FA:16:2D:37:F8:23
        Context context = view.getContext();
        EditText macAdd = findViewById(R.id.qrNum);
        // do something with the text views and start the next activity.

        String name = "Unknown";
        String address = macAdd.getText().toString();

        if(address.matches("")){
            Toast.makeText(context, "Enter a valid address", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(QRActivity.this, BTLEServicesActivity.class);
            intent.putExtra(BTLEServicesActivity.EXTRA_NAME, name);
            intent.putExtra(BTLEServicesActivity.EXTRA_ADDRESS, address);
            startActivityForResult(intent, BTLE_SERVICES);
        }

    }

}
