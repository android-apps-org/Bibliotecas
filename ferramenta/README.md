# [Android Widgets](https://developer.android.com/guide/topics/appwidgets/overview)

## [RemoteViews](https://developer.android.com/reference/android/widget/RemoteViews)

- Widget Layouts are based on RemoteViews
- RemoteView is used to describe view hierarchy
- Widget is treated as a separate app that runs on the home screen
- Internally widgets communicate with the app using broadcast messages
- This makes widgets BroadcastReceivers
- Need to be registered in manifest with receiver tag
- Android Studio -> app -> New -> Widget -> AppWidget
  - Let Android Studio generate widget files
- updatePeriodMillis: 86_400_000 (24 hours)
- how RemoteViews handle clickEvents:
  - link to PendingIntent
  - will launch once View (widget) is clicked
  ```
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plant_widget);

    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    views.setOnClickPendingIntent(R.id.iv_widget_plant, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetId, views);
  ```

## [PendingIntent](https://developer.android.com/reference/kotlin/android/app/PendingIntent)

- Wrapper around Intent
- But allows other apps to have access to run intent on your app

## Resources

- [App Widget Design Guidelines](https://tool.oschina.net/uploads/apidocs/android/guide/practices/ui_guidelines/widget_design.html)
- [AppWidgetManager](https://developer.android.com/reference/android/appwidget/AppWidgetManager)

