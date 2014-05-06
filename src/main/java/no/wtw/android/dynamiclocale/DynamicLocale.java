package no.wtw.android.dynamiclocale;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import no.wtw.android.dynamiclocale.interfaces.DynamicWord;
import no.wtw.android.dynamiclocale.interfaces.PersistentWordProvider;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DynamicLocale {

    private static final String TAG = DynamicLocale.class.getSimpleName();
    private static final String NEEDS_LANGUAGE_UPDATE = "needs_language_update";
    private final Context context;
    private HashMap<String, String> words;
    private HashMap<Integer, String> defaultWords;
    private SharedPreferences prefs;
    private PersistentWordProvider wordProvider;
    private static DynamicLocale instance;

    public DynamicLocale(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("dynamic_locale", Context.MODE_PRIVATE);
        loadDefaultWords();
    }

    public static DynamicLocale getInstance(Context context) {
        if (instance == null)
            instance = new DynamicLocale(context);
        return instance;
    }

    public DynamicLocale setPersistentWordProvider(PersistentWordProvider provider) {
        wordProvider = provider;
        loadPersistentWords();
        return this;
    }

    private void loadDefaultWords() {
        defaultWords = new HashMap<Integer, String>();
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
        words = new HashMap<String, String>();
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
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(NEEDS_LANGUAGE_UPDATE, false);
            edit.commit();
            loadPersistentWords();
        }
    }

    public void localeChanged() {
        Log.d(TAG, "Locale changed to " + Locale.getDefault());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(NEEDS_LANGUAGE_UPDATE, true);
        edit.commit();
    }

    public boolean needsUpdate() {
        return prefs.getBoolean(NEEDS_LANGUAGE_UPDATE, true);
    }

}
