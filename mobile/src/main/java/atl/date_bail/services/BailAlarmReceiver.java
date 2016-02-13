package atl.date_bail.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import atl.date_bail.MainActivity;
import atl.date_bail.R;

public class BailAlarmReceiver extends BroadcastReceiver {
    public static final int BAIL_NOTI_ID = 1945;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            showNotification(context, intent.getExtras().getLong("id", -1));
        } catch (Exception e) {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showNotification(Context context, long maybeid) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1945, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Intent delayIntent = new Intent(context, DelayBailService.class);
        delayIntent.putExtra("id", maybeid);
        PendingIntent delay = PendingIntent.getService(context, 1001, delayIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent bailIntent = new Intent(context, BailingService.class);
        bailIntent.putExtra("id", maybeid);
        PendingIntent bail = PendingIntent.getService(context, 1002, bailIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_edit_black_48dp)
                .setContentTitle("Just checking in.")
                .setContentText("Would you like to bail or nail?");
        mBuilder.addAction(R.drawable.ic_thumb_up_white_48dp, "Later", delay);
        mBuilder.addAction(R.drawable.ic_thumb_down_white_48dp, "BAIL!", bail);
        mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(BAIL_NOTI_ID, mBuilder.build());
    }
}
