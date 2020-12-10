package com.jdemaagd.horadocha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

public class OrderActivity extends AppCompatActivity {

    private int mQuantity = 0;
    private int mTotalPrice = 0;

    private static final int SMALL_PRICE = 5;
    private static final int MEDIUM_PRICE = 6;
    private static final int LARGE_PRICE = 7;

    private static final String TEA_SIZE_SMALL = "Small ($5/cup)";
    private static final String TEA_SIZE_MEDIUM = "Medium ($6/cup)";
    private static final String TEA_SIZE_LARGE = "Large ($7/cup)";

    private String mMilkType;
    private String mSugarType;
    private String mTeaName = "";

    private String mSize;

    public final static String EXTRA_TOTAL_PRICE = "com.jdemaagd.horadocha.EXTRA_TOTAL_PRICE";
    public final static String EXTRA_TEA_NAME = "com.jdemaagd.horadocha.EXTRA_TEA_NAME";
    public final static String EXTRA_SIZE = "com.jdemaagd.horadocha.EXTRA_SIZE";
    public final static String EXTRA_MILK_TYPE = "com.jdemaagd.horadocha.EXTRA_MILK_TYPE";
    public final static String EXTRA_SUGAR_TYPE = "com.jdemaagd.horadocha.EXTRA_SUGAR_TYPE";
    public final static String EXTRA_QUANTITY = "com.jdemaagd.horadocha.EXTRA_QUANTITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().setTitle(getString(R.string.order_title));

        Intent intent = getIntent();
        mTeaName = intent.getStringExtra(MenuActivity.EXTRA_TEA_NAME);

        TextView teaNameTextView = findViewById(R.id.tea_name_text_view);
        teaNameTextView.setText(mTeaName);

        TextView costTextView = findViewById(R.id.cost_text_view);
        costTextView.setText(getString(R.string.initial_cost));

        setupSizeSpinner();
        setupMilkSpinner();
        setupSugarSpinner();
    }

    private void setupSizeSpinner() {

        Spinner mSizeSpinner = findViewById(R.id.tea_size_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tea_size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(adapter);

        mSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mSize = getString(R.string.tea_size_small);
                        break;
                    case 1:
                        mSize = getString(R.string.tea_size_medium);
                        break;
                    case 2:
                        mSize = getString(R.string.tea_size_large);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSize = getString(R.string.tea_size_small);
            }
        });
    }

    private void setupMilkSpinner() {

        Spinner mSizeSpinner = findViewById(R.id.milk_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.milk_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(adapter);

        mSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mMilkType = getString(R.string.milk_type_none);
                        break;
                    case 1:
                        mMilkType = getString(R.string.milk_type_nonfat);
                        break;
                    case 2:
                        mMilkType = getString(R.string.milk_type_1_percent);
                        break;
                    case 3:
                        mMilkType = getString(R.string.milk_type_2_percent);
                        break;
                    case 4:
                        mMilkType = getString(R.string.milk_type_whole);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMilkType = getString(R.string.milk_type_none);
            }
        });
    }

    private void setupSugarSpinner() {

        Spinner mSizeSpinner = findViewById(R.id.sugar_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sugar_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(adapter);

        mSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mSugarType = getString(R.string.sweet_type_0);
                        break;
                    case 1:
                        mSugarType = getString(R.string.sweet_type_25);
                        break;
                    case 2:
                        mSugarType = getString(R.string.sweet_type_50);
                        break;
                    case 3:
                        mSugarType = getString(R.string.sweet_type_75);
                        break;
                    case 4:
                        mSugarType = getString(R.string.sweet_type_100);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSugarType = getString(R.string.sweet_type_100);
            }
        });
    }

    public void increment(View view) {
        mQuantity = mQuantity + 1;
        displayQuantity(mQuantity);
        mTotalPrice = calculatePrice();
        displayCost(mTotalPrice);
    }

    public void decrement(View view) {
        if (mQuantity > 0) {
            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            mTotalPrice = calculatePrice();
            displayCost(mTotalPrice);
        }
    }

    private int calculatePrice() {
        switch (mSize) {
            case TEA_SIZE_SMALL:
                mTotalPrice = mQuantity * SMALL_PRICE;
                break;
            case TEA_SIZE_MEDIUM:
                mTotalPrice = mQuantity * MEDIUM_PRICE;
                break;
            case TEA_SIZE_LARGE:
                mTotalPrice = mQuantity * LARGE_PRICE;
                break;
        }

        return mTotalPrice;
    }

    private void displayQuantity(int numberOfTeas) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfTeas));
    }

    private void displayCost(int totalPrice) {
        TextView costTextView = (TextView) findViewById(R.id.cost_text_view);

        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        costTextView.setText(convertPrice);
    }

    public void brewTea(View view) {
        Intent intent = new Intent(OrderActivity.this, OrderSummaryActivity.class);
        intent.putExtra(EXTRA_TOTAL_PRICE, mTotalPrice);
        intent.putExtra(EXTRA_TEA_NAME, mTeaName);
        intent.putExtra(EXTRA_SIZE, mSize);
        intent.putExtra(EXTRA_MILK_TYPE, mMilkType);
        intent.putExtra(EXTRA_SUGAR_TYPE, mSugarType);
        intent.putExtra(EXTRA_QUANTITY, mQuantity);

        startActivity(intent);
    }
}