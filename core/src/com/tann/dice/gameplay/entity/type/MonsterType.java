package com.tann.dice.gameplay.entity.type;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.types.TriggerCowardly;
import com.tann.dice.gameplay.effect.trigger.types.TriggerDamageAttackers;
import com.tann.dice.gameplay.effect.trigger.types.TriggerOnDeathEffect;
import com.tann.dice.gameplay.effect.trigger.types.TriggerRetaliate;
import com.tann.dice.gameplay.entity.Monster;

import static com.tann.dice.gameplay.entity.die.Side.*;

import java.util.ArrayList;
import java.util.List;

import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize.*;

public class MonsterType extends EntityType<MonsterType>{

    public static final List<MonsterType> ALL_MONSTERS = new ArrayList<>();
    public int fleePip = -99999;
    protected MonsterType() {
        ALL_MONSTERS.add(this);
    }

    //smol

    public static final MonsterType archer = new MonsterType().name("Archer").hp(2).size(smol)
            .sides(smol_arrow3, smol_arrow3, smol_arrow2, smol_arrow2, smol_arrow2, smol_arrow2)
            .trait(new TriggerCowardly());
    public static final MonsterType rat = new MonsterType().name("Rattie").hp(3).size(smol)
            .sides(smol_nip3, smol_nip3, smol_nip2, smol_nip2, smol_nipPoison1, smol_nipPoison1);
    public static final MonsterType skeleton = new MonsterType().name("Skeleton").hp(3).size(smol)
            .sides(smol_sword4, smol_sword4, smol_sword4, smol_sword4, smol_sword3, smol_sword3);
    public static final MonsterType zombie = new MonsterType().name("Zombie").hp(4).size(smol)
            .sides(smol_nipPoison1, smol_nipPoison1, smol_nip3, smol_nip3, smol_nip2, smol_nip2);
    public static final MonsterType slimie = new MonsterType().name("Slimelet").hp(3).size(smol)
            .sides(smol_slime2, smol_slime2, smol_slime2, smol_slime2, smol_slime, smol_slime);

    //reg

    public static final MonsterType goblin = new MonsterType().name("Goblin").hp(5)
        .sides(sword2, sword2, sword2, sword1, sword1, sword1);
//            .sides(sword4, sword4, sword4, sword4, sword4, sword4);
    public static final MonsterType snake = new MonsterType().name("Snake").hp(7).fleePip(2)
            .sides(snakePoison1, snakePoison1, snakePoison1, sword3, sword3, sword4);
    public static final MonsterType slime = new MonsterType().name("Slime").hp(6)
            .sides(slimeUpDown2, slimeUpDown2, slimeUpDown2, slime_triple, slime_triple, slime_triple)
            .trait(new TriggerOnDeathEffect(new Eff().summon("Slimelet", 3)));

    //big

    public static final MonsterType spikeGolem = new MonsterType().name("Spiker").hp(11).size(big)
            .sides(big_stomp1, big_stomp1, big_punch4, big_punch4, big_punch4, big_healMostDamaged3)
            .trait(new TriggerDamageAttackers(1));
    public static final MonsterType bird = new MonsterType().name("Awk").hp(7).size(big).fleePip(2)
            .sides(big_claw2, big_claw2, big_peck3, big_peck3, big_peck5, big_peck5);
    public static final MonsterType lich = new MonsterType().name("Lich").hp(21).size(big)
            .sides(big_summonSkeleton2, big_summonSkeleton2, big_summonZombie2, big_summonZombie2, big_decay3, big_decay3)
            .trait(new TriggerOnDeathEffect(new Eff().untargeted().destroyAllSummons()));
    public static final MonsterType slimoBig = new MonsterType().name("Slimer").hp(12).size(big)
            .sides(big_slimeTriple2, big_slimeTriple2, big_slimeTriple2, big_slimeTriple2, big_slimeUpDown3, big_slimeUpDown3)
            .trait(new TriggerOnDeathEffect(new Eff().summon("Slimelet", 6)));

    //huge

    public static final MonsterType dragon = new MonsterType().name("Lizard").hp(40).size(huge)
            .sides(huge_flame3, huge_flame3, huge_chomp8, huge_chomp8, huge_poisonBreath1, huge_poisonBreath1)
            .trait(new TriggerRetaliate(new int[]{10,20,30}, new Eff().damage(1).enemyGroup()));
    public static final MonsterType slimoHuge = new MonsterType().name("Slime Queen").hp(30).size(huge)
            .sides(huge_slimeTriple4, huge_slimeTriple4, huge_slimeTriple4, huge_slimeTriple4, huge_slimeUpDown5, huge_slimeUpDown5)
            .trait(new TriggerOnDeathEffect(new Eff().summon("Slimelet", 9)));

    private MonsterType fleePip(int fleePip){
        this.fleePip = fleePip;
        return this;
    }

    public Monster buildMonster(){
        Monster m = new Monster(this);
        return m;
    }

    public static MonsterType byName(String name){
        for(MonsterType mt:ALL_MONSTERS){
            if(mt.name.equals(name)){
                return mt;
            }
        }
        return null;
    }

    public static List<Monster> monsterList(MonsterType... monsters){
        List<Monster> results = new ArrayList<>();
        for(MonsterType type:monsters){
            results.add(type.buildMonster());
        }
        return results;
    }

    public static List<Monster> monsterList(List<MonsterType> monsters){
        List<Monster> results = new ArrayList<>();
        for(MonsterType type:monsters){
            results.add(type.buildMonster());
        }
        return results;
    }
}
