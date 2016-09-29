package com.example.webservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CreateActivity extends AppCompatActivity {
    Button button;
    EditText action;
    String name;

    private static final String TAG_ACNAME = "action_name";
    private static String urldef = "http://104.199.194.215:3000/action_lists";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                action = (EditText) findViewById(R.id.editText);
                name = action.getText().toString();
                Log.d("step: ", " onPreExecute " + name);
//                AsyncT asyncT = new AsyncT();
//                asyncT.execute();

                try{
                    JSONObject jsonObject = new JSONObject();
                    JSONObject param = new JSONObject();
                    param.put(TAG_ACNAME,name);
//                    param.put("Hours","12:50");

                    jsonObject.put("data",param);
//                    jsonObject.put("to", "TokenOfTheDevice");
                    post("http://104.199.194.215:3000/action_lists", param.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // Something went wrong
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Log.d("REspone", "respo" +responseStr);
                                // Do what you want to do with the response.
                            } else {
                                // Request not successful
                                Log.d("REspone", "fuxkkkkkkkkk" );

                            }
                        }
                    });

                }
                catch (JSONException e){

                }
                setResult(RESULT_OK, null);
                finish();

            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
//                .addHeader("Authorization","key=YourApiKey")
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    //    private class SaveStudents extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            // Creating service handler class instance
//            try {
//                JSONObject jsonParam = new JSONObject();
//                jsonParam.put(TAG_ACNAME, name);
//                Log.d("jsonParam: ", " : " + jsonParam);
//                Log.d("step: ", " doInBackground ");
//                String response = new WebRequest().post().to(urldef).executeSync();
//
//                // Making a request to url and getting response
////            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);
//
//                Log.d("Response: ", "> " + response);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//
//        }
//    }
}