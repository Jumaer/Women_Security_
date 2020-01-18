package com.women.security.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.women.security.R;

public class chat_rec extends RecyclerView.ViewHolder  {



    public TextView leftText,rightText;

    public chat_rec(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);


    }
}
