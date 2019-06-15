package com.example.urdunews;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<News> arrayList;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {return  true; }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent=new Intent(getApplicationContext(),Relavent_News.class);
        intent.putExtra("ActivityName",menuItem.getTitle());
        startActivity(intent);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try
        {

            FirebaseApp.initializeApp(this);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase fbdb=FirebaseDatabase.getInstance();
            DatabaseReference dbref= fbdb.getReferenceFromUrl("https://urdu-news-65e17.firebaseio.com/");
            dbref.keepSynced(true);
                //dbref.limitToLast(2);
            String id= Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
            Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("id").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {}
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(),Signup.class);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            arrayList=new ArrayList<News>();

            ListView urdulist=(ListView)findViewById(R.id.listview1);
            final UrduLIstAdapter urduLIstAdapter=new UrduLIstAdapter(this,R.layout.row,arrayList);
            urdulist.setAdapter(urduLIstAdapter);

            dbref.child("News").limitToLast(7).addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Map<String,Object> data=(Map<String, Object>) dataSnapshot.getValue();
                        News urdu4=new News(data.get("img").toString(),data.get("title").toString(),data.get("date").toString(),data.get("description").toString(),data.get("id").toString(),data.get("category").toString());
                        arrayList.add(0,urdu4);
                        urduLIstAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String,Object> data=(Map<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }
        );
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
                            addLog(arrayList.get(in).id);
                        }
                    }
                    else
                    {
                        for (final int[] i = {temp[0]}; i[0] < firstVisibleItem + visibleItemCount; i[0]++)
                        {
                            addLog(arrayList.get(i[0]).id);
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
                intent.putExtra("News",arrayList.get(position));
                startActivity(intent);

            }
        });




        }catch (Exception e)
        {
            Log.e("firebaseerror",e.toString());
        }

    }
    public void addLog(final String newsid)
    {
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://urdu-news-65e17.firebaseio.com");
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("NewsId",newsid);
                map.put("UserId", Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID));
                map.put("operation","view");
                reference.child("User_Logs").push().setValue(map);
            }
        };
        thread.run();
    }

    @Override
    public void onBackPressed() {
        onDestroy();
        onDestroy();
    }
}
