package atl.date_bail.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class BailAlarmer {
    public static final int BAIL_CODE = 1738;

    public BailAlarmer() {}
    public void setAlarm(Context context, long dateId) {
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 15 minutes to the calendar object
//        cal.add(Calendar.MINUTE, 15);
        cal.add(Calendar.SECOND, 3);
        Intent intent = new Intent(context, BailAlarmReceiver.class);
        intent.putExtra("id", dateId);
        PendingIntent sender = PendingIntent.getBroadcast(context, BAIL_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }

}
