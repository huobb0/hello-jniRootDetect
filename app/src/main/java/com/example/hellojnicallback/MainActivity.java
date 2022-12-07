/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hellojnicallback;

import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int hour = 0;
    int minute = 0;
    int second = 0;
    TextView tickView;
    AlertDialog.Builder builder;
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tickView = (TextView) findViewById(R.id.tickView);
        final Button button = (Button) findViewById(R.id.checkbutton);
        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                String result = stringFromJNI();
                Log.d("TAG------>",result);
                if(result.matches("1")){
                    builder.setTitle("Result")
                            .setMessage("Rooted device")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert).show();
                } else {
                    builder.setTitle("Result")
                            .setMessage("Clean device")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert).show();
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        hour = minute = second = 0;

        ((TextView)findViewById(R.id.hellojniMsg)).setText("Click on the button");

        startTicks();
    }

    @Override
    public void onPause () {
        super.onPause();
        StopTicks();
    }

    /*
     * A function calling from JNI to update current timer
     */
    @Keep
    private void updateTimer() {
        ++second;
        if(second >= 60) {
            ++minute;
            second -= 60;
            if(minute >= 60) {
                ++hour;
                minute -= 60;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String ticks = "" + MainActivity.this.hour + ":" +
                        MainActivity.this.minute + ":" +
                        MainActivity.this.second;
                MainActivity.this.tickView.setText(ticks);
                ((TextView)findViewById(R.id.hellojniMsg)).setText(stringFromJNI());
            }
        });
    }
    static {
        System.loadLibrary("hello-jnicallback");
    }
    public native String stringFromJNI();
    public native void startTicks();
    public native void StopTicks();
}
