package com.su.doublesampling;

import java.util.List;

/**
 * Created by 苏照亮 on 2017/10/25.
 */

public class ProductBean {

    public String sellerName;
    public List<DataBean> list;

    public ProductBean(String sellerName, List<DataBean> list) {
        this.sellerName = sellerName;
        this.list = list;
    }

    public static class DataBean{
        public DataBean(String producrName, double price, boolean isSelect) {
            this.producrName = producrName;
            this.price = price;
            this.isSelect = isSelect;
        }

        public String producrName;
        public double price;
        public boolean isSelect;
    }

}
