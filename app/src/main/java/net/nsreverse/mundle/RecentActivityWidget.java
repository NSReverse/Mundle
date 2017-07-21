package net.nsreverse.mundle;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import net.nsreverse.mundle.data.UserDefaults;

/**
 * Implementation of App Widget functionality.
 */
public class RecentActivityWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String lastUsername = UserDefaults.getDefaultUsername(context);
        int classCount = UserDefaults.WidgetInfo.getClassSubsciptionCount(context);
        int noteCount = UserDefaults.WidgetInfo.getNoteCount(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recent_activity_widget);
        views.setTextViewText(R.id.widget_text_view_last_username, lastUsername);
        views.setTextViewText(R.id.widget_text_view_class_count, "" + classCount);
        views.setTextViewText(R.id.widget_text_view_note_count, "" + noteCount);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

