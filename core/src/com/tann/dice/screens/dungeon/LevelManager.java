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

public class LevelManager {

    private static final int START_LEVEL = 9;
    private int level = START_LEVEL;
    public boolean easy = false;
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
//                addLevel(rat, goblin, bird, dragon); // all sizes
//        addLevel(dragon, dragon, dragon, dragon);

        addLevel(goblin, goblin, goblin, goblin);
        addLevel(goblin, archer, goblin, archer, goblin);
        addLevel(goblin, goblin, goblin, spikeGolem); // retest this
        addLevel(bird, archer, bird);
        addLevel(archer, slimoBig, goblin, goblin);

        addLevel(skeleton, lich, zombie);
        addLevel(archer, spikeGolem, spikeGolem, archer);
        addLevel(slimoHuge); // retest this
        addLevel(bird, spikeGolem, spikeGolem, bird); // restest this
        addLevel(archer, dragon, bird); // retest this a couple of times??
    }


    private void addLevel(MonsterType... monsterTypes) {
        levels.add(Arrays.asList(monsterTypes));
    }

    public void nextLevel(){
        level++;

        Explanel.get().remove();
        PhaseManager.get().clearPhases();

        if (level < levels.size()) {
            PhaseManager.get().pushPhase(new LevelEndPhase());
            startLevel();
        } else {
            PhaseManager.get().pushPhase(new VictoryPhase());
            PhaseManager.get().kickstartPhase(VictoryPhase.class);
            return;
        }

        Party.get().reset();
        PhaseManager.get().kickstartPhase();
    }

    public void startLevel() {

        Party.get().rejig();
        DungeonScreen.get().spellButt.setSpellHolder(DungeonScreen.get().spellHolder);
        DungeonScreen.get().spellHolder.setup(Party.get().getSpells());

        List<Monster> monsters = MonsterType.monsterList(levels.get(level));
        Room.get().reset();
        Room.get().setEntities(monsters);
        BulletStuff.reset();
        BulletStuff.refresh(EntityGroup.getEveryEntity());
        DungeonScreen.get().spellButt.hide();
        DungeonScreen.get().enemy.setEntities(monsters);
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
        PartyManagementPanel.get();
        Party.get().somethingChanged();
    }

    public void restart() {
        reset();
        startGame();
    }

    public void reset(){
        Monster.resetLocks();
        level = START_LEVEL;
        Party.get().fullyReset();
        PhaseManager.get().clearPhases();
    }

    public void startGame() {
        reset();
        startLevel();
        PhaseManager.get().kickstartPhase();
    }

    public int getLevel(){
        return level;
    }
}
