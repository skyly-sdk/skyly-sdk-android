package com.skyly.skylysdk.utils;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.skyly.skylysdk.SkylySDK;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class HTTPRequestTask extends AsyncTask<String, Integer, String> {

    private final HTTPRequestTaskCompletionHandler completionHandler;
    private final Map<String, String> queryParams;

    public HTTPRequestTask(Map<String, String> queryParams, HTTPRequestTaskCompletionHandler completionHandler) {
        this.queryParams = queryParams;
        this.completionHandler = completionHandler;
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "utf-8");
    }

    @Override
    protected String doInBackground(String... params) {
        String _url = params[0];
        try {
            StringBuilder urlStringBuilder = new StringBuilder(_url);

            Set<Map.Entry<String, String>> entrySet = queryParams.entrySet();
            String prefix = "?";
            for (Map.Entry<String, String> entry : entrySet) {
                String value = entry.getValue();
                if (TextUtils.isEmpty(value)) {
                    continue;
                }
                urlStringBuilder.append(prefix)
                        .append(entry.getKey())
                        .append("=")
                        .append(encodeValue(value));
                prefix = "&";
            }

            URL url = new URL(urlStringBuilder.toString());
            Log.d(SkylySDK.LOG_TAG, "calling url : " + url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            try {
                httpURLConnection.setDoInput(true);

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder textBuilder = new StringBuilder();
                try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")))) {
                    int c = 0;
                    while ((c = reader.read()) != -1) {
                        textBuilder.append((char) c);
                    }
                }

                // to log the response code of your request
                Log.d(SkylySDK.LOG_TAG, "MyHttpRequestTask doInBackground : " + httpURLConnection.getResponseCode());
                // to log the response message from your server after you have tried the request.
                Log.d(SkylySDK.LOG_TAG, "MyHttpRequestTask doInBackground : " + httpURLConnection.getResponseMessage());

                return textBuilder.toString();
            } catch (Exception e) {
                Log.e(SkylySDK.LOG_TAG, "", e);
            } finally {
                // this is done so that there are no open connections left when this task is going to complete
                httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        this.completionHandler.onComplete(s);
    }

    public interface HTTPRequestTaskCompletionHandler {
        void onComplete(@Nullable String result);
    }
}