package com.tetteh1568.valley_bread_client;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mBreadList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  boolean backclick=false;


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        mBreadList = (RecyclerView) findViewById(R.id.breadList);
        mBreadList.setHasFixedSize(true);
        mBreadList.setLayoutManager(new GridLayoutManager(this,2));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MenuActivity.this,MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Bread,BreadViewHolder> FBRA = new FirebaseRecyclerAdapter<Bread,BreadViewHolder>(
                Bread.class,
                R.layout.singlemenuitem,
                BreadViewHolder.class,
                mDatabase
        ){
           @Override
           protected  void populateViewHolder(BreadViewHolder viewHolder, Bread model, int position){
                   viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());

               final String bread_key = getRef(position).getKey().toString();
               viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                   @Override
                   public void onClick(View v) {
                            Intent singleBreadActivity = new Intent(MenuActivity.this,SingleFOODActivity.class);
                            singleBreadActivity.putExtra("BreadId",bread_key);
                            startActivity(singleBreadActivity);
                   }
               }

               );
           }
        };
        mBreadList.setAdapter(FBRA);
    }

    public void fabclick(View view) {
        startActivity(new Intent(MenuActivity.this,BillActivity.class));
    }

    public static  class BreadViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BreadViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setName (String name){
            TextView bread_name = (TextView) mView.findViewById(R.id.breadName);
            bread_name.setText(name);
        }
        public void setDesc (String desc){
            TextView bread_desc = (TextView) mView.findViewById(R.id.breadDesc);
            bread_desc.setText(desc);

        }
        public void setPrice (String price){
            TextView bread_price = (TextView) mView.findViewById(R.id.breadPrice);
            bread_price.setText(price);

        }
        public void setImage (Context ctx, String image){
            ImageView bread_image = (ImageView) mView.findViewById(R.id.breadImage);
            Picasso.with(ctx).load(image).into(bread_image);

        }
    }




    @Override
    public void onBackPressed() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("LOGOUT");
        a.setMessage("Do you want to logout ? ");
        a.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        a.show();

    }
    }

