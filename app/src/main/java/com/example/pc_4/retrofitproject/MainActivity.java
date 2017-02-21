package com.example.pc_4.retrofitproject;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText text;
    private TextView translated;
    private Button translateBtn;

    private final String URL = "https://translate.yandex.net";
    private final String KEY = "trnsl.1.1.20170221T140211Z.b7f8bd9ea2ba6d48.727e46e53629c46bf15f563e5b7a46caf15f05b4";

    private Gson gson = new GsonBuilder().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private Link intf = retrofit.create(Link.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (EditText) findViewById(R.id.text);
        translated = (TextView) findViewById(R.id.translated);
        translateBtn = (Button) findViewById(R.id.btnTranslate);

        //Снимает ограничения с потока главного, не рекомендуется
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> mapJson = new HashMap<String, String>();
                mapJson.put("key", KEY);
                mapJson.put("text", text.getText().toString());
                mapJson.put("lang", "en-ru");

                Call<Object> call = intf.translate(mapJson);

                try {
                    Response <Object> response = call.execute();

                    Map<String,String> map = gson.fromJson(response.body().toString(), Map.class);

                    for(Map.Entry e : map.entrySet()){
                        if(e.getKey().equals("text")){
                            translated.setText(e.getValue().toString());
                        }
                        System.out.println(e.getKey() + " " + e.getValue());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
