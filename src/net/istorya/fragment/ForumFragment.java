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

package net.istorya.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import net.istorya.R;
import net.istorya.data.Forum;

public class ForumFragment extends SherlockFragment {

    private Forum mForum;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	if (savedInstanceState != null) {
	    if (savedInstanceState.containsKey("mForum")) {
		mForum = savedInstanceState.getParcelable("mForum");
	    }
	}
	TextView forumTitle = (TextView) getSherlockActivity().findViewById(R.id.forum_text);
	forumTitle.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
	forumTitle.setTextColor(Color.DKGRAY);
	forumTitle.setTextSize(14);
	forumTitle.setText(mForum.title + "...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_forum, container, false);
	return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	savedInstanceState.putParcelable("mForum", mForum);
	super.onSaveInstanceState(savedInstanceState);
    }

    public void setForum(Forum forum) {
	mForum = forum;
    }
}
