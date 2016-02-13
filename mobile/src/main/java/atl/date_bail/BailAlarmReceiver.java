package atl.date_bail;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Alan on 13/02/2016.
 */
public class BailAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            showNotification(context    );
        } catch (Exception e) {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
            new Intent(context, MainActivity.class), 0);

        PendingIntent delay = PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), 0);
        PendingIntent bail = PendingIntent.getActivity(context, -1, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Just checking in.")
                .setContentText("Would you like to bail or nail?")
                .addAction(R.drawable.ic_thumb_up_white_48dp, "Later", delay)
                .addAction(R.drawable.ic_thumb_down_white_48dp, "BAIL!", delay);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
