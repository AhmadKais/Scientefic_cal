package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WolframAlphaAPI {

    private static final String BASE_URL = "http://api.wolframalpha.com/v2/query";
    private static final String API_KEY = "KG26JX-3T3LTU3VTP";

    public interface APIResponseListener {
        void onResponse(String response);
    }

    public static void calculateLimit(String limitQuery, APIResponseListener listener) {
        String url = BASE_URL + "?input=" + limitQuery + "&format=plaintext&output=JSON&appid=" + API_KEY;

        new APIRequestTask(listener).execute(url);
    }

    private static class APIRequestTask extends AsyncTask<String, Void, String> {
        private APIResponseListener listener;

        public APIRequestTask(APIResponseListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response.isEmpty()) {
                listener.onResponse("");
            } else {
                listener.onResponse(response);
            }
        }
    }
}
