package com.example.urdunews;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class News_Details extends AppCompatActivity {
    DatabaseReference newref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__details);
        Intent intent=getIntent();
        News news=(News) intent.getSerializableExtra("News");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://urdu-news-65e17.firebaseio.com/User_Logs");
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("NewsId",news.id);
        map.put("UserId", Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID));
        map.put("operation","Detail");
        newref=reference.push();
        newref.setValue(map);


        EditText title=(EditText)findViewById(R.id.title);
        TextView date=(TextView)findViewById(R.id.date);
        TextView description=(TextView)findViewById(R.id.description);
        ImageView imageView=(ImageView)findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load(news.image).into(imageView);
        title.setText(news.title);
        date.setText(news.date);
        description.setText(news.description);
    }
}
