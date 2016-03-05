package edu.grinnell.sandb.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;

import edu.grinnell.sandb.ArticleFetchTask;
import edu.grinnell.sandb.Fragments.ArticleListFragment;
import edu.grinnell.sandb.R;
import edu.grinnell.sandb.Util.VersionUtil;

/* The main activity that the app will initialize to. This activity hosts the ArticleListFragment */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SELECTED_TAB = "selected_tab";

    private ArticleListFragment mListFrag;
    private ViewPager mPager;
    private TabsAdapter mTabsAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private boolean mUpdateInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        // set transition things for lollipop
        if (VersionUtil.isLollipop()) {
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
        }

        // setup tool bar
        Toolbar tooldbar = (Toolbar) findViewById(R.id.toolbar_main);

        // setup navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, ArticleListFragment.CATEGORIES));


        // setup a pager to scroll between category tabs
        mPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
        addTabs(mTabsAdapter);

        mPager.setAdapter(mTabsAdapter);

        // setup the sliding tab strip
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mPager);


        if (savedInstanceState != null) {
            mPager.setCurrentItem(savedInstanceState.getInt(SELECTED_TAB), false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "B3PJX5MJNYMNSB9XQS3P");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void updateArticles() {
        if (!mUpdateInProgress) {
            String url = "http://www.thesandb.com/api/get_recent_posts?count=50/";
            String[] params = {url};
            ArticleFetchTask task = new ArticleFetchTask(getApplicationContext()) {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mTabsAdapter.setRefreshing(true);
                    mUpdateInProgress = true;
                    Toast.makeText(getApplicationContext(), "OnPreExecute", Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void onPostExecute(Integer status) {
                    super.onPostExecute(status);
                    if (status != 0) {
                        Toast.makeText(getApplicationContext(), "Error Downloading Articles", Toast.LENGTH_SHORT).show();
                    }
                    // Clear the loading bar when the articles are loaded
                    mUpdateInProgress = false;
                    mTabsAdapter.setRefreshing(false);
                    mTabsAdapter.refresh();
                    Toast.makeText(getApplicationContext(), "POSTExecute", Toast.LENGTH_SHORT).show();
                }
            };
            task.execute(params);

        }
    }


    // Add the category tabs
    private void addTabs(TabsAdapter ta) {
        for (String category : ArticleListFragment.CATEGORIES) {
            Bundle args = new Bundle();
            args.putString(ArticleListFragment.ARTICLE_CATEGORY_KEY, category);
            ta.addTab(ArticleListFragment.class, args);
        }
    }

    /* FragmentPagerAdapter to handle the category tabs */
    public class TabsAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<TabInfo> tabs = new ArrayList<>();

        final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addTab(Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tabs.add(info);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = tabs.get(position);
            mListFrag = (ArticleListFragment) Fragment.instantiate(
                    MainActivity.this, info.clss.getName(), info.args);
            return mListFrag;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ArticleListFragment.CATEGORIES[position];
        }

        public void refresh() {
            for (int i = 0; i < getCount(); i++) {
                Fragment f = getSupportFragmentManager().findFragmentByTag(
                        getFragmentTag(i));
                if (f != null)
                    ((ArticleListFragment) f).update();
            }
        }

        private String getFragmentTag(int pos) {
            return "android:switcher:" + R.id.pager + ":" + pos;
        }

        public void setRefreshing(boolean refreshing) {
            for (int i = 0; i < getCount(); i++) {
                Fragment f = getSupportFragmentManager().findFragmentByTag(
                        getFragmentTag(i));
                if (f != null)
                    ((ArticleListFragment) f).setRefreshing(refreshing);
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(SELECTED_TAB, mPager.getCurrentItem());
        super.onSaveInstanceState(state);
    }

}
