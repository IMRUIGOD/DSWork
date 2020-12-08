package edu.wschina.baceadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity {
    private ShowAdapter adapter;
    private SQLiteDatabase db;
    private MyHelp myHelp;
    private Cai cai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        myHelp = new MyHelp(MainActivity4.this);
        TextView cz = findViewById(R.id.tv_4_cz);
        TextView rq = findViewById(R.id.tv_4_rq);
        TextView sj = findViewById(R.id.tv_4_sj);
        TextView cP = findViewById(R.id.tv_4_countP);
        ListView lv = findViewById(R.id.lsit_4_OKcai);

        SharedPreferences sp = getSharedPreferences("info_sp",MODE_PRIVATE);
        cz.setText("餐桌类型："+sp.getString("cz",""));
        rq.setText("日期时间："+sp.getString("rq",""));
        sj.setText("时间段："+sp.getString("sj",""));


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

        int ca = 0;

        for (int j = 0;j<list.size();j++){
            ca += list.get(j).count * list.get(j).price;
        }


        adapter = new ShowAdapter(MainActivity4.this,list);
        lv.setAdapter(adapter);

        cP.setText("总价："+ca+"元");


    }
}