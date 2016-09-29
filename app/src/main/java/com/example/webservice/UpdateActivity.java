package com.example.webservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class UpdateActivity extends AppCompatActivity {

    Button button;
    EditText action;
    String name;

    private String actionId;
    private String actionName;
    private static final String TAG_ACNAME = "action_name";
    private static final String urldef = "http://104.199.194.215:3000/action_lists";
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        actionId = getIntent().getStringExtra("actionId");
        actionName = getIntent().getStringExtra("actionName");
        Toast.makeText(getBaseContext(), this.actionId+ " " +this.actionName, Toast.LENGTH_LONG).show();

        action = (EditText) findViewById(R.id.editText);
        action.setText(actionName);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                action = (EditText) findViewById(R.id.editText);
                name = action.getText().toString();
                Log.d("step: ", " onPreExecute " + name);
                try{
                    JSONObject jsonObject = new JSONObject();
                    JSONObject param = new JSONObject();
                    param.put(TAG_ACNAME,name);

                    jsonObject.put("data",param);
                    String url = urldef+"/"+actionId;
                    Log.d("REspone", "fuxkkkkkkkkk url =" + url);

                    put(url, param.toString(), new Callback() {
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
                setResult(RESULT_OK, null);
                finish();

            }
        });
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    Call put(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Log.d("REspone", "pute pute" +json );

        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
//                .addHeader("Authorization","key=YourApiKey")
                .url(url)
                .put(body)
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
