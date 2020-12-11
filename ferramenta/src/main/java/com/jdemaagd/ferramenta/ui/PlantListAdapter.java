package com.jdemaagd.ferramenta.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jdemaagd.ferramenta.R;
import com.jdemaagd.ferramenta.provider.PlantContract.PlantEntry;
import com.jdemaagd.ferramenta.utils.PlantUtils;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.PlantViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    /**
     * Constructor
     *
     * @param context the calling context/activity
     */
    public PlantListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
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
        View view = inflater.inflate(R.layout.plant_list_item, parent, false);

        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlantViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(PlantEntry._ID);
        int createTimeIndex = mCursor.getColumnIndex(PlantEntry.COLUMN_CREATION_TIME);
        int waterTimeIndex = mCursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERED_TIME);
        int plantTypeIndex = mCursor.getColumnIndex(PlantEntry.COLUMN_PLANT_TYPE);

        long plantId = mCursor.getLong(idIndex);
        int plantType = mCursor.getInt(plantTypeIndex);
        long createdAt = mCursor.getLong(createTimeIndex);
        long wateredAt = mCursor.getLong(waterTimeIndex);
        long timeNow = System.currentTimeMillis();

        int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);

        holder.plantImageView.setImageResource(imgRes);
        holder.plantNameView.setText(String.valueOf(plantId));
        holder.plantImageView.setTag(plantId);
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (mCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    /**
     * Return count of items in cursor
     *
     * @return Number of items in the cursor, or 0 if null
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;

        return mCursor.getCount();
    }

    /**
     * ViewHolder for RecyclerView item
     */
    class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImageView;
        TextView plantNameView;

        public PlantViewHolder(View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.iv_plant);
            plantNameView = itemView.findViewById(R.id.tv_plant_name);
        }
    }
}