package com.example.ocrdictionary.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("mes")
    @Expose
    private String mes;
    @SerializedName("read_text")
    @Expose
    private String readText;
    @SerializedName("search_text")
    @Expose
    private List<SearchText> searchText = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchResponse() {
    }

    /**
     *
     * @param searchText
     * @param readText
     * @param mes
     */
    public SearchResponse(String mes, String readText, List<SearchText> searchText) {
        super();
        this.mes = mes;
        this.readText = readText;
        this.searchText = searchText;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getReadText() {
        return readText;
    }

    public void setReadText(String readText) {
        this.readText = readText;
    }

    public List<SearchText> getSearchText() {
        return searchText;
    }

    public void setSearchText(List<SearchText> searchText) {
        this.searchText = searchText;
    }

}

