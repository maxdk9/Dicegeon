package com.tann.dice.screens.dungeon;

import com.tann.dice.Main;
import com.tann.dice.gameplay.phase.Phase;

import java.util.ArrayList;
import java.util.List;

public class PhaseManager {
    private static PhaseManager self;
    public static PhaseManager get(){
        if(self == null){
            self = new PhaseManager();
        }
        return self;
    }

    public static Runnable popPhaseRunnable = new Runnable() {
        @Override
        public void run() {
            get().popPhase();
        }
    };

    private List<Phase> phaseStack = new ArrayList<>();

    public Phase getPhase() {
        if(phaseStack.size()==0) return null;

        return phaseStack.get(0);
    }

    public void pushPhase(Phase phase) {
        phaseStack.add(phaseStack.size(), phase);
    }

    public void popPhase() {
        Phase popped = phaseStack.remove(0);
        popped.deactivate();
        if (phaseStack.size() == 0) {
            System.err.println("popping error, previous phase was " + popped.toString());
        }
        getPhase().activate();
        Main.getCurrentScreen().activatePhase(getPhase());
    }

    public void popPhase(Class clazz) {
        if (!clazz.isInstance(getPhase())) {
            System.err.println(
                    "Trying to pop a class of type " + clazz.getSimpleName() + " when the phase is " + getPhase().toString());
            return;
        }
        popPhase();
    }

    public void clearPhases() {
        phaseStack.clear();
    }

    public void kickstartPhase(Class clazz) {
        if (!clazz.isInstance(getPhase())) {
            System.err.println("Trying to kickstart a class of type  " + clazz.getSimpleName() + " when the phase is " + getPhase().toString());
            return;
        }
        kickstartPhase();
    }

    public void kickstartPhase() {
        getPhase().activate();
        Main.getCurrentScreen().activatePhase(getPhase());
    }

    public void checkPhaseIsDone() {
        Phase p = getPhase();
        if(p!=null) p.checkIfDone();
    }
}
