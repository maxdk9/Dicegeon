package com.tann.dice.gameplay.fightLog.action;

import com.tann.dice.gameplay.fightLog.Snapshot;

public abstract class Command {
    public abstract void act(Snapshot snapshot);
}
