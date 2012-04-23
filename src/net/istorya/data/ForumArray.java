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

package net.istorya.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ForumArray extends ArrayList<Forum> implements Parcelable {

    private static final long serialVersionUID = 5402892932794841351L;

    public static final Parcelable.Creator<ForumArray> CREATOR = new Parcelable.Creator<ForumArray>() {
	@Override
	public ForumArray createFromParcel(Parcel source) {
	    return new ForumArray(source);
	}

	@Override
	public ForumArray[] newArray(int size) {
	    return new ForumArray[size];
	}
    };

    public ForumArray() {
    }

    public ForumArray(Parcel in) {
	readFromParcel(in);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    private void readFromParcel(Parcel in) {
	int size = in.readInt();

	for (int i = 0; i < size; i++) {
	    add((Forum) in.readParcelable(Forum.class.getClassLoader()));
	}
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	int size = size();

	dest.writeInt(size);

	for (int i = 0; i < size; i++) {
	    dest.writeParcelable(get(i), flags);
	}
    }
}
