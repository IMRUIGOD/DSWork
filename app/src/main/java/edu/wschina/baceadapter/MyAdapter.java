package edu.wschina.baceadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Cai> list;
    private MainActivity2 context;

    public MyAdapter(MainActivity2 c , ArrayList<Cai> list) {
        this.list = list;
        this.context = c;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null){
            view = inflater.inflate(R.layout.layout_cai_item,parent,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        Cai cai = list.get(position);
        viewHolder.bindData(cai);
        return view;
    }

    private class ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView tv_cm,tv_jg,count;
        private Button btn_add,btn_rev;
        private Cai cai;

        public ViewHolder(View view){
            imageView = view.findViewById(R.id.img_c);
            tv_cm = view.findViewById(R.id.cm);
            tv_jg = view.findViewById(R.id.jg);
            count = view.findViewById(R.id.sl);
            btn_add = view.findViewById(R.id.add);
            btn_rev = view.findViewById(R.id.rev);
        }

        public void bindData(Cai cai) {
            this.cai = cai;
            Call call = new OkHttpClient().newCall(new Request.Builder().url(cai.imgUrl).build());
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == 1) {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        imageView.setImageBitmap(bitmap);
                    }
                }
            };
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.obj = bitmap;
                    message.what = 1;
                    handler.sendMessage(message);
                }
            });

            tv_cm.setText(cai.name);
            tv_jg.setText(cai.price+"元");
            cai.count = context.getCountID(cai.id);
            count.setText(cai.count+"");
            btn_rev.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            if (cai.count<1){
                count.setVisibility(View.GONE);
                btn_rev.setVisibility(View.GONE);
            }else {
                count.setVisibility(View.VISIBLE);
                btn_rev.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onClick(View v) {
            MainActivity2 ct = context;
            switch (v.getId()){
                case R.id.add: {
                    int c = ct.getCountID(cai.id);
                    if (c < 1) {
                        btn_rev.setVisibility(View.VISIBLE);
                        count.setVisibility(View.VISIBLE);
                    }
                    ct.add(cai, false);
                    c++;
                    count.setText(String.valueOf(c));
                }
                    break;
                case R.id.rev: {
                    int c = ct.getCountID(cai.id);
                    if (c < 2) {
                        btn_rev.setVisibility(View.GONE);
                        count.setVisibility(View.GONE);
                    }
                    ct.rev(cai, false);
                    c--;
                    count.setText(String.valueOf(c));
                }
                    break;
                default:
                    break;
            }

        }


    }
}
