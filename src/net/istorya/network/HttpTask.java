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
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpTask extends AsyncTask<URL, Void, byte[]> {
    private Context mContext;
    private HttpCache mHttpCache;
    private HttpListener mHttpListener;

    private String mUrl;
    private String mTag;
    private int mError;

    private static String mUserAgent;
    private static int mConnectTimeout;
    private static int mReadTimeout;

    public HttpTask(Context context) {
	mContext = context;
    }

    @Override
    protected byte[] doInBackground(URL... url) {
	mError = 0;
	mHttpCache = null;

	byte[] data = null;
	HttpURLConnection http = null;

	try {
	    http = (HttpURLConnection) (url[0].openConnection());
	} catch (IOException e) {
	    mError = Http.CONNECTION_ERROR;
	}

	if (mError == 0) {
	    http.setRequestProperty("User-Agent", mUserAgent);
	    http.setConnectTimeout(mConnectTimeout);
	    http.setReadTimeout(mReadTimeout);

	    try {
		if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
		    mHttpCache = new HttpCache(mContext, http);
		    data = mHttpCache.getContent();
		} else {
		    mError = Http.HTTP_NOT_OK;
		}
	    } catch (IOException e) {
		mError = Http.CONNECTION_ERROR;
	    }

	    http.disconnect();
	}

	return data;
    }

    public void getURL(String url, String tag) throws MalformedURLException {
	mUrl = url;
	mTag = tag;

	execute(new URL(url));
    }

    @Override
    protected void onPostExecute(byte[] result) {
	super.onPostExecute(result);

	if (mHttpListener != null) {
	    if (mError == 0) {
		mHttpListener.onHttpFinish(mUrl, result, mTag);
	    } else {
		if (mHttpCache != null) {
		    mHttpCache.deleteCachedFile();
		}

		mHttpListener.onHttpError(mError);
	    }
	}
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();

	if (mHttpListener != null) {
	    mHttpListener.onHttpStart();
	}
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
	mUserAgent = agent;
    }
}