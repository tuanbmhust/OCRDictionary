package com.example.ocrdictionary.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchText {

    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("meaning")
    @Expose
    private List<String> meaning = null;
    @SerializedName("trait")
    @Expose
    private List<String> trait = null;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchText() {
    }

    /**
     *
     * @param v
     * @param meaning
     * @param index
     * @param trait
     * @param id
     * @param type
     * @param key
     */
    public SearchText(Integer v, String id, Integer index, String key, List<String> meaning, List<String> trait, String type) {
        super();
        this.v = v;
        this.id = id;
        this.index = index;
        this.key = key;
        this.meaning = meaning;
        this.trait = trait;
        this.type = type;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getMeaning() {
        return meaning;
    }

    public void setMeaning(List<String> meaning) {
        this.meaning = meaning;
    }

    public List<String> getTrait() {
        return trait;
    }

    public void setTrait(List<String> trait) {
        this.trait = trait;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
