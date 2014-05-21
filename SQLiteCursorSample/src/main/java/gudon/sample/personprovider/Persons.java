package gudon.sample.personprovider;

/*
* コンテンツプロバイダ - 愚鈍人 さまより
* http://ichitcltk.hustle.ne.jp/gudon/modules/pico_rd/index.php?content_id=75
* */

import android.net.Uri;
import android.provider.BaseColumns;

final public class Persons implements BaseColumns {
    public static final String AUTHORITY = "gudon.sample.personprovider.personprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/persons");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.gudon.persons";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gudon.persons";

    public static final String NAME = "name";
    public static final String AGE = "age";
}
