package com.example.myyoutube;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ListView listView;

    ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
     HashMap<String,String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);
        listView=findViewById(R.id.listview);

        //https://youtu.be/nxLnb522rqM?si=bq3d-sL4ksf2GigK এখানের nxLnb522rqM এই টুকু নিবো ।
        //https://www.youtube.com/watch?v=nxLnb522rqM&ab_channel=RaFiIslaM এখানের nxLnb522rqM এই টুকু নিবো ।

        String url ="https://nubsoft.xyz/Video.json";
        JsonArrayRequest arrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            //response এটা একটা Array হিসেবে এখানে কাজ করছে
            @Override
            public void onResponse(JSONArray response) {
                //JSONObject jsonObject=response.getJSONObject(0); এতটুক লেখার পর
                //getJSONObject এ red bulb এ click করে try catch নিবো ।

                progressBar.setVisibility(View.GONE);
                try {

                    for(int x=0; x<response.length();x++) {

                        JSONObject jsonObject = response.getJSONObject(x);
                        //JsonObject থেকে Title & Video id use করবো
                        String title = jsonObject.getString("title");
                        String video_id = jsonObject.getString("video_id");


                        hashMap=new HashMap<>();
                        hashMap.put("title",title);
                        hashMap.put("video_id",video_id);
                        arrayList.add(hashMap);

                    }

                    MyAdapter myAdapter=new MyAdapter();
                    listView.setAdapter(myAdapter);



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            //error এর জন্য এখানে new (space) suggetion ধরবো ।
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(arrayRequest);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
private class MyAdapter extends BaseAdapter{
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=getLayoutInflater();
        View myView = layoutInflater.inflate(R.layout.item,null);

        TextView tvtitle=myView.findViewById(R.id.textView);
        ImageView ImageThumb=myView.findViewById(R.id.imageThumb);

        HashMap<String,String> hashMap = arrayList.get(position);
        String title=hashMap.get("title");
        String video_id=hashMap.get("video_id");

        String image_url = "https://img.youtube.com/vi/"+video_id+"/0.jpg";

        tvtitle.setText(title);

        //.placeholder(R.drawable.car) এটা ছাড়াও কাজ চলবে ।

        Glide.with(myView)
                .load(image_url)
                .placeholder(R.drawable.car)
                .into(ImageThumb);

        return myView; // return 0; এমন থাকবে অবশ্যই myView add করবা নাইলে app crash করবে ।
    }
}
}
