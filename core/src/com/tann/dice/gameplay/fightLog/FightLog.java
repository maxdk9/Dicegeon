package com.tann.dice.gameplay.fightLog;

import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.fightLog.action.Command;

import java.util.ArrayList;
import java.util.List;

public class FightLog {

    private static FightLog self;
    public static FightLog get(){
        if(self == null){
            self = new FightLog();
        }
        return self;
    }
    public static void clearStatic() {
        self = null;
    }

    Snapshot base;
    Snapshot current;
    Snapshot future;
    List<Command> actionQueue = new ArrayList<>();

    private FightLog(){ }

    public void setup(){
        base = new Snapshot(Party.get(), Room.get());
        current = base.copy();
        future = base;
    }

    public void addAction(Command command, boolean future) {
        if(future){
            actionQueue.add(command);
        }
        else{
            actionQueue.add(getCurrentIndex(), command);
            next();
        }
    }

    private void next() {
        current.action(actionQueue.get(getCurrentIndex()));
    }

    private int getCurrentIndex(){
        return actionQueue.indexOf(current.recentestCommand);
    }



}
