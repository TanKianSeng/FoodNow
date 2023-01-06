package com.example.androideatit;

import static com.example.androideatit.Common.Common.convertCodeToStatus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    FirebaseDatabase db;
    DatabaseReference request;
    RecyclerView recyclerView;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //setup database
        db = FirebaseDatabase.getInstance();
        request = db.getReference("Request");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent().getExtras()==null){
            loadOrders(Common.User_current.getPhone());
        }else{
            loadOrders(getIntent().getStringExtra("userPhone"));
        }

    }

    private void loadOrders(String phone) {
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(request.orderByChild("phone").equalTo(phone), Request.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull Request model) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.setItemClickListener((view, position1, isLongClick) -> Toast.makeText(OrderStatus.this, "clicked", Toast.LENGTH_SHORT).show());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout,parent,false);
                return new OrderViewHolder(itemView);
            }
        };
        recyclerView.setAdapter(adapter);

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