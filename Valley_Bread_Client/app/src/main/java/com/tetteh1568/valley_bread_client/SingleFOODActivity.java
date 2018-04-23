package com.tetteh1568.valley_bread_client;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SingleFOODActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private String bread_key = null;
    private String[] numbers={"1","2","3","4","5","6"};
    private DatabaseReference mDatabase, userData;
    private TextView singleBreadTitle,singleBreadDesc,singleBreadPrice;
    private ImageView singleBreadImage;
    private Button orderButton;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private DatabaseReference mRef;
    String bread_name,bread_price,bread_desc,bread_image,bread_number;
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);

        bread_key = getIntent().getExtras().getString("BreadId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        singleBreadDesc = (TextView) findViewById(R.id.singleDesc);
        singleBreadPrice = (TextView) findViewById(R.id.singlePrice);
        singleBreadTitle =(TextView) findViewById(R.id.singleTitle);
        singleBreadImage = (ImageView) findViewById(R.id.singleImageView);
        ed=findViewById(R.id.number);
        ed.setShowSoftInputOnFocus(false);

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

                singleBreadTitle.setText("NAME : "+bread_name);
                singleBreadPrice.setText("PRICE : "+bread_price+"$");
                singleBreadDesc.setText("INFO : \n"+bread_desc);
                Picasso.with(SingleFOODActivity.this).load(bread_image).into(singleBreadImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void orderItemClicked(View view) {
        if (ed.getText().toString().isEmpty()) {
            Snackbar.make(findViewById(R.id.single_bread), "Please select the number of Orders", Snackbar.LENGTH_SHORT).show();
            ed.setError("Click Here");
        } else {
            final DatabaseReference newOrders = mRef.push();
            userData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newOrders.child("itemname").setValue(bread_name);
                    newOrders.child("username").setValue(dataSnapshot.child("Name").getValue());
                    newOrders.child("itemprice").setValue(bread_price);
                    newOrders.child("image").setValue(bread_image);
                    newOrders.child("plates").setValue(ed.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(SingleFOODActivity.this, "Order for "+ed.getText().toString().trim()+" "+bread_name+" is been placed.", Snackbar.LENGTH_SHORT).show();

                            NavUtils.navigateUpFromSameTask(SingleFOODActivity.this);
                            finish();

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }



    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        super.onBackPressed();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        bread_number=numbers[i];
        ed.setText(bread_number);
        ed.setShowSoftInputOnFocus(false);
    }


    public void number(View view) {
        AlertDialog.Builder a = new AlertDialog.Builder(SingleFOODActivity.this);
        a.setTitle("How many Plates ?");
        a.setItems(numbers,SingleFOODActivity.this);

        a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        a.create().show();

    }


}
