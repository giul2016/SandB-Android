package edu.grinnell.sandb.Services.Interfaces;

import android.util.Pair;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Map;

import edu.grinnell.sandb.Model.Article;
import edu.grinnell.sandb.Model.RealmArticle;
import io.realm.Realm;

/**
 *
 * Extracts the main functionality of a local cache client.
 * All the classes that implement this class will provide concrete implementations as to how to
 * interact with the different Android local caching systems.
 *
 * @author Albert Owusu-Asare
 * @version 0.1 Wed Mar 16 01:09:53 CDT 2016
 */
public interface LocalCacheClient {
    /**
     * Persists a list of article objects to the local cache.
     * @param articles the list of articles.
     * @return {@code true} if the persistence was successful.
     */
    void saveArticles(List<RealmArticle> articles);

    /**
     * Persists an article object to the local cache.
     * @return {@code true} if the persistence was successful.
     */
   void saveArticle(RealmArticle article);

    /**
     * Returns the Article that sits on top  of the local  cache.
     *
     * <p> Note that it may be assumed that the data is stored in chronological order such that
     * the most recent {@link Article} in terms of time posted will be the most recent entry in the
     * Article cache.</p>
     * @return
     */
    RealmArticle getFirst();

    /**
     * Returns a list of the most recent articles belonging to a particular category.
     *
     * Note that the number of articles returns depends on a set default value for what a page is.
     * This value may differ per implementation.
     * @param categoryName the category to query the local cache by.
     * @return the list of Articles of category : "category".
     */
    List<RealmArticle> getArticlesByCategory(String categoryName);

    /**
     * @return a list of all the categories represented in the Articles cache.
     */
    List<String> getCategories();
    /**
     * Returns the next page of results as specified by the set default value for the number of
     * articles in a page.
     *
     * @param categoryName the category to query from
     * @param currentPageNumber the page that last accessed.
     * @param lastVisibleArticleDate the date of the last visible Article. This is useful
     *                               to query the local database for the next page
     * @return a list of Articles satisfying the query.
     */
    List<Article> getNextPage(String categoryName, int currentPageNumber,String lastVisibleArticleDate);

    /**
     * @return true if the cache is empty.
     */
    boolean isCacheEmpty();
    /**
     * Returns all the articles
     * @return list of all the articles
     */
    List<RealmArticle> getAll();

    /**
     * Drops the table referenced to by clazz in the SQLiteDb
     * @param tableName the name of the SQLite table
     */
    void deleteAllEntries(String tableName);


    /**
     * Returns a list of Articles posted from {@code date} and belonging to the category
     * {@code category}
     * @param category
     * @param date
     * @return the list of articles satisfying the query.
     */
    List<RealmArticle> getArticlesAfter(String category,String date);

    /**
     * Sets the number of articles per page.
     * <p> This is useful in determining how many articles to query for each page.</p>
     * @param numArticlesPerPage, the number of articles to query per page.
     */
    void setNumArticlesPerPage(int numArticlesPerPage);

    /**
     * @return the number of articles per page;
     */
    int getNumArticlesPerPage();

    /**
     * Updates the number of articles for all the categories
     */

    void updateCategorySizes();

    /**
     * Updates the number of articles of the specific category that exist in the database
     */

    void initialize();

    void updateNumEntriesPerCategory(String category,int updatedArticlesSize);
    void updateNumEntriesAll(int numRecentUpdates,String latestDateUpdated);
 Map<String, Pair<Integer, String>> getDbMetaData();





}
