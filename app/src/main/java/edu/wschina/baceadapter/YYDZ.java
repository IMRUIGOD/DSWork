package edu.wschina.baceadapter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class YYDZ {
    private String cz;
    private String rq;
    private String sjd;
    private ArrayList<Cai> list;
    private int zj;

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getSjd() {
        return sjd;
    }

    public void setSjd(String sjd) {
        this.sjd = sjd;
    }

    public ArrayList<Cai> getList() {
        return list;
    }

    public void setList(ArrayList<Cai> list) {
        this.list = list;
    }

    public int getZj() {
        return zj;
    }

    public void setZj(int zj) {
        this.zj = zj;
    }
}

class Cai {
    public int id;
    public String name;
    public String imgUrl;
    public int count;
    public int price;


    public Cai(int id, String name, String imgUrl, int count, int price) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.count = count;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cai{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    private static ArrayList<Cai> caiArrayList;

    public static ArrayList<Cai> getCaiArrayList() {
        if (caiArrayList == null){
            initData();
        }
        return caiArrayList;
    }

    private static void initData(){
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        String url = "http://192.168.1.106:8080/api/v2/";
        final String json = "{" +
                "    \"code\":200," +
                "    \"msg\":\"success\"," +
                "    \"data\":[" +
                "{" +
                "\"id\":1," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"鱼香肉丝\"," +
                "\"price\":35" +
                "}," +
                "{" +
                "\"id\":2," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"水煮肉片\"," +
                "\"price\":60" +
                "}," +
                "{" +
                "\"id\":3," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"毛血旺\"," +
                "\"price\":40" +
                "}," +
                "{" +
                "\"id\":4," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"剁椒鱼头\",\n" +
                "\"price\":57" +
                "}," +
                "{" +
                "\"id\":5," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"烤羊腿\"," +
                "\"price\":32" +
                "}," +
                "{" +
                "\"id\":6," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"西红柿炖牛腩\"," +
                "\"price\":34" +
                "}," +
                "{" +
                "\"id\":7," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"孜然牛肉\"," +
                "\"price\":45" +
                "}," +
                "{" +
                "\"id\":8," +
                "\"image_url\":\"https://cp1.douguo.com/upload/caiku/e/e/1/600_eeda516778aee8b34289e88cae1bc9d1.jpg\", \"name\":\"夫妻肺片\"," +
                "\"price\":56" +
                "}" +
                "] }";


                // 判断获取的数据

        // 填充组件
        try {
            JSONObject json1 = new JSONObject(json);
            String data = json1.get("data").toString();
            JSONArray array = new JSONArray(data);
            caiArrayList = new ArrayList<>();
            for (int i = 0;i<array.length();i++){
                JSONObject cai1 = new JSONObject(array.get(i).toString());
                Cai cai = new Cai(cai1.getInt("id"),cai1.getString("name"),cai1.getString("image_url"),0,cai1.getInt("price"));
                caiArrayList.add(cai);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
