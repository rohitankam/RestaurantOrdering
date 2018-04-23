package com.tetteh1568.valley_bread_client;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText email,pass;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String user_name,pass_text,email_text;
    ConnectivityManager conn;
    NetworkInfo net;
    private  boolean backclick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.editEmail);
        pass =   (EditText) findViewById(R.id.editPass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        //INTERNET CONNECTED OR NOT
        conn=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        net=conn.getActiveNetworkInfo();

    }

    public void signupButtonClicked(View view) {
        conn=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        net=conn.getActiveNetworkInfo();
         user_name = email.getText().toString().trim();
         pass_text = pass.getText().toString().trim();
         email_text=user_name+"@gmail.com";
        if (valid()) {
            if(net!=null && net.isConnected()){

            mAuth.createUserWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                       Log.d("success", "createUserWithEmail:success");

                       String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mDatabase.child(user_id);
                        current_user.child("Name").setValue(email_text);
                       current_user.child("Pass").setValue(pass_text);


                       Intent login = new Intent(MainActivity.this,LoginActivity.class);
                       startActivity(login);
                       finish();
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w("unsuccess", "createUserWithEmail:failure", task.getException());
//                       Toast.makeText(MainActivity.this,"UserName already exist",Toast.LENGTH_SHORT).show();
                       Snackbar.make(findViewById(R.id.signup),"Username Already Exists",Snackbar.LENGTH_SHORT).show();
                    }
                    }
            });

            }
            else{
                Snackbar.make(findViewById(R.id.signup),"No Internet connection",Snackbar.LENGTH_SHORT).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        conn=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                        net=conn.getActiveNetworkInfo();
                        if(net!=null && net.isConnected()) {
                            Snackbar.make(findViewById(R.id.signup),"Connected",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).show();
//                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                conn=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                net=conn.getActiveNetworkInfo();
            }
        }
    }
    public void signinButtonClicked(View view){
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }


    public boolean valid(){
        boolean valid =true;
        if(user_name.isEmpty()||user_name.length()>32){
            email.setError("Please enter valid name");
            valid=false;
        }
        if(pass_text.isEmpty()){
            pass.setError("Please enter valid password");
            valid=false;
        }
        else if(pass_text.length()<6){
            pass.setError("Please enter minimum 6 characters");
            valid=false;
        }
        return valid;
    }

    public void menu(View view) {
        startActivity(new Intent(MainActivity.this,MenuActivity.class));
    }

    @Override
    public void onBackPressed() {
        if(!backclick){
            Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
            backclick=true;
        }
        else {
            super.onBackPressed();
        }

        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                backclick=false;
            }
        }.start();
    }

}
