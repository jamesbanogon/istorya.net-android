package net.istorya.fragment;

import net.istorya.data.Forum;

public interface CategoryListFragmentListener {

    public void onForumSelected(Forum forum);

    public void onReloadCategoryListError();

    public void onReloadCategoryListFinish();

    public void onReloadCategoryListStart();
}
