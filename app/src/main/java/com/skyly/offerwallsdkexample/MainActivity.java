package com.skyly.offerwallsdkexample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skyly.skylysdk.SkylySDK;
import com.skyly.skylysdk.jsonmodel.FeedItem;
import com.skyly.skylysdk.model.Gender;
import com.skyly.skylysdk.model.OfferWallRequest;
import com.skyly.skylysdk.model.OfferWallRequestBuilder;

import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OfferWallRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SkylySDK.getInstance()
                .setApiKey("API_KEY")
                .setPublisherId("PUB_ID")
                .setApiDomain("www.mob4pass.com"); // apiDomain is optional


        this.request = new OfferWallRequestBuilder()
                .setUserId("YOUR_USER_ID")
                .setZipCode("75017") // optional
                .setUserAge(31) //optional
                .setUserGender(Gender.MALE) //optional
                .setUserSignupDate(new GregorianCalendar(2018, 0, 13).getTime())
                .setCallbackParameters(new String[]{"param0", "param1"})
                .build();
    }

    public void getOffers(View view) {
        SkylySDK.getInstance().getOfferWall(
                getApplicationContext(),
                request,
                new SkylySDK.OfferWallRequestCompletionHandler() {
                    @Override
                    public void onComplete(List<FeedItem> feed) {
                        //Here display offers
                        Log.d("SkylySDK", feed.toString());
                        Toast.makeText(getApplicationContext(), "Got " + feed.size() + " offers", Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onError(Exception e) {
                        // an error occurred
                        Log.e("SkylySDK", "", e);
                    }
                }
        );
    }

    public void copyUrl(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String url = SkylySDK.getInstance().getHostedOfferWallUrl(getApplicationContext(), request);
                    Log.d("SkylySDK", "Use the url in your own webview: " + url);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("OfferWall", url);
                    clipboard.setPrimaryClip(clip);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Copied to clipboard: " + url, Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void openBrowser(View view) {
        SkylySDK.getInstance().showOfferWall(this, request, SkylySDK.OfferWallPresentationMode.BROWSER);
    }

    public void openWebView(View view) {
        SkylySDK.getInstance().showOfferWall(this, request, SkylySDK.OfferWallPresentationMode.WEB_VIEW);
    }
}