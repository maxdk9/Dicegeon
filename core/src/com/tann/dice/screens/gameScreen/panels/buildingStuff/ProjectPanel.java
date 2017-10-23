package com.tann.dice.screens.gameScreen.panels.buildingStuff;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import com.tann.dice.gameplay.village.project.Project;
import com.tann.dice.screens.gameScreen.panels.eventStuff.CostTab;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Fonts;
import com.tann.dice.util.Layoo;
import com.tann.dice.util.TextBox;

public class ProjectPanel extends Group{

	public Project building;
	
	public static final float WIDTH = 180, HEIGHT=230;
	public static final float IMAGE_SIZE= 60;
	private static final int border = 3;



	public ProjectPanel(Project building) {
		setSize(WIDTH, HEIGHT);
		setProject(building);
	}
	
	public ProjectPanel() {
		setSize(WIDTH, HEIGHT);
	}
	
	public void clearBuilding(){
		clearChildren();
		this.building=null;
	}
	
	public void setProject(Project project){
		clearBuilding();
		this.building=project;
		Layoo l = new Layoo(this);
		l.absRow(10);
		TextBox title = new TextBox(project.name, Fonts.fontSmall, WIDTH, Align.center);
		l.actor(title);
		l.absRow(10);
		Group descGroup = new Group();
		descGroup.setSize(WIDTH, 80);
		TextBox tb = new TextBox(project.description, Fonts.fontTiny, getWidth()*.9f, Align.center);
        descGroup.addActor(tb);
        tb.setPosition(descGroup.getWidth()/2, descGroup.getHeight()/2, Align.center);
        l.actor(descGroup);


		l.row(1);
        BuildingEffectPanel bep =new BuildingEffectPanel(project.effects);
        l.actor(bep);
		l.row(1);

		l.layoo();

        CostTab ct = new CostTab(project.cost);
        addActor(ct);
        ct.setPosition(getWidth()/2-ct.getWidth()/2, getHeight());

        if(project.unlockedBy!=null){
            UnlockedByPanel pan = new UnlockedByPanel(project.unlockedBy.name);
            addActor(pan);
            pan.setPosition(0,-pan.getHeight());
        }
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.setColor(highlightColour);
		Draw.fillActor(batch, this, Colours.brown_dark, highlightColour, border);
		super.draw(batch, parentAlpha);
	}

	Color highlightColour = Colours.brown_light;
	public void highlight(boolean b) {
		highlightColour = b?Colours.light:Colours.brown_light;
	}
	
}
