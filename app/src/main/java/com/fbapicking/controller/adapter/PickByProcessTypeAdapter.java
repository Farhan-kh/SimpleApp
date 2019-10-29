package com.fbapicking.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import com.fbapicking.R;
import com.fbapicking.controller.activity.PickByProcessTypeActivity;
import com.fbapicking.model.order.PickByProcessTypeModel;
import com.fbapicking.utility.Commons;

public class PickByProcessTypeAdapter extends ArrayAdapter<PickByProcessTypeModel> {
    Typeface font;
    Activity activity;
    PickByProcessTypeModel pickByProcessTypeModel;
    public ArrayList<String> qtyArray, pickedQtyArray;
    String processType;
    boolean is_usn_base;

    public PickByProcessTypeAdapter(Activity activity, Context context, ArrayList<String> qtyArray, ArrayList<String> pickedQtyArray, String processType) {
        super(context, 0);
        this.activity = activity;
        font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Museo300Regular.otf");
        this.qtyArray = qtyArray;
        this.pickedQtyArray = pickedQtyArray;
        this.processType = processType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            if (processType != null && processType.equalsIgnoreCase("locations")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_pick_by_locations, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.row_pick_by_process_type, parent, false);
            }

            holder.pickByItemRow = (LinearLayout) convertView.findViewById(R.id.pickByItemRow);
            holder.layoutBatch = (LinearLayout) convertView.findViewById(R.id.layoutBatch);
            holder.layoutExpiry = (LinearLayout) convertView.findViewById(R.id.layoutExpiry);

            holder.lblProductKey = (TextView) convertView.findViewById(R.id.lblProductKey);
            holder.lblProductSKUKey = (TextView) convertView.findViewById(R.id.lblProductSKUKey);
            holder.lblProductUPCKey = (TextView) convertView.findViewById(R.id.lblProductUPCKey);
            holder.lblProductShelfKey = (TextView) convertView.findViewById(R.id.lblProductShelfKey);
            holder.lblProductQtyKey = (TextView) convertView.findViewById(R.id.lblProductQtyKey);
            holder.lblPickedQtyKey = (TextView) convertView.findViewById(R.id.lblPickedQtyKey);
            holder.lblBatchKey = (TextView) convertView.findViewById(R.id.lblBatchKey);
            holder.lblExpiryKey = (TextView) convertView.findViewById(R.id.lblExpiryKey);

            holder.lblProductColon = (TextView) convertView.findViewById(R.id.lblProductColon);
            holder.lblProductSKUColon = (TextView) convertView.findViewById(R.id.lblProductSKUColon);
            holder.lblProductUPCColon = (TextView) convertView
                    .findViewById(R.id.lblProductUPCColon);
            holder.lblProductShelfColon = (TextView) convertView
                    .findViewById(R.id.lblProductShelfColon);
            holder.lblProductQtyColon = (TextView) convertView
                    .findViewById(R.id.lblProductQtyColon);
            holder.lblPickedQtyColon = (TextView) convertView.findViewById(R.id.lblPickedQtyColon);
            holder.lblBatchColon = (TextView) convertView.findViewById(R.id.lblBatchColon);
            holder.lblExpiryColon = (TextView) convertView.findViewById(R.id.lblExpiryColon);

            holder.lblIndex = (TextView) convertView
                    .findViewById(R.id.lblIndex);
            holder.lblSKU = (TextView) convertView.findViewById(R.id.lblSKU);
            holder.lblUPC = (TextView) convertView.findViewById(R.id.lblUPC);
            holder.lblProduct = (TextView) convertView
                    .findViewById(R.id.lblProduct);
            holder.lblQty = (TextView) convertView.findViewById(R.id.lblQty);
            holder.lblShelf = (TextView) convertView
                    .findViewById(R.id.lblShelf);
            holder.lblPickedQty = (EditText) convertView.findViewById(R.id.lblPickedQty);
            holder.lblBatch = (TextView) convertView.findViewById(R.id.lblBatch);
            holder.lblExpiry = (TextView) convertView.findViewById(R.id.lblExpiry);

