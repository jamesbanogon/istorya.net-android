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

public class Forum implements Parcelable {

    public String title;
    public String description;
    public String link;
    public String icon;

    public static final Parcelable.Creator<Forum> CREATOR = new Parcelable.Creator<Forum>() {
	@Override
	public Forum createFromParcel(Parcel in) {
	    return new Forum(in);
	}

	@Override
	public Forum[] newArray(int size) {
	    return new Forum[size];
	}
    };

    public Forum() {
	clear();
    }

    public Forum(Parcel in) {
	readFromParcel(in);
    }

    public Forum(String title, String description, String link, String icon) {
	this.title = title;
	this.description = description;
	this.link = link;
	this.icon = icon;
    }

    public void clear() {
	title = "";
	description = "";
	link = "";
	icon = "";
    }

    @Override
    public int describeContents() {
	return 0;
    }

    public boolean isEmpty() {
	return title.isEmpty() && description.isEmpty() && link.isEmpty() && icon.isEmpty();
    }

    private void readFromParcel(Parcel in) {
	title = in.readString();
	description = in.readString();
	link = in.readString();
	icon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(title);
	dest.writeString(description);
	dest.writeString(link);
	dest.writeString(icon);
    }
}
