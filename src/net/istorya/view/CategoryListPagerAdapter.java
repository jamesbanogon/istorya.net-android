/*
 * Copyright (C) 2012 Henry James M. Banogon Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package net.istorya.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.viewpagerindicator.TitleProvider;

import net.istorya.R;
import net.istorya.data.Categories;

import java.util.ArrayList;

public class CategoryListPagerAdapter extends PagerAdapter implements TitleProvider {

    private Context mContext;
    private ArrayList<ForumList> mCategoryList;
    private ForumListItemListener mForumListItemListener;

    public CategoryListPagerAdapter(Context context) {
	mContext = context;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {
	((ViewPager) pager).removeView((ForumList) view);
    }

    @Override
    public int getCount() {
	return mCategoryList.size();
    }

    @Override
    public String getTitle(int position) {
	return mCategoryList.get(position).getTitle().toUpperCase().replace("TECHNOLOGY", "TECH");
    }

    @Override
    public Object instantiateItem(View pager, int position) {
	if (mCategoryList == null || mCategoryList.size() == 0) {
	    return null;
	}

	ForumList forumList = mCategoryList.get(position);

	((ViewPager) pager).addView(forumList);

	return forumList;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
	return view.equals(object);
    }

    public void setCategories(Categories categories) {
	int categoryListSize = categories.size();

	mCategoryList = new ArrayList<ForumList>();

	for (int c = 0; c < categoryListSize; c++) {
	    ForumList forumList = new ForumList(mContext);

	    forumList.setTitle(categories.get(c).title);
	    forumList.setForumListItemListener(mForumListItemListener);

	    int forumListSize = categories.get(c).forums.size();

	    int drawable = forumListSize % 2 == 0 ? R.drawable.selector_forum_even : R.drawable.selector_forum_odd;
	    forumList.setBackgroundDrawable(mContext.getResources().getDrawable(drawable));

	    for (int f = 0; f < forumListSize; f++) {
		drawable = f % 2 == 0 ? R.drawable.selector_forum_even : R.drawable.selector_forum_odd;
		forumList.addItem(categories.get(c).forums.get(f), drawable);
	    }

	    mCategoryList.add(forumList);
	}
    }

    public void setForumListItemListener(ForumListItemListener listener) {
	mForumListItemListener = listener;
    }
}