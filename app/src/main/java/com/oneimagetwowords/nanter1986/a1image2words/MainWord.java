package com.oneimagetwowords.nanter1986.a1image2words;

/**
 * Created by user on 22/8/2017.
 */

public enum MainWord {
    CAR("car"),
    CAT("cat"),
    HOUSE("house"),
    TREE("tree"),
    PEOPLE("people"),
    YEAR("year"),
    WORK("work"),
    TIME("time"),
    WORLD("world"),
    MAN("man"),
    LIFE("life"),
    HAND("hand"),
    EYE("eye"),
    GOVERNMENT("government"),
    COMPANY("company"),
    PROBLEM("problem"),
    PLANET("planet"),
    PHONE("phone"),
    DOG("dog"),
    FATHER("father"),
    MOTHER("mother"),
    PRESIDENT("president"),
    ACTOR("actor"),
    ACTRESS("actress"),
    ADVENTURE("adventure"),
    AIRCRAFT("aircraft"),
    AIRPORT("airport"),
    ALCOHOL("alcohol"),
    COUCH("couch"),
    HAIR("hair"),
    CHURCH("church"),
    SPACE("space"),
    GALAXY("galaxy"),
    STAR("star"),
    SUN("sun"),
    PANDA("panda"),
    DINOSAUR("dinosaur"),
    FROG("frog"),
    FRUIT("fruit"),
    UFO("ufo"),
    TOWER("tower"),
    ZOMBIE("zombie"),
    VAMPIRE("vampire"),
    BEACH("beach"),
    WAVE("wave"),
    TURTLE("turtle"),
    DRAGON("dragon"),
    SWORD("sword"),
    COMPUTER("computer"),
    WATERFALL("waterfall"),
    FISH("fish"),
    BIRD("bird"),
    MAMMAL("mammal"),
    SHARK("shark"),
    LIZARD("lizard"),
    CITY("city"),
    VILLAGE("village"),
    TORNADO("tornado"),
    MOUNTAIN("mountain"),
    RIVER("river"),
    SKY("sky"),
    DAWN("dawn"),
    CLOUD("cloud");

    private String mainword;


    MainWord(String mainword) {
        this.mainword=mainword;
    }

    public String getMainWord(){
        return mainword;
    }
}
