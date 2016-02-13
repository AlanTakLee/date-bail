package atl.date_bail.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Alan on 13/02/2016.
 */
public class DelayBailService extends IntentService {

    public DelayBailService() {
        super("");
    }

    public DelayBailService(String name){
        super(name);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager mNotificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(BailAlarmReceiver.BAIL_NOTI_ID);
        long maybeId = intent.getExtras().getLong("id", -1);
        if(maybeId > -1){
            BailAlarmer ba = new BailAlarmer();
            ba.setAlarm(getBaseContext(), maybeId);

        }
    }
}
