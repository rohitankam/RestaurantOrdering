package com.tetteh1568.valley_bread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpenOrders extends AppCompatActivity {

    private RecyclerView mFoodList;
    private DatabaseReference mDatabase;
    public static int tot,num,finalprice,payment;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_orders);

        total = findViewById(R.id.total);
        total.setText(String.valueOf(tot));
        mFoodList = (RecyclerView) findViewById(R.id.orderLayout);
        mFoodList.setHasFixedSize(true);
        mFoodList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders");

    }

    @Override
    protected void onStart() {
        super.onStart();
        total = findViewById(R.id.total);
        total.setText(String.valueOf(tot));
        tot = 0;
        FirebaseRecyclerAdapter<Order,OrderViewHolder> FRBA = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(

                Order.class,
                R.layout.singleorderitem,
                OrderViewHolder.class,
                mDatabase

        ) {

            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Order model, int position) {
                viewHolder.setItemNo(model.getPlates());
                viewHolder.setUserName(model.getUsername());
                viewHolder.setItemName(model.getItemname());
                viewHolder.setItemprice(model.getItemprice());
            }
        };
        mFoodList.setAdapter(FRBA);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        View orderView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderView = itemView;
        }

        public void setUserName(String username){
            TextView username_content=(TextView)orderView.findViewById(R.id.orderUserName);
            //rohit@gmail to rohit
            String s=username.substring(0,username.length()-10);
            username_content.setText(s);
        }
        public void setItemName(String itemname){
            TextView itemname_content=(TextView)orderView.findViewById(R.id.orderItemName);
            itemname_content.setText(itemname);
        }

        public void setItemprice(String itemprice) {


            TextView item_price = (TextView) orderView.findViewById(R.id.orderItemprice);

            Log.i("a1", item_price.getText().toString());
            finalprice = Integer.valueOf(itemprice) * num;
            item_price.setText(String.valueOf(finalprice+"$"));
            Log.i("b1", String.valueOf(finalprice));
            tot = tot + Integer.valueOf(finalprice);
        }
        public void setItemNo(String itemno){
            TextView item_no=(TextView)orderView.findViewById(R.id.orderItemNo);
            item_no.setText(itemno);
            num=Integer.valueOf(itemno);
        }
    }
}
