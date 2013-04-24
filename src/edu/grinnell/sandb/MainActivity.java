package edu.grinnell.sandb;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import edu.grinnell.grinnellsandb.R;
import edu.grinnell.sandb.xmlpull.XmlPullReceiver;
import edu.grinnell.sandb.xmlpull.XmlPullService;

public class MainActivity extends SherlockFragmentActivity implements ArticleListFragment.Callbacks {
	
	private static final String TAG = "MainActivity";
	private static final String SELECTED_TAB = "selected_tab";
	
	private static final String[] CATEGORIES = {"All", "Arts", "Sports", "Community", "Opinion", "Features"};
	
	private PendingIntent mSendFeedLoaded;
	private ArticleListFragment mListFrag;
	private View mLoading;
	private ViewPager mPager;
	private TabsAdapter mTabsAdapter;
	
	private boolean mTwoPane = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLoading = (View) findViewById(R.id.loading);
		
	    // setup action bar for tabs
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    //mFragmentList = new ArrayList<ArticleListFragment>(actionBar.getNavigationItemCount());
	    

	    mPager = (ViewPager) findViewById(R.id.pager);
	    mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), mPager);
	    addTabs(actionBar, mTabsAdapter);
	    
		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_TAB));
		}

	    //TODO
//		Log.d(TAG, "Perparing to start service..");
//		mLoading.setVisibility(View.VISIBLE);
//		startXmlPullService();
	    
	}

	private void startXmlPullService() {
		Intent feedLoaded = new Intent();
		feedLoaded.setAction(XmlPullReceiver.FEED_PROCESSED);
		mSendFeedLoaded = PendingIntent.getBroadcast(this, 0, feedLoaded, 0);
		Intent loadFeed = new Intent(this, XmlPullService.class);
		loadFeed.setAction(XmlPullService.DOWNLOAD_FEED);
		loadFeed.putExtra(XmlPullService.RESULT_ACTION, mSendFeedLoaded);
		startService(loadFeed);
	}
	
	@Override
	protected void onNewIntent(Intent i) {
		String action = i.getAction();
		Log.d(TAG, "onNewIntent called | " + action);

		if (ArticleListFragment.UPDATE.equals(action)) {
			mListFrag.update();
			mLoading.setVisibility(View.GONE);
			findViewById(android.R.id.content).invalidate();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//if (mSendFeedLoaded != null) 
		//	mSendFeedLoaded.cancel();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			mLoading.setVisibility(View.VISIBLE);
			this.startXmlPullService();
			break;
		case R.id.menu_settings:
			// startActivityForResult(new Intent(this, PrefActiv.class), PREFS);
			break;
		default:
			
			break;
		}

		return false;
	}
	
	@Override
	public void onItemSelected(int position) {
		if(mTwoPane) {
			// add two pane layout
		} else {
			if (mSendFeedLoaded != null) mSendFeedLoaded.cancel();
		}		
	}

	@Override
	public void setListActivateState() {
		// TODO Auto-generated method stub
		
	}
	
	private void addTabs(ActionBar actionBar, TabsAdapter ta) {    
        for (String category : CATEGORIES) {
        	Bundle args = new Bundle();
            args.putString(ArticleListFragment.ARTICLE_CATEGORY_KEY, category);
            Tab tab = actionBar.newTab()
    	            .setText(category);
    	    ta.addTab(tab, ArticleListFragment.class, args);
        }
	}
	
	public class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {

		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(FragmentManager fm, ViewPager pager) {
			super(fm);
			mActionBar = getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(MainActivity.this, info.clss.getName(),
					info.args);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}
	
    @Override
    public void onSaveInstanceState(Bundle state)
    {
        state.putInt(SELECTED_TAB, getSupportActionBar().getSelectedNavigationIndex());
        super.onSaveInstanceState(state);
    }
    
}
