package com.tann.dice.screens.dungeon;

import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.LevelEndPhase;
import com.tann.dice.gameplay.phase.VictoryPhase;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tann.dice.gameplay.entity.type.MonsterType.*;
import static com.tann.dice.gameplay.entity.type.MonsterType.bird;
import static com.tann.dice.gameplay.entity.type.MonsterType.dragon;

public class LevelManager {

    private static LevelManager self;
    public final List<List<MonsterType>> levels = new ArrayList<>();
    public static LevelManager get(){
        if(self == null) {
            self = new LevelManager();
            self.init();
        }
        return self;
    }

    private void init() {
        //        addLevel(rat, goblin, bird, dragon); // all sizes
        addLevel(bird, bird, bird); // ezpz
        addLevel(goblin, goblin, goblin, goblin);
        addLevel(goblin, archer, goblin, archer, goblin);
        addLevel(goblin, goblin, bird, goblin);
        addLevel(spikeGolem, bird);
        addLevel(archer, slimoBig, goblin);
        addLevel(skeleton, summoner, zombie);
        addLevel(spikeGolem, spikeGolem, snake, snake);
        addLevel(slimoHuge);
        addLevel(bird, spikeGolem, spikeGolem, bird);
        addLevel(archer, dragon, bird);
    }


    private void addLevel(MonsterType... monsterTypes) {
        levels.add(Arrays.asList(monsterTypes));
    }

    public void nextLevel(){

//        spellButt.removeAllHovers();
        Explanel.get().remove();
        Party.get().rejig();
        DungeonScreen.get().spellButt.setSpellHolder(DungeonScreen.get().spellHolder);
        DungeonScreen.get().spellHolder.setup(Party.get().getSpells());
        PhaseManager.get().clearPhases();

        if (level < levels.size()) {
            setup(MonsterType.monsterList(levels.get(level)));
            level++;
        } else {
            PhaseManager.get().pushPhase(new VictoryPhase());
            PhaseManager.get().kickstartPhase(VictoryPhase.class);
            return;
        }

        if (level > 1) {
            PhaseManager.get().pushPhase(new LevelEndPhase());
            Party.get().reset();
        }
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
//        PhaseManager.get().pushPhase(new LevelEndPhase());
        PhaseManager.get().kickstartPhase();

        PartyManagementPanel.get();
        Party.get().somethingChanged();
    }

    public int level = 0;

    public void setup(List<Monster> monsters) {
        Room.get().reset();
        Room.get().setEntities(monsters);
        BulletStuff.reset();
        BulletStuff.refresh(EntityGroup.getAllActive());
        DungeonScreen.get().spellButt.hide();
        DungeonScreen.get().enemy.setEntities(monsters);
    }

    public void restart() {
        level = 0;
        Party.get().fullyReset();
        nextLevel();
    }

}
