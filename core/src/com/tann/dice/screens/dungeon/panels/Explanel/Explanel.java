package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.DieSidePanel;
import com.tann.dice.util.*;

public class Explanel extends InfoPanel implements OnPop {

    private static final int textWidth = 98, panelWidth = 106, topAndBottom=3;


    private static Explanel self;

    public Side side;
    public Equipment equipment;
    public Spell spell;
    public Trigger trigger;

    public static Explanel get(){
        if(self == null) self = new Explanel();
        return self;
    }

    public Explanel(){
    }

    private void reset() {
        this.side = null;
        this.equipment = null;
        this.spell = null;
        this.trigger = null;
        clearChildren();
    }

    private Pixl getPixl(){
        return new Pixl(this, 2, panelWidth).row(topAndBottom);
    }

    private void finalise(Pixl p) {
        p.row(topAndBottom);
        p.pix();
    }

    public void setup(Targetable targetable, boolean usable){
        if(targetable instanceof Spell) setup((Spell) targetable, usable);
        else if(targetable instanceof Die){
            Die d = (Die) targetable;
            setup(d.getActualSide(), usable, d.entity);
        }
    }

    public void setup(final Equipment equipment){
        reset();
        this.equipment = equipment;
        final int scale = 1;
        Actor equipDraw = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                equipment.draw(batch, getX(), getY(), scale);
            }
        };
        equipDraw.setSize(equipment.image.getRegionWidth()*scale, equipment.image.getRegionHeight()*scale);

        Pixl p = new Pixl(this, 3);
        p.actor(equipment.image).gap(2).text("[orange]"+equipment.name)
                .row()
                .actor(new TextWriter("[grey]"+equipment.fluff+"[nh][light]"+equipment.getDescription(), textWidth));
        finalise(p);
    }

    public void setup(final Trigger trigger, float width, DiceEntity entity){
        reset();
        this.trigger= trigger;
        int borderSize = 2;
        String text = "[white][image][light][h]: "+trigger.describeForPanelText();
        TextWriter tw = new TextWriter(text, (int) width-borderSize*2, entity.getColour(), borderSize, trigger.getImage());
        if(trigger.highlightForNewPlayers()) {
            Prefs.setBoolean(trigger.getClass().getSimpleName(), true);
        }
        tw.setWidth(width);
        addActor(tw);
        setSize(tw.getWidth(), tw.getHeight());
    }


    public void setup(Side side, boolean usable, DiceEntity entity){
        reset();
        this.side = side;
        int gap = 3;
        int textWidth = 61;
        Pixl p = new Pixl(this, gap);
        p.actor(new TextWriter("[light]"+side.getTitle()))
            .row(4)
            .actor(new DieSidePanel(side, entity, 1)).gap(gap).actor(new TextWriter("[grey]"+side.toString(), textWidth));
        p.pix();
    }


    private void setup(final Spell spell, boolean usable){
        reset();
        this.spell = spell;
        final int scale = 1;
        boolean enoughMagic = Party.get().getAvaliableMagic()>=spell.getCost();

        Pixl p = new Pixl(this, 3).row(topAndBottom);
        int gap = 2;
        p.image(spell.getImage());
        p.actor(new TextWriter("[blue]"+spell.getName()));
        p.gap(5);
        for(int i=0;i<spell.getCost();i++) {
            p.actor(new ImageActor(Images.magic));
        }
        p.row();
        p.actor(new TextWriter(spell.getDescription(), textWidth));
        if(usable && enoughMagic){
            switch (spell.getEffects()[0].targetingType) {
                case EnemyGroup:
                case FriendlyGroup:
                case RandomEnemy:
                case Untargeted:
                    if(enoughMagic) {
                        p.row().actor(new TextWriter("[blue]Tap again to cast", textWidth));
                    }
                    break;
            }
        }
        p.row(gap+1);
        p.pix();


        if(usable) {
            if(PhaseManager.get().getPhase().canTarget() && !enoughMagic){
                String text = "[red]Not enough magic";
                if(Party.get().getTotalTotalTotalAvailableMagic() >= spell.getCost()){
                    text += "[n][light]Tap your magic dice to gain magic";
                }
                TextWriter tw = new TextWriter(text, Integer.MAX_VALUE, Colours.red, 2);
                tw.setColor(Colours.red);
                addActor(tw);
                tw.setPosition((int)(getWidth()/2-tw.getWidth()/2), (int)(-tw.getHeight()-2));
            }
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(trigger == null) {
            Draw.fillActor(batch, this, Colours.dark, Colours.light, 1);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void onPop() {
        EntityGroup.clearTargetedHighlights();
        TargetingManager.get().deselectTargetable();
    }

}
