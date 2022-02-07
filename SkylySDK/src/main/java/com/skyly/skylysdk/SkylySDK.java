package com.skyly.skylysdk;

import android.text.TextUtils;

public class SkylySDK {

    private static final SkylySDK instance = new SkylySDK();

    private String apiKey;
    private String apiDomain = "www.mob4pass.com";
    private String publisherId;

    private SkylySDK() {
    }

    public static final SkylySDK getInstance() {
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiDomain() {
        return apiDomain;
    }

    public void setApiDomain(String apiDomain) {
        this.apiDomain = apiDomain;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}
