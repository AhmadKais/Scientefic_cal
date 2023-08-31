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
    public static void calculateDerivative(String function, APIResponseListener successListener, APIResponseListener failureListener) {
        String input = "derivative of " + function;
        String url = BASE_URL + "input=" + Uri.encode(input) + "&format=plaintext&output=JSON&appid=" + APP_ID;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray podsArray = jsonResponse.getJSONObject("queryresult").getJSONArray("pods");

                String result = "Not found";

                for (int i = 0; i < podsArray.length(); i++) {
                    JSONObject pod = podsArray.getJSONObject(i);
                    if ("Derivative".equals(pod.getString("title"))) { // Adjust this title to match WolframAlpha's result title for derivative.
                        result = pod.getJSONArray("subpods").getJSONObject(0).getString("plaintext");
                        break;
                    }
                }

                successListener.onResponse(result);
            } catch (JSONException e) {
                e.printStackTrace();
                failureListener.onResponse("Error parsing the result.");
            }
        }, error -> failureListener.onResponse("API request failed."));

        queue.add(request);
    }
    public static void calculateIntegral(String function, double lowerLimit, double upperLimit, APIResponseListener successListener, APIResponseListener failureListener) {
        String lowerLimitStr = (lowerLimit == Double.POSITIVE_INFINITY) ? "inf" :
                (lowerLimit == Double.NEGATIVE_INFINITY) ? "-inf" :
                        String.valueOf(lowerLimit);

        String upperLimitStr = (upperLimit == Double.POSITIVE_INFINITY) ? "inf" :
                (upperLimit == Double.NEGATIVE_INFINITY) ? "-inf" :
                        String.valueOf(upperLimit);

        String input = String.format("integral of %s from %s to %s", function, lowerLimitStr, upperLimitStr);
        String url = BASE_URL + "input=" + Uri.encode(input) + "&format=plaintext&output=JSON&appid=" + APP_ID;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray podsArray = jsonResponse.getJSONObject("queryresult").getJSONArray("pods");

                String result = "Not found";
                System.out.println(response);

                for (int i = 0; i < podsArray.length(); i++) {
                    JSONObject pod = podsArray.getJSONObject(i);
                    if ("Definite integral".equals(pod.getString("title"))) {
                        result = pod.getJSONArray("subpods").getJSONObject(0).getString("plaintext");
                        break;
                    }
                }

                successListener.onResponse(result);
            } catch (JSONException e) {
                e.printStackTrace();
                failureListener.onResponse("Error parsing the result.");
            }
        }, error -> failureListener.onResponse("API request failed."));

        queue.add(request);
    }
    public static void calculateGeneralIntegral(String function, APIResponseListener successListener, APIResponseListener failureListener) {
        String input = String.format("integral of %s", function);
        String url = BASE_URL + "input=" + Uri.encode(input) + "&format=plaintext&output=JSON&appid=" + APP_ID;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray podsArray = jsonResponse.getJSONObject("queryresult").getJSONArray("pods");

                String result = "Not found";

                for (int i = 0; i < podsArray.length(); i++) {
                    JSONObject pod = podsArray.getJSONObject(i);
                    if ("Indefinite integral".equals(pod.getString("title")) || "Definite integral".equals(pod.getString("title"))) {
                        result = pod.getJSONArray("subpods").getJSONObject(0).getString("plaintext");
                        break;
                    }
                }

                successListener.onResponse(result);
            } catch (JSONException e) {
                e.printStackTrace();
                failureListener.onResponse("Error parsing the result.");
            }
        }, error -> failureListener.onResponse("API request failed."));

        queue.add(request);
    }

    public static void checkConvergence(String function, double lowerLimit, double upperLimit, APIResponseListener successListener, APIResponseListener failureListener) {
        // Handling infinity in input for WolframAlpha's query language
        String lowerLimitStr = (lowerLimit == Double.POSITIVE_INFINITY) ? "infinity" :
                (lowerLimit == Double.NEGATIVE_INFINITY) ? "-infinity" :
                        String.valueOf(lowerLimit);

        String upperLimitStr = (upperLimit == Double.POSITIVE_INFINITY) ? "infinity" :
                (upperLimit == Double.NEGATIVE_INFINITY) ? "-infinity" :
                        String.valueOf(upperLimit);

        // Ensure the proper phrasing for the query
        String input = String.format("Does the integral from %s to %s of %s dx converge?", lowerLimitStr, upperLimitStr, function);

        String url = BASE_URL + "input=" + Uri.encode(input) + "&format=plaintext&output=JSON&appid=" + APP_ID;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray podsArray = jsonResponse.getJSONObject("queryresult").getJSONArray("pods");

                String result = "Not found";
                for (int i = 0; i < podsArray.length(); i++) {
                    JSONObject pod = podsArray.getJSONObject(i);
                    if ("Result".equals(pod.getString("title"))) {
                        result = pod.getJSONArray("subpods").getJSONObject(0).getString("plaintext");
                        break;
                    }
                }

                successListener.onResponse(result);
            } catch (JSONException e) {
                e.printStackTrace();
                failureListener.onResponse("Error parsing the result.");
            }
        }, error -> failureListener.onResponse("API request failed."));

        queue.add(request);
    }



}
