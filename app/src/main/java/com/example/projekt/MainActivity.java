package com.example.projekt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Companies> lukasCompanies=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<Companies> adapter=new ArrayAdapter<Companies>(this,R.layout.list_item_textview,R.id.my_item_textview, lukasCompanies);

        ListView my_listView=(ListView) findViewById(R.id.my_listview);

        my_listView.setAdapter(adapter);

        my_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Toast.makeText(getApplicationContext(),lukasCompanies.get(position).info(),Toast.LENGTH_LONG).show();

            }
        });

        new FetchData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about) {
            //När man trycker på about knappen utförs både en refresh på sidan för listan och information om appens visas
            lukasCompanies.clear();
            Toast.makeText(getApplicationContext(),"This app is about the companies that had the best revenue in Sweden in the year 2016 \n \n The companies rating is based on their postition in the list",Toast.LENGTH_LONG).show();
            new FetchData().execute();
            return true;}

        return super.onOptionsItemSelected(item);
    }

    private class FetchData extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {

                URL url = new URL("http://wwwlab.iit.his.se/a18lukto/Android%20Projekt/Projetk.php");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);

            Log.d("Mountain",o);
            try {


                JSONArray jsonForetag = new JSONArray(o);
                Log.d("Foretag",jsonForetag.get(0).toString());
                //Skapar foretag utifrån datan som json hämtar
                for (int i = 0; i < jsonForetag.length(); i++) {
                    JSONObject fore = jsonForetag.getJSONObject(i);
                    String foreName = fore.getString("name");
                    String foreLocation = fore.getString("location");
                    int foreAnstallda = fore.getInt("size");
                    int foreOmsattning = fore.getInt("cost");
                    String foreOmrade = fore.getString("company");
                    Log.d("a18lukto", foreName+" "+foreLocation+" "+foreAnstallda);

                    lukasCompanies.add(new Companies(foreName,foreLocation,foreAnstallda,foreOmsattning,foreOmrade));


                }

                ArrayAdapter<Companies> adapter=new ArrayAdapter<Companies>(getApplicationContext(),R.layout.list_item_textview,R.id.my_item_textview, lukasCompanies);

                ListView my_listView=(ListView) findViewById(R.id.my_listview);

                my_listView.setAdapter(adapter);


            } catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());
            }
        }
    }
}
