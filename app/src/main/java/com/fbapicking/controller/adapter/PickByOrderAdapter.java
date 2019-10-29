package com.fbapicking.controller.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fbapicking.R;
import com.fbapicking.controller.activity.PickByOrderActivity;
import com.fbapicking.model.order.OrderItemListModel;
import com.fbapicking.utility.Commons;

public class PickByOrderAdapter extends ArrayAdapter<OrderItemListModel> {
    Typeface font;
    Activity activity;
    OrderItemListModel orderItemListModel;
    public List<OrderItemListModel> selectedOrderItemListArray;
    List<OrderItemListModel> orderItemArray;
    public List<Boolean> selectedItemListArray;
    public boolean is_usn_base;
    public ArrayList<String> orderQtyArray, pickedQtyArray;
    ArrayList<String> scannedUSNList, scannedIMEIList;
    public ArrayList<String> allUSNList;
    String pickingPreference;

    public PickByOrderAdapter(Activity activity, Context context, String pickingPreference, boolean is_usn_base, List<Boolean> selectedItemListArray, List<OrderItemListModel> orderItemArray) {
        super(context, 0);
        this.activity = activity;
        font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Museo300Regular.otf");
        this.pickingPreference = pickingPreference;
        this.is_usn_base = is_usn_base;
        selectedOrderItemListArray = new ArrayList<OrderItemListModel>();
        this.selectedItemListArray = selectedItemListArray;
        this.orderItemArray = orderItemArray;
    }

