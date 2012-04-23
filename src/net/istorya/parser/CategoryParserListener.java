package net.istorya.parser;

import net.istorya.data.Categories;

public interface CategoryParserListener {

    public void onCategoryParserError(int error);

    public void onCategoryParserFinish(Categories categories);

    public void onCategoryParserStart();
}
