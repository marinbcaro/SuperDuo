package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
/**
 * Created by carolinamarin on 8/30/15.
 */


/**
 * IntentService which handles updating all Today widgets with the latest data
 */
public class ScoresWidgetIntentService extends IntentService {


    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL,
    };


    public ScoresWidgetIntentService() {
        super("ScoresWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoresWidgetProvider.class));


        Uri scoreWithDateUri = DatabaseContract.scores_table.buildScoreWithDate();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = formatter.format(today);
        Cursor data = getContentResolver().query(scoreWithDateUri, SCORES_COLUMNS, null,
                new String[]{todayDate}, DatabaseContract.scores_table.HOME_GOALS_COL + " ASC");



        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(getPackageName(),
                    R.layout.widget_layout);


            Intent svcIntent = new Intent(getApplicationContext(), WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                    svcIntent);


            views.setEmptyView(R.id.listViewWidget, R.id.empty_view);


            Intent startActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.listViewWidget, startActivityPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }
        data.close();
    }

//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
//        views.setRemoteAdapter(R.id.widget_list,
//                new Intent(context, ScoresWidgetIntentService.class));
//    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, ScoresWidgetIntentService.class));
    }

    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_list,
                new Intent(context, ScoresWidgetIntentService.class));
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        // views.setContentDescription(R.id.widget_icon, description);
    }
}