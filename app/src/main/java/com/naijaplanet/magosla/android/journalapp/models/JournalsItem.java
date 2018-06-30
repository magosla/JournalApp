package com.naijaplanet.magosla.android.journalapp.models;

/**
 * Journal Item class file
 * This is a stripped down version of the Journal class with the removal of Journal entries content
 */
public class JournalsItem {
    private String key;
    private String title;
    private Long timestamp;

    @SuppressWarnings("UnnecessaryBoxing")
    private Long editTimestamp = Long.valueOf(0);
    private String type;

    public enum Type {THOUGHTS, FEELINGS}

    @SuppressWarnings("WeakerAccess")
    public JournalsItem() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getEditTimestamp() {
        return editTimestamp;
    }

    public void setEditTimestamp(Long editTimestamp) {
        this.editTimestamp = editTimestamp;
    }

}
