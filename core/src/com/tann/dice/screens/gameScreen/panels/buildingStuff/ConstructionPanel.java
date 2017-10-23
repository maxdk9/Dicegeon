package com.tann.dice.screens.gameScreen.panels.buildingStuff;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Cost;
import com.tann.dice.gameplay.island.islands.Island;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.project.Project;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.eventStuff.CostTab;
import com.tann.dice.screens.gameScreen.panels.review.InfoPanel;
import com.tann.dice.util.*;

public class ConstructionPanel extends InfoPanel{

	Array<ProjectPanel> availables = new Array<>();
	static final float WIDTH = 650, HEIGHT = 350;
	Cost resetCost = new Cost().wood(2);
    Button refreshButton;
	public ConstructionPanel() {
		setSize(WIDTH, HEIGHT);

		Layoo l = new Layoo(this);
        l.row(1);
        Fonts.font.setColor(Colours.light);
        TextBox title = new TextBox("choose a project", Fonts.font, WIDTH, Align.center);
        l.actor(title);
        l.row(1);
        l.absRow(CostTab.height());
		l.gap(1);
		
		for(int i=0;i<3;i++){
			ProjectPanel bpan = new ProjectPanel();
			l.actor(bpan);
			availables.add(bpan);
			l.gap(1);
		}
        l.absRow(UnlockedByPanel.height());
		l.row(.5f);
		l.layoo();
		

		for(final ProjectPanel bp:availables){
			bp.addListener(new InputListener(){
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				    if(!Village.getPhase().allowBuying()) return false;
					attemptToBuy(bp.building);
					return super.touchDown(event, x, y, pointer, button);
				}
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if(!Village.getPhase().allowBuying()) return;
					bp.highlight(true);
					super.enter(event, x, y, pointer, fromActor);
				}
				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					bp.highlight(false);
					super.exit(event, x, y, pointer, toActor);
				}
			});
		}

		refreshButton = new Button(50, 50, .7f, Images.refresh, Colours.dark, new Runnable() {
			@Override
			public void run() {
				resetButtonPush();
			}
		});
		addActor(refreshButton);
		refreshButton.setBorder(Colours.dark, Colours.brown_light, 3);
        Group scaleGroup = new Group();
        refreshButton.addActor(scaleGroup);
        float scale = .45f;
        scaleGroup.setScale(scale,scale);
        scaleGroup.addActor(new CostTab(resetCost, false));
        scaleGroup.setPosition(0,refreshButton.getHeight());
        refreshButton.setPosition(getWidth()-refreshButton.getWidth()-5, getHeight()-refreshButton.getHeight()-5-CostTab.height()*scale);
        resetAvailablePanels();
    }

    private void resetButtonPush() {
	    if(!Village.getInventory().checkCost(resetCost)){
	        Sounds.playSound(Sounds.error, 1,1);
	        return;
        }
        Village.getInventory().spendCost(resetCost);
	    resetAvailablePanels();
        addActor(new Flasher(this, Colours.grey));
    }

    public void attemptToBuy(Project b){
		// maybe have an inventory manager class to deal with this kind of thing.
		// doesn't really make sense to pass it onto gamescreen :P
		if(!Village.getInventory().checkCost(b.cost)){
			return;
		}
		Village.getInventory().spendCost(b.cost);
		startProject(b);
	}

	private void startProject(Project p ){
        Village.get().addBuilding(p);
        p.onCommence();
        built=true;
        for(ProjectPanel bp:availables){
            bp.setVisible(false);
        }
        refreshButton.setVisible(false);
        GameScreen.get().pop();
    }

	private void resetAvailablePanels() {
		int levelToGenerate = 0;
		levelToGenerate = Math.min(1, levelToGenerate);
		Project[] generated = Island.get().getRandomProjects(availables.size);
		for(int i=0;i<availables.size;i++){
            ProjectPanel bp = availables.get(i);
		    bp.setVisible(true);
			bp.setProject(generated[i]);
		}
		refreshButton.setVisible(true);
	}

	boolean built;
	public void turn(){
	    if(built){
	        built=false;
	        resetAvailablePanels();
        }
    }

    @Override
    public void clipEnd() {
        Sounds.playSound(Sounds.cancel, 1, 1);
    }

    public static float staticHeight() {
	    return 300;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(built){
            Fonts.draw(batch, "Come back next turn", Fonts.font, Colours.light, getX(), getY(), getWidth(), getHeight(), Align.center);
        }
        else if(!Village.getPhase().allowBuying()){
            batch.setColor(0,0,0,.6f);
            Draw.fillActor(batch,this);
            batch.setColor(Colours.dark);
            float w = 550;
            float h = 50;
            Draw.fillRectangle(batch, getX()+getWidth()/2-w/2, getY()+getHeight()/2-h/2, w, h);
            Fonts.draw(batch, "Can only build while rolling", Fonts.font, Colours.light, getX(), getY(), getWidth(), getHeight(), Align.center);
        }
    }
}
