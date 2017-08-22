package com.oneimagetwowords.nanter1986.a1image2words;

/**
 * Created by user on 22/8/2017.
 */

public enum Adjectives {
    RED("red"),
    BLUE("blue"),
    GREEN("green"),
    YELLOW("yellow");

    private String Adjective;



    Adjectives(String adj) {
        this.Adjective =adj;
    }

    public String getColor(){
        return this.Adjective;
    }

}
