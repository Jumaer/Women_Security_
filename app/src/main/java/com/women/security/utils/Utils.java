package com.women.security.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Utils
{
    //private Context context=this;
    public static int APP_LOAD_TIME=2000;
    public static int POLICE_CONTACT_NUMER=0;
    public static SharedPreferences sharedPreferences;
    public static String TAG="Women Security";
    public static final String MyPreferences="women";
    public static final String FName="fname";
    public static final String LName="lname";
    public static final String Phone="phone";
    public static final String Fnumber="fnumber";
    public static final String Snumber="snumber";
    public static final String Email="email";
    public static final String Police="police";
    public static final String Text="text";
    public static int Time;



    public static void ToastShort(Context c, String m){
        Toast.makeText(c,m,Toast.LENGTH_SHORT).show();
    }

//    public static void Test(Context c){
//        sharedPreferences=c.getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
//    }
}