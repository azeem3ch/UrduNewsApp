package com.example.urdunews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UrduLIstAdapter extends ArrayAdapter {
    ArrayList<News> Data;
    public UrduLIstAdapter(Context context, int resource,ArrayList<News> data)
    {
        super(context,resource,data);
        Data =data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row=convertView;
        Urdurow urdurow=new Urdurow();
        LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(row==null)
        {
            row=layoutInflater.inflate(R.layout.row,parent,false);
            urdurow.text=(TextView) row.findViewById(R.id.title);
            urdurow.image=(ImageView)row.findViewById(R.id.imageView2);
            row.setTag(urdurow);
        }
        else{
            urdurow=(Urdurow) row.getTag();
        }
        if(Data.size()-1==position)
        {
            final String i=Data.get(position).id;
            Query query= FirebaseDatabase.getInstance().getReference("News").orderByKey().endAt(i);
            query.limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        ArrayList<News> tmp=new ArrayList<News>();
                        for (DataSnapshot dat:dataSnapshot.getChildren() ) {
                            Map<String,Object> data=(Map<String, Object>) dat.getValue();
                            if(!data.get("id").toString().equals(i))
                            {
                                Log.e("erroorr",data.get("id").toString()+i);
                                News urdu4=new News(data.get("img").toString(),data.get("title").toString(),data.get("date").toString(),data.get("description").toString(),data.get("id").toString(),data.get("category").toString());
                                tmp.add(urdu4);
                            }
                        }
                        for(int i=tmp.size()-1; i>=0; i--){Data.add(tmp.get(i));}
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //UrduRow u=(UrduRow)getItem(position);
        urdurow.text.setText(Data.get(position).getTitle());
        Picasso.with(getContext()).load(Data.get(position).image).into(urdurow.image);
        return row;
    }
    static class Urdurow{
        TextView text;
        ImageView image;
    }
}
