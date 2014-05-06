package no.wtw.android.dynamiclocale.implementations.activeandroid;

import android.util.Log;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import no.wtw.android.dynamiclocale.interfaces.DynamicWord;
import no.wtw.android.dynamiclocale.interfaces.PersistentWordProvider;

import java.util.ArrayList;
import java.util.List;

public class ActiveAndroidPersistentWordProvider implements PersistentWordProvider {

    private static final String TAG = ActiveAndroidPersistentWordProvider.class.getSimpleName();

    @Override
    public List<DynamicWord> getPersistentWords() {
        ArrayList<DynamicWord> dynamicWords = new ArrayList<DynamicWord>();
        List<ActiveAndroidWord> activeAndroidWords = new Select().from(ActiveAndroidWord.class).execute();
        for (ActiveAndroidWord word : activeAndroidWords)
            dynamicWords.add(word);
        return dynamicWords;
    }

    @Override
    public boolean setPersistentWords(List<DynamicWord> newWords) {
        boolean success = false;
        ActiveAndroid.beginTransaction();
        try {
            for (DynamicWord newWord : newWords) {
                Log.d(TAG, "Inserting persistent word: " + newWord.toString());
                newWord.savePersistent();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        } catch (Exception e) {
        }
        ActiveAndroid.endTransaction();
        return success;
    }
}
