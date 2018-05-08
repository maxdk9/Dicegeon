package com.tann.dice.screens.dungeon;

import static com.tann.dice.gameplay.entity.type.MonsterType.archer;
import static com.tann.dice.gameplay.entity.type.MonsterType.bird;
import static com.tann.dice.gameplay.entity.type.MonsterType.dragon;
import static com.tann.dice.gameplay.entity.type.MonsterType.goblin;
import static com.tann.dice.gameplay.entity.type.MonsterType.lich;
import static com.tann.dice.gameplay.entity.type.MonsterType.skeleton;
import static com.tann.dice.gameplay.entity.type.MonsterType.slimoBig;
import static com.tann.dice.gameplay.entity.type.MonsterType.slimoHuge;
import static com.tann.dice.gameplay.entity.type.MonsterType.spikeGolem;
import static com.tann.dice.gameplay.entity.type.MonsterType.zombie;

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

public class LevelManager {

    private static final int START_LEVEL = 5;
    private int level = START_LEVEL;

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
//        addLevel(skeleton, lich, zombie);

        addLevel(goblin, goblin, goblin, goblin);
        addLevel(goblin, archer, goblin, archer, goblin);
        addLevel(goblin, spikeGolem, goblin);
        addLevel(bird, archer, bird);
        addLevel(archer, slimoBig, goblin);

        addLevel(skeleton, lich, zombie);
        addLevel(archer, spikeGolem, spikeGolem, archer);
        addLevel(slimoHuge);
        addLevel(bird, spikeGolem, spikeGolem, bird);
        addLevel(archer, dragon, bird);
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
        BulletStuff.refresh(EntityGroup.getAllActive());
        DungeonScreen.get().spellButt.hide();
        DungeonScreen.get().enemy.setEntities(monsters);
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
        PartyManagementPanel.get();
        Party.get().somethingChanged();
    }

    public void restart() {
        Monster.resetLocks();
        level = START_LEVEL;
        Party.get().fullyReset();
        PhaseManager.get().clearPhases();
        startGame();
    }

    public void startGame() {
        startLevel();
        PhaseManager.get().kickstartPhase();
    }

    public int getLevel(){
        return level;
    }
}
