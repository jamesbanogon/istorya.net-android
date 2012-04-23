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

package net.istorya.parser;

import android.os.AsyncTask;
import android.text.Html;

import net.istorya.data.Categories;
import net.istorya.data.Category;
import net.istorya.data.Forum;

import java.util.regex.Pattern;

class CategoryParserTask extends AsyncTask<String, Void, Categories> {

    private CategoryParserListener mCallback;

    public CategoryParserTask(CategoryParserListener callback) {
	mCallback = callback;
    }

    @Override
    protected Categories doInBackground(String... data) {
	Categories result = new Categories();
	Regex category = new Regex(data[0], "<div class=\"forumhead[^>]+>(.*?)</div>", Pattern.DOTALL);

	while (category.find() && !isCancelled()) {
	    Category categoryItem = new Category();
	    String categoryData = category.match(1);

	    Regex categoryTitle = new Regex(categoryData, "<span class=\"forumtitle\"><a[^>]+>(.*?)</a></span>");
	    if (categoryTitle.find()) {
		categoryItem.title = Html.fromHtml(categoryTitle.match(1)).toString();
	    }

	    Regex categoryId = new Regex(categoryData, "<a class=\"collapse\" id=\"[^_]+_(.*?)\" href=\"");
	    if (categoryId.find()) {
		categoryItem.id = Html.fromHtml(categoryId.match(1)).toString();
	    }

	    Regex list = new Regex(data[0], "<ol id=\"" + categoryItem.id + "(.*?)(<div class=\"forumhead|<!-- /main -->)", Pattern.DOTALL);
	    if (list.find()) {
		String listData = list.match(1);
		Regex forum = new Regex(listData, "<div class=\"forumrow table\">(.*?)</div>\\s+</div>", Pattern.DOTALL);

		while (forum.find() && !isCancelled()) {
		    Forum forumItem = new Forum();
		    String forumData = forum.match(1);

		    Regex forumIcon = new Regex(forumData, "<img src=\"(.*?)\".*class=\"forumicon\"");
		    if (forumIcon.find()) {
			forumItem.icon = forumIcon.match(1);
		    }

		    Regex forumTitle = new Regex(forumData, "<h2 class=\"forumtitle\"><a href=\"(.*?)\">(.*?)</a></h2>");
		    if (forumTitle.find()) {
			forumItem.link = Html.fromHtml(forumTitle.match(1)).toString();

			forumItem.title = Html.fromHtml(forumTitle.match(2)).toString();
			forumItem.title = forumItem.title.replace(" (Old)", "").replace(" Discussions", "");
		    }

		    Regex forumDescription = new Regex(forumData, "<p class=\"forumdescription\">(.*?)</p>", Pattern.DOTALL);
		    if (forumDescription.find()) {
			forumItem.description = Html.fromHtml(forumDescription.match(1)).toString();
			forumItem.description = forumItem.description.replaceAll("\\s*::\\s*", "");
		    }

		    categoryItem.forums.add(forumItem);
		}
	    }

	    result.add(categoryItem);
	}

	return result;
    }

    @Override
    protected void onCancelled(Categories result) {
	super.onCancelled(result);
    }

    @Override
    protected void onPostExecute(Categories result) {
	super.onPostExecute(result);

	mCallback.onCategoryParserFinish(result);
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();

	mCallback.onCategoryParserStart();
    }
}