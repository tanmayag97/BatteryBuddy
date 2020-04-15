package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //set nav bar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        final ListView troubleList = findViewById(R.id.troubleList);

        String[] troubleshoots = new String[] {
                "A compatible Battery is nearby",
                "Bluetooth pairing is enabled",
                "Battery has charge/is charging",
                "No data change",
                "Battery is not responding"
        };

        final List<String> troubleshootList = new ArrayList<String>(Arrays.asList(troubleshoots));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, troubleshootList);

        troubleList.setAdapter(arrayAdapter);


        final Button helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(HelpActivity.this, FeedbackActivity.class);
                startActivity(i);
            }
        });
        final Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(HelpActivity.this, ScanningActivity.class);
                startActivity(i);
            }
        });
        final Button qrButton = findViewById(R.id.qrButton);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(HelpActivity.this, QRActivity.class);
                startActivity(i);
            }
        });
    }

}
