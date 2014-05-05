package no.wtw.android.dynamiclocale.implementations.activeandroid;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import no.wtw.android.dynamiclocale.interfaces.DynamicWord;
import no.wtw.android.dynamiclocale.interfaces.PersistentWordProvider;
import no.wtw.visitoslo.oslopass.android.utility.Log;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ActiveAndroidPersistentWordProvider implements PersistentWordProvider {

    private static final String TAG = ActiveAndroidPersistentWordProvider.class.getSimpleName();

    @Override
    public List<DynamicWord> getPersistentWords() {
        ArrayList<DynamicWord> dynamicWords = new ArrayList<>();
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
            Log.d(TAG, "Inserting " + newWords.size() + " new words");
            for (DynamicWord newWord : newWords) {
                Log.d(TAG, "Inserting: " + newWord.getKey() + ": " + newWord.getValue());
                newWord.savePersistent();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        ActiveAndroid.endTransaction();
        return success;
    }
}
