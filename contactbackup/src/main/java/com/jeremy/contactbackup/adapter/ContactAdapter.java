package com.jeremy.contactbackup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremy.contactbackup.R;
import com.jeremy.contactbackup.beans.ContactInfoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangjun on 2017/3/7.
 * 通讯录适配器
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<ContactInfoEntity> mContactInfoEntities;

    public ContactAdapter(Context context, List<ContactInfoEntity> contactInfoEntities) {
        layoutInflater = LayoutInflater.from(context);
        this.mContactInfoEntities = contactInfoEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvName.setText(mContactInfoEntities.get(position).name);
        //先隐藏所有显示电话号码的TextView
        for (int i = 0; i < holder.llPhoneNumbers.getChildCount(); i++) {
            holder.llPhoneNumbers.getChildAt(i).setVisibility(View.GONE);
        }
        for (int i = 0; i < mContactInfoEntities.get(position).phoneNumbers.size(); i++) {
            if (holder.llPhoneNumbers.getChildCount() > i) {
                ((TextView) holder.llPhoneNumbers.getChildAt(i)).setText(mContactInfoEntities.get(position).phoneNumbers.get(i));
                //再显示需要显示电话号码的TextView
                holder.llPhoneNumbers.getChildAt(i).setVisibility(View.VISIBLE);
            } else {
                holder.createTextView().setText(mContactInfoEntities.get(position).phoneNumbers.get(i));
                //再显示需要显示电话号码的TextView
                holder.llPhoneNumbers.getChildAt(i).setVisibility(View.VISIBLE);
            }
        }
        //设置复选框点击监听器并设置当前选中状态
        holder.cbChoose.setOnCheckedChangeListener(new CheckedChangeListener(position));
        holder.cbChoose.setChecked(mContactInfoEntities.get(position).isChecked);
    }

    private class CheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        CheckedChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mContactInfoEntities.get(position).isChecked = isChecked;
        }
    }

    @Override
    public int getItemCount() {
        return mContactInfoEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_choose)
        CheckBox cbChoose;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_phone_numbers)
        LinearLayout llPhoneNumbers;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * 在LinearLayout中添加显示电话号码的TextView
         *
         * @return 返回添加成功的TextView，如果能创建则返回创建成功的view，否则返回null
         */
        TextView createTextView() {
            TextView tvPhoneNumber = new TextView(llPhoneNumbers.getContext());
            //最多添加5个电话号码，在下面为子View添加id
            switch (llPhoneNumbers.getChildCount()) {
                case 0:
                    tvPhoneNumber.setId(R.id.phoneNumber1);
                    break;
                case 1:
                    tvPhoneNumber.setId(R.id.phoneNumber2);
                    break;
                case 2:
                    tvPhoneNumber.setId(R.id.phoneNumber3);
                    break;
                case 3:
                    tvPhoneNumber.setId(R.id.phoneNumber4);
                    break;
                case 4:
                    tvPhoneNumber.setId(R.id.phoneNumber5);
                    break;
                default:
                    break;
            }
            if (llPhoneNumbers.getChildCount() < 5) {
                int height = llPhoneNumbers.getContext().getResources().getDimensionPixelSize(R.dimen.textview_height);
                tvPhoneNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                tvPhoneNumber.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                tvPhoneNumber.setVisibility(View.GONE);
                llPhoneNumbers.addView(tvPhoneNumber);
            } else {
                tvPhoneNumber = null;
            }
            return tvPhoneNumber;
        }
    }
}
