package com.tann.dice.gameplay.fightLog;

import com.tann.dice.gameplay.fightLog.action.Command;
import com.tann.dice.gameplay.entity.EntityState;

import java.util.ArrayList;
import java.util.List;

public class Snapshot {
    Command recentestCommand;
    List<EntityState> entityStateList = new ArrayList<>();

    public void action(Command command){
        //todo check if the command is valid and the next command to process
        command.act(this);
        recentestCommand = command;
    }

}
