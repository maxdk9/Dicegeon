package com.tann.dice.screens.gameScreen.panels.villagerStuff;

import com.tann.dice.util.Lay;

public class XpPanel extends Lay{



    private static XpPanel self;
    public static XpPanel get(){
        if(self==null) self = new XpPanel();
        return self;
    }

    private XpPanel() {
        layout();
    }

    @Override
    public void layout() {

    }
}
