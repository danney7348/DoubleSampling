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

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mShoppingCatTop;
    private CheckBox mCheckAll;
    private TextView mSumPrice;
    private Button mBtnPay;
    private LinearLayout mShoppingCatBottom;
    private RecyclerView mShoppingCatSellerRecycler;
    private ShoppingCartAdapter adapter;
    private double sumPrice;
    private int sumNum;
    private DecimalFormat decimalFormat;
    private List<ProductBean1.DataBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cat_layout);
        initView();
        initData();
        decimalFormat = new DecimalFormat("#0.00");

    }
    private void isCheckAllChecked() {
        boolean flag = true;
        for (ProductBean1.DataBean dataBean : list) {
            for (ProductBean1.DataBean.ListBean listBean : dataBean.getList()) {
                if(!listBean.isSelect){
                    flag = false;
                    break;
                }
            }
        }
        mCheckAll.setChecked(flag);
    }
    private void initData() {
        Okutils okutils = new Okutils();
        okutils.getdata("http://120.27.23.105/product/getCarts?uid=170", new Okutils.Backquer() {
            @Override
            public void onfailure(Call call, IOException e) {
            }
            @Override
            public void onresponse(Call call, Response response) {

                try {
                    String string = response.body().string();
                    Gson gson = new Gson();
                    ProductBean1 productBean1 = gson.fromJson(string, ProductBean1.class);
                    list = productBean1.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setData() {
        mShoppingCatSellerRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingCartAdapter(this, list);
        mShoppingCatSellerRecycler.setAdapter(adapter);
        adapter.setOnItemChickListener(new ShoppingCartAdapter.OnItemChickListener() {
            @Override
            public void onChilcCheckChangeListener(int sellerPosition, int productPosition) {
                if (list.get(sellerPosition).getList().get(productPosition).isSelect) {
                    sumPrice = sumPrice + list.get(sellerPosition).getList().get(productPosition).getBargainPrice();
                    sumNum++;
                } else {
                    sumPrice = sumPrice - list.get(sellerPosition).getList().get(productPosition).getBargainPrice();
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

        for (ProductBean1.DataBean dataBean : list) {
            for (ProductBean1.DataBean.ListBean listBean : dataBean.getList()) {
                if(mCheckAll.isChecked()){
                    if (!listBean.isSelect) {
                        listBean.isSelect = true;
                        sumPrice = sumPrice + listBean.getBargainPrice();
                        sumNum++;
                    }
                }else {
                    if(listBean.isSelect){
                        listBean.isSelect = false;
                        sumPrice = sumPrice - listBean.getBargainPrice();
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
