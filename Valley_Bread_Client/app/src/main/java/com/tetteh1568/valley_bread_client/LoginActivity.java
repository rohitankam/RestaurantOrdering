package com.tetteh1568.valley_bread_client;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText userEmail,userPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,mUser;
    private String username,pass,email,userName;
    ConnectivityManager conn;
    NetworkInfo net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPass);
        mAuth =  FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        userName=userEmail.getText().toString().trim()+"@gmail.com";
        //INTERNET CONNECTED OR NOT
        conn=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        net=conn.getActiveNetworkInfo();
    }


    public void signinButtonClicked(View view) {
         username = userEmail.getText().toString().trim();
         pass = userPass.getText().toString().trim();
         email=username+"@gmail.com";
        if (valid()) {
            if(net!=null && net.isConnected()){

                Log.d("success", email);
                Log.d("success", pass);
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("success", "createUserWithEmail:success");
                            checkUserExists();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"UserName or Password Incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(LoginActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkUserExists(){
        final String user_id = mAuth.getCurrentUser().getUid();//user root
        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)){
                    Log.d("success", "b");

                    mUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("success", "c");
                            if(email.equals(dataSnapshot.child("Name").getValue().toString()) && pass.equals(dataSnapshot.child("Pass").getValue().toString())) {
                                Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(menuIntent);
                    }
                    else{
                        Log.d("success", dataSnapshot.child("Name").getValue().toString());
                        Log.d("success", dataSnapshot.child("Pass").getValue().toString());
                                Log.d("success", email);
                                Log.d("success", pass);

                        Toast.makeText(LoginActivity.this,"sfdfsfds",Toast.LENGTH_SHORT).show();
                    }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//
                     }
                     else {
                    Toast.makeText(LoginActivity.this,"UserName or Password Incorrect",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean valid(){
        boolean valid =true;
        if(username.isEmpty()||username.length()>32){
            userEmail.setError("Please enter valid name");
            valid=false;
        }
        if(pass.isEmpty()){
            userPass.setError("Please enter valid password");
            valid=false;
        }
        else if(userPass.length()<6){
            userPass.setError("Please enter minimum 6 characters");
            valid=false;
        }
        return valid;
    }




}

