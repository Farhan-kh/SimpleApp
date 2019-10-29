package com.fbapicking.controller.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fbapicking.R;
import com.fbapicking.controller.activity.PickByProcessTypeActivity;
import com.fbapicking.model.order.PickByProcessTypeModel;
import com.fbapicking.utility.Commons;

public class SummaryAdapter extends ArrayAdapter<PickByProcessTypeModel> {
    private Typeface font;
    private Activity activity;
    private boolean failedTab;

    public SummaryAdapter(Activity activity, Context context, boolean failedTab) {
        super(context, 0);
        this.activity = activity;
        this.failedTab = failedTab;
        font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Museo300Regular.otf");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_summary, parent, false);

            holder.layoutSummary = convertView.findViewById(R.id.layoutSummary);
            holder.layoutBatch = convertView.findViewById(R.id.layoutBatch);
            holder.layoutExpiry = convertView.findViewById(R.id.layoutExpiry);
            holder.layoutPickedQty = convertView.findViewById(R.id.layoutPickedQty);
            holder.layoutRemainingQty = convertView.findViewById(R.id.layoutRemainingQty);
            holder.layoutAction = convertView.findViewById(R.id.layoutAction);

            holder.lblOrderNoKey = convertView
                    .findViewById(R.id.lblOrderNoKey);
            holder.lblProductKey = convertView
                    .findViewById(R.id.lblProductKey);
            holder.lblProductSKUKey = convertView
                    .findViewById(R.id.lblProductSKUKey);
            holder.lblProductUPCKey = convertView
                    .findViewById(R.id.lblProductUPCKey);
            holder.lblProductShelfKey = convertView
                    .findViewById(R.id.lblProductShelfKey);
            holder.lblProductQtyKey = convertView
                    .findViewById(R.id.lblProductQtyKey);
            holder.lblPickedQtyKey = convertView
                    .findViewById(R.id.lblPickedQtyKey);
            holder.lblRemainingQtyKey = convertView
                    .findViewById(R.id.lblRemainingQtyKey);
            holder.lblBatchKey = convertView.findViewById(R.id.lblBatchKey);
            holder.lblExpiryKey = convertView.findViewById(R.id.lblExpiryKey);

            holder.lblOrderNoColon = convertView
                    .findViewById(R.id.lblOrderNoColon);
            holder.lblProductColon = convertView
                    .findViewById(R.id.lblProductColon);
            holder.lblProductSKUColon = convertView
                    .findViewById(R.id.lblProductSKUColon);
            holder.lblProductUPCColon = convertView
                    .findViewById(R.id.lblProductUPCColon);
            holder.lblProductShelfColon = convertView
                    .findViewById(R.id.lblProductShelfColon);
            holder.lblProductQtyColon = convertView
                    .findViewById(R.id.lblProductQtyColon);
            holder.lblPickedQtyColon = convertView
                    .findViewById(R.id.lblPickedQtyColon);
            holder.lblRemainingQtyColon = convertView
                    .findViewById(R.id.lblRemainingQtyColon);
            holder.lblBatchColon = convertView.findViewById(R.id.lblBatchColon);
            holder.lblExpiryColon = convertView.findViewById(R.id.lblExpiryColon);

            holder.lblOrderNo = convertView
                    .findViewById(R.id.lblOrderNo);
            holder.lblSKU = convertView.findViewById(R.id.lblSKU);
            holder.lblUPC = convertView.findViewById(R.id.lblUPC);
            holder.lblShelf = convertView
                    .findViewById(R.id.lblShelf);
            holder.lblProduct = convertView
                    .findViewById(R.id.lblProduct);
            holder.lblProductQty = convertView.findViewById(R.id.lblProductQty);
            holder.lblPickedQty = convertView.findViewById(R.id.lblPickedQty);
            holder.lblRemainingQty = convertView.findViewById(R.id.lblRemainingQty);
            holder.lblBatch = convertView.findViewById(R.id.lblBatch);
            holder.lblExpiry = convertView.findViewById(R.id.lblExpiry);

            holder.lblOrderNoKey.setTypeface(font);
            holder.lblProductKey.setTypeface(font);
            holder.lblProductSKUKey.setTypeface(font);
            holder.lblProductUPCKey.setTypeface(font);
            holder.lblProductShelfKey.setTypeface(font);
            holder.lblProductQtyKey.setTypeface(font);
            holder.lblPickedQtyKey.setTypeface(font);
            holder.lblRemainingQtyKey.setTypeface(font);
            holder.lblBatchKey.setTypeface(font);
            holder.lblExpiryKey.setTypeface(font);

            holder.lblOrderNoColon.setTypeface(font);
            holder.lblProductColon.setTypeface(font);
            holder.lblProductSKUColon.setTypeface(font);
            holder.lblProductUPCColon.setTypeface(font);
            holder.lblProductShelfColon.setTypeface(font);
            holder.lblProductQtyColon.setTypeface(font);
            holder.lblPickedQtyColon.setTypeface(font);
            holder.lblRemainingQtyColon.setTypeface(font);
            holder.lblBatchColon.setTypeface(font);
            holder.lblExpiryColon.setTypeface(font);

            holder.lblOrderNo.setTypeface(font);
            holder.lblSKU.setTypeface(font);
            holder.lblUPC.setTypeface(font);
            holder.lblShelf.setTypeface(font);
            holder.lblProduct.setTypeface(font);
            holder.lblProductQty.setTypeface(font, Typeface.BOLD);
            holder.lblPickedQty.setTypeface(font, Typeface.BOLD);
            holder.lblRemainingQty.setTypeface(font, Typeface.BOLD);
            holder.lblBatch.setTypeface(font);
            holder.lblExpiry.setTypeface(font);

            holder.removeButton = convertView.findViewById(R.id.removeButton);
            holder.removeButton.setTypeface(font, Typeface.BOLD);

            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));

            // set tag
            convertView.setTag(holder);
        } else {
            // get tag
            holder = (ViewHolder) convertView.getTag();
        }

        String sku;
        String upc;
        String product;

        PickByProcessTypeModel pickByProcessTypeModel = getItem(position);
        if (pickByProcessTypeModel != null) {
            sku = pickByProcessTypeModel.getSku().length() != 0 ? pickByProcessTypeModel
                    .getSku() : "NA";
            if (pickByProcessTypeModel.getProduct_code() != null) {
                upc = pickByProcessTypeModel.getProduct_code().length() != 0 ? pickByProcessTypeModel
                        .getProduct_code() : "NA";
            } else {
                upc = "NA";
            }
            product = pickByProcessTypeModel.getProduct_name().length() != 0 ? pickByProcessTypeModel
                    .getProduct_name() : "NA";

            holder.lblOrderNo.setText(pickByProcessTypeModel.getOrder_number());
            holder.lblSKU.setText(sku);
            holder.lblUPC.setText(upc);
            holder.lblProduct.setText(product);
            holder.lblProductQty.setText(pickByProcessTypeModel.getQuantity());
            holder.lblShelf.setText(!pickByProcessTypeModel.getLocation()
                    .equalsIgnoreCase("") ? pickByProcessTypeModel.getLocation()
                    : "NA");

            if (failedTab) {
                holder.layoutPickedQty.setVisibility(View.VISIBLE);
                holder.layoutRemainingQty.setVisibility(View.VISIBLE);

                holder.lblPickedQty.setText(pickByProcessTypeModel.getPicked_quantity());
                int remainingQty = (Integer.parseInt(pickByProcessTypeModel.getQuantity()) - Integer.parseInt(pickByProcessTypeModel.getPicked_quantity()));
                holder.lblRemainingQty.setText(String.valueOf(remainingQty));

                if (remainingQty > 0) {
                    holder.layoutAction.setVisibility(View.VISIBLE);
                } else {
                    holder.layoutAction.setVisibility(View.GONE);
                }

                if (remainingQty > 0 && Integer.parseInt(pickByProcessTypeModel.getPicked_quantity()) > 0) {
                    holder.lblOrderNoKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblOrderNoColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblOrderNo.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblProductKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProductColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProduct.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblProductSKUKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProductSKUColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblSKU.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblProductUPCKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProductUPCColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblUPC.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblProductQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProductQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProductQty.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblPickedQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblPickedQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblPickedQty.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblRemainingQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblRemainingQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblRemainingQty.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblProductShelfKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblProductShelfColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblShelf.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblBatchKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblBatchColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblBatch.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.lblExpiryKey.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblExpiryColon.setTextColor(getContext().getResources().getColor(R.color.text_red));
                    holder.lblExpiry.setTextColor(getContext().getResources().getColor(R.color.text_red));

                    holder.layoutSummary.setBackgroundColor(getContext().getResources().getColor(R.color.text_light_yellow));
                } else if (remainingQty == 0 && Integer.parseInt(pickByProcessTypeModel.getPicked_quantity()) > 0) {
                    holder.lblOrderNoKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblOrderNoColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblOrderNo.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblProductKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProductColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProduct.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblProductSKUKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProductSKUColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblSKU.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblProductUPCKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProductUPCColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblUPC.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblProductQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProductQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProductQty.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblPickedQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblPickedQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblPickedQty.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblRemainingQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblRemainingQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblRemainingQty.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblProductShelfKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblProductShelfColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblShelf.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblBatchKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblBatchColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblBatch.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.lblExpiryKey.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblExpiryColon.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));
                    holder.lblExpiry.setTextColor(getContext().getResources().getColor(R.color.text_dark_green));

                    holder.layoutSummary.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                } else {
                    holder.lblOrderNoKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblOrderNoColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblOrderNo.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblProductKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProductColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProduct.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblProductSKUKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProductSKUColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblSKU.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblProductUPCKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProductUPCColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblUPC.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblProductQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProductQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProductQty.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblPickedQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblPickedQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblPickedQty.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblRemainingQtyKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblRemainingQtyColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblRemainingQty.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblProductShelfKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblProductShelfColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblShelf.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblBatchKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblBatchColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblBatch.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.lblExpiryKey.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblExpiryColon.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    holder.lblExpiry.setTextColor(getContext().getResources().getColor(R.color.text_black));

                    holder.layoutSummary.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                }
            } else {
                holder.layoutPickedQty.setVisibility(View.GONE);
                holder.layoutRemainingQty.setVisibility(View.GONE);
                holder.layoutAction.setVisibility(View.GONE);

                holder.lblPickedQty.setText("");
                holder.lblRemainingQty.setText("");

                holder.layoutSummary.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
            }

            if (pickByProcessTypeModel.getEnable_expiry_management().equalsIgnoreCase("true") && pickByProcessTypeModel.getEnable_batch_management().equalsIgnoreCase("true")) {
                holder.layoutExpiry.setVisibility(View.VISIBLE);
                holder.layoutBatch.setVisibility(View.VISIBLE);

                if (pickByProcessTypeModel.getExpiry_date() != null) {
                    if (!pickByProcessTypeModel.getExpiry_date().equalsIgnoreCase("NA")) {
                        holder.lblExpiry.setText(Commons.DateConverter.formatedDate(pickByProcessTypeModel.getExpiry_date()));
                    } else {
                        holder.lblExpiry.setText("NA");
                    }
                } else {
                    holder.lblExpiry.setText("NA");
                }
                holder.lblBatch.setText(pickByProcessTypeModel.getBatch_number());
            } else if (pickByProcessTypeModel.getEnable_expiry_management().equalsIgnoreCase("false") && pickByProcessTypeModel.getEnable_batch_management().equalsIgnoreCase("true")) {
                holder.layoutExpiry.setVisibility(View.GONE);
                holder.layoutBatch.setVisibility(View.VISIBLE);

                holder.lblExpiry.setText("");
                holder.lblBatch.setText(pickByProcessTypeModel.getBatch_number());
            } else {
                holder.layoutExpiry.setVisibility(View.GONE);
                holder.layoutBatch.setVisibility(View.GONE);

                holder.lblExpiry.setText("");
                holder.lblBatch.setText("");
            }

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickByProcessTypeModel pickByProTypeModel = getItem((Integer) v.getTag());
                    if (activity instanceof PickByProcessTypeActivity) {
                        ((PickByProcessTypeActivity) activity)
                                .pickConfirm(pickByProTypeModel);
                    }
                }
            });
        }

        holder.removeButton.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        private LinearLayout layoutSummary;
        private LinearLayout layoutBatch;
        private LinearLayout layoutExpiry;
        private LinearLayout layoutRemainingQty;
        private LinearLayout layoutPickedQty;
        private LinearLayout layoutAction;

        private TextView lblOrderNoKey;
        private TextView lblProductKey;
        private TextView lblProductSKUKey;
        private TextView lblProductUPCKey;
        private TextView lblProductShelfKey;
        private TextView lblProductQtyKey;
        private TextView lblPickedQtyKey;
        private TextView lblRemainingQtyKey;
        private TextView lblBatchKey;
        private TextView lblExpiryKey;

        private TextView lblOrderNoColon;
        private TextView lblProductColon;
        private TextView lblProductSKUColon;
        private TextView lblProductUPCColon;
        private TextView lblProductShelfColon;
        private TextView lblProductQtyColon;
        private TextView lblPickedQtyColon;
        private TextView lblRemainingQtyColon;
        private TextView lblBatchColon;
        private TextView lblExpiryColon;

        private TextView lblOrderNo;
        private TextView lblSKU;
        private TextView lblUPC;
        private TextView lblProduct;
        private TextView lblProductQty;
        private TextView lblPickedQty;
        private TextView lblRemainingQty;
        private TextView lblShelf;
        private TextView lblBatch;
        private TextView lblExpiry;

        private Button removeButton;
    }
}
