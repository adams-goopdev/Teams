package edu.ags.teams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RestClient {

    private static final String TAG = "RestClient";

    public static void executeGetRequest(String url, Context context, VolleyCallback volleyCallback) {

        Log.d(TAG, "executeGetRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Team> teams = new ArrayList<>();

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            //Process the JSON into an ArrayList<Team>
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);
                                    Team team = new Team();
                                    team.setId(object.getInt("id"));
                                    team.setName(object.getString("name"));
                                    team.setCity(object.getString("city"));
                                    team.setRating((float) object.getDouble("rating"));
                                    team.setCellNumber(object.getString("cellNumber"));
                                    team.setFavorite(object.getBoolean("isFavorite"));
                                    team.setLatitude(object.getDouble("latitude"));
                                    team.setLongitude(object.getDouble("longitude"));

                                    //Get and process the photo
                                    String jsonPhoto = object.getString("photo");
                                    if (jsonPhoto != "null" && !jsonPhoto.equals(null)) {
                                        Log.d(TAG, "onResponse: " + jsonPhoto);
                                        byte[] bytePhoto = null;
                                        bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                        team.setPhoto(bmp);

                                    }

                                    teams.add(team);

                                }
                                volleyCallback.onSuccess(teams);
                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: " + e.getMessage());
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

        } catch (Exception e) {
            Log.d(TAG, "executeGetRequest: " + e.getMessage());
        }

    }

    public static void executeGetOneRequest(String url, Context context, VolleyCallback volleyCallback) {

        Log.d(TAG, "executeGetOneRequest: START");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Team> teams = new ArrayList<>();

        try {
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
                                team.setLatitude(object.getDouble("latitude"));
                                team.setLongitude(object.getDouble("longitude"));



                                //Get and process the photo
                                String jsonPhoto = object.getString("photo");
                                if (jsonPhoto != "null" && !jsonPhoto.equals(null)) {
                                    Log.d(TAG, "onResponse: " + jsonPhoto);
                                    byte[] bytePhoto = null;
                                    bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                    Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                    team.setPhoto(bmp);
                                }

                                teams.add(team);

                                volleyCallback.onSuccess(teams);
                            } catch (JSONException e) {
                                Log.d(TAG, "onResponse: " + e.getMessage());
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

        } catch (Exception e) {
            Log.d(TAG, "executeGetOneRequest: " + e.getMessage());
        }

    }

    public static void executePostRequest(Team team, String url, Context context, VolleyCallback volleyCallback) {
        try {
            executeRequest(team, url, context, volleyCallback, Request.Method.POST);

        } catch (Exception e) {
            Log.d(TAG, "executePostRequest: " + e.getMessage());
        }

    }

    public static void executePutRequest(Team team, String url, Context context, VolleyCallback volleyCallback) {
        try {
            executeRequest(team, url, context, volleyCallback, Request.Method.PUT);

        } catch (Exception e) {
            Log.d(TAG, "executePostRequest: " + e.getMessage());
        }

    }

    public static void executeDeleteRequest(Team team, String url, Context context, VolleyCallback volleyCallback) {
        try {
            executeRequest(team, url, context, volleyCallback, Request.Method.DELETE);

        } catch (Exception e) {
            Log.d(TAG, "executePostRequest: " + e.getMessage());
        }

    }

    private static void executeRequest(Team team, String url, Context context, VolleyCallback volleyCallback, int method) {
        Log.d(TAG, "executeRequest: " + url);
        try {
            RequestQueue queue = Volley.newRequestQueue(context);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", team.getId());
            jsonObject.put("name", team.getName());
            jsonObject.put("city", team.getCity());
            jsonObject.put("rating", team.getRating());
            jsonObject.put("cellNumber", team.getCellNumber());
            jsonObject.put("isFavorite", team.getFavorite());
            jsonObject.put("latitude", team.getLatitude());
            jsonObject.put("longitude", team.getLongitude());


            //Process the photo
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = Bitmap.createScaledBitmap(team.getPhoto(), 200, 200, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String jsonPhoto = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            jsonObject.put("photo", jsonPhoto);

            final String requestBody = jsonObject.toString();
            Log.d(TAG, "executeRequest: " + requestBody);


            JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                public byte[] getBody() {
                    Log.i(TAG, "getBody: " + jsonObject.toString());
                    return jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                }

            };
            //Important line
            queue.add(request);

        } catch (Exception e) {
            Log.d(TAG, "executeRequest: " + e.getMessage());
        }
    }


}
