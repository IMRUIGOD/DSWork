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

public class ShowAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Cai> list;
    private Context context;

    public ShowAdapter(Context c, ArrayList<Cai> list) {
        this.list = list;
        this.context = c;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list == null) {
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
        ShowAdapter.Holder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_item, null, false);
            holder = new ShowAdapter.Holder(view);
            view.setTag(holder);
        } else {
            holder = (ShowAdapter.Holder) view.getTag();
        }
        Cai cai = list.get(position);
        holder.bindData(cai);
        return view;
    }

    private class Holder {
        private ImageView imageView;
        private TextView tv_cm, tv_jg, count;
        private Cai cai;

        public Holder(View view) {
            imageView = view.findViewById(R.id.img_c);
            tv_cm = view.findViewById(R.id.cm);
            tv_jg = view.findViewById(R.id.jg);
            count = view.findViewById(R.id.sl);

        }

        public void bindData(Cai ca) {
            this.cai = ca;
            tv_cm.setText(cai.name);
            tv_jg.setText(cai.price + "元");
            count.setText(cai.count + "");

            Call call = new OkHttpClient().newCall(new Request.Builder().url(cai.imgUrl).build());
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == 3) {
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
                    message.what = 3;
                    handler.sendMessage(message);
                }
            });
        }
    }
}
