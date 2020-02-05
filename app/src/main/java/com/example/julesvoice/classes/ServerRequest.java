package com.example.julesvoice.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerRequest
{
    private Context _context;
    private PingAndInternetListener callback;
    private int _id;

    public ServerRequest(Context context, PingAndInternetListener cb){
        callback = cb;
        _context = context;
    }

    public ServerRequest(PingAndInternetListener cb, Context context, int id)
    {
        callback = cb;
        _context = context;
        _id = id;
    }

    public void getData(final ArrayList<ArgumentRequest> parametre, String url)
    {
        final ProgressDialog dialog = ProgressDialog.show(_context, null, "César traite actuellement votre voix ...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                if(response != null && !response.trim().isEmpty())
                {
                    Toast.makeText(_context, response, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(_context, "Transcription impossible, veuillez recommencer.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                if(isNetworkAvailable())
                {
                    callback.onPingFailed();
                }
                else
                {
                    callback.noInternetConnexion();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                for(ArgumentRequest r : parametre)
                {
                    params.put(r.getKey(), r.getValeur());
                    //params.put("wav", wavInBase64);
                }

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        requestQueue.add(stringRequest);
    }

    public void ping(String urlPing)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPing, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                callback.onPingCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onPingFailed();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(_context);
        requestQueue.add(stringRequest);
    }

    // https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
