package edu.grinnell.sandb;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Relevant Constants used throughout the appication;
 *
 * @Author prabir on 3/7/16, AppDev Grinnell.
 * @Author Albert Owusu-Asare
 */
public class Constants {

    /* convert the font size saved in settings to sp */
    public static final int[] FONT_SIZE_TO_SP = {12, 14, 16, 18};

    /* Remote Service Constants */
    public static final String PUBLIC_API =
            "https://public-api.wordpress.com/rest/v1.1/sites/www.thesandb.com/";
    public static final String JSON_API_URL =
            "http://www.thesandb.com/api/get_recent_posts?count=50/";
    public static final int DEFAULT_NUM_ARTICLES_PER_PAGE = 10;
    public static String KEY_CLIENT = "Client"; // Key for bundling the client object in a fragment
    public static final int DEFAULT_HTTP_CODE = 200;

    /* Numeric Constants */
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int FIRST_PAGE = ONE;

    /* HTTP Acces Codes */
    public static final int OK = 200;

    /* Table Names */
    public enum TableNames{
        ARTICLE("Article"),
        CATEGORY("Category");
        private final String name;

        private TableNames(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    /* Animation constants */
    public static final int TOOLBAR_ANIMATION_DURATION = 200;

    /* Snack Bar Messages */

    public enum SnackBarMessages{
        CONNECTED("Connection Established"),
        CHECK_UPDATES("Checking for New Articles ..."),
        FOUND_NEW("Found new articles.."),
        NEW_STORIES("New Stories");
        private final String name;

        SnackBarMessages(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    /* Application state constants */
    public static boolean FIRST_CALL_TO_UPDATE = true;
    public static final String SELECTED_TAB = "selected_tab";

    /* Article Category constants */
    public static String ARTICLE_CATEGORY_KEY = "category";
    public static final Map<String, String> titleToKey = new LinkedHashMap<String, String>(); // LinkedHashMap retains insertion ordering
    public static final String[] CATEGORIES;

    static {
        titleToKey.put("All", "all");
        titleToKey.put("News", "news");
        titleToKey.put("Arts", "arts");
        titleToKey.put("Community", "community");
        titleToKey.put("Features", "features");
        titleToKey.put("Opinion", "opinion");
        titleToKey.put("Sports", "sports");
        CATEGORIES = titleToKey.keySet().toArray(new String[titleToKey.size()]);
    }

    public enum ArticleCategories {
        ALL("All"),
        NEWS("News"),
        ARTS("Arts"),
        COMMUNITY("Community"),
        FEATURES("Features"),
        OPINION("Opinion"),
        SPORTS("Sports");
        private final String name;

        ArticleCategories(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName == null) ? false : name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum UpdateType {
        REFRESH,
        NEXT_PAGE,
        INITIALIZE
    }

    public final class ArticleTableColumnNames {
        public static final String CATEGORY ="category";
        public static final String REALM_DATE ="realmDate";
        private ArticleTableColumnNames(){

        }
    }


}
