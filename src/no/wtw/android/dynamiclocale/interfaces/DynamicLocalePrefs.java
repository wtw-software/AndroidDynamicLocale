package no.wtw.android.dynamiclocale.interfaces;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface DynamicLocalePrefs {

    @DefaultBoolean(true)
    boolean needsLanguageUpdate();

}
