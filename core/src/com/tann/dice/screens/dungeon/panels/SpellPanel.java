package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class SpellPanel extends Actor{

    final Spell spell;
    boolean big;
    boolean targetable;
    public SpellPanel(final Spell spell, boolean big, final boolean targetable){
        this.targetable = targetable;
        this.big = big;
        int size = big?Images.spellBorderBig.getRegionHeight():Images.spellBorder.getRegionHeight();
        setSize(size, size);
        this.spell = spell;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(PartyManagementPanel.get().getSelectedEquipment()!=null){
                    return false;
                }
                TargetingManager.get().click(spell, targetable);
                event.cancel();
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.blue);
        if(targetable && spell.isUsable() && PhaseManager.get().getPhase().canTarget()){
            batch.setColor(Colours.light);
        }
        batch.draw(big?Images.spellBorderBig:Images.spellBorder, getX(), getY());
        int imageSize = spell.getImage().getRegionHeight();
        batch.setColor(Colours.z_white);
        batch.draw(spell.getImage(), getX()+getWidth()/2-imageSize/2, getY()+getHeight()/2-imageSize/2);
        super.draw(batch, parentAlpha);
    }
}