            holder.lblProductKey.setTypeface(font);
            holder.lblProductSKUKey.setTypeface(font);
            holder.lblProductUPCKey.setTypeface(font);
            holder.lblProductShelfKey.setTypeface(font);
            holder.lblProductQtyKey.setTypeface(font);
            holder.lblPickedQtyKey.setTypeface(font);
            holder.lblBatchKey.setTypeface(font);
            holder.lblExpiryKey.setTypeface(font);

            holder.lblProductColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductSKUColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductUPCColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductShelfColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductQtyColon.setTypeface(font, Typeface.BOLD);
            holder.lblPickedQtyColon.setTypeface(font, Typeface.BOLD);
            holder.lblBatchColon.setTypeface(font, Typeface.BOLD);
            holder.lblExpiryColon.setTypeface(font, Typeface.BOLD);

            holder.lblIndex.setTypeface(font);
            holder.lblSKU.setTypeface(font);
            holder.lblUPC.setTypeface(font);
            holder.lblProduct.setTypeface(font);
            holder.lblQty.setTypeface(font);
            holder.lblShelf.setTypeface(font);
            holder.lblPickedQty.setTypeface(font);
            holder.lblBatch.setTypeface(font);
            holder.lblExpiry.setTypeface(font);

            // set tag
            convertView.setTag(holder);
        } else {
            // get tag
            holder = (ViewHolder) convertView.getTag();
        }

        String sku = "";
        String upc = "";
        String product = "";

        pickByProcessTypeModel = getItem(position);
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
            is_usn_base = pickByProcessTypeModel.getIs_usn_base().equalsIgnoreCase("true");
            int index = position;

            holder.lblIndex.setText(String.valueOf(index + 1));
            holder.lblSKU.setText(sku);
            holder.lblUPC.setText(upc);
            holder.lblProduct.setText(product);
            holder.lblQty.setText(pickByProcessTypeModel.getQuantity());
            holder.lblPickedQty.setText(pickedQtyArray.get(index));
            holder.lblShelf.setText(!pickByProcessTypeModel.getLocation()
                    .equalsIgnoreCase("") ? pickByProcessTypeModel.getLocation()
                    : "NA");

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

            String pickQty = pickedQtyArray.get(position);
            if (pickByProcessTypeModel.getQuantity().equalsIgnoreCase(pickQty)) {
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
            } else {
                if (is_usn_base) {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                } else {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                }
            }

            final ViewHolder final_holder = holder;

            holder.lblPickedQty.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int action = event.getAction();

                    if (action == KeyEvent.ACTION_UP && v == final_holder.lblPickedQty) {
                        PickByProcessTypeModel pModel = getItem(Integer.parseInt(final_holder.lblPickedQty.getTag().toString()));

                        String rawData = final_holder.lblPickedQty.getText().toString();
                        String finalQuantity = rawData.replaceFirst("^0+(?!$)", "");

                        if (finalQuantity.equalsIgnoreCase("")) {
                            pickedQtyArray.set(Integer.parseInt(final_holder.lblPickedQty.getTag().toString()), "0");
                        } else {
                            try {
                                if (Integer.parseInt(finalQuantity) > Integer.parseInt(pModel.getQuantity())) {
                                    if (activity instanceof PickByProcessTypeActivity) {
                                        ((PickByProcessTypeActivity) activity)
                                                .showErrorMessage("Picked Qty Should Not Be Greater Than Order Qty", true);
                                    }

                                    final_holder.lblPickedQty.setText("");
                                } else {
                                    pickedQtyArray.set(Integer.parseInt(final_holder.lblPickedQty.getTag().toString()), finalQuantity);
                                }
                            } catch (NumberFormatException ex) {
                                if (activity instanceof PickByProcessTypeActivity) {
                                    ((PickByProcessTypeActivity) activity)
                                            .showErrorMessage("Picked quantity exceeded allowed number of digits", true);
                                }
                            }
                        }

                        if (finalQuantity.equalsIgnoreCase(pModel.getQuantity())) {
                            final_holder.pickByItemRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                        } else {
                            if (is_usn_base) {
                                final_holder.pickByItemRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                            } else {
                                final_holder.pickByItemRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                            }
                        }

                        checkValidation();

                        return true;
                    }
                    return false;
                }
            });

            holder.lblPickedQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int keyCode,
                                              KeyEvent event) {
                    if (keyCode == EditorInfo.IME_ACTION_DONE && v == final_holder.lblPickedQty) {
                        hideKeyBoard(v);

                        PickByProcessTypeModel pModel = getItem(Integer.parseInt(v.getTag().toString()));

                        String rawData = final_holder.lblPickedQty.getText().toString();
                        String finalQuantity = rawData.replaceFirst("^0+(?!$)", "");

                        if (finalQuantity.equalsIgnoreCase("")) {
                            pickedQtyArray.set(Integer.parseInt(v.getTag().toString()), "0");
                        } else {
                            try {
                                if (Integer.parseInt(finalQuantity) > Integer.parseInt(pModel.getQuantity())) {
                                    if (activity instanceof PickByProcessTypeActivity) {
                                        ((PickByProcessTypeActivity) activity)
                                                .showErrorMessage("Picked Qty Should Not Be Greater Than Order Qty", true);
                                    }

                                    final_holder.lblPickedQty.setText("");
                                } else {
                                    pickedQtyArray.set(Integer.parseInt(v.getTag().toString()), finalQuantity);
                                }
                            } catch (NumberFormatException ex) {
                                if (activity instanceof PickByProcessTypeActivity) {
                                    ((PickByProcessTypeActivity) activity)
                                            .showErrorMessage("Picked quantity exceeded allowed number of digits", true);
                                }
                            }
                        }

                        if (finalQuantity.equalsIgnoreCase(pModel.getQuantity())) {
                            final_holder.pickByItemRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                        } else {
                            if (is_usn_base) {
                                final_holder.pickByItemRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                            } else {
                                final_holder.pickByItemRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                            }
                        }

                        checkValidation();

                        return true;
                    }
                    return false;
                }
            });
        }

        holder.lblPickedQty.setTag(position);

        return convertView;
    }

    // Method to enable or disable confirm button based on criteria
    private void checkValidation() {
        ArrayList<String> uniqueArray = new ArrayList<String>(
                new HashSet<String>(pickedQtyArray));
        if (uniqueArray.contains("0") && uniqueArray.size() == 1) {
            if (activity instanceof PickByProcessTypeActivity) {
                ((PickByProcessTypeActivity) activity)
                        .disableConfirmButton();
            }
        } else {
            if (activity instanceof PickByProcessTypeActivity) {
                ((PickByProcessTypeActivity) activity)
                        .enableConfirmButton();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return qtyArray.size();
    }

    private static class ViewHolder {
        private LinearLayout pickByItemRow;
        private LinearLayout layoutBatch;
        private LinearLayout layoutExpiry;

        private TextView lblProductKey;
        private TextView lblProductSKUKey;
        private TextView lblProductUPCKey;
        private TextView lblProductShelfKey;
        private TextView lblProductQtyKey;
        private TextView lblPickedQtyKey;
        private TextView lblBatchKey;
        private TextView lblExpiryKey;

        private TextView lblProductColon;
        private TextView lblProductSKUColon;
        private TextView lblProductUPCColon;
        private TextView lblProductShelfColon;
        private TextView lblProductQtyColon;
        private TextView lblPickedQtyColon;
        private TextView lblBatchColon;
        private TextView lblExpiryColon;

        private TextView lblIndex;
        private TextView lblSKU;
        private TextView lblUPC;
        private TextView lblProduct;
        private TextView lblQty;
        private TextView lblShelf;
        private EditText lblPickedQty;
        private TextView lblBatch;
        private TextView lblExpiry;
    }

    // Method to hide KeyBoard
    private void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
