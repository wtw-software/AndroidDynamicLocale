package no.wtw.android.dynamiclocale.interfaces;

import java.util.List;

public interface PersistentWordProvider {

    public List<DynamicWord> getPersistentWords();

    public boolean setPersistentWords(List<DynamicWord> newWords);

}
