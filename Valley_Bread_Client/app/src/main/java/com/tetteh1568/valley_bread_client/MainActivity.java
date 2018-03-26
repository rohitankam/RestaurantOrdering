package com.tetteh1568.valley_bread_client;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.editEmail);
        pass =   (EditText) findViewById(R.id.editPass);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");



    }

    public void signupButtonClicked(View view) {
        final String user_name = email.getText().toString().trim();
        final String pass_text = pass.getText().toString().trim();
        final String email_text=user_name+"@gmail.com";

        if (!TextUtils.isEmpty(email_text) && !TextUtils.isEmpty(pass_text)){
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
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w("unsuccess", "createUserWithEmail:failure", task.getException());

                    }
                    }
            });
        }
    }
    public void signinButtonClicked(View view){
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }

}
