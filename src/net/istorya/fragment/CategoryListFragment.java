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

package net.istorya.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.viewpagerindicator.TitlePageIndicator;

import net.istorya.R;
import net.istorya.data.Categories;
import net.istorya.network.Http;
import net.istorya.network.HttpListener;
import net.istorya.parser.CategoryParser;
import net.istorya.parser.CategoryParserListener;
import net.istorya.view.CategoryListPagerAdapter;
import net.istorya.view.ForumListItem;
import net.istorya.view.ForumListItemListener;

public class CategoryListFragment extends SherlockFragment implements CategoryParserListener, ForumListItemListener, HttpListener {

    private static final String CATEGORY = "CATEGORY";

    public static CategoryListFragment newInstance(Categories categories) {
	Bundle arguments = new Bundle();
	arguments.putParcelable("categories", categories);

	CategoryListFragment fragment = new CategoryListFragment();
	fragment.setArguments(arguments);

	return fragment;
    }

    private Context mContext;
    private Categories mCategories;

    private Http mHttp;
    private CategoryParser mCategoryParser;

    private int mSelectedPage;
    private ForumListItem mSelectedForum;

    private CategoryListFragmentListener mCategoryListFragmentListener;

    public void clearForumSelection() {
	if (mSelectedForum != null) {
	    mSelectedForum.setFocus(false);
	    mSelectedForum = null;
	}
    }

    private void createCategoryPager() {
	mSelectedPage = mSelectedPage >= mCategories.size() ? 0 : mSelectedPage;

	CategoryListPagerAdapter adapter = new CategoryListPagerAdapter(mContext);
	adapter.setForumListItemListener(this);
	adapter.setCategories(mCategories);

	ViewPager pager = (ViewPager) getSherlockActivity().findViewById(R.id.viewpager);
	pager.setAdapter(adapter);

	TitlePageIndicator indicator = (TitlePageIndicator) getSherlockActivity().findViewById(R.id.indicator);
	indicator.setViewPager(pager);
	indicator.setCurrentItem(mSelectedPage);

	indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	    @Override
	    public void onPageSelected(int position) {
		mSelectedPage = position;
	    }
	});
    }

    public boolean isForumSelected() {
	return mSelectedForum != null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	createCategoryPager();
    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);

	mContext = activity.getApplicationContext();
	mCategoryListFragmentListener = (CategoryListFragmentListener) activity;
    }

    @Override
    public void onCategoryParserError(int error) {
	if (isVisible()) {
	    mCategoryListFragmentListener.onReloadCategoryListError();
	}
    }

    @Override
    public void onCategoryParserFinish(Categories categories) {
	mCategories = categories;

	if (isVisible()) {
	    createCategoryPager();
	    mCategoryListFragmentListener.onReloadCategoryListFinish();
	}
    }

    @Override
    public void onCategoryParserStart() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRetainInstance(true);

	mCategories = getArguments().getParcelable("categories");
    }

    @Override
    public void onCreateForumListItem(ForumListItem item) {
	if (mSelectedForum != null && mSelectedForum.getForum().equals(item.getForum())) {
	    mSelectedForum = item;
	    mSelectedForum.setFocus(true);
	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_forum_list, container, false);
	return view;
    }

    @Override
    public void onForumListItemSelected(ForumListItem item) {
	if (mSelectedForum != null) {
	    mSelectedForum.setFocus(false);
	}

	mSelectedForum = item;
	mSelectedForum.setFocus(true);

	mCategoryListFragmentListener.onForumSelected(mSelectedForum.getForum());
    }

    @Override
    public void onHttpError(int error) {
	if (isVisible()) {
	    mCategoryListFragmentListener.onReloadCategoryListError();
	}
    }

    @Override
    public void onHttpFinish(String url, byte[] data, String tag) {
	if (tag.equals(CATEGORY)) {
	    mCategoryParser = new CategoryParser();
	    mCategoryParser.setCategoryParserListener(this);
	    mCategoryParser.parse(data);
	}
    }

    @Override
    public void onHttpStart() {
	mCategoryListFragmentListener.onReloadCategoryListStart();
    }

    public void refreshCategoryList() {
	mHttp = new Http(mContext);
	mHttp.setHttpListener(this);
	mHttp.getURL("http://www.istorya.net/forums/", CATEGORY);
    }
}
