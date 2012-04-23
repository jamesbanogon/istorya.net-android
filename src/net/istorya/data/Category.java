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

public class Category implements Parcelable {

    public String id;
    public String title;
    public ForumArray forums = new ForumArray();

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
	@Override
	public Category createFromParcel(Parcel source) {
	    return new Category(source);
	}

	@Override
	public Category[] newArray(int size) {
	    return new Category[size];
	}
    };

    public Category() {
    }

    public Category(Parcel in) {
	readFromParcel(in);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    private void readFromParcel(Parcel in) {
	id = in.readString();
	title = in.readString();

	int size = in.readInt();

	for (int index = 0; index < size; index++) {
	    Forum forum = new Forum(in.readString(), in.readString(), in.readString(), in.readString());
	    forums.add(forum);
	}
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(id);
	dest.writeString(title);

	int size = forums.size();

	dest.writeInt(size);

	for (int index = 0; index < size; index++) {
	    dest.writeString(forums.get(index).title);
	    dest.writeString(forums.get(index).description);
	    dest.writeString(forums.get(index).link);
	    dest.writeString(forums.get(index).icon);
	}
    }
}
