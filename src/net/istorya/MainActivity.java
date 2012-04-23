/*
 * Copyright (C) 2012 Henry James M. Banogon Jr.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.istorya;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.istorya.data.Categories;
import net.istorya.data.Forum;
import net.istorya.fragment.CategoryListFragment;
import net.istorya.fragment.CategoryListFragmentListener;

public class MainActivity extends SherlockFragmentActivity implements CategoryListFragmentListener {

    private static final String LOADING = "LOADING";
    private static final String CATEGORIES = "CATEGORIES";

    private Menu mMenu;
    private boolean mLoading;
    private Toast mToast;

    public void enableLoadingIndicator(boolean loading) {
	MenuItem refresh = mMenu.findItem(R.id.menu_refresh);

	refresh.setActionView(null);

	if (loading) {
	    refresh.setActionView(R.layout.menu_loading_indicator);
	}

	mLoading = loading;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_main);
	getSupportActionBar().setHomeButtonEnabled(true);

	mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);

	if (savedInstanceState == null) {
	    SherlockFragment fragment = CategoryListFragment.newInstance((Categories) getIntent().getParcelableExtra(CATEGORIES));

	    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
	    transaction.add(R.id.main, fragment, CATEGORIES);
	    transaction.commit();
	} else {
	    mLoading = savedInstanceState.getBoolean(LOADING, false);
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getSupportMenuInflater().inflate(R.menu.main_menu, menu);

	return true;
    }

    @Override
    public void onForumSelected(Forum forum) {
	enableLoadingIndicator(true);
	showPopupMessage("Loading....");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    showPopupMessage(null);

	    CategoryListFragment categoryList = (CategoryListFragment) getSupportFragmentManager().findFragmentByTag(CATEGORIES);

	    if (categoryList.isVisible() && categoryList.isForumSelected()) {
		enableLoadingIndicator(false);
		categoryList.clearForumSelection();

		return true;
	    }
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case android.R.id.home:
		showPopupMessage("iSTORYA.NET 0.1.0");

		break;

	    case R.id.menu_refresh:
		CategoryListFragment categoryList = (CategoryListFragment) getSupportFragmentManager().findFragmentByTag(CATEGORIES);

		if (categoryList.isVisible()) {
		    categoryList.refreshCategoryList();
		}

		/*
		new Handler().postDelayed(new Runnable() {
		    @Override
		    public void run() {
			enableLoadingIndicator(false);
		    }
		}, 5000);
		*/

		break;
	}

	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	mMenu = menu;

	if (mLoading) {
	    enableLoadingIndicator(true);
	}

	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onReloadCategoryListError() {
	enableLoadingIndicator(false);
	showPopupMessage("Unable to reload category list.");
    }

    @Override
    public void onReloadCategoryListFinish() {
	enableLoadingIndicator(false);
	showPopupMessage(null);
    }

    @Override
    public void onReloadCategoryListStart() {
	enableLoadingIndicator(true);
	showPopupMessage("Reloading....");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	mLoading = savedInstanceState.getBoolean(LOADING, false);

	super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
	outState.putBoolean(LOADING, mLoading);

	super.onSaveInstanceState(outState);
    }

    public void showPopupMessage(String text) {
	if (text != null) {
	    mToast.cancel();
	    mToast.setText(text);
	    mToast.show();
	} else {
	    mToast.cancel();
	}
    }

    /*
    private void showFragment(Forum forum) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

    if (forum != null) {
        mForumFragment.setForum(forum);
    } else {
        enableLoadingIndicator(false);

        if (!categmCategoriesFragmentsible()) {
    	transaction.replace(R.id.main, categmCategoriesFragment      }
    }

    transaction.commit();
    }
    */
}
