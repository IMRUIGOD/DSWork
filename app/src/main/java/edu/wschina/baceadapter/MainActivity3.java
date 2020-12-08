package edu.wschina.baceadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity3 extends AppCompatActivity {
    private ShowAdapter adapter;
    private SQLiteDatabase db;
    private MyHelp myHelp;
    private Cai cai;
    private TextView tv;
    private ListView lv;
    private TextView cop;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        tv = findViewById(R.id.tv_dm);
        lv = findViewById(R.id.lsit_OKcai);
        cop = findViewById(R.id.countP);
        button = findViewById(R.id.btn_zf);
        myHelp = new MyHelp(this);

        timerStart();

        ArrayList<Cai> list = new ArrayList();
        db = myHelp.getReadableDatabase();

        Cursor cursor = db.query("dc",null,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
            cai = new Cai(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
            list.add(cai);
            while (cursor.moveToNext()){
                cai = new Cai(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
                Log.i("count",cursor.getInt(3)+"盘");
                Log.i("count",cursor.getInt(4)+"元");
                list.add(cai);
            }
            cursor.close();
            db.close();
        }

        int cP = 0;

        for (int j = 0;j<list.size();j++){
            cP += list.get(j).count * list.get(j).price;
        }


        adapter = new ShowAdapter(MainActivity3.this,list);
        lv.setAdapter(adapter);

        cop.setText("总价："+cP+"元");


        JSONArray jsonArray = new JSONArray();
        for (int i = 0;i<list.size();i++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("food_id",list.get(i).id);
                jsonObject.put("count",list.get(i).count);
                jsonArray.put(i,jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.i("json",jsonArray.toString());
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        final RequestBody body = RequestBody.create(JSON,jsonArray.toString());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = getSharedPreferences("pd_sp",MODE_PRIVATE);
                SharedPreferences.Editor editor = sh.edit();
                editor.putString("flag","1");
                editor.commit();

                Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
                startActivity(intent);


//                String url = "http://example.com/payment";
//                Call call = new OkHttpClient().newCall(new Request.Builder().post(body).url(url).build());
//                call.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.body().string());
//                            if (jsonObject != null){
//                                if (jsonObject.optString("msg").equals("success")){
//
//                                }else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(MainActivity3.this, "支付失败", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });

            }
        });
    }


    private CountDownTimer timer = new CountDownTimer(60*1000,1000) {
        @Override
        //倒计时中调用
        public void onTick(long millisUntilFinished) {
            tv.setText(millisUntilFinished/1000+"");
        }

        @Override
        //倒计时结束调用
        public void onFinish() {
            Intent intent = new Intent(MainActivity3.this,MainActivity2.class);
            startActivity(intent);
        }
    };
    public void timerStart() {
        //调用倒计时
        timer.start();
    }

}