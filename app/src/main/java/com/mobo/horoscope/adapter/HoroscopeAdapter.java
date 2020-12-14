package com.mobo.horoscope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobo.horoscope.R;
import com.mobo.horoscope.bean.Horoscope;
import com.mobo.horoscope.bean.TextStyleSetting;

import java.util.List;

/**
 * 十二星座九宫格图片数据适配器
 */
public class HoroscopeAdapter extends RecyclerView.Adapter<HoroscopeAdapter.ItemViewHolder>
        implements View.OnClickListener {
    private List<Horoscope> mHoroscopeList;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    /**
     * @param horoscopeList
     */
    public HoroscopeAdapter(List<Horoscope> horoscopeList) {
        mHoroscopeList = horoscopeList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horoscope, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        setTvStyle(new TextView[]{holder.mName,holder.mDate});
        holder.itemView.setTag(R.id.tv_analysis, position);
        holder.itemView.setOnClickListener(this);

        Horoscope horoscope = mHoroscopeList.get(position);
        holder.mImage.setImageResource(horoscope.getIconId_2());
        holder.mName.setText(horoscope.getName());
        holder.mDate.setText(horoscope.getDate());
    }

    /** */
    private void setTvStyle(TextView[] textViews){
        for (TextView tv:textViews){
            TextStyleSetting.setTvStyleFandolSong(tv,context);
        }
    }

    @Override
    public int getItemCount() {
        if (mHoroscopeList == null) {
            return 0;
        }
        return mHoroscopeList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener == null) {
            return;
        }
        int position = (int) v.getTag(R.id.tv_analysis);
        mOnItemClickListener.onItemClick(position, mHoroscopeList.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Horoscope horoscope);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mName;
        private TextView mDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.iv_image);
            mName = itemView.findViewById(R.id.tv_analysis);
            mDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
