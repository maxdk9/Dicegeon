package com.tann.dice.gameplay.entity.group;

public class Room extends EntityGroup {

    private static Room self;
    public static Room get(){
        if(self==null) self = new Room();
        return self;
    }

}
