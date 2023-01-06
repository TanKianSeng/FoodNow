package com.example.androideatit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androideatit.Common.Common;
import com.example.androideatit.Model.Category;
import com.example.androideatit.Service.ListenOrder;
import com.example.androideatit.ViewHolder.MenuViewHolder;
import com.example.androideatit.databinding.ActivityHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;


    //init firebase
    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ActivityHomeBinding binding;

    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);


        //init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        binding.appBarHome.toolbar.setTitle("Menu");
        binding.appBarHome.fab.setOnClickListener(view -> {
            Intent cartIntent = new Intent(Home.this,Cart.class);
            startActivity(cartIntent);
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = findViewById(R.id.navigationView);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();



        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);----------obsolete code------
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_home);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            // Setup NavigationUI here
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.bringToFront();
        }


        //Set Name for user
        View headView = navigationView.getHeaderView(0);
        txtFullName = headView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.User_current.getName());

        //setup layout
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        loadMenu();

        Intent service = new Intent(Home.this, ListenOrder.class);
        startService(service);

    }

    private void loadMenu() {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(category,Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options){

            @Override
            public void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int position, @NonNull Category category) {
                viewHolder.menu_name.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).fit().into(viewHolder.menu_image);

                viewHolder.setItemClickListener((view, position1, isLongClick) -> {
                    //get Category ID and sent it to new Activity
                    //Toast.makeText(Home.this, "" + a, Toast.LENGTH_SHORT).show();//----test code for onClick functionality
                    Intent intent = new Intent(Home.this,FoodList.class);
                    //Because Category ID is key, so we need to get key from this item
                    intent.putExtra("categoryId",adapter.getRef(position).getKey());
                    startActivity(intent);


                });
            }
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onNavigationItemSelected(MenuItem item){
        // This handle navigation view item click

        int id = item.getItemId();

        if (id == R.id.navIcon_home){
            //Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.navIcon_cart){
            Intent cartIntent = new Intent(Home.this,Cart.class);
            startActivity(cartIntent);
        }else if(id == R.id.navIcon_order){
            Intent orderIntent = new Intent(Home.this,OrderStatus.class);
            startActivity(orderIntent);
        }else if(id == R.id.navIcon_logout){
            Intent signIntent = new Intent(Home.this,MainActivity.class);
            signIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signIntent);
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}