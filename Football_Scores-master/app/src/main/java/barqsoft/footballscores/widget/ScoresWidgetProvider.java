package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import barqsoft.footballscores.R;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by carolinamarin on 8/30/15.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, ScoresWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, ScoresWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        String ant = intent.getAction();
        String ant3 = myFetchService.ACTION_DATA_UPDATED;
        if (myFetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));


            if (Build.VERSION.SDK_INT >= 11) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
            } else {

                Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                context.sendBroadcast(update);
            }
            context.startService(new Intent(context, ScoresWidgetIntentService.class));
        }
    }
}
