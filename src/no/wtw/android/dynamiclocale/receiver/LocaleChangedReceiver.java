package no.wtw.android.dynamiclocale.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import no.wtw.android.dynamiclocale.DynamicLocale;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EReceiver;

@EReceiver
public class LocaleChangedReceiver extends BroadcastReceiver {

    @Bean
    protected DynamicLocale i18n;

    @Override
    public void onReceive(Context context, Intent intent) {
        i18n.localeChanged();
    }

}
