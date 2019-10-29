package com.fbapicking.controller.adapter;

import com.fbapicking.model.user.UserRoleModel;
import com.fbapicking.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RoleAdapter extends ArrayAdapter<UserRoleModel> {
    Typeface font;
    Activity activity;

    public RoleAdapter(Activity activity, Context context) {
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
                    R.layout.row_role, parent, false);

            // get controls
            holder.lblRole = (TextView) convertView
                    .findViewById(R.id.lblRole);
            holder.lblRole.setTypeface(font);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserRoleModel currentRole = getItem(position);
        if (currentRole != null) {
            holder.lblRole
                    .setText(currentRole.getDisplay_name());
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView lblRole;
    }
}
