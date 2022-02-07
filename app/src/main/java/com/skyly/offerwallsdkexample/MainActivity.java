package com.skyly.offerwallsdkexample;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.skyly.skylysdk.SkylySDK;
import com.skyly.skylysdk.jsonmodel.FeedItem;
import com.skyly.skylysdk.model.Gender;
import com.skyly.skylysdk.model.OfferWallRequestBuilder;

import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SkylySDK.getInstance()
                .setApiKey("API_KEY")
                .setPublisherId("PUB_ID")
                .setApiDomain("www.mob4pass.com"); // apiDomain is optional

        try {
            SkylySDK.getInstance().getOfferWall(
                    getApplicationContext(),
                    new OfferWallRequestBuilder()
                            .setUserId("YOUR_USER_ID")
                            .setZipCode("75017") // optional
                            .setUserAge(31) //optional
                            .setUserGender(Gender.MALE) //optional
                            .setUserSignupDate(new GregorianCalendar(2018, 0, 13).getTime())
                            .setCallbackParameters(new String[]{"param0", "param1"})
                            .build(),
                    new SkylySDK.OfferWallRequestCompletionHandler() {
                        @Override
                        public void onComplete(List<FeedItem> feed) {
                            //Here display offers
                            Log.d("SkylySDK", feed.toString());
                        }

                        @Override
                        public void onError(Exception e) {
                            // an error occurred
                            Log.e("SkylySDK", "", e);
                        }
                    }
            );
        } catch (Exception e) {
            Log.e("Skyly", "", e);
        }
    }
}