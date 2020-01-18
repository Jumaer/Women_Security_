package com.women.security.views;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.women.security.R;
import com.women.security.utils.Utils;

public class SettingsActivity extends AppCompatActivity
{
    ImageView change_primary, change_secondary;
    AppCompatTextView primary_number,secondary_number;
    AppCompatEditText primary,secondary,msg,secondery1,secondery2;
    AppCompatButton msg_save;
    private Context context=this;


    //public static String primaryNumber="r";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        change_primary=(ImageView) findViewById (R.id.change_primary);
        change_secondary=(ImageView) findViewById (R.id.change_secondary);

        //msg save options
        msg=(AppCompatEditText) findViewById(R.id.msg);
        msg_save=(AppCompatButton) findViewById (R.id.msg_save);
        msgSave();

        //TextView
        primary_number=(AppCompatTextView) findViewById(R.id.primary_number);
        secondary_number=(AppCompatTextView) findViewById(R.id.secondary_number);

        Utils.sharedPreferences=getSharedPreferences(Utils.MyPreferences, Context.MODE_PRIVATE);

        btnAction();

        textValue();
    }

    private void msgSave(){
        msg_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=Utils.sharedPreferences.edit();
                editor.putString(Utils.Text,msg.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),msg.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void textValue() {
        String primaryValue=Utils.sharedPreferences.getString(Utils.Fnumber,"");
        primary_number.setText(primaryValue);
        String seconadryValue=Utils.sharedPreferences.getString(Utils.Snumber,"");
        secondary_number.setText(seconadryValue);
    }

    private void btnAction() {
        change_primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Primary Number change", Toast.LENGTH_SHORT).show();
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.activity_number);

                AppCompatTextView txt=(AppCompatTextView) dialog.findViewById(R.id.txt);
                txt.setText("Enter/Change Primary Number");

                primary=(AppCompatEditText)  dialog.findViewById (R.id.number);

                //final String  primaryNumber = primary.getText().toString();
                //System.out.println(primaryNumber);

                AppCompatButton save=(AppCompatButton) dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor=Utils.sharedPreferences.edit();
                        editor.putString(Utils.Fnumber,primary.getText().toString());
                        editor.commit();
                        Toast.makeText(getApplicationContext(),primary.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        change_secondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Change Secondary change", Toast.LENGTH_SHORT).show();
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.activity_number);

                AppCompatTextView txt=(AppCompatTextView) dialog.findViewById(R.id.txt);
                txt.setText("Enter/Change Secondary Number");

                secondary=(AppCompatEditText) dialog.findViewById (R.id.number);

                AppCompatButton save=(AppCompatButton) dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor=Utils.sharedPreferences.edit();
                        editor.putString(Utils.Snumber,secondary.getText().toString());
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Data Save Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
}
