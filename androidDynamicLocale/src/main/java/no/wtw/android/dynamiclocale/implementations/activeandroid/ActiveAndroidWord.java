package no.wtw.android.dynamiclocale.implementations.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;
import no.wtw.android.dynamiclocale.interfaces.DynamicWord;

@Table(name = ActiveAndroidWord.DB.TABLE)
public class ActiveAndroidWord extends Model implements DynamicWord {

    @Column(name = DB.COLUMN_KEY, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @SerializedName("key")
    protected String key;
    @Column(name = DB.COLUMN_VALUE)
    @SerializedName("value")
    protected String value;

    public ActiveAndroidWord() {
        super();
    }

    public ActiveAndroidWord(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void savePersistent() {
        save();
    }

    public static class DB {
        public static final String TABLE = "Words";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_VALUE = "value";
    }

}