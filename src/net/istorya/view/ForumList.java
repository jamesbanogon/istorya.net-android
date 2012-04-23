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
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import net.istorya.R;
import net.istorya.data.Forum;

public class ForumList extends ScrollView {

    private Context mContext;
    private String mTitle;
    private LinearLayout mCategoryList;
    private ForumListItemListener mForumListItemListener;

    public ForumList(Context context) {
	super(context);

	mContext = context;
	mCategoryList = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.forum_list, null);

	addView(mCategoryList);
    }

    public void addItem(Forum forum, int drawable) {
	ForumListItem item = new ForumListItem(mContext, forum, drawable);

	if (mForumListItemListener != null) {
	    item.setForumListItemListener(mForumListItemListener);
	    mForumListItemListener.onCreateForumListItem(item);
	}

	mCategoryList.addView(item);
    }

    public String getTitle() {
	return mTitle;
    }

    public void setForumListItemListener(ForumListItemListener listener) {
	mForumListItemListener = listener;
    }

    public void setTitle(String title) {
	mTitle = title;
    }
}
