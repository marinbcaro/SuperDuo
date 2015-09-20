package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by carolinamarin on 9/11/15.
 */
public class ListProvider implements RemoteViewsFactory {

    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL,
    };
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private ContentResolver resolver = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent, ContentResolver resolver) {
        this.context = context;
        this.resolver = resolver;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {


        Uri scoreWithDateUri = DatabaseContract.scores_table.buildScoreWithDate();
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = formatter.format(today);
        Cursor data = this.resolver.query(scoreWithDateUri, SCORES_COLUMNS, null,
                new String[]{todayDate}, DatabaseContract.scores_table.TIME_COL + " ASC");

        if (data == null) {
            return;
        }

        if (data.moveToFirst()) {
            do {
                String home = data.getString(data.getColumnIndex("home"));
                String away = data.getString(data.getColumnIndex("away"));
                String home_goals = data.getString(data.getColumnIndex("home_goals"));
                String away_goals = data.getString(data.getColumnIndex("away_goals"));
                String time_game = data.getString(data.getColumnIndex("time"));
                ListItem listItem = new ListItem();
                listItem.home_name = home;
                listItem.away_name = away;
                listItem.home_score = home_goals;
                listItem.away_score = away_goals;
                listItem.time_game = time_game;
                listItemList.add(listItem);
            } while (data.moveToNext());
        }
        data.close();
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        ListItem listItem = listItemList.get(position);
        String home_score="0";
        String away_score="0";
        if(!listItem.home_score.equals("-1")){
            home_score=listItem.home_score;
        }
        if(!listItem.away_score.equals("-1")){
            away_score=listItem.away_score;
        }

        remoteView.setTextViewText(R.id.home_name, listItem.home_name);
        remoteView.setTextViewText(R.id.away_name, listItem.away_name);
        remoteView.setTextViewText(R.id.home_score,home_score);
        remoteView.setTextViewText(R.id.away_score, away_score);
        remoteView.setTextViewText(R.id.time_game, listItem.time_game);

        Intent fillInIntent = new Intent();
        remoteView.setOnClickFillInIntent(R.id.item_layout, fillInIntent);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
    }

}
