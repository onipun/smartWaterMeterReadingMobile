package com.example.meterreading.data;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meterreading.data.model.LoggedInUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    Context ctx;
    Result<LoggedInUser> result;

    private static final String TAG = LoginDataSource.class.getSimpleName();
    // Instantiate the RequestQueue.
    RequestQueue queue;
    StringRequest stringRequest;

    //for emulator debugging
    //String url ="http://10.0.2.2:8080/user/login-test";

    //for mobile debugging
    String url ="http://192.168.43.173:8080/user/login-test";

    public LoginDataSource(Context ctx) {
        this.ctx = ctx;
        queue = Volley.newRequestQueue(ctx);
    }

    // "Result<LoggedInUser>" java generic type of class
    public Result<LoggedInUser> login(final String username, final String password) {


        try {
            // TODO: handle loggedInUser authentication

            // Request a string response from the provided URL.
            stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
//                            Toast.makeText(ctx, response.substring(0,500), Toast.LENGTH_SHORT).show();
                            LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), username);
                            result = new Result.Success<>(fakeUser);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: ",error );
                    result = new Result.Error(error);

                }

            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = username+":"+password;
                    String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Authorization", auth);
                    headers.put("cache-control", "no-cache");
                    return headers;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);


        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }

        return result;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
