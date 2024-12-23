package wang.switchy.hin2n.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import wang.switchy.hin2n.R;
import wang.switchy.hin2n.entity.ConfigItemEntity;

/**
 * Created by janiszhang on 2018/5/6.
 */

public class SettingItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<ConfigItemEntity> mSettingItemEntities;
    private final LayoutInflater mLayoutInflater;

    public SettingItemAdapter(Context context, List<ConfigItemEntity> settingItemEntities) {
        mContext = context;
        mSettingItemEntities = settingItemEntities;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mSettingItemEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return mSettingItemEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final ConfigItemEntity configItemEntity = mSettingItemEntities.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_setting_item, null);
            viewHolder = new ViewHolder();
            viewHolder.root = (RelativeLayout) convertView.findViewById(R.id.item_container);
            viewHolder.settingName = (TextView) convertView.findViewById(R.id.tv_setting_name);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.iv_selected);
            viewHolder.moreInfo = (ImageButton) convertView.findViewById(R.id.iv_info);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        View.OnClickListener onSelect = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configItemEntity.mOnSelectBtnClickListener.onClick(position);
            }
        };

        viewHolder.root.setOnClickListener(onSelect);
        viewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                configItemEntity.mOnLongClickListener.onLongClick(position);
                return true;
            }
        });

        viewHolder.settingName.setText(configItemEntity.getSettingName());
        if (configItemEntity.isSelected()) {
            viewHolder.radioButton.setChecked(true);
        } else {
            viewHolder.radioButton.setChecked(false);
        }

        viewHolder.radioButton.setOnClickListener(onSelect);
        viewHolder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configItemEntity.mOnMoreBtnClickListener.onClick(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        RelativeLayout root;
        TextView settingName;
        RadioButton radioButton;
        ImageButton moreInfo;
    }
}
