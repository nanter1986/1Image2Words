package com.oneimagetwowords.nanter1986.a1image2words;

/**
 * Created by user on 22/8/2017.
 */

public enum MainWord {
    CAR("car"),
    CAT("cat"),
    HOUSE("house"),
    TREE("tree");

    private String mainword;


    MainWord(String mainword) {
        this.mainword=mainword;
    }

    public String getMainWord(){
        return mainword;
    }
}
