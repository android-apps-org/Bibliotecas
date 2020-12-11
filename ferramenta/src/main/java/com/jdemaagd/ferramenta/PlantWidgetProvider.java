package com.jdemaagd.ferramenta;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.jdemaagd.ferramenta.provider.PlantContract;
import com.jdemaagd.ferramenta.ui.MainActivity;
import com.jdemaagd.ferramenta.ui.PlantDetailActivity;

public class PlantWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int imgRes, long plantId, boolean showWater, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        RemoteViews views;

        if (width < 300) {
            views = getSinglePlantRemoteView(context, imgRes, plantId, showWater);
        } else {
            views = getGardenGridRemoteView(context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PlantWateringService.startActionUpdatePlantWidgets(context);
    }

    /**
     * Updates all widget instances given the widget Ids and display information
     *
     * @param context          The calling context
     * @param appWidgetManager The widget manager
     * @param imgRes           The image resource for single plant mode
     * @param plantId          The database ID for that plant
     * @param showWater        Boolean to show/hide water drop button
     * @param appWidgetIds     Array of widget Ids to be updated
     */
    public static void updatePlantWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int imgRes, long plantId, boolean showWater, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, imgRes, plantId, showWater, appWidgetId);
        }
    }

    /**
     * Creates RemoteViews to be displayed in single plant mode widget
     *
     * @param context   The context
     * @param imgRes    The image resource of the plant image to be displayed
     * @param plantId   The database plant Id for watering button functionality
     * @param showWater Boolean to either show/hide the water drop
     * @return The RemoteViews for the single plant mode widget
     */
    private static RemoteViews getSinglePlantRemoteView(Context context, int imgRes, long plantId, boolean showWater) {
        Intent intent;

        if (plantId == PlantContract.INVALID_PLANT_ID) {
            intent = new Intent(context, MainActivity.class);
        } else {
            Log.d(PlantWidgetProvider.class.getSimpleName(), "plantId=" + plantId);
            intent = new Intent(context, PlantDetailActivity.class);
            intent.putExtra(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plant_widget);

        views.setImageViewResource(R.id.iv_widget_plant, imgRes);
        views.setTextViewText(R.id.tv_widget_plant_name, String.valueOf(plantId));

        if (showWater) views.setViewVisibility(R.id.iv_widget_water, View.VISIBLE);
        else views.setViewVisibility(R.id.iv_widget_water, View.INVISIBLE);

        views.setOnClickPendingIntent(R.id.iv_widget_plant, pendingIntent);

        Intent wateringIntent = new Intent(context, PlantWateringService.class);
        wateringIntent.setAction(PlantWateringService.ACTION_WATER_PLANT);

        wateringIntent.putExtra(PlantWateringService.EXTRA_PLANT_ID, plantId);
        PendingIntent wateringPendingIntent = PendingIntent.getService(
                context, 0, wateringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.iv_widget_water, wateringPendingIntent);

        return views;
    }

    /**
     * Creates RemoteViews to be displayed in GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    private static RemoteViews getGardenGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.gv_widget, intent);

        Intent appIntent = new Intent(context, PlantDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.gv_widget, appPendingIntent);

        views.setEmptyView(R.id.gv_widget, R.id.rl_empty_view);

        return views;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        PlantWateringService.startActionUpdatePlantWidgets(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }
}