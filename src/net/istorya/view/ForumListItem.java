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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.istorya.R;
import net.istorya.data.Forum;

public class ForumListItem extends LinearLayout implements OnClickListener {

    private ForumListItemListener mForumListItemListener;
    private Forum mForum;
    private int mDrawable;

    public ForumListItem(Context context, Forum forum, int drawable) {
	super(context);

	mForum = forum;
	mDrawable = drawable;

	setOrientation(LinearLayout.HORIZONTAL);
	setBackgroundDrawable(getResources().getDrawable(drawable));

	setClickable(true);
	setOnClickListener(this);

	LayoutInflater inflater = LayoutInflater.from(context);

	LinearLayout forumIcon = (LinearLayout) inflater.inflate(R.layout.forum_list_item_icon, null);
	LinearLayout forumText = (LinearLayout) inflater.inflate(R.layout.forum_list_item_text, null);

	((TextView) forumText.findViewById(R.id.forum_list_item_title)).setText(forum.title);
	((TextView) forumText.findViewById(R.id.forum_list_item_description)).setText(forum.description);

	addView(forumIcon);
	addView(forumText);
    }

    public Forum getForum() {
	return mForum;
    }

    @Override
    public void onClick(View view) {
	if (mForumListItemListener != null) {
	    mForumListItemListener.onForumListItemSelected((ForumListItem) view);
	}
    }

    public void setFocus(boolean focus) {
	setBackgroundDrawable(getResources().getDrawable(focus ? R.drawable.selector_forum_focus : mDrawable));
    }

    public void setForumListItemListener(ForumListItemListener listener) {
	mForumListItemListener = listener;
    }
}
