package com.example.quickbuy2.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.quickbuy2.Adapter.SizeAdapter;
import com.example.quickbuy2.Adapter.SliderAdapter;
import com.example.quickbuy2.Domain.ItemsDomain;
import com.example.quickbuy2.Domain.SliderItems;
import com.example.quickbuy2.Fragment.DescriptionFragment;
import com.example.quickbuy2.Fragment.ReviewFragment;
import com.example.quickbuy2.Fragment.SoldFragment;
import com.example.quickbuy2.Helper.ManagementCart;
import com.example.quickbuy2.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemsDomain object;
    private int numberOrder=1;
    private ManagementCart managmentCart;
    private Handler sliderHandle=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart=new ManagementCart(this);

        getBundles();
        initBanners();
        initSize();
        setupViewPager();
    }

    private void initSize() {
        ArrayList<String>list=new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXL");

        binding.recyclerSize.setAdapter(new SizeAdapter(list));
        binding.recyclerSize.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i=0;i<object.getPicUrl().size();i++){
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }
        binding.viewPagerSlider.setAdapter(new SliderAdapter(sliderItems,binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundles() {
        object=(ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("Sh"+object.getPrice());
        binding.ratingBar.setRating(object.getRating());
        binding.ratingTxt.setText(object.getRating()+"Rating");

        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberinCart(numberOrder);
                managmentCart.insertItem(object);
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setupViewPager(){
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        DescriptionFragment tab1=new DescriptionFragment();
        ReviewFragment tab2=new ReviewFragment();
        SoldFragment tab3=new SoldFragment();

        Bundle bundle1=new Bundle();
        Bundle bundle2=new Bundle();
        Bundle bundle3=new Bundle();
        bundle1.putString("dwscription",object.getDescription());
        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1,"Description");
        adapter.addFrag(tab2,"Reviews");
        adapter.addFrag(tab3,"Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tableLayout.setupWithViewPager(binding.viewpager);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList=new ArrayList<>();
        private final List<String> mfragmentTitleList=new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        private void addFrag(Fragment fragment,String title){
            mFragmentList.add(fragment);
            mfragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mfragmentTitleList.get(position);
        }
    }
}