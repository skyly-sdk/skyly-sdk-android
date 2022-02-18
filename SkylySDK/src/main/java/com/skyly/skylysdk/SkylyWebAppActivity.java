package com.skyly.skylysdk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skyly.skylysdk.databinding.ActivitySkylyWebAppBinding;

public class SkylyWebAppActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "com.skyly.skylysdk.webview.URL";

    private ActivitySkylyWebAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySkylyWebAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);

        binding.webView.loadUrl(url);
    }
}