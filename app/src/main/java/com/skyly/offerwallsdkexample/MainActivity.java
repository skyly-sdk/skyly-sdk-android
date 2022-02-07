package com.skyly.offerwallsdkexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.skyly.skylysdk.SkylySDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SkylySDK.getInstance().setApiKey("API_KEY");
        SkylySDK.getInstance().setPublisherId("PUB_ID");
        SkylySDK.getInstance().setApiDomain("www.mob4pass.com"); // optional
    }
}