    public PickByOrderAdapter(Activity activity, Context context, String pickingPreference, boolean is_usn_base, ArrayList<String> orderQtyArray, ArrayList<String> pickedQtyArray) {
        super(context, 0);
        this.activity = activity;
        font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Museo300Regular.otf");
        this.pickingPreference = pickingPreference;
        this.is_usn_base = is_usn_base;
        this.orderQtyArray = orderQtyArray;
        this.pickedQtyArray = pickedQtyArray;
        if (is_usn_base) {
            scannedUSNList = new ArrayList<String>();
            scannedIMEIList = new ArrayList<String>();

            allUSNList = new ArrayList<String>();
            for (String str : orderQtyArray) {
                allUSNList.add("");
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_pick_by_order, parent, false);

            holder.pickRow = (LinearLayout) convertView.findViewById(R.id.pickRow);
            holder.layoutSKU = (LinearLayout) convertView.findViewById(R.id.layoutSKU);
            holder.layoutUSN = (LinearLayout) convertView.findViewById(R.id.layoutUSN);
            holder.layoutBatch = (LinearLayout) convertView.findViewById(R.id.layoutBatch);
            holder.layoutExpiry = (LinearLayout) convertView.findViewById(R.id.layoutExpiry);

            holder.lblProductKey = (TextView) convertView
                    .findViewById(R.id.lblProductKey);
            holder.lblProductSKUKey = (TextView) convertView
                    .findViewById(R.id.lblProductSKUKey);
            holder.lblProductUPCKey = (TextView) convertView
                    .findViewById(R.id.lblProductUPCKey);
            holder.lblProductShelfKey = (TextView) convertView
                    .findViewById(R.id.lblProductShelfKey);
            holder.lblProductQtyKey = (TextView) convertView
                    .findViewById(R.id.lblProductQtyKey);
            holder.lblSKUKey = (TextView) convertView
                    .findViewById(R.id.lblSKUKey);
            holder.lblUSNKey = (TextView) convertView
                    .findViewById(R.id.lblUSNKey);
            holder.lblPickedQtyKey = (TextView) convertView.findViewById(R.id.lblPickedQtyKey);
            holder.lblBatchKey = (TextView) convertView.findViewById(R.id.lblBatchKey);
            holder.lblExpiryKey = (TextView) convertView.findViewById(R.id.lblExpiryKey);

            holder.lblProductColon = (TextView) convertView
                    .findViewById(R.id.lblProductColon);
            holder.lblProductSKUColon = (TextView) convertView
                    .findViewById(R.id.lblProductSKUColon);
            holder.lblProductUPCColon = (TextView) convertView
                    .findViewById(R.id.lblProductUPCColon);
            holder.lblProductShelfColon = (TextView) convertView
                    .findViewById(R.id.lblProductShelfColon);
            holder.lblProductQtyColon = (TextView) convertView
                    .findViewById(R.id.lblProductQtyColon);
            holder.lblSKUColon = (TextView) convertView
                    .findViewById(R.id.lblSKUColon);
            holder.lblUSNColon = (TextView) convertView
                    .findViewById(R.id.lblUSNColon);
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
            holder.checkboxStatus = (CheckBox) convertView
                    .findViewById(R.id.checkboxStatus);
            holder.productImage = (ImageView) convertView
                    .findViewById(R.id.productImage);
            holder.lblSKUEditText = (EditText) convertView.findViewById(R.id.lblSKUEditText);
            holder.lblUSN = (EditText) convertView.findViewById(R.id.lblUSN);
            holder.lblPickedQty = (TextView) convertView.findViewById(R.id.lblPickedQty);
            holder.lblBatch = (TextView) convertView.findViewById(R.id.lblBatch);
            holder.lblExpiry = (TextView) convertView.findViewById(R.id.lblExpiry);

            holder.lblProductKey.setTypeface(font);
            holder.lblProductSKUKey.setTypeface(font);
            holder.lblProductUPCKey.setTypeface(font);
            holder.lblProductShelfKey.setTypeface(font);
            holder.lblProductQtyKey.setTypeface(font);
            holder.lblSKUKey.setTypeface(font);
            holder.lblUSNKey.setTypeface(font);
            holder.lblPickedQtyKey.setTypeface(font);
            holder.lblBatchKey.setTypeface(font);
            holder.lblExpiryKey.setTypeface(font);

            holder.lblProductColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductSKUColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductUPCColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductShelfColon.setTypeface(font, Typeface.BOLD);
            holder.lblProductQtyColon.setTypeface(font, Typeface.BOLD);
            holder.lblSKUColon.setTypeface(font, Typeface.BOLD);
            holder.lblUSNColon.setTypeface(font, Typeface.BOLD);
            holder.lblPickedQtyColon.setTypeface(font, Typeface.BOLD);
            holder.lblBatchColon.setTypeface(font, Typeface.BOLD);
            holder.lblExpiryColon.setTypeface(font, Typeface.BOLD);

            holder.lblIndex.setTypeface(font);
            holder.lblSKU.setTypeface(font);
            holder.lblUPC.setTypeface(font);
            holder.lblProduct.setTypeface(font);
            holder.lblQty.setTypeface(font);
            holder.lblShelf.setTypeface(font);
            holder.lblSKUEditText.setTypeface(font);
            holder.lblUSN.setTypeface(font);
            holder.lblPickedQty.setTypeface(font);
            holder.lblBatch.setTypeface(font);
            holder.lblExpiry.setTypeface(font);

            if (pickingPreference.equalsIgnoreCase("scan")) {
                if (is_usn_base) {
                    holder.layoutUSN.setVisibility(View.VISIBLE);
                    holder.layoutSKU.setVisibility(View.GONE);
                    holder.checkboxStatus.setVisibility(View.INVISIBLE);

                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                } else {
                    holder.layoutUSN.setVisibility(View.GONE);
                    holder.layoutSKU.setVisibility(View.VISIBLE);
                    holder.checkboxStatus.setVisibility(View.INVISIBLE);

                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                }
            } else {
                if (is_usn_base) {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                } else {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                }

                holder.layoutUSN.setVisibility(View.GONE);
                holder.layoutSKU.setVisibility(View.GONE);
                holder.checkboxStatus.setVisibility(View.VISIBLE);
            }

            // set tag
            convertView.setTag(holder);
        } else {
            // get tag
            holder = (ViewHolder) convertView.getTag();
        }

        String sku = "";
        String upc = "";
        String product = "";

        orderItemListModel = getItem(position);
        if (orderItemListModel != null) {
            sku = orderItemListModel.getSku().length() != 0 ? orderItemListModel
                    .getSku() : "NA";
            upc = orderItemListModel.getUPC().length() != 0 ? orderItemListModel
                    .getUPC() : "NA";
            product = orderItemListModel.getProduct_name().length() != 0 ? orderItemListModel
                    .getProduct_name() : "NA";
            int index = position;

            holder.lblIndex.setText(String.valueOf(index + 1));
            holder.lblSKU.setText(sku);
            holder.lblUPC.setText(upc);
            holder.lblProduct.setText(product);
            holder.lblQty.setText(orderItemListModel.getQuantity());

            if (pickingPreference.equalsIgnoreCase("scan")) {
                holder.lblPickedQty.setText(pickedQtyArray.get(index));
            } else {
                holder.lblPickedQty.setText(orderItemListModel.getQuantity());
            }

            holder.lblShelf.setText(!orderItemListModel.getShelf()
                    .equalsIgnoreCase("") ? orderItemListModel.getShelf()
                    : "NA");

            if (orderItemListModel.getEnable_expiry_management().equalsIgnoreCase("true") && orderItemListModel.getEnable_batch_management().equalsIgnoreCase("true")) {
                holder.layoutExpiry.setVisibility(View.VISIBLE);
                holder.layoutBatch.setVisibility(View.VISIBLE);

                if (orderItemListModel.getExpiryDate().equalsIgnoreCase("")) {
                    holder.lblExpiry.setText("NA");
                } else {
                    holder.lblExpiry.setText(Commons.DateConverter.formatedDate(orderItemListModel.getExpiryDate()));
                }
                holder.lblBatch.setText(orderItemListModel.getBatchNumber());
            } else if (orderItemListModel.getEnable_expiry_management().equalsIgnoreCase("false") && orderItemListModel.getEnable_batch_management().equalsIgnoreCase("true")) {
                holder.layoutExpiry.setVisibility(View.GONE);
                holder.layoutBatch.setVisibility(View.VISIBLE);

                holder.lblExpiry.setText("");
                holder.lblBatch.setText(orderItemListModel.getBatchNumber());
            } else {
                holder.layoutExpiry.setVisibility(View.GONE);
                holder.layoutBatch.setVisibility(View.GONE);

                holder.lblExpiry.setText("");
                holder.lblBatch.setText("");
            }

            if (!orderItemListModel.getProductImageURL().equalsIgnoreCase("")) {
                String requestedURL = orderItemListModel.getProductImageURL();
                if (requestedURL.length() > 0) {
                    requestedURL = ensure_has_protocol(requestedURL);
                }

                Glide.with(activity)
                        .load(requestedURL)
                        .asBitmap()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontTransform()
                        .override(300, 300)
                        .placeholder(R.drawable.noimage)
                        .error(R.drawable.noimage)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(R.drawable.noimage);
            }

            if (pickingPreference.equalsIgnoreCase("scan") && is_usn_base) {
                if (orderItemListModel.getIs_serial_number_base().equalsIgnoreCase("true")) {
                    holder.lblUSN.setHint(getContext().getResources().getString(R.string.hintUSNSNIMEI));
                } else {
                    holder.lblUSN.setHint(getContext().getResources().getString(R.string.hintUSN));
                }
            }

            if (pickingPreference.equalsIgnoreCase("scan")) {
                String pickQty = pickedQtyArray.get(position);
                if (orderItemListModel.getQuantity().equalsIgnoreCase(pickQty)) {
                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));

                    if (is_usn_base) {
                        holder.layoutUSN.setVisibility(View.INVISIBLE);
                        holder.layoutSKU.setVisibility(View.GONE);
                    } else {
                        holder.layoutSKU.setVisibility(View.INVISIBLE);
                        holder.layoutUSN.setVisibility(View.GONE);
                    }
                } else {
                    if (is_usn_base) {
                        convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));

                        holder.layoutUSN.setVisibility(View.VISIBLE);
                        holder.layoutSKU.setVisibility(View.GONE);
                    } else {
                        convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));

                        holder.layoutUSN.setVisibility(View.GONE);
                        holder.layoutSKU.setVisibility(View.VISIBLE);
                    }
                }

                holder.checkboxStatus.setVisibility(View.INVISIBLE);
            }

            final ViewHolder final_holder = holder;

            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            activity);
                    builder.setNegativeButton("Close",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    LayoutInflater inflater = (LayoutInflater) getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogLayout = inflater.inflate(
                            R.layout.custom_imageview, null);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.show();

                    ImageView imageView = (ImageView) dialog
                            .findViewById(R.id.thumbnail);
                    if (imageView != null && final_holder.productImage.getDrawable() != null)
                        imageView
                                .setImageBitmap(((BitmapDrawable) final_holder.productImage
                                        .getDrawable()).getBitmap());
                }
            });

            if (!pickingPreference.equalsIgnoreCase("scan")) {
                holder.checkboxStatus.setVisibility(View.VISIBLE);
                holder.checkboxStatus.setChecked(selectedItemListArray
                        .get(position));

                if (selectedItemListArray
                        .get(position)) {
                    if (!selectedOrderItemListArray
                            .contains(orderItemListModel)) {
                        selectedOrderItemListArray.add(orderItemListModel);
                    }

                    convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));

                    String pQty = (orderItemListModel.getPicked_quantity().equalsIgnoreCase("") ? "0" : orderItemListModel.getPicked_quantity());
                    if (orderItemListModel.getQuantity().equalsIgnoreCase(pQty)) {
                        holder.checkboxStatus.setEnabled(false);
                    } else {
                        holder.checkboxStatus.setEnabled(true);
                    }
                } else {
                    if (is_usn_base) {
                        convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                    } else {
                        convertView.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                    }
                    holder.checkboxStatus.setEnabled(true);
                }

                /*************** Already picked item case start **************/
                if (selectedItemListArray.size() == selectedOrderItemListArray
                        .size()) {
                    if (activity instanceof PickByOrderActivity) {
                        ((PickByOrderActivity) activity)
                                .enablePickButton();

                        ((PickByOrderActivity) activity)
                                .disablePartialPickButton();
                    }
                } else {
                    if (activity instanceof PickByOrderActivity) {
                        ((PickByOrderActivity) activity)
                                .disablePickButton();

                        if (selectedOrderItemListArray.size() > 0) {
                            ((PickByOrderActivity) activity)
                                    .enablePartialPickButton();
                        } else {
                            ((PickByOrderActivity) activity)
                                    .disablePartialPickButton();
                        }
                    }
                }
                /*************** Already picked item case end ****************/
            } else {
                holder.checkboxStatus.setVisibility(View.INVISIBLE);
            }

            holder.checkboxStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderItemListModel orderItemListModel = getItem(Integer
                            .parseInt(v.getTag().toString()));

                    if (((CheckBox) v).isChecked()) {
                        final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));

                        selectedOrderItemListArray.add(orderItemListModel);
                        selectedItemListArray.set(
                                Integer.parseInt(v.getTag().toString()), true);

                        if (selectedItemListArray.size() == selectedOrderItemListArray
                                .size()) {
                            if (activity instanceof PickByOrderActivity) {
                                ((PickByOrderActivity) activity)
                                        .enablePickButton();

                                ((PickByOrderActivity) activity)
                                        .disablePartialPickButton();
                            }
                        } else {
                            if (activity instanceof PickByOrderActivity) {
                                ((PickByOrderActivity) activity)
                                        .disablePickButton();

                                if (selectedOrderItemListArray.size() > 0) {
                                    ((PickByOrderActivity) activity)
                                            .enablePartialPickButton();
                                }
                            }
                        }
                    } else {
                        if (is_usn_base) {
                            final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_yellow));
                        } else {
                            final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_custom_cell));
                        }

                        if (selectedOrderItemListArray
                                .contains(orderItemListModel)) {
                            selectedOrderItemListArray
                                    .remove(orderItemListModel);
                            selectedItemListArray.set(
                                    Integer.parseInt(v.getTag().toString()),
                                    false);

                            if (selectedItemListArray.size() == selectedOrderItemListArray
                                    .size()) {
                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .enablePickButton();

                                    ((PickByOrderActivity) activity)
                                            .disablePartialPickButton();
                                }
                            } else {
                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .disablePickButton();

                                    if (selectedOrderItemListArray.size() > 0) {
                                        ((PickByOrderActivity) activity)
                                                .enablePartialPickButton();
                                    } else {
                                        ((PickByOrderActivity) activity)
                                                .disablePartialPickButton();
                                    }
                                }
                            }
                        }
                    }
                }
            });

            holder.lblSKUEditText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int action = event.getAction();

                    if (action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER
                            && v == final_holder.lblSKUEditText) {
                        hideKeyBoard(v);

                        OrderItemListModel orderItemListModel = getItem(Integer.parseInt(final_holder.lblSKUEditText.getTag().toString()));

                        if (final_holder.lblSKUEditText.getText().toString().length() > 0) {
                            if ((orderItemListModel.getSku().equalsIgnoreCase(final_holder.lblSKUEditText.getText().toString())) || (orderItemListModel.getUPC().equalsIgnoreCase(final_holder.lblSKUEditText.getText().toString()))) {
                                int pickedQty = 0;
                                pickedQty = Integer.parseInt(final_holder.lblPickedQty.getText().toString());
                                pickedQtyArray.set(Integer.parseInt(final_holder.lblSKUEditText.getTag().toString()), String.valueOf(pickedQty + 1));
                                final_holder.lblPickedQty.setText(String.valueOf(pickedQty + 1));

                                if (final_holder.lblPickedQty.getText().toString().equalsIgnoreCase(orderItemListModel.getQuantity())) {
                                    final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                                    final_holder.lblSKUEditText.setVisibility(View.GONE);
                                }

                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .showErrorMessage("Scanned Successfully", false);
                                }

                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .enablePartialPickButton();
                                }

                                if (orderQtyArray.equals(pickedQtyArray)) {
                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .enablePickButton();

                                        ((PickByOrderActivity) activity)
                                                .disablePartialPickButton();
                                    }
                                }
                            } else {
                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .showErrorMessage("Invalid SKU/UPC Scanned", true);
                                }
                            }
                            final_holder.lblSKUEditText.setText("");
                            final_holder.lblSKUEditText.requestFocus();
                        }
                        return true;
                    }
                    return false;
                }
            });

            holder.lblSKUEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int keyCode,
                                              KeyEvent event) {
                    if (keyCode == EditorInfo.IME_ACTION_DONE
                            && v == final_holder.lblSKUEditText) {
                        hideKeyBoard(v);

                        OrderItemListModel orderItemListModel = getItem(Integer
                                .parseInt(v.getTag().toString()));

                        if (v.getText().toString().length() > 0) {
                            if ((orderItemListModel.getSku().equalsIgnoreCase(v.getText().toString())) || (orderItemListModel.getUPC().equalsIgnoreCase(v.getText().toString()))) {
                                int pickedQty = 0;
                                pickedQty = Integer.parseInt(final_holder.lblPickedQty.getText().toString());
                                pickedQtyArray.set(Integer
                                        .parseInt(v.getTag().toString()), String.valueOf(pickedQty + 1));
                                final_holder.lblPickedQty.setText(String.valueOf(pickedQty + 1));

                                if (final_holder.lblPickedQty.getText().toString().equalsIgnoreCase(orderItemListModel.getQuantity())) {
                                    final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                                    final_holder.lblSKUEditText.setVisibility(View.GONE);
                                }

                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .showErrorMessage("Scanned Successfully", false);
                                }

                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .enablePartialPickButton();
                                }

                                if (orderQtyArray.equals(pickedQtyArray)) {
                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .enablePickButton();

                                        ((PickByOrderActivity) activity)
                                                .disablePartialPickButton();
                                    }
                                }
                            } else {
                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .showErrorMessage("Invalid SKU/UPC Scanned", true);
                                }
                            }
                            final_holder.lblSKUEditText.setText("");
                            final_holder.lblSKUEditText.requestFocus();
                        } else {
                            if (activity instanceof PickByOrderActivity) {
                                ((PickByOrderActivity) activity)
                                        .showErrorMessage("Please Enter/Scan SKU/UPC", true);
                            }
                        }

                        return true;
                    }
                    return false;
                }
            });

            holder.lblUSN.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int action = event.getAction();

                    if (action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER
                            && v == final_holder.lblUSN) {
                        hideKeyBoard(v);

                        OrderItemListModel orderItemListModel = getItem(Integer.parseInt(final_holder.lblUSN.getTag().toString()));

                        if (final_holder.lblUSN.getText().toString().length() > 0) {
                            if (scannedUSNList.contains(final_holder.lblUSN.getText().toString().toUpperCase()) || scannedIMEIList.contains(final_holder.lblUSN.getText().toString().toUpperCase())) {
                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .showErrorMessage("Already Scanned", true);
                                }
                            } else {
                                int pickedQty = 0;
                                if (orderItemListModel.getPossible_usns().contains(final_holder.lblUSN.getText().toString().toUpperCase())) {
                                    pickedQty = Integer.parseInt(final_holder.lblPickedQty.getText().toString());
                                    pickedQtyArray.set(Integer.parseInt(final_holder.lblUSN.getTag().toString()), String.valueOf(pickedQty + 1));
                                    final_holder.lblPickedQty.setText(String.valueOf(pickedQty + 1));

                                    if (final_holder.lblPickedQty.getText().toString().equalsIgnoreCase(orderItemListModel.getQuantity())) {
                                        final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                                        final_holder.lblUSN.setVisibility(View.GONE);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .showErrorMessage("Scanned Successfully", false);
                                    }

                                    scannedUSNList.add(final_holder.lblUSN.getText().toString().toUpperCase());

                                    String previousUSN = allUSNList.get(Integer.parseInt(final_holder.lblUSN.getTag().toString()));
                                    if (previousUSN.equalsIgnoreCase("")) {
                                        previousUSN = final_holder.lblUSN.getText().toString().toUpperCase();
                                    } else {
                                        previousUSN = previousUSN + "," + final_holder.lblUSN.getText().toString().toUpperCase();
                                    }
                                    allUSNList.set(Integer.parseInt(final_holder.lblUSN.getTag().toString()), previousUSN);

                                    int tempIndex = orderItemListModel.getPossible_usns().indexOf(final_holder.lblUSN.getText().toString().toUpperCase());
                                    if (tempIndex != -1 && orderItemListModel.getPossible_serial_numbers().size() > tempIndex) {
                                        String SN = orderItemListModel.getPossible_serial_numbers().get(tempIndex);
                                        scannedIMEIList.add(SN);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .enablePartialPickButton();
                                    }

                                    if (orderQtyArray.equals(pickedQtyArray)) {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .enablePickButton();

                                            ((PickByOrderActivity) activity)
                                                    .disablePartialPickButton();
                                        }
                                    }
                                } else if (orderItemListModel.getPossible_serial_numbers().contains(final_holder.lblUSN.getText().toString().toUpperCase())) {
                                    pickedQty = Integer.parseInt(final_holder.lblPickedQty.getText().toString());
                                    pickedQtyArray.set(Integer.parseInt(final_holder.lblUSN.getTag().toString()), String.valueOf(pickedQty + 1));
                                    final_holder.lblPickedQty.setText(String.valueOf(pickedQty + 1));

                                    if (final_holder.lblPickedQty.getText().toString().equalsIgnoreCase(orderItemListModel.getQuantity())) {
                                        final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                                        final_holder.lblUSN.setVisibility(View.GONE);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .showErrorMessage("Scanned Successfully", false);
                                    }

                                    scannedIMEIList.add(final_holder.lblUSN.getText().toString().toUpperCase());
                                    int tempIndex = orderItemListModel.getPossible_serial_numbers().indexOf(final_holder.lblUSN.getText().toString().toUpperCase());
                                    if (tempIndex != -1 && orderItemListModel.getPossible_usns().size() > tempIndex) {
                                        String USN = orderItemListModel.getPossible_usns().get(tempIndex);
                                        scannedUSNList.add(USN);

                                        String previousUSN = allUSNList.get(Integer.parseInt(final_holder.lblUSN.getTag().toString()));
                                        if (previousUSN.equalsIgnoreCase("")) {
                                            previousUSN = USN;
                                        } else {
                                            previousUSN = previousUSN + "," + USN;
                                        }
                                        allUSNList.set(Integer.parseInt(final_holder.lblUSN.getTag().toString()), previousUSN);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .enablePartialPickButton();
                                    }

                                    if (orderQtyArray.equals(pickedQtyArray)) {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .enablePickButton();

                                            ((PickByOrderActivity) activity)
                                                    .disablePartialPickButton();
                                        }
                                    }
                                } else {
                                    if (orderItemListModel.getIs_serial_number_base().equalsIgnoreCase("true")) {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .showErrorMessage("Invalid USN/SN/IMEI Scanned", true);
                                        }
                                    } else {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .showErrorMessage("Invalid USN Scanned", true);
                                        }
                                    }
                                }
                            }
                            final_holder.lblUSN.setText("");
                            final_holder.lblUSN.requestFocus();
                        }
                        return true;
                    }
                    return false;
                }
            });

            holder.lblUSN.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int keyCode,
                                              KeyEvent event) {
                    if (keyCode == EditorInfo.IME_ACTION_DONE
                            && v == final_holder.lblUSN) {
                        hideKeyBoard(v);

                        OrderItemListModel orderItemListModel = getItem(Integer
                                .parseInt(v.getTag().toString()));

                        if (v.getText().toString().length() > 0) {
                            if (scannedUSNList.contains(v.getText().toString().toUpperCase()) || scannedIMEIList.contains(v.getText().toString().toUpperCase())) {
                                if (activity instanceof PickByOrderActivity) {
                                    ((PickByOrderActivity) activity)
                                            .showErrorMessage("Already Scanned", true);
                                }
                            } else {
                                int pickedQty = 0;
                                if (orderItemListModel.getPossible_usns().contains(v.getText().toString().toUpperCase())) {
                                    pickedQty = Integer.parseInt(final_holder.lblPickedQty.getText().toString());
                                    pickedQtyArray.set(Integer
                                            .parseInt(v.getTag().toString()), String.valueOf(pickedQty + 1));
                                    final_holder.lblPickedQty.setText(String.valueOf(pickedQty + 1));

                                    if (final_holder.lblPickedQty.getText().toString().equalsIgnoreCase(orderItemListModel.getQuantity())) {
                                        final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                                        final_holder.lblUSN.setVisibility(View.GONE);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .showErrorMessage("Scanned Successfully", false);
                                    }

                                    scannedUSNList.add(v.getText().toString().toUpperCase());

                                    String previousUSN = allUSNList.get(Integer.parseInt(v.getTag().toString()));
                                    if (previousUSN.equalsIgnoreCase("")) {
                                        previousUSN = v.getText().toString().toUpperCase();
                                    } else {
                                        previousUSN = previousUSN + "," + v.getText().toString().toUpperCase();
                                    }
                                    allUSNList.set(Integer.parseInt(v.getTag().toString()), previousUSN);

                                    int tempIndex = orderItemListModel.getPossible_usns().indexOf(v.getText().toString().toUpperCase());
                                    if (tempIndex != -1 && orderItemListModel.getPossible_serial_numbers().size() > tempIndex) {
                                        String SN = orderItemListModel.getPossible_serial_numbers().get(tempIndex);
                                        scannedIMEIList.add(SN);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .enablePartialPickButton();
                                    }

                                    if (orderQtyArray.equals(pickedQtyArray)) {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .enablePickButton();
                                        }

                                        ((PickByOrderActivity) activity)
                                                .disablePartialPickButton();
                                    }
                                } else if (orderItemListModel.getPossible_serial_numbers().contains(v.getText().toString().toUpperCase())) {
                                    pickedQty = Integer.parseInt(final_holder.lblPickedQty.getText().toString());
                                    pickedQtyArray.set(Integer
                                            .parseInt(v.getTag().toString()), String.valueOf(pickedQty + 1));
                                    final_holder.lblPickedQty.setText(String.valueOf(pickedQty + 1));

                                    if (final_holder.lblPickedQty.getText().toString().equalsIgnoreCase(orderItemListModel.getQuantity())) {
                                        final_holder.pickRow.setBackgroundColor(getContext().getResources().getColor(R.color.text_green));
                                        final_holder.lblUSN.setVisibility(View.GONE);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .showErrorMessage("Scanned Successfully", false);
                                    }

                                    scannedIMEIList.add(v.getText().toString().toUpperCase());
                                    int tempIndex = orderItemListModel.getPossible_serial_numbers().indexOf(v.getText().toString().toUpperCase());
                                    if (tempIndex != -1 && orderItemListModel.getPossible_usns().size() > tempIndex) {
                                        String USN = orderItemListModel.getPossible_usns().get(tempIndex);
                                        scannedUSNList.add(USN);

                                        String previousUSN = allUSNList.get(Integer.parseInt(v.getTag().toString()));
                                        if (previousUSN.equalsIgnoreCase("")) {
                                            previousUSN = USN;
                                        } else {
                                            previousUSN = previousUSN + "," + USN;
                                        }
                                        allUSNList.set(Integer.parseInt(v.getTag().toString()), previousUSN);
                                    }

                                    if (activity instanceof PickByOrderActivity) {
                                        ((PickByOrderActivity) activity)
                                                .enablePartialPickButton();
                                    }

                                    if (orderQtyArray.equals(pickedQtyArray)) {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .enablePickButton();
                                        }

                                        ((PickByOrderActivity) activity)
                                                .disablePartialPickButton();
                                    }
                                } else {
                                    if (orderItemListModel.getIs_serial_number_base().equalsIgnoreCase("true")) {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .showErrorMessage("Invalid USN/SN/IMEI Scanned", true);
                                        }
                                    } else {
                                        if (activity instanceof PickByOrderActivity) {
                                            ((PickByOrderActivity) activity)
                                                    .showErrorMessage("Invalid USN Scanned", true);
                                        }
                                    }
                                }
                            }
                            final_holder.lblUSN.setText("");
                            final_holder.lblUSN.requestFocus();
                        } else {
                            if (activity instanceof PickByOrderActivity) {
                                ((PickByOrderActivity) activity)
                                        .showErrorMessage("Please Enter/Scan USN/SN/IMEI", true);
                            }
                        }

                        return true;
                    }
                    return false;
                }
            });
        }

        holder.checkboxStatus.setTag(position);
        holder.lblSKUEditText.setTag(position);
        holder.lblUSN.setTag(position);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (pickingPreference.equalsIgnoreCase("scan")) {
            return orderQtyArray.size();
        } else {
            return orderItemArray.size();
        }
    }

    private static class ViewHolder {
        private LinearLayout pickRow;
        private LinearLayout layoutSKU;
        private LinearLayout layoutUSN;
        private LinearLayout layoutBatch;
        private LinearLayout layoutExpiry;

        private TextView lblProductKey;
        private TextView lblProductSKUKey;
        private TextView lblProductUPCKey;
        private TextView lblProductShelfKey;
        private TextView lblProductQtyKey;
        private TextView lblUSNKey;
        private TextView lblSKUKey;
        private TextView lblPickedQtyKey;
        private TextView lblBatchKey;
        private TextView lblExpiryKey;

        private TextView lblProductColon;
        private TextView lblProductSKUColon;
        private TextView lblProductUPCColon;
        private TextView lblProductShelfColon;
        private TextView lblProductQtyColon;
        private TextView lblUSNColon;
        private TextView lblSKUColon;
        private TextView lblPickedQtyColon;
        private TextView lblBatchColon;
        private TextView lblExpiryColon;

        private TextView lblIndex;
        private TextView lblSKU;
        private TextView lblUPC;
        private TextView lblProduct;
        private TextView lblQty;
        private TextView lblShelf;
        private ImageView productImage;
        private EditText lblUSN;
        private EditText lblSKUEditText;
        private TextView lblPickedQty;
        private CheckBox checkboxStatus;
        private TextView lblBatch;
        private TextView lblExpiry;
    }

    // Method to hide KeyBoard
    private void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    // Method to validate url startWith http:// or not?
    private String ensure_has_protocol(final String requestedURL) {
        if (!requestedURL.startsWith("http://")
                && !requestedURL.startsWith("https://")) {
            return "http://" + requestedURL;
        }
        return requestedURL;
    }
}
