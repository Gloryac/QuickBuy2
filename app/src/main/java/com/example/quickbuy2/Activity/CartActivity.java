package com.example.quickbuy2.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quickbuy2.Adapter.CartAdapter;
import com.example.quickbuy2.Helper.ChangeNumberItemsListener;
import com.example.quickbuy2.Helper.ManagementCart;
import com.example.quickbuy2.R;
import com.example.quickbuy2.databinding.ActivityCartBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.quickbuy2.R.id.bottom_cart;
//import static com.example.quickbuy2.R.id.bottom_settings;
import static com.example.quickbuy2.R.id.totalTxt;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private double tax;
    Button button;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        button = findViewById(R.id.checkOutBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PayActivity.class);
                intent.putExtra("totalAmount",totalTxt);
                startActivity(intent);
            }
        });

        managementCart=new ManagementCart(this);
        calculatorCart();
        setVariable();
        initCartList();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(bottom_cart);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == bottom_cart) {
                return true;
            } else if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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


    private void initCartList() {
        if(managementCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculatorCart();
            }
        }));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
    }

    private void calculatorCart() {
        double percentTax=0.2;
        double discount=10;
        tax=Math.round((managementCart.getTotalFee()*percentTax*100.0))/100.0;

        double total=Math.round((managementCart.getTotalFee()+tax+discount)*100)/100;
        double itemTotal=Math.round(managementCart.getTotalFee()*100)/100;
        binding.totalFeeTxt.setText("Sh."+itemTotal);
        binding.discountTxt.setText("Sh."+discount);
        binding.totalTxt.setText("Sh."+total);
    }
}