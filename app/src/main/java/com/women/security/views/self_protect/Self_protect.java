package com.women.security.views.self_protect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.women.security.R;

public class Self_protect extends AppCompatActivity {


    CardView kick,knife,spray,punch,scream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_protect);


        kick= (CardView)findViewById(R.id.card_kick);

        knife= (CardView)findViewById(R.id.card_knife);

        punch= (CardView)findViewById(R.id.card_slap);

        spray= (CardView)findViewById(R.id.card_spray);

        scream= (CardView)findViewById(R.id.card_shout);



        kick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Self_protect.this, Kick.class);
                startActivity(i);
            }
        });

        knife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Self_protect.this, Knife.class);
                startActivity(i);
            }
        });
        punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Self_protect.this, Punch.class);
                startActivity(i);
            }
        });
        spray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Self_protect.this, Spray.class);
                startActivity(i);
            }
        });
        scream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Self_protect.this, Scream.class);
                startActivity(i);
            }
        });











    }
}
