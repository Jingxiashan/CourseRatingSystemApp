package com.courseratingsystem.app.datebase;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 辰星启明 on 2017/8/11.
 */

public class SearchHistory extends DataSupport {

    private String searchText;

    public SearchHistory(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

}