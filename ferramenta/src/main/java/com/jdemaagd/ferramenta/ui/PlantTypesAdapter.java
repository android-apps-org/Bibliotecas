package com.jdemaagd.ferramenta.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdemaagd.ferramenta.R;
import com.jdemaagd.ferramenta.utils.PlantUtils;

public class PlantTypesAdapter extends RecyclerView.Adapter<PlantTypesAdapter.PlantViewHolder> {

    Context mContext;
    TypedArray mPlantTypes;

    /**
     * Constructor
     *
     * @param context the calling context/activity
     */
    public PlantTypesAdapter(Context context) {
        mContext = context;
        Resources res = mContext.getResources();
        mPlantTypes = res.obtainTypedArray(R.array.plant_types);
    }

    /**
     * Called RecyclerView needs new ViewHolder of given type to represent an item
     *
     * @param parent   The ViewGroup into which the new View will be added
     * @param viewType The view type of the new View
     * @return A new PlantViewHolder that holds a View with the plant_list_item layout
     */
    @Override
    public PlantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.plant_types_list_item, parent, false);

        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlantViewHolder holder, int position) {
        int imgRes = PlantUtils.getPlantImgRes(
                mContext, position,
                PlantUtils.PlantStatus.ALIVE,
                PlantUtils.PlantSize.FULLY_GROWN);
        holder.plantImageView.setImageResource(imgRes);
        holder.plantTypeText.setText(PlantUtils.getPlantTypeName(mContext, position));
        holder.plantImageView.setTag(position);
    }

    /**
     * Return count of items in cursor
     *
     * @return Number of items in the cursor, or 0 if null
     */
    @Override
    public int getItemCount() {
        if (mPlantTypes == null) return 0;

        return mPlantTypes.length();
    }

    /**
     * ViewHolder for RecyclerView item
     */
    class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImageView;
        TextView plantTypeText;

        public PlantViewHolder(View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.iv_plant_type);
            plantTypeText = itemView.findViewById(R.id.tv_plant_type);
        }
    }
}