package abercrombiettd.com.abf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PromotionActivity extends Activity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        new MyAsyncTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_promotion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(PromotionActivity.this);
        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {

            String url_select = "https://www.abercrombie.com/anf/nativeapp/Feeds/promotions.json";

            ArrayList<Promotions> param = new ArrayList<Promotions>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpget = new HttpGet(url_select);

                HttpResponse httpResponse = httpClient.execute(httpget);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
              //  Log.e("UnsupportedEncodingException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();
                Log.i("RES",""+result);

            } catch (Exception e) {
                //Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }
            return null;
        } // protected Void doInBackground(String... params)

        protected void onPreExecute() {

            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask.this.cancel(true);
                }
            });
        }
        protected void onPostExecute(Void v) {
            //parse JSON data
            /*Gson gson = new Gson();
            PromotionFeedPojo result1 = gson.fromJson(result, PromotionFeedPojo.class);*/

            try {


                List<Promotions> promotionslist=new ArrayList<Promotions>();
                String target;
                String title;
                JSONObject upper=new JSONObject(result);
                JSONArray promotions=upper.getJSONArray("promotions");
                for(int i=0;i<promotions.length();i++){
                    Promotions promotionsObject = new Promotions();
                    Buttons buttonsObject=new Buttons();
                  JSONObject object=promotions.getJSONObject(i);
                    if(i==1){
                        JSONArray buttons=object.getJSONArray("button");
                        for(int j=0;j<buttons.length();j++){
                            buttonsObject.setTarget(buttons.getJSONObject(j).getString("target"));
                            buttonsObject.setTitle(buttons.getJSONObject(j).getString("title"));
                        promotionsObject.setButtons(buttonsObject);}
                        promotionsObject.setDescription(object.getString("description"));
                        if(object.has("footer")){
                            promotionsObject.setFooter(object.getString("footer"));}
                        promotionsObject.setImage(object.getString("image"));
                        promotionsObject.setTitle1(object.getString("title"));
                    }
                     else {
                        JSONObject button=object.getJSONObject("button");
                        buttonsObject.setTarget(button.getString("target"));
                        buttonsObject.setTitle(button.getString("title"));
                        promotionsObject.setButtons(buttonsObject);
                    }
                    promotionsObject.setDescription(object.getString("description"));
                    if(object.has("footer")){
                    promotionsObject.setFooter(object.getString("footer"));}
                    promotionsObject.setImage(object.getString("image"));
                    promotionsObject.setTitle1(object.getString("title"));
                    promotionslist.add(promotionsObject);
                }
                lv=(ListView) findViewById(R.id.listView);
                lv.setAdapter(new CustomAdapter(PromotionActivity.this,promotionslist));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.i("DATA", "" + result1.getDescription());
            this.progressDialog.dismiss();
        } // protected void onPostExecute(Void v)
    } //class MyAsyncTas
}
