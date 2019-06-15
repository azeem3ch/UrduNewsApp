package com.example.urdunews;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Relavent_News extends AppCompatActivity {
    ArrayList<News> ArrayList;
    String category,ActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relavent__news);
        Intent intent=getIntent();
        ActivityName=intent.getStringExtra("ActivityName");
        getcategory();
        try{
            ArrayList=new ArrayList<News>();
            ListView urdulist=(ListView)findViewById(R.id.listview1);
            final UrduLIstAdapter urduLIstAdapter=new UrduLIstAdapter(this,R.layout.row,ArrayList);
            urdulist.setAdapter(urduLIstAdapter);
            Log.e("qwer",category+"");
            Query query=FirebaseDatabase.getInstance().getReference("News").orderByChild("category").equalTo(category);
            query.limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            Map<String,Object> data=(Map<String, Object>) dataSnapshot1.getValue();
                            News urdu4=new News(data.get("img").toString(),data.get("title").toString(),data.get("date").toString(),data.get("description").toString(),data.get("id").toString(),data.get("category").toString());
                            ArrayList.add(0,urdu4);
                            urduLIstAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final int[] temp = {0};
            urdulist.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(final AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(firstVisibleItem==0&& visibleItemCount==7)
                    {
                        Log.e("paki","roala");
                        return;
                    }
                    if(temp[0] ==0)
                    {
                        temp[0] =firstVisibleItem+visibleItemCount;
                        for (int in = firstVisibleItem; in < firstVisibleItem + visibleItemCount; in++)
                        {
                            addLog(ArrayList.get(in).id);
                        }
                    }
                    else
                    {
                        for (final int[] i = {temp[0]}; i[0] < firstVisibleItem + visibleItemCount; i[0]++)
                        {
                            addLog(ArrayList.get(i[0]).id);
                        }
                        temp[0]=firstVisibleItem+visibleItemCount;
                    }
                    Log.e("roor",firstVisibleItem+" "+visibleItemCount);
                    //Toast.makeText(getApplicationContext(),firstVisibleItem+" "+visibleItemCount,Toast.LENGTH_SHORT).show();
                }
            });

            urdulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(getApplicationContext(),News_Details.class);
                    intent.putExtra("News",ArrayList.get(position));
                    startActivity(intent);

                }
            });
        }
        catch (Exception e){
            Log.e("qwer",e.toString());
        }
    }
    public void addLog(final String newsid)
    {
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://urdu-news-65e17.firebaseio.com");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("NewsId",newsid);
                map.put("UserId", Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID));
                map.put("operation","view");
                reference.child("User_Logs").push().setValue(map);
            }
        };
        thread.run();
    }
    public void getcategory()
    {
        if(ActivityName.equals("پاکستان"))
        {
            category="pakistan";
        }
        if(ActivityName.equals("انٹر نیشنل"))
        {
            category="International";
        }
        if(ActivityName.equals("کھیل"))
        {
            category="sports";
        }
        if(ActivityName.equals("شوبز"))
        {
            category="saqafat";
        }
        if(ActivityName.equals("دلچسپ و عجیب"))
        {
            category="weird-news";
        }
        if(ActivityName.equals("صحت"))
        {
            category="health";
        }
        if(ActivityName.equals("سائنس و ٹیکنالوجی"))
        {
            category="science";
        }
        if(ActivityName.equals("بزنس"))
        {
            category="business";
        }
    }
}
