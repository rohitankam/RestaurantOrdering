package com.tetteh1568.valley_bread_client;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class BillActivity extends AppCompatActivity {

    RecyclerView list;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    public static int tot,num,finalprice,payment;
    TextView total;


    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        View orderView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderView = itemView;
        }




        public void setItemName(String itemname){
            TextView itemname_content=(TextView)orderView.findViewById(R.id.name);
            itemname_content.setText(itemname);
        }
        public void setItemprice(String itemprice){


            TextView item_price=(TextView)orderView.findViewById(R.id.price);

            Log.i("a1",item_price.getText().toString());
            finalprice=Integer.valueOf(itemprice)*num;
            item_price.setText(String.valueOf(finalprice));
            Log.i("b1",String.valueOf(finalprice));
            tot=tot+Integer.valueOf(finalprice);

//            TextView tot=orderView.findViewById(R.id.total);
//            tot.setText(String.valueOf(finalprice));


        }
        public void setImage(Context ctx, String image){
            ImageView bread_image = (ImageView) orderView.findViewById(R.id.imageView);
            Picasso.with(ctx).load(image).into(bread_image);
        }
        public void setItemNo(String itemno){
            TextView item_no=(TextView)orderView.findViewById(R.id.no);
            item_no.setText(itemno);
            num=Integer.valueOf(itemno);
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        total = findViewById(R.id.total);
        list = findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders");
        Log.i("create", String.valueOf(tot));
        total.setText(String.valueOf(tot));
        tot = 0;


    }



        @Override
        protected void onStart () {
            super.onStart();
            FirebaseRecyclerAdapter<Order, OrderViewHolder> FRBA = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(

                    Order.class,
                    R.layout.bill_item,
                    OrderViewHolder.class,
                    mDatabase

            ) {

                @Override
                protected void populateViewHolder(OrderViewHolder viewHolder, Order model, int position) {
                    viewHolder.setItemNo(model.getPlates());
                    viewHolder.setItemName(model.getItemname());
                    viewHolder.setItemprice(model.getItemprice());
                    viewHolder.setImage(getApplicationContext(), model.getImage());

                }
            };
            list.setAdapter(FRBA);


        }


//    public static class abc extends OrderViewHolder{
//
//        TextView t;
//        int tot;
//
//
//        public abc(View itemView) {
//            super(itemView);
//        }
//        public int get(TextView itemView,int total){
//            t=itemView;
//            tot=total;
//            return tot;
//        }
//
//    }
//



//    public int totalprice(String price)
//    {
//        tot=tot+Integer.valueOf(item_price.getText().toString());
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        total.setText(String.valueOf(tot));
//
//        Log.i("start",String.valueOf(tot));
//        tot=0;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        total.setText(String.valueOf(tot));
//
//        Log.i("resume",String.valueOf(tot));
//        tot=0;
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        total.setText(String.valueOf(tot));
//
//        Log.i("post",String.valueOf(tot));
//        tot=0;
//    }
//


}