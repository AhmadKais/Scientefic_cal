package com.example.myapplication;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WolframAlphaAPI {

    private static final String BASE_URL = "https://api.wolframalpha.com/v2/query?";

    private static final String APP_ID = "KG26JX-3T3LTU3VTP";


    private static RequestQueue queue; // Add this line for the RequestQueue

    public interface APIResponseListener {
        void onResponse(String response);
    }

    // Initialize the RequestQueue
    public static void init(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static void calculateLimit(String input, APIResponseListener listener) {
        String url = BASE_URL + "input=" + Uri.encode(input) + "&format=plaintext&output=JSON&appid=" + APP_ID;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray podsArray = jsonResponse.getJSONObject("queryresult").getJSONArray("pods");

                String result = "Not found";

                // Iterate through the pods array to find the pod with title "Limit"
                for (int i = 0; i < podsArray.length(); i++) {
                    JSONObject pod = podsArray.getJSONObject(i);
                    if ("Limit".equals(pod.getString("title"))) {
                        result = pod.getJSONArray("subpods").getJSONObject(0).getString("plaintext");
                        break; // Break the loop once we've found the result
                    }
                }

                // Handle infinity symbols
                if (result.contains("â")) {
                    result = result.replace("â", "infinity");
                }

                listener.onResponse(result);
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onResponse("Error parsing the result.");
            }
        }, error -> listener.onResponse("API request failed."));

        queue.add(request);
    }



}
