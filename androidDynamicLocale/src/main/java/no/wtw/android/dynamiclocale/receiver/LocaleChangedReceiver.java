package no.wtw.android.dynamiclocale.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import no.wtw.android.dynamiclocale.DynamicLocale;

public class LocaleChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DynamicLocale.getInstance(context).localeChanged();
    }

}
