package com.women.security.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.view.View;
import android.widget.Toast;

import com.women.security.R;
import com.women.security.utils.Utils;

public class InformationActivity extends AppCompatActivity {

    private AppCompatButton save_btn, skip_btn;
    private AppCompatEditText fname,lname,phone,fnumber,lnumber,email,police;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        fname=(AppCompatEditText) findViewById (R.id.fname);
        lname=(AppCompatEditText) findViewById (R.id.lname);
        phone=(AppCompatEditText) findViewById (R.id.phone);
        fnumber=(AppCompatEditText) findViewById (R.id.fnumber);
        lnumber=(AppCompatEditText) findViewById (R.id.lnumber);
        email=(AppCompatEditText) findViewById (R.id.email);
        police=(AppCompatEditText) findViewById (R.id.police);



        save_btn = (AppCompatButton) findViewById(R.id.save_btn);
        skip_btn = (AppCompatButton) findViewById(R.id.skip_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Action Required", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor= Utils.sharedPreferences.edit();
                editor.putString(Utils.FName,fname.getText().toString());
                editor.putString(Utils.LName,lname.getText().toString());
                editor.putString(Utils.Fnumber,fnumber.getText().toString());
                editor.putString(Utils.Snumber,lnumber.getText().toString());
                editor.putString(Utils.Phone,phone.getText().toString());
                editor.putString(Utils.Email,email.getText().toString());
                editor.putString(Utils.Police,police.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(InformationActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InformationActivity.this, MainActivity.class);
                InformationActivity.this.startActivity(i);
                InformationActivity.this.finish();
            }
        });

    }


}
