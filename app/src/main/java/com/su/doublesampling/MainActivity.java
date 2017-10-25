package com.su.doublesampling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mShoppingCatTop;
    private CheckBox mCheckAll;
    private TextView mSumPrice;
    private Button mBtnPay;
    private LinearLayout mShoppingCatBottom;
    private RecyclerView mShoppingCatSellerRecycler;
    private List<ProductBean> list;
    private ShoppingCartAdapter adapter;
    private double sumPrice;
    private int sumNum;
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cat_layout);
        initView();
        initData();
        decimalFormat = new DecimalFormat("#0.00");
        mShoppingCatSellerRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingCartAdapter(this, list);
        mShoppingCatSellerRecycler.setAdapter(adapter);
        adapter.setOnItemChickListener(new ShoppingCartAdapter.OnItemChickListener() {
            @Override
            public void onChilcCheckChangeListener(int sellerPosition, int productPosition) {
                if (list.get(sellerPosition).list.get(productPosition).isSelect) {
                    sumPrice = sumPrice + list.get(sellerPosition).list.get(productPosition).price;
                    sumNum++;
                } else {
                    sumPrice = sumPrice - list.get(sellerPosition).list.get(productPosition).price;
                    sumNum--;
                }
                mSumPrice.setText(decimalFormat.format(sumPrice));
                mBtnPay.setText("去结算(" + sumNum + ")");
                isCheckAllChecked();
            }

            @Override
            public void onItemClickListener(int sellerPosition) {
            }
        });

    }

    private void isCheckAllChecked() {
        boolean flag = true;
        for (ProductBean productBean : list) {
            for (ProductBean.DataBean dataBean : productBean.list) {
                if (!dataBean.isSelect) {
                    flag = false;
                    break;
                }
            }
        }
        mCheckAll.setChecked(flag);
    }
    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            List<ProductBean.DataBean> dataBeen = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                dataBeen.add(new ProductBean.DataBean("商品" + j, 10.2 * (j + 1), false));
            }
            list.add(new ProductBean("商家" + i, dataBeen));
        }
    }

    private void initView() {
        mShoppingCatTop = (RelativeLayout) findViewById(R.id.shopping_cat_top);
        mCheckAll = (CheckBox) findViewById(R.id.check_all);
        mCheckAll.setOnClickListener(this);
        mSumPrice = (TextView) findViewById(R.id.sum_price);
        mBtnPay = (Button) findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);
        mShoppingCatBottom = (LinearLayout) findViewById(R.id.shopping_cat_bottom);
        mShoppingCatSellerRecycler = (RecyclerView) findViewById(R.id.shopping_cat_seller_recycler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_all:

                checkAll();

                break;
            case R.id.btn_pay:
                break;
        }
    }

    private void checkAll() {

        for (ProductBean productBean : list) {
            for (ProductBean.DataBean dataBean : productBean.list) {
                if (mCheckAll.isChecked()) {
                    if (!dataBean.isSelect) {
                        dataBean.isSelect = true;
                        sumPrice = sumPrice + dataBean.price;
                        sumNum++;
                    }
                } else {
                    if (dataBean.isSelect) {
                        dataBean.isSelect = false;
                        sumPrice = sumPrice - dataBean.price;
                        sumNum--;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
        mSumPrice.setText(decimalFormat.format(sumPrice));
        mBtnPay.setText("去结算(" + sumNum + ")");

    }
}
