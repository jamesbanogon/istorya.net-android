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
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class HttpCache {
    private static final String TAG = "HttpCache";
    private static final boolean LOCAL_LOGV = true;

    public static String convertToHex(byte[] data) {
	StringBuffer buffer = new StringBuffer();

	for (byte element : data) {
	    int halfByte = (element >>> 4) & 0x0F;
	    int twoHalves = 0;

	    do {
		if ((0 <= halfByte) && (halfByte <= 9)) {
		    buffer.append((char) ('0' + halfByte));
		} else {
		    buffer.append((char) ('a' + (halfByte - 10)));
		}

		halfByte = element & 0x0F;
	    } while (twoHalves++ < 1);
	}

	return buffer.toString();
    }

    public static File getCacheFile(Context context, String url) {
	try {
	    MessageDigest message = MessageDigest.getInstance("SHA-1");
	    message.update(url.getBytes("iso-8859-1"), 0, url.length());

	    byte[] digest = new byte[40];
	    digest = message.digest();

	    String filename = context.getCacheDir() + "/" + convertToHex(digest);
	    String extension = "html";

	    if (url != null && url.contains(".")) {
		extension = url.substring(url.lastIndexOf('.'));
	    }

	    if (LOCAL_LOGV) {
		Log.v(TAG, "File name is " + filename);
		Log.v(TAG, "File extension is " + extension);
	    }

	    return new File(filename);
	} catch (NoSuchAlgorithmException e) {
	    return null;
	} catch (UnsupportedEncodingException e) {
	    return null;
	}
    }

    private HttpURLConnection mHttp;

    private long mContentDate;
    private long mContentExpire;
    private String mContentType;
    private boolean mContentCacheable;

    private File mCacheFile;
    private long mCacheFileTime;

    public HttpCache(Context context, HttpURLConnection http) {
	mHttp = http;

	mContentDate = http.getDate();
	mContentDate = mContentDate == 0 ? System.currentTimeMillis() : mContentDate;
	mContentExpire = http.getExpiration();
	mContentType = http.getContentType();
	mContentCacheable = true;

	mCacheFile = getCacheFile(context, http.getURL().toString());

	if (mContentType != null) {
	    if (mContentType.contains(";")) {
		String[] fields = mContentType.split(";");
		mContentType = fields[0].trim().toLowerCase();
	    }

	    if (LOCAL_LOGV) {
		Log.v(TAG, "Content MIME type is " + mContentType + ".");
	    }
	}

	// Check for the expiration date
	if (mContentExpire < mContentDate) {
	    mContentCacheable = false;

	    if (mCacheFile.exists()) {
		mCacheFile.delete();
	    }
	}

	// Check if file cache is stale
	if (mContentCacheable && mCacheFile.exists()) {
	    mCacheFileTime = ((System.currentTimeMillis() - mCacheFile.lastModified()) / 1000) / 60;

	    // Delete if file cache is older than 30 minutes
	    if (mCacheFileTime > 30) {
		mCacheFile.delete();
	    }
	}
    }

    public void deleteCachedFile() {
	if (mCacheFile.exists()) {
	    mCacheFile.delete();

	    if (LOCAL_LOGV) {
		Log.v(TAG, "Cached file deleted.");
	    }
	}
    }

    public byte[] getContent() throws IOException {
	InputStream input = null;
	OutputStream output = null;
	ByteArrayOutputStream data = new ByteArrayOutputStream(1024);

	if (mContentCacheable && mCacheFile.exists()) {
	    input = new BufferedInputStream(new FileInputStream(mCacheFile));

	    if (LOCAL_LOGV) {
		Log.v(TAG, "File cache found. Using it for input instead of the remote source.");
	    }
	} else {
	    input = new BufferedInputStream(mHttp.getInputStream());

	    if (mContentCacheable) {
		output = new FileOutputStream(mCacheFile);

		if (LOCAL_LOGV) {
		    Log.v(TAG, "File cache does not exist. Using the remote source for input while caching it at the same time.");
		}
	    } else {
		if (LOCAL_LOGV) {
		    Log.v(TAG, "Content is not cacheable.");
		}
	    }
	}

	int inputByte = input.read();

	while (inputByte != -1) {
	    data.write(inputByte);

	    if (output != null) {
		output.write(inputByte);
	    }

	    inputByte = input.read();
	}

	data.flush();
	data.close();

	input.close();

	if (output != null) {
	    output.flush();
	    output.close();
	}

	if (LOCAL_LOGV) {
	    Log.v(TAG, "Content size is " + data.toString().length() + " bytes.");
	}

	return data.toByteArray();
    }
}
