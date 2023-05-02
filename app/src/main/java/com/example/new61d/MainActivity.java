package com.example.new61d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView manu_button;
    NavigationView home_navigation;
    ListView manu_listView;
    String[] manu_value_list;
    ArrayList<OrderModel> home_order_list, my_order_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        manu_listView.setAdapter((new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,manu_value_list)));

        //choose manu item
        manu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Log.v("orderlist for homepage",home_order_list.toString());
                        HomeFragment homeFragment = new HomeFragment(home_order_list);
                        setFragment(homeFragment);

                        break;
                    case(2):
                        break;
                }
            }
        });
        manu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.current_fragment,fragment)
                .commit();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void initial() {
        drawerLayout = findViewById(R.id.manu_drawable_layout);
        manu_button = findViewById(R.id.manu_button);
        home_navigation = findViewById(R.id.manu_NavigationView);
        manu_listView = findViewById(R.id.manu_listView);
        manu_value_list = getResources().getStringArray(R.array.manu_value);



        //home data:
        home_order_list = new ArrayList<OrderModel>();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("orderlist");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot orderSnapshot : snapshot.getChildren()){
                    OrderModel orderModel = new OrderModel(orderSnapshot.child("receiver_name").getValue().toString(),
                            orderSnapshot.child("sender_name").getValue().toString(),
                            orderSnapshot.child("pick_up_date").getValue().toString(),
                            orderSnapshot.child("pick_up_time").getValue().toString(),
                            orderSnapshot.child("pick_up_location").getValue().toString(),
                            orderSnapshot.child("good_type").getValue().toString(),
                            orderSnapshot.child("weight").getValue().toString(),
                            orderSnapshot.child("width").getValue().toString(),
                            orderSnapshot.child("length").getValue().toString(),
                            orderSnapshot.child("height").getValue().toString(),
                            orderSnapshot.child("vehicle_type").getValue().toString(),
                            orderSnapshot.child("image").getValue().toString()
                            );
                    Log.v("order",orderModel.toString());
                    home_order_list.add(orderModel);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setFragment(new HomeFragment(home_order_list));



    }
}