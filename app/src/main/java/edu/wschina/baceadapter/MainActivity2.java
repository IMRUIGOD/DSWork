package edu.wschina.baceadapter;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Cai> list;
    private SparseArray<Cai> sparseArray;
    public static TextView tv;
    private RelativeLayout rl;
    private ListView listView;
    private MyAdapter myAdapter;
    private SparAdapter sparAdapter;
    private View sheet;
    private Dialog dialog;
    private SQLiteDatabase db;
    private MyHelp myHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        list = Cai.getCaiArrayList();
        sparseArray = new SparseArray<>();
        myHelp = new MyHelp(this);
        db = myHelp.getWritableDatabase();

//        delete from TableName;  //清空数据
//        update sqlite_sequence SET seq = 0 where name ='TableName';//自增长ID为0

        db.execSQL("DELETE FROM dc");
        db.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE NAME = 'dc'");

        db.close();


        initView();

    }



    private void initView() {
        rl = findViewById(R.id.gwc);
        listView = findViewById(R.id.lv_cai);
        tv = findViewById(R.id.tv_countPrice);
        dialog = new Dialog(MainActivity2.this,R.style.Theme_AppCompat_Dialog);
        rl.setOnClickListener(this);
        myAdapter = new MyAdapter(this,list);
        listView.setAdapter(myAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gwc:
                showD();
            break;
            case R.id.btn_jz:
                db = myHelp.getWritableDatabase();
                for (int i = 0;i<sparseArray.size();i++){
                    ContentValues values = new ContentValues();
                    values.put("name",sparseArray.valueAt(i).name);
                    values.put("imgUrl",sparseArray.valueAt(i).imgUrl);
                    values.put("count",sparseArray.valueAt(i).count);
                    values.put("price",sparseArray.valueAt(i).price);

                    db.insert("dc",null,values);
                    Log.i("ok","成功");

                }
                db.close();
                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
            break;
            case R.id.btn_qk:
                qkGWC();
            break;

            default:
                break;
        }
    }

    public void add(Cai cai,boolean b){
        Cai temp = sparseArray.get(cai.id);
        if (temp == null){
            cai.count = 1;
            sparseArray.append(cai.id,cai);
        }else{
            temp.count++;
        }
        update(b);

    }

    public void rev(Cai cai,boolean b){
        Cai temp = sparseArray.get(cai.id);
        if (temp != null){
            if (temp.count < 2){
                sparseArray.remove(temp.id);
            }else {
                cai.count--;
            }
        }
        update(b);
    }

    private void update(boolean b) {
        int size = sparseArray.size();
        int count = 0;
        double countPrice = 0;
        for (int i = 0;i<size;i++){
            Cai cai = sparseArray.valueAt(i);
            count += cai.count;
            countPrice += cai.count * cai.price;
        }

        tv.setText("总价："+countPrice+"元");
        if (myAdapter != null && b){
            myAdapter.notifyDataSetChanged();
        }
        if (sparAdapter != null){
            sparAdapter.notifyDataSetChanged();
        }
        if (dialog.isShowing() && sparseArray.size() < 1){
            dialog.dismiss();
        }

    }

    public int getCountID(int id){
        Cai cai = sparseArray.get(id);
        if (cai == null){
            return 0;
        }
        return cai.count;
    }

    private void qkGWC() {
        sparseArray.clear();
        update(true);
    }
    private View setshowDialog() {
        View view = LayoutInflater.from(MainActivity2.this).inflate(R.layout.dialong, (ViewGroup) getWindow().getDecorView(),false);
        TextView tv_zj = view.findViewById(R.id.tv_cP);
        Button btn_qk = view.findViewById(R.id.btn_qk);
        Button btn_jz = view.findViewById(R.id.btn_jz);
        ListView lv = view.findViewById(R.id.lv_dian);
        btn_jz.setOnClickListener(this);
        btn_qk.setOnClickListener(this);
        sparAdapter = new SparAdapter(MainActivity2.this,sparseArray);
        lv.setAdapter(sparAdapter);
        return view;
    }

    private void showD(){

        if (sheet == null){
            sheet = setshowDialog();
        }
        if (dialog.isShowing()){
            dialog.dismiss();
        }else {
            if (sparseArray.size() != 0){
                dialog.setContentView(sheet);
                Window window = dialog.getWindow();
                window.getDecorView().setPadding(0,0,0,0);
                window.setBackgroundDrawableResource(android.R.color.transparent);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = 800;
                window.setAttributes(lp);
                window.setGravity(Gravity.BOTTOM);
                TextView tvcp = sheet.findViewById(R.id.tv_cP);
                tvcp.setText(MainActivity2.tv.getText().toString());
                dialog.show();
            }
        }




    }




}