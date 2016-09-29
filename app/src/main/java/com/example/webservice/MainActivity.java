package com.example.webservice;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends ListActivity {
    // URL to get contacts JSON
//    private static String url = "https://raw.githubusercontent.com/mobilesiri/JSON-Parsing-in-Android/master/index.html";
    private static String url = "http://104.199.194.215:3000/action_lists.json";

    OkHttpClient client = new OkHttpClient();

    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String TAG_ACNAME = "action_name";
    private static final String TAG_CREATED = "created_at";
    private static final String TAG_UPDATED = "updated_at";
    private static final String TAG_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Calling async task to get json
        new GetStudents().execute();

        Button button = (Button) findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 1);

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
    /** Called when the user touches the button */
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetStudents extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> studentList;
        ProgressDialog pDialog;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            Log.d("step: ", " onPreExecute ");

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance

            Log.d("step: ", " doInBackground ");
            String response = new WebRequest().get().to(url).executeSync();

            // Making a request to url and getting response
//            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + response);

            studentList = ParseJSON(response);

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            Log.d("step: ", " onPostExecute ");

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

//            createMenu Strat
            SwipeMenuCreator swipeCreator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    // create "open" item
                    SwipeMenuItem delItem = new SwipeMenuItem(
                            getApplicationContext());
                    delItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                            0xCE)));
                    delItem.setTitleSize(20);
                    delItem.setBackground(R.color.colorAccent);
                    delItem.setWidth((250));
                    delItem.setTitleColor(Color.WHITE);
                    delItem.setTitle("Delete");
                    menu.addMenuItem(delItem);
                }
            };
            // createMenu end
            // set data start
            final SimpleAdapter adapter = new SimpleAdapter(

                    MainActivity.this, studentList,
                    R.layout.list_item, new String[]{TAG_ACNAME, TAG_ID,
                    TAG_URL}, new int[]{R.id.name,
                    R.id.id, R.id.url});
            setListAdapter(adapter);

            // set data end
            final SwipeMenuListView list2 = (SwipeMenuListView) getListView();

            Log.d("REspone", "fuxkkkkkkkkk" + list2.getCount() );

            list2.setMenuCreator(swipeCreator);

            list2.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            list2.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    HashMap<String,String> map = (HashMap<String,String>) list2.getItemAtPosition(position);

                    String acid = map.get(TAG_ID).toString();
                    Log.d("REspone", "fuxkkkkkkkkk" + map.get(TAG_ID));

                    switch (index) {
                        case 0:
                            // delete
                            try{

                                JSONObject jsonObject = new JSONObject();
                                JSONObject param = new JSONObject();

                                Toast.makeText(getApplicationContext(), "Delete Index: " + index + " " + "Position: " + position + " id "+ list2.getCount(), Toast.LENGTH_LONG).show();
                                jsonObject.put("data",param);
                                String url = "http://104.199.194.215:3000/action_lists/"+acid;


                                delete(url, param.toString(), new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("REspone", "fuxkkkkkkkkk onFailure");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            String responseStr = response.body().string();
                                            Log.d("REspone", "respo" +responseStr);
                                            // Do what you want to do with the response.
                                        } else {
                                            // Request not successful
                                            Log.d("REspone", "fuxkkkkkkkkk onResponse" );

                                        }
                                    }
                                });

                            }
                            catch (JSONException e){

                            }
                            break;
                    }
                    // false : close the menu; true : not close the menu
                    finish();
                    startActivity(getIntent());
//                    onPostExecute(result);
                    return false;
                }
            });

            list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView name = (TextView) view.findViewById(R.id.name);
                    TextView actionid = (TextView) view.findViewById(R.id.id);
                    String itemId = actionid.getText().toString();
                    String itemName = name.getText().toString();


//                    Toast.makeText(getBaseContext(), itemId+itemName, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                    intent.putExtra("actionId",itemId);
                    intent.putExtra("actionName",itemName);

                    startActivityForResult(intent, 0);

                }
            });





        }

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
                Log.d("ParseJSON: ", " ArrayList ");

                JSONArray jsonarray = new JSONArray(json);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject c = jsonarray.getJSONObject(i);
                    JSONObject jsonobj = jsonarray.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String action_name = c.getString(TAG_ACNAME);
                    String created_at = c.getString(TAG_CREATED);
                    String updated_at = c.getString(TAG_UPDATED);
                    String url = c.getString(TAG_URL);


                    HashMap<String, String> student = new HashMap<String, String>();

//                    // adding each child node to HashMap key => value
                    student.put(TAG_ID, id);
                    student.put(TAG_ACNAME, action_name);
                    student.put(TAG_URL, url);
                    studentList.add(student);

                }
                Log.d("ParseJSON: ", " return studentList ");

                return studentList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    Call delete(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Log.d("REspone", "pute pute" +json );

        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
//                .addHeader("Authorization","key=YourApiKey")
                .url(url)
                .delete()
                .build();

        Log.d("REspone", "request" +request );
        Log.d("REspone", "headers" +request.headers() );
        Log.d("REspone", "url" +request.url() );
        Log.d("REspone", "body" +request.body() );

        Call call = client.newCall(request);
        Log.d("REspone", "callback" +callback );

        call.enqueue(callback);
        return call;
    }
}
