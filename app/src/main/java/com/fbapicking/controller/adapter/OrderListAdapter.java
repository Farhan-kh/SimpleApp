package com.fbapicking.controller.adapter;

import com.fbapicking.R;
import com.fbapicking.model.order.OrderModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderListAdapter extends ArrayAdapter<OrderModel> {
    Typeface font;
    Activity activity;

    // Initialize constructor
    public OrderListAdapter(Activity activity, Context context) {
        super(context, 0);
        this.activity = activity;
        font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Museo300Regular.otf");
    }

    // Method to get view (i.e. custom cell)
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_orderlist, parent, false);

            // get controls
            holder.lblOrderNo = (TextView) convertView
                    .findViewById(R.id.lblOrderNo);
            holder.lblOrderNo.setTypeface(font, Typeface.BOLD);
            holder.lblSellerName = (TextView) convertView
                    .findViewById(R.id.lblSeller);
            holder.lblSellerName.setTypeface(font, Typeface.BOLD);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderModel currentOrder = getItem(position);
        if (currentOrder != null) {
            holder.lblOrderNo
                    .setText(currentOrder.getOrder_number());

            if (currentOrder.getCompany_name().equalsIgnoreCase("")
                    || currentOrder.getCompany_name() != null) {
                holder.lblSellerName.setText(currentOrder.getCompany_name());
            } else {
                holder.lblSellerName.setText("NA");
            }

            if (currentOrder.getExpress_delivery().equalsIgnoreCase("true")) {
                holder.lblOrderNo.setTextColor(getContext().getResources().getColor(R.color
                        .text_red));
                holder.lblSellerName.setTextColor(getContext().getResources().getColor(R.color
                        .text_red));
            } else {
                holder.lblOrderNo.setTextColor(getContext().getResources().getColor(R.color
                        .text_black));
                holder.lblSellerName.setTextColor(getContext().getResources().getColor(R.color
                        .text_black));
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView lblOrderNo;
        private TextView lblSellerName;
    }
}
