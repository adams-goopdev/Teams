package edu.ags.teams;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestClient {

    private  static final String TAG = "RestClient";

    public static void executeGetRequest(String url, Context context, VolleyCallback volleyCallback){

        Log.d(TAG, "executeGetRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Team> teams = new ArrayList<>();

        try{
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            //Process the JSON into an ArrayList<Team>
                            try {
                                JSONArray items = new JSONArray(response);

                                for(int i = 0; i < items.length(); i++){
                                    JSONObject object = items.getJSONObject(i);
                                    Team team = new Team();
                                    team.setId(object.getInt("id"));
                                    team.setName(object.getString("name"));
                                    team.setCity(object.getString("city"));
                                    team.setRating((float) object.getDouble("rating"));
                                    team.setCellNumber(object.getString("cellNumber"));
                                    team.setFavorite(object.getBoolean("isFavorite"));
                                    teams.add(team);

                                }
                                volleyCallback.onSuccess(teams);
                            }
                            catch (JSONException e)
                            {
                                Log.d(TAG, "onResponse: "+ e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        }
            });

            //THE MOST IMPORTANT LINE HERE.
            queue.add(stringRequest);

        }
        catch (Exception e)
        {
            Log.d(TAG, "executeGetRequest: " + e.getMessage());
        }

    }
    public static void executeGetOneRequest(String url, Context context, VolleyCallback volleyCallback){

        Log.d(TAG, "executeGetOneRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Team> teams = new ArrayList<>();

        try{
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            //Process the JSON into an ArrayList<Team>
                            try {

                                JSONObject object = new JSONObject(response);
                                Team team = new Team();
                                team.setId(object.getInt("id"));
                                team.setName(object.getString("name"));
                                team.setCity(object.getString("city"));
                                team.setRating((float) object.getDouble("rating"));
                                team.setCellNumber(object.getString("cellNumber"));
                                team.setFavorite(object.getBoolean("isFavorite"));
                                teams.add(team);

                                volleyCallback.onSuccess(teams);
                            }
                            catch (JSONException e)
                            {
                                Log.d(TAG, "onResponse: "+ e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }
            });

            //THE MOST IMPORTANT LINE HERE.
            queue.add(stringRequest);

        }
        catch (Exception e)
        {
            Log.d(TAG, "executeGetOneRequest: " + e.getMessage());
        }

    }

}
