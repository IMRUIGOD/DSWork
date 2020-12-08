package edu.wschina.baceadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private String[] zzlx = {"小桌（2座）", "中桌（4座）", "大桌（8座）", "房间（12座）"};
    private String[] xzrq = new String[7];
    private String[] sjd = {"08:00-10:00", "10:00-12:00", "12:00-14:00", "14:00-16:00", "16:00-18:00", "18:00-20:00",};
    private YYDZ yydz = new YYDZ();
    private Button button;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sh = getSharedPreferences("pd_sp",MODE_PRIVATE);

        String flag = sh.getString("flag","");
        Log.i("fl",flag);
        if (flag.equals("1")){
            Intent intent = new Intent(MainActivity.this,MainActivity4.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }else{
            btn1 = findViewById(R.id.button1);
            btn2 = findViewById(R.id.button2);
            btn3 = findViewById(R.id.button3);
            button = findViewById(R.id.tijiao);

            int today = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
            for (int i = 0;i<xzrq.length;i++){
                xzrq[i] = (today+i)+"日";
            }

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(btn1,zzlx);
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(btn2,xzrq);
                }
            });
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(btn3,sjd);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sp = getSharedPreferences("info_sp",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("cz",yydz.getCz());
                    editor.putString("rq",yydz.getRq());
                    editor.putString("sj",yydz.getSjd());
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(intent);
                }
            });


        }


    }

    private void showDialog(final Button btn, final String[] s) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(btn.getText().toString());
        dialog.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                btn.setText(s[position]);
                if (btn1.equals(btn)) {
                    yydz.setCz(s[position]);
                } else if (btn2.equals(btn)) {
                    yydz.setRq(s[position]);
                } else if (btn3.equals(btn)) {
                    yydz.setSjd(s[position]);
                }
            }
        });
        dialog.show();
    }
}