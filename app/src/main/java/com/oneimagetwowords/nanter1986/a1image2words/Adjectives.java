package com.oneimagetwowords.nanter1986.a1image2words;

/**
 * Created by user on 22/8/2017.
 */

public enum Adjectives {
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    YELLOW("yellow"),
    FIRST("first"),
    LAST("last"),
    OLD("old"),
    BIG("big"),
    HIGH("high"),
    DIFFERENT("different"),
    SMALL("small"),
    LARGE("large"),
    YOUNG("young"),
    PUBLIC("public"),
    BAD("bad"),
    GOOD("good"),
    COLD("cold"),
    HOT("hot"),
    COOL("cool"),
    HUGE("huge"),
    ENORMOUS("enormous"),
    PRETTY("pretty"),
    UGLY("ugly"),
    CHUBBY("chubby"),
    GREAT("great"),
    LONG("long"),
    SOCIAL("social"),
    EARLY("early"),
    STRONG("strong"),
    FREE("free"),
    EASY("easy"),
    POOR("poor"),
    SERIOUS("serious");

    private String Adjective;



    Adjectives(String adj) {
        this.Adjective =adj;
    }

    public String getColor(){
        return this.Adjective;
    }

}
