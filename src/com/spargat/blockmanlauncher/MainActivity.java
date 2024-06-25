/*******************************************************************************
 * Copyright 2023-2024 Comical Blockman Launcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.spargat.blockmanlauncher;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {

    private WebView webView;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

String welcome = getString(R.string.welcome);
        Toast.makeText(this, welcome, Toast.LENGTH_LONG).show();
        // Verifică și cere permisiunile la runtime
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        } else {
            // Versiuni mai vechi de Android (mai mici decât 6.0) nu necesită verificarea la runtime
            initializeWebView();
        }
    }

    private void initializeWebView() {
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.web);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Activează JavaScript

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl("https://blockmanlauncher.spargat.tech/");
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
               // Manifest.permission.CAMERA,
                //Manifest.permission.RECORD_AUDIO,
                //Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(permissions)) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        } else {
            initializeWebView();
        }
    }

    private boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        // Dacă oricare dintre permisiuni nu este acordată, afișează un mesaj și închide aplicația
String permission_refused = getString(R.string.permission_refused);

                        Toast.makeText(this, permission_refused, Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // Toate permisiunile au fost acordate, inițializează WebView
                initializeWebView();
            } else {
                // Nicio permisiune nu a fost acordată, afișează un mesaj și închide aplicația
String permission_refused1 = getString(R.string.permission_refused);

                Toast.makeText(this, permission_refused1, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
