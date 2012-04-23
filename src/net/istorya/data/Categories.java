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

public class Categories extends ArrayList<Category> implements Parcelable {

    private static final long serialVersionUID = 3940931192612304015L;

    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {
	@Override
	public Categories createFromParcel(Parcel source) {
	    return new Categories(source);
	}

	@Override
	public Categories[] newArray(int size) {
	    return new Categories[size];
	}
    };

    public Categories() {
    }

    public Categories(Parcel in) {
	readFromParcel(in);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    private void readFromParcel(Parcel in) {
	int size = in.readInt();

	for (int i = 0; i < size; i++) {
	    add((Category) in.readParcelable(Category.class.getClassLoader()));
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
