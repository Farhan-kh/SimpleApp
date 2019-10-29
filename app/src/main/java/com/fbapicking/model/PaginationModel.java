package com.fbapicking.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PaginationModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("current_page")
    private int current_page;
    @SerializedName("next_page")
    private int next_page;
    @SerializedName("prev_page")
    private int prev_page;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("total_count")
    private int total_count;

    /**
     * @return the id
     */
    public int getCurrent_page() {
        return current_page;
    }

    /**
     * @param current_page the current_page to set
     */
    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    /**
     * @return the next_page
     */
    public int getNext_page() {
        return next_page;
    }

    /**
     * @param next_page the next_page to set
     */
    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    /**
     * @return the prev_page
     */
    public int getPrev_page() {
        return prev_page;
    }

    /**
     * @param prev_page the prev_page to set
     */
    public void setPrev_page(int prev_page) {
        this.prev_page = prev_page;
    }

    /**
     * @return the total_pages
     */
    public int getTotal_pages() {
        return total_pages;
    }

    /**
     * @param total_pages the total_pages to set
     */
    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    /**
     * @return the total_count
     */
    public int getTotal_count() {
        return total_count;
    }

    /**
     * @param total_count the total_count to set
     */
    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}
