package com.example.wisps;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class WarnInfoAdapter extends RecyclerView.Adapter<WarnInfoAdapter.ViewHolder> {

    private static final String TAG = WarnInfoAdapter.class.getSimpleName();

    private List<String> infoList;

    public WarnInfoAdapter(List<String> infoList) {
        this.infoList = infoList;
    }

    private OnChangeCompleteStateListener mListener;

    private boolean isChecked = false;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @NonNull
    @Override
    public WarnInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_warning_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WarnInfoAdapter.ViewHolder holder, int position) {
        String info = infoList.get(position);
        holder.warnInfo.setText(info);
        holder.checkBox.setChecked(isChecked);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: " + isChecked + ": " + position);
                if (mListener != null) {
                    if (isChecked == true) {
                        mListener.onIsCheckedTrue(info);
                    } else {
                        mListener.onIsCheckedFalse(info);
                    }
                }
            }
        });
        holder.itemView.findViewById(R.id.layout_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + position);
            }
        });
    }

    /**
     * 删除info
     * @param info
     */
    public void deleteInfo(String info) {
        infoList.remove(info);
        Log.d(TAG, "deleteInfo: ");
        for (String str : infoList) {
            Log.d(TAG, str);
        }
        notifyDataSetChanged();
    }


    /**
     * 添加info
     * @param info
     */
    public void addInfo(String info) {
        infoList.add(info);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView warnInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            warnInfo = (TextView) itemView.findViewById(R.id.warning_time);
        }
    }

    public interface OnChangeCompleteStateListener {
        void onIsCheckedTrue(String info);

        void onIsCheckedFalse(String info);
    }

    /**
     * 给Adapter设置CheckBox发生改变的监听
     * @param onChangeCompleteStateListener
     */
    public void setOnChangeCompleteStateListener(OnChangeCompleteStateListener onChangeCompleteStateListener) {
        this.mListener = onChangeCompleteStateListener;
    }
}
