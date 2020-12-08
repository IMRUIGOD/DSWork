package edu.wschina.baceadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SparAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private SparseArray<Cai> list;
    private MainActivity2 context;

    public SparAdapter(MainActivity2 c , SparseArray<Cai> list) {
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
        SViewHolder sviewHolder = null;
        if (view == null){
            view = inflater.inflate(R.layout.layout_cai_item,null,false);
            sviewHolder = new SViewHolder(view);
            view.setTag(sviewHolder);
        }else{
            sviewHolder = (SViewHolder) view.getTag();
        }
        Cai cai = list.valueAt(position);
        sviewHolder.bindData(cai);
        return view;
    }

    private class SViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView tv_cm,tv_jg,count,tv_zj;
        private Button btn_add,btn_rev;
        private Cai cai;

        public SViewHolder(View view){
            imageView = view.findViewById(R.id.img_c);
            tv_cm = view.findViewById(R.id.cm);
            tv_jg = view.findViewById(R.id.jg);
            count = view.findViewById(R.id.sl);
            btn_add = view.findViewById(R.id.add);
            btn_rev = view.findViewById(R.id.rev);

        }

        public void bindData(Cai ca) {
            this.cai = ca;
            tv_cm.setText(cai.name);
            tv_jg.setText(cai.count*cai.price+"元");
            count.setText(cai.count+"");
            btn_rev.setOnClickListener(this);
            btn_add.setOnClickListener(this);

            Call call = new OkHttpClient().newCall(new Request.Builder().url(cai.imgUrl).build());
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == 2) {
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
                    message.what = 2;
                    handler.sendMessage(message);
                }
            });
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add:
                    context.add(cai,true);
                    break;
                case R.id.rev:
                    context.rev(cai,true);
                    break;
                default:
                    break;
            }
        }
    }
}
