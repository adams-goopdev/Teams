package edu.ags.teams;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.DoubleSummaryStatistics;

public class InternetCheck extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = "myDebug";

    private Consumer consumer;

    public interface Consumer {
        void accept(Boolean b);
    }
    public InternetCheck(Consumer consumer)
    {
        this.consumer = consumer;
        execute();
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        try
        {
            Log.d(TAG, "doInBackground: First step");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("151.101.193.67",80), 5000);
            socket.close();
            return true;
        }
        catch (IOException e)
        {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            return false;
        }
    }

    protected void onProgressUpdate(Void... progress) {

    }

    @Override
    protected void onPostExecute(Boolean b) {

        consumer.accept(b);
    }
}
