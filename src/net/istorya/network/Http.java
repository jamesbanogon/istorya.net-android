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

package net.istorya.network;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.MalformedURLException;

public class Http {
    public static final int NO_NETWORK_AVAILABLE = 1;
    public static final int CONNECTION_ERROR = 2;
    public static final int CONNECTION_TIMEOUT = 3;
    public static final int HTTP_NOT_OK = 4;
    public static final int INVALID_URL = 5;

    private static String mAgent = "Android";
    private static int mConnectTimeout = 30000;
    private static int mReadTimeout = 30000;

    private Context mContext;
    private HttpListener mHttpListener;
    private HttpTask mHttp;

    public Http(Context context) {
	mContext = context;
    }

    public void cancel(boolean state) {
	if (mHttp != null) {
	    mHttp.cancel(state);
	    mHttp = null;
	}
    }

    public void getURL(String url, String tag) {
	mHttp = new HttpTask(mContext);

	mHttp.setUserAgent(mAgent);
	mHttp.setConnectTimeout(mConnectTimeout);
	mHttp.setReadTimeout(mReadTimeout);

	if (mHttpListener != null) {
	    mHttp.setHttpListener(mHttpListener);

	    if (isNetworkAvailable()) {
		try {
		    mHttp.getURL(url, tag);
		} catch (MalformedURLException e) {
		    mHttpListener.onHttpError(Http.INVALID_URL);
		}
	    } else {
		mHttpListener.onHttpError(Http.NO_NETWORK_AVAILABLE);
	    }
	}
    }

    private boolean isNetworkAvailable() {
	ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

	return connectivity.getActiveNetworkInfo() != null;
    }

    public void setConnectTimeout(int timeout) {
	mConnectTimeout = timeout;
    }

    public void setHttpListener(HttpListener listener) {
	mHttpListener = listener;
    }

    public void setReadTimeout(int timeout) {
	mReadTimeout = timeout;
    }

    public void setUserAgent(String agent) {
	mAgent = agent;
    }
}
