package com.example.urdunews;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri uri;
    EditText dob;
    CircleImageView circleImageView;
    EditText name;
    EditText email;
    EditText address;
    Bundle b;
    int chk=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=savedInstanceState;
        setContentView(R.layout.activity_signup);
        circleImageView=(CircleImageView) findViewById(R.id.profile);
        //uploading from gallery
//        circleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent opengallery=new Intent();
//                opengallery.setType("image/*");
//                //opengallery.setType("camera/*");
//                opengallery.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(opengallery,"Select Profile"),1);
//                //startActivityForResult(opengallery,pick);
//            }
//        });
        Calendar calendar=Calendar.getInstance();
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int month=calendar.get(Calendar.MONTH);
        final int year1=calendar.get(Calendar.YEAR);
        dob=(EditText)findViewById(R.id.dob);
        dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DatePickerDialog dpd = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofyear, int dayOfMonth) {
                        monthofyear+=1;
                        dob.setText(dayOfMonth +"/"+monthofyear+"/"+year);
                    }
                },year1,month,day);
                dpd.show();
                return false;
            }
        });
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        address=(EditText)findViewById(R.id.address);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chkfields();
                if(name.getText().toString().equals(""))
                    name.setError("This field cannot be blank");
                else
                    name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chkfields();
                if(email.getText().toString().equals(""))
                    email.setError("This field cannot be blank");
                else
                    email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Button b=(Button)findViewById(R.id.button);
                if(!email.getText().toString().equals(""))
                {
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        b.setEnabled(false);
                        email.setError("Invalid Format");
                    }
                    else
                    {
                        email.setError(null);
                        chkfields();
                    }
                }

            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chkfields();
                if(address.getText().toString().equals(""))
                    address.setError("This field cannot be blank");
                else
                    address.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chkfields();
                if(dob.getText().toString().equals(""))
                    dob.setError("This field cannot be blank");
                else
                    dob.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button register =(Button)findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chk=2;
                dob=(EditText)findViewById(R.id.dob);
                String id= Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
                DatabaseReference ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://urdu-news-65e17.firebaseio.com/Users/"+id);
                ref.child("id").setValue(id);
                ref.child("Name").setValue(name.getText().toString());
                ref.child("Email").setValue(email.getText().toString());
                ref.child("Address").setValue(address.getText().toString());
                ref.child("DOB").setValue(name.getText().toString());
                RadioGroup radioGroup=(RadioGroup)findViewById(R.id.gender);
                int r=radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton=(RadioButton)findViewById(r);
                ref.child("Gender").setValue(radioButton.getText().toString());
                Signup.super.onBackPressed();
            }

        });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK && data!=null) {
//            uri = data.getData();
//            try {
//
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                circleImageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //    }
//        }
//    }
    public void chkfields()
    {
        Button b=(Button)findViewById(R.id.button);
        if(name.getText().toString().equals("")|| email.getText().toString().equals("")|| address.getText().toString().equals("")|| dob.getText().toString().equals(""))
        {
           b.setEnabled(false);
        }
        else{
            b.setEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("asd","qwer");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(chk==1)
        {
            android.os.Process.killProcess(android.os.Process.myPid());
            super.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {

        Log.e("asd","opi");
        if(chk==1)
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Exit me", true);
            startActivity(intent);
            finish();
        }
    }
}
