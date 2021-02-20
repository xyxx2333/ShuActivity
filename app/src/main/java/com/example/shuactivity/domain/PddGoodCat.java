package com.example.shuactivity.domain;

import com.pdd.pop.ext.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PddGoodCat implements Serializable {
    private Integer level;

    @Override
    public String toString() {
        return "PddGoodCat{" +
                "level=" + level +
                ", cat_name='" + cat_name + '\'' +
                ", parent_cat_id=" + parent_cat_id +
                ", cat_id=" + cat_id +
                ", selected=" + selected +
                '}';
    }

    private String cat_name;
    private Long parent_cat_id;
    private Long cat_id;
    private int icon;
    private boolean selected;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public Long getParent_cat_id() {
        return parent_cat_id;
    }

    public void setParent_cat_id(Long parent_cat_id) {
        this.parent_cat_id = parent_cat_id;
    }

    public Long getCat_id() {
        return cat_id;
    }

    public void setCat_id(Long cat_id) {
        this.cat_id = cat_id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }



    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
