package com.skyly.skylysdk;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.skyly.skylysdk.jsonmodel.FeedItem;
import com.skyly.skylysdk.jsonmodel.JsonParser;
import com.skyly.skylysdk.model.OfferWallRequest;
import com.skyly.skylysdk.utils.AeSimpleSHA1;
import com.skyly.skylysdk.utils.DeviceUtils;
import com.skyly.skylysdk.utils.HTTPRequestTask;
import com.skyly.skylysdk.utils.IPUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SkylySDK {

    private final static SkylySDK instance = new SkylySDK();
    public static final String LOG_TAG = "SkylySDK";

    private String apiKey;
    private String apiDomain = "www.mob4pass.com";
    private String publisherId;

    private SkylySDK() {
    }

    public static SkylySDK getInstance() {
        return instance;
    }

    private void checkConfig() throws Exception {
        if (TextUtils.isEmpty(apiKey)) {
            throw new Exception("SkylySDK: missing apiKey");
        }
        if (TextUtils.isEmpty(apiDomain)) {
            throw new Exception("SkylySDK: missing apiDomain");
        }
        if (TextUtils.isEmpty(publisherId)) {
            throw new Exception("SkylySDK: missing publisherId");
        }
    }

    public void getOfferWall(Context context, OfferWallRequest request, OfferWallRequestCompletionHandler completionHandler) {
        try {
            checkConfig();
        } catch (Exception e) {
            Log.e(LOG_TAG, "", e);
            completionHandler.onError(e);
            return;
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getOfferWall(context, request, completionHandler);
                }
            }.start();
            return;
        }

        try {
            String url = "https://" + this.apiDomain + "/api/feed/v2/";
            Map<String, String> queryParams = getQueryParams(context, request);
            new HTTPRequestTask(queryParams, result -> {
                if (TextUtils.isEmpty(result)) {
                    completionHandler.onComplete(new ArrayList<>());
                    return;
                }
                try {
                    List<FeedItem> feed = JsonParser.parseFeedFromResponse(result);
                    completionHandler.onComplete(feed);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing response: " + result, e);
                    completionHandler.onError(e);
                }

            }).execute(url);
        } catch (Exception e) {
            Log.e(LOG_TAG, "", e);
            completionHandler.onError(e);
        }
    }

    private Map<String, String> getQueryParams(Context context, OfferWallRequest request) throws Exception {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("pubid", this.publisherId);

        int timestamp = Math.round(new Date().getTime() / 1000f);
        queryParams.put("timestamp", timestamp + "");
        queryParams.put("hash", AeSimpleSHA1.SHA1(timestamp + this.apiKey));

        queryParams.put("device", "android");
        queryParams.put("devicemodel", Build.MODEL);
        queryParams.put("os_version", Build.VERSION.RELEASE);
        queryParams.put("is_tablet", DeviceUtils.isTablet(context) ? "1" : "0");
        queryParams.put("country", Locale.getDefault().getCountry());
        queryParams.put("locale", Locale.getDefault().getLanguage().startsWith("fr") ? "fr" : "en");
        queryParams.put("ip", IPUtils.getIPAddress(true));

        queryParams.put("userid", request.getUserId());
        queryParams.put("zip", request.getZipCode());
        if (request.getUserGender() != null) {
            queryParams.put("user_gender", request.getUserGender().getRawValue());
        }
        if (request.getUserAge() != null) {
            queryParams.put("user_age", request.getUserAge() + "");
        }
        if (request.getUserSignupDate() != null) {
            queryParams.put("user_signup_timestamp", Math.round(request.getUserSignupDate().getTime() / 1000f) + "");
        }

        String[] callbackParameters = request.getCallbackParameters();
        if (callbackParameters != null) {
            for (int i = 0; i < callbackParameters.length; i++) {
                queryParams.put("pub" + i, callbackParameters[i]);
            }
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        queryParams.put("carrier", telephonyManager.getNetworkOperatorName());

        AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
        if (!adInfo.isLimitAdTrackingEnabled()) {
            String gaId = adInfo.getId();
            if (gaId != null && !TextUtils.isEmpty(gaId)) {
                queryParams.put("gaid", gaId);
                queryParams.put("gaidsha1", AeSimpleSHA1.SHA1(gaId));
            }
        }

        return queryParams;
    }

    public interface OfferWallRequestCompletionHandler {
        /**
         * Called with offers when request is done. **Warning** this won't be on the main thread
         *
         * @param feed the offers available for this user
         */
        void onComplete(List<FeedItem> feed);

        void onError(Exception e);
    }

    // getters and setters

    @SuppressWarnings("UnusedReturnValue")
    public SkylySDK setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public SkylySDK setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public SkylySDK setPublisherId(String publisherId) {
        this.publisherId = publisherId;
        return this;
    }
}
