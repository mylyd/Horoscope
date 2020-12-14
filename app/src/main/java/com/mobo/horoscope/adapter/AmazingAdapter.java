package com.mobo.horoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.AmazingInfo;
import com.mobo.horoscope.bean.TextStyleSetting;

import java.util.List;

public class AmazingAdapter extends RecyclerView.Adapter<AmazingAdapter.ViewHolder> {
    List<AmazingInfo> mlist;
    private Context context;

    public AmazingAdapter(List<AmazingInfo> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amazing,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AmazingInfo amazingInfo = mlist.get(position);
        holder.mAmazingNameSubtitle.setText(amazingInfo.getTvSubtitle()+" ");
        holder.mAmazingNameText.setText(amazingInfo.getTvText());
        TextStyleSetting.setTvStyleRobotoBold(holder.mAmazingNameSubtitle,context);
        TextStyleSetting.setTvStyleFandolSong(holder.mAmazingNameText,context);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAmazingNameSubtitle;
        private TextView mAmazingNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAmazingNameSubtitle = itemView.findViewById(R.id.tv_amazing_name_text_subtitle);
            mAmazingNameText = itemView.findViewById(R.id.tv_amazing_name_text_text);
        }
    }
}
