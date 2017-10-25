package com.su.doublesampling;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 苏照亮 on 2017/10/18.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {

    private Context context;
    private List<ProductBean1.DataBean> list;
    private OnItemChickListener onItemChickListener;

    public void setOnItemChickListener(OnItemChickListener onItemChickListener) {
        this.onItemChickListener = onItemChickListener;
    }

    public ShoppingCartAdapter(Context context,List<ProductBean1.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ShoppingCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.shopping_cart_child_recycle, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        System.out.println(list.get(position).getSellerid()+"+++++++++++++++++++++++++++++++++");
        final ProductBean1.DataBean dataBean = list.get(position);
        isSellerChecked(holder, dataBean);
        holder.mTvSellerName.setText(dataBean.getSellerName());
        holder.mClassifyChildGridRecycle.setLayoutManager(new LinearLayoutManager(context));
        final ShoppingCartChildAdapter adapter = new ShoppingCartChildAdapter(context, dataBean.getList());
        holder.mClassifyChildGridRecycle.setAdapter(adapter);
        if (onItemChickListener != null){
            adapter.setOnItemCheckListener(new ShoppingCartChildAdapter.OnItemCheckListener() {
                @Override
                public void onCheckListener(int position) {
                    isSellerChecked(holder, dataBean);
                    onItemChickListener.onChilcCheckChangeListener(holder.getLayoutPosition(), position);
                }
            });
            holder.mSellerCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (ProductBean1.DataBean.ListBean listBean : dataBean.getList()) {
                        if(holder.mSellerCb.isChecked()){
                            listBean.isSelect = true;
                        }else {
                            listBean.isSelect = false;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    onItemChickListener.onItemClickListener(holder.getLayoutPosition());
                }
            });
        }

    }

    private void isSellerChecked(MyViewHolder holder,ProductBean1.DataBean dataBean) {
        boolean flag = true;
        for (ProductBean1.DataBean.ListBean listBean : dataBean.getList()) {
            if(!listBean.isSelect){
                flag = false;
                break;
            }
        }
        holder.mSellerCb.setChecked(flag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox mSellerCb;
        RecyclerView mClassifyChildGridRecycle;
        TextView mTvSellerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.mSellerCb = (CheckBox) itemView.findViewById(R.id.seller_cb);
            this.mClassifyChildGridRecycle = (RecyclerView) itemView.findViewById(R.id.classify_child_grid_recycle);
            this.mTvSellerName = (TextView) itemView.findViewById(R.id.tv_seller_name);
        }
    }

    public interface OnItemChickListener {
        void onChilcCheckChangeListener(int sellerPosition, int productPosition);
        void onItemClickListener(int sellerPosition);
    }
}
