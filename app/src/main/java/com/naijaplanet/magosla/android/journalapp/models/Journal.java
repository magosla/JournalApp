package com.naijaplanet.magosla.android.journalapp.models;


/**
 * Extend the Journal Item class to include the journal content
 */
public final class Journal extends JournalsItem {
    private String content;

    public Journal() {
        super();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
