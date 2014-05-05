package no.wtw.android.dynamiclocale;

import android.content.Context;
import no.wtw.android.dynamiclocale.interfaces.DynamicLocalePrefs_;
import no.wtw.android.dynamiclocale.interfaces.DynamicWord;
import no.wtw.android.dynamiclocale.interfaces.PersistentWordProvider;
import no.wtw.visitoslo.oslopass.android.utility.Log;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@EBean(scope = EBean.Scope.Singleton)
public class DynamicLocale {

    private static final String TAG = DynamicLocale.class.getSimpleName();
    private HashMap<String, String> words;
    private HashMap<Integer, String> defaultWords;

    @Pref
    protected DynamicLocalePrefs_ prefs;
    @RootContext
    protected Context context;
    protected PersistentWordProvider wordProvider;

    public DynamicLocale setPersistentWordProvider(PersistentWordProvider provider) {
        wordProvider = provider;
        loadPersistentWords();
        return this;
    }

    @AfterInject
    protected void init() {
        loadDefaultWords();
    }

    private void loadDefaultWords() {
        defaultWords = new HashMap<>();
        Field[] fields = getStringFields();
        for (Field field : fields) {
            try {
                defaultWords.put(field.getInt(null), field.getName().replace("R_string_", ""));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    protected Field[] getStringFields() {
        try {
            String packageName = context.getApplicationContext().getPackageName();
            return context.getClassLoader().loadClass(packageName + ".R$string").getFields();
        } catch (ClassNotFoundException e) {
            return new Field[0];
        }
    }

    private void loadPersistentWords() {
        words = new HashMap<>();
        try {
            if (wordProvider != null) {
                List<DynamicWord> wordList = wordProvider.getPersistentWords();
                for (DynamicWord w : wordList)
                    words.put(w.getKey(), w.getValue());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static String _(int resourceId, Context context) {
        return DynamicLocale_.getInstance_(context)._(resourceId);
    }

    public String _(int resourceId) {
        return getString(resourceId);
    }

    private String getString(int resourceId) {
        String resourceFieldName = defaultWords.get(resourceId);
        if (words != null && words.containsKey(resourceFieldName))
            return words.get(resourceFieldName);
        else
            return context.getString(resourceId);
    }

    public void saveNewWords(List<DynamicWord> newWords) {
        if (newWords.size() == 0)
            return;
        if (wordProvider != null && wordProvider.setPersistentWords(newWords)) {
            prefs.edit().needsLanguageUpdate().put(false).apply();
            loadPersistentWords();
        }
    }

    public void localeChanged() {
        Log.d(TAG, "Locale changed to " + Locale.getDefault());
        prefs.edit().needsLanguageUpdate().put(true).apply();
    }

    public boolean needsUpdate() {
        return prefs.needsLanguageUpdate().get();
    }

}
