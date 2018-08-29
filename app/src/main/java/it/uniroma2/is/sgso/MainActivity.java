package it.uniroma2.is.sgso;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    private GetData mAuthTask = null;
    MyArrayAdapter<String> adapter;

    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Myriad.ttf");
        titleText = (TextView) findViewById(R.id.titletv);
        titleText.setTypeface(myTypeface);

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setTypeface(myTypeface);

        Button getDataButton = (Button) findViewById(R.id.getdata_button);
        getDataButton.setTypeface(myTypeface);

        ListView lv = (ListView) findViewById(R.id.sondaggilv);

        adapter = new MyArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lv.setAdapter(adapter);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, LoginActivity.class);
                registerActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerActivity);
            }
        });

        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                GetData gd = new GetData(adapter);
                gd.execute();
            }
        });
    }

    public class GetData extends AsyncTask<Void, Void, JSONArray> {

        MyArrayAdapter<String> adapter;

        GetData(MyArrayAdapter<String> adapter) {
            this.adapter = adapter;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            String data = null;
            JSONArray ret = null;
            try {
                // Simulate network access.
                URL url = new URL("https://whispering-reaches-88332.herokuapp.com/prova/tasks");
                HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                data = br.readLine();
                ret = new JSONArray(data);

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return null;
            } catch (Exception e){
                return null;
            }
            return ret;
        }

        @Override
        protected void onPostExecute(final JSONArray ja) {
            mAuthTask = null;
            if(ja != null){
                try{
                    for(int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        String titolo = jo.get("titolo").toString();
                        String data = jo.get("data").toString();
                        String autore = jo.get("autore").toString();
                        String stato = jo.get("stato").toString();
                        adapter.add("Titolo: " + titolo + "\n" +
                                "Data: " + data + "\n" +
                                "Autore: " + autore + "\n" +
                                "Stato: " + stato);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e){

                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
