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

package net.istorya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.istorya.data.Categories;
import net.istorya.network.Http;
import net.istorya.network.HttpListener;
import net.istorya.parser.CategoryParser;
import net.istorya.parser.CategoryParserListener;

public class SplashActivity extends Activity implements HttpListener, CategoryParserListener {

    private static final String CATEGORY = "CATEGORY";

    private Http mHttp;
    private CategoryParser mCategoryParser;

    private void enableProgressIndicator(boolean state) {
	LinearLayout splash = (LinearLayout) findViewById(R.id.splash_progress);

	if (state) {
	    splash.addView(findViewById(R.id.splash_progress_indicator));
	} else {
	    splash.removeView(findViewById(R.id.splash_progress_indicator));
	}

    }

    @Override
    public void onCategoryParserError(int error) {
	enableProgressIndicator(false);

	switch (error) {
	    case CategoryParser.INVALID_INPUT:
		setStatusIndicator("Unable to parse data.");
		break;

	    default:
		setStatusIndicator("Unknown parser error.");
		break;
	}
    }

    @Override
    public void onCategoryParserFinish(Categories categories) {
	setStatusIndicator("Starting...");

	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	intent.putParcelableArrayListExtra("CATEGORIES", categories);
	startActivity(intent);

	finish();
    }

    @Override
    public void onCategoryParserStart() {
	setStatusIndicator("Reading...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_splash);

	mHttp = new Http(getApplicationContext());
	mHttp.setHttpListener(this);

	mCategoryParser = new CategoryParser();
	mCategoryParser.setCategoryParserListener(this);
    }

    @Override
    public void onHttpError(int error) {
	enableProgressIndicator(false);

	switch (error) {
	    case Http.NO_NETWORK_AVAILABLE:
		setStatusIndicator("No network available.");
		break;

	    case Http.CONNECTION_ERROR:
		setStatusIndicator("Network connection error.");
		break;

	    case Http.CONNECTION_TIMEOUT:
		setStatusIndicator("Network connection timeout.");
		break;

	    case Http.HTTP_NOT_OK:
		setStatusIndicator("Server replied not ok.");
		break;

	    default:
		setStatusIndicator("Unknown network error.");
		break;
	}

	new Handler().postDelayed(new Runnable() {
	    @Override
	    public void run() {
		finish();
	    }
	}, 5000);
    }

    @Override
    public void onHttpFinish(String url, byte[] data, String tag) {
	if (tag.equals(CATEGORY)) {
	    mCategoryParser.parse(data);
	}
    }

    @Override
    public void onHttpStart() {
	setStatusIndicator("Connecting...");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    finish();

	    return true;
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
	super.onPause();

	mHttp.cancel(true);
	mCategoryParser.cancel(true);
    }

    @Override
    protected void onResume() {
	super.onResume();

	mHttp.getURL("http://www.istorya.net/forums/", CATEGORY);
    }

    private void setStatusIndicator(String text) {
	((TextView) findViewById(R.id.splash_status_indicator)).setText(text);
    }
}
