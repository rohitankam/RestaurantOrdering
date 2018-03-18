package com.tetteh1568.valley_bread_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleBreadActivity extends AppCompatActivity {

    private String bread_key = null;
    private DatabaseReference mDatabase, userData;
    private TextView singleBreadTitle,singleBreadDesc,singleBreadPrice;
    private ImageView singleBreadImage;
    private Button orderButton;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private DatabaseReference mRef;
    String bread_name,bread_price,bread_desc,bread_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bread);

        bread_key = getIntent().getExtras().getString("BreadId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        singleBreadDesc = (TextView) findViewById(R.id.singleDesc);
        singleBreadPrice = (TextView) findViewById(R.id.singlePrice);
        singleBreadTitle =(TextView) findViewById(R.id.singleTitle);
        singleBreadImage = (ImageView) findViewById(R.id.singleImageView);

        mAuth = FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference().child("orders");

        current_user = mAuth.getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid());

        mDatabase.child(bread_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 bread_name = (String) dataSnapshot.child("name").getValue();
                 bread_price = (String) dataSnapshot.child("price").getValue();
                 bread_desc = (String) dataSnapshot.child("desc").getValue();
                 bread_image = (String) dataSnapshot.child("image").getValue();

                singleBreadTitle.setText(bread_name);
                singleBreadPrice.setText(bread_price);
                singleBreadDesc.setText(bread_desc);
                Picasso.with(SingleBreadActivity.this).load(bread_image).into(singleBreadImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void orderItemClicked(View view) {
        final DatabaseReference newOrders=mRef.push();
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newOrders.child("itemname").setValue(bread_name);
                newOrders.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SingleBreadActivity.this,"Order placed",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SingleBreadActivity.this,MenuActivity.class));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
