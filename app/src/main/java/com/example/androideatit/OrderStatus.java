package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.androideatit.Common.Common;
import com.example.androideatit.Model.Request;
import com.example.androideatit.ViewHolder.OrderViewHolder;
import com.example.androideatit.databinding.ActivityOrderStatusBinding;
import com.example.androideatit.databinding.OrderLayoutBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {
    ActivityOrderStatusBinding binding;
    OrderLayoutBinding orderLayoutBinding;

    FirebaseDatabase db;
    DatabaseReference request;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setup database
        db = FirebaseDatabase.getInstance();
        request = db.getReference("Request");

        binding.listOrders.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        binding.listOrders.setLayoutManager(layoutManager);

        loadOrders(Common.User_current.getPhone());
    }

    private void loadOrders(String phone) {
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(request.orderByChild("phone").equalTo(phone), Request.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                orderLayoutBinding.orderId.setText(adapter.getRef(position).getKey());
                orderLayoutBinding.orderStatus.setText(convertCodeToStatus(model.getStatus()));
                orderLayoutBinding.orderAddress.setText(model.getAddress());
                orderLayoutBinding.orderPhone.setText(model.getPhone());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                orderLayoutBinding = OrderLayoutBinding.inflate(inflater,parent,false);

                return new OrderViewHolder(orderLayoutBinding);
            }


        };
        binding.listOrders.setAdapter(adapter);


    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        public OrderViewHolder(@NonNull OrderLayoutBinding itemView) {
            super(itemView.getRoot());
        }
    }
    private String convertCodeToStatus(String status){
        if (status.equals("0")){
            return "Placed";
        }else if (status.equals("1")){
            return "On my way";
        }else{
            return "Shipped";
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();

        }
    }
}