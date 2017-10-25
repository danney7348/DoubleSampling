package com.su.doublesampling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.aizhonghui.shopnum.AmountView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 苏照亮 on 2017/10/18.
 */

public class ShoppingCartChildAdapter extends RecyclerView.Adapter<ShoppingCartChildAdapter.MyViewHolder> {

    private Context context;
    private List<ProductBean.DataBean> list;
    private OnItemCheckListener onItemCheckListener;

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public ShoppingCartChildAdapter(Context context, List<ProductBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductBean.DataBean dataBean = list.get(position);
        if (dataBean.isSelect) {
            holder.mItemCb.setChecked(true);
        } else {
            holder.mItemCb.setChecked(false);
        }
        holder.amount_view.setGoods_storage(50);
        holder.amount_view.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                Toast.makeText(context, "amount_view"+amount, Toast.LENGTH_SHORT).show();
            }
        });
        holder.mTvProductTitle.setText(dataBean.producrName);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        holder.mProductPrice.setText(decimalFormat.format(dataBean.price));
        if (onItemCheckListener != null) {
            holder.mItemCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    dataBean.isSelect = b;
                    onItemCheckListener.onCheckListener(holder.getLayoutPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox mItemCb;
        ImageView mProductIcon;
        TextView mTvProductTitle;
        TextView mTvProductSubhead;
        TextView mProductPrice;
        private final AmountView amount_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mItemCb = (CheckBox) itemView.findViewById(R.id.item_cb);
            this.mProductIcon = (ImageView) itemView.findViewById(R.id.product_icon);
            this.mTvProductTitle = (TextView) itemView.findViewById(R.id.tv_product_title);
            this.mTvProductSubhead = (TextView) itemView.findViewById(R.id.tv_product_subhead);
            this.mProductPrice = (TextView) itemView.findViewById(R.id.product_price);
            amount_view = itemView.findViewById(R.id.amount_view);
        }
    }

    public interface OnItemCheckListener {
        void onCheckListener(int position);
    }

}
