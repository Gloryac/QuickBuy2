package com.example.quickbuy2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.quickbuy2.Adapter.CategoryAdapter;
import com.example.quickbuy2.Adapter.PopularAdapter;
import com.example.quickbuy2.Adapter.SliderAdapter;
import com.example.quickbuy2.Domain.CategoryDomain;
import com.example.quickbuy2.Domain.ItemsDomain;
import com.example.quickbuy2.Domain.SliderItems;
import com.example.quickbuy2.R;
import com.example.quickbuy2.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.quickbuy2.R.id.bottom_home;
//import static com.example.quickbuy2.R.id.bottom_settings;





public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_cart) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                return true;
            /*} else if (itemId == bottom_settings) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
                return true;*/
            } else {
                return false;
            }
        });


    }



    private void initPopular() {
        DatabaseReference myref=database.getReference("Items");
        Log.d("init popular", "database reference at initPopular: " + myref.toString());
        binding.progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("Snapshot popular", "Snapshot data: " + snapshot.toString());
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        //items.add(issue.getValue(ItemsDomain.class));
                        ItemsDomain item = issue.getValue(ItemsDomain.class);
                        Log.d("item", "item at init popular: " + item.toString());
                        if (item != null) { // Add null check for safety
                            items.add(item);
                        } else {
                            items.add(item);
                        }
                        Log.d("item", "items at init popular: " + items.toString());
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));

                    }
                    binding.progressBarPopular.setVisibility(View.GONE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void initCategory() {
        DatabaseReference myref=database.getReference("Category");
        binding.progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
//                        items.add(issue.getValue(CategoryDomain.class));
                       // HashMap categoryItem = (HashMap) issue.getValue();
                        HashMap<String, Object> categoryItem = (HashMap<String, Object>) issue.getValue();


                        String picUrl = (String) categoryItem.get("picUrl");
                        String title = (String) categoryItem.get("title");
                        //int id = (int) (long)categoryItem.get("id");
                        Long idLong = (Long) categoryItem.get("id");
                        int id = idLong != null ? Math.toIntExact(idLong) : 0;  // Set default if null


                        CategoryDomain categoryDomain = new CategoryDomain(title, id, picUrl); // Assuming a constructor with image URL
                        items.add(categoryDomain);

                    }
                    if(!items.isEmpty()){
                        Log.d("Popular object", "item at home activity: " +items.toString() );
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL,false) );
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));

                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        //Log.d(myRef);
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems>items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot issue:snapshot.getChildren()){
//                        items.add(issue.getValue(SliderItems.class));
                        String imageUrl = issue.getValue(String.class);
                        SliderItems sliderItem = new SliderItems(imageUrl); // Assuming a constructor with image URL
                        items.add(sliderItem);
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items,binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }
}