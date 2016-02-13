package atl.date_bail.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Alan on 13/02/2016.
 */
public class DelayedAlarmService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DelayedAlarmService(String name) {
        super(name);
    }

    public DelayedAlarmService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BailAlarmer ba = new BailAlarmer();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            ba.setAlarm(getBaseContext(), bundle.getLong("id", -1));
    }
}
