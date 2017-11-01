package com.tann.dice;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Images {
	public static final TextureRegion food = Main.atlas.findRegion("resource/food");
	public static final TextureRegion food_storage = Main.atlas.findRegion("resource/crate");
    public static final TextureRegion brain = Main.atlas.findRegion("resource/brain");
    public static final TextureRegion brainempty = Main.atlas.findRegion("resource/brainempty");
    public static final TextureRegion brainFilling = Main.atlas.findRegion("resource/brainfilling");
	public static final TextureRegion wood = Main.atlas.findRegion("resource/wood");
    public static final TextureRegion morale = Main.atlas.findRegion("resource/morale");

    // morale compass
    public static final TextureRegion morale_outer = Main.atlas.findRegion("generalIcons/moraleouter");
    public static final TextureRegion morale_inner = Main.atlas.findRegion("generalIcons/moralecenter");
    public static final TextureRegion morale_pointer = Main.atlas.findRegion("generalIcons/pointer");

    public static final TextureRegion starvation = Main.atlas.findRegion("generalIcons/starvation");

	public static final TextureRegion fate = Main.atlas.findRegion("resource/fate");
	public static final TextureRegion level_up = Main.atlas.findRegion("resource/levelup");
    public static final TextureRegion skull = Main.atlas.findRegion("resource/skull");
    public static final TextureRegion skull_red = Main.atlas.findRegion("resource/skullRed");
    public static final TextureRegion baby = Main.atlas.findRegion("resource/baby");
	public static final TextureRegion roll = Main.atlas.findRegion("roll");
	public static final TextureRegion refresh = Main.atlas.findRegion("generalIcons/refresh");
    public static final TextureRegion turn = Main.atlas.findRegion("generalIcons/hourglass");
    public static final TextureRegion tick = Main.atlas.findRegion("tick");
    public static final TextureRegion hammer = Main.atlas.findRegion("hammer");
    public static final TextureRegion gem = Main.atlas.findRegion("resource/emerald");
    public static final TextureRegion dotdotdot = Main.atlas.findRegion("resource/special");
    public static final TextureRegion lock = Main.atlas.findRegion("lock");
    public static final TextureRegion sunflower = Main.atlas.findRegion("resource/sunflower");
    public static final Texture ball = new Texture(Gdx.files.internal("ball.png"));

    //objective
    public static final TextureRegion obj_village = Main.atlas.findRegion("objective/village");
    public static final TextureRegion obj_wheel = Main.atlas.findRegion("objective/ship_wheel");
    public static final TextureRegion obj_pocketwatch = Main.atlas.findRegion("objective/pocketwatch");
    public static final TextureRegion obj_hourglass = Main.atlas.findRegion("objective/hourglass");
    public static final TextureRegion obj_gems = Main.atlas.findRegion("objective/gems");

    public static final TextureRegion eagle = Main.atlas.findRegion("generalIcons/eagle");

    {
        ball.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    public static final TextureRegion crystal = Main.atlas.findRegion("crystal-ball");
	
	// map stuff
	public static final TextureRegion island0 = Main.atlas.findRegion("map/island0");
    public static final TextureRegion island1 = Main.atlas.findRegion("map/island1");
    public static final TextureRegion island2 = Main.atlas.findRegion("map/island2");
	
	
	//3d
	
	public static final TextureRegion side_sword = Main.atlas_3d.findRegion("dice/face/sword1");
	
	public static final TextureRegion side_food_1= Main.atlas_3d.findRegion("dice/face/food1");
	public static final TextureRegion side_food_2= Main.atlas_3d.findRegion("dice/face/food2");
	public static final TextureRegion side_food_3 = Main.atlas_3d.findRegion("dice/face/food3");
	
	public static final TextureRegion side_food_1_wood_1 = Main.atlas_3d.findRegion("dice/face/food1wood1");
	public static final TextureRegion side_wood_1 = Main.atlas_3d.findRegion("dice/face/wood1");
	public static final TextureRegion side_wood_2 = Main.atlas_3d.findRegion("dice/face/wood2");
	public static final TextureRegion side_wood_3 = Main.atlas_3d.findRegion("dice/face/wood3");
	
	public static final TextureRegion side_skull = Main.atlas_3d.findRegion("dice/face/nothing");

	
	public static final TextureRegion side_morale_1 = Main.atlas_3d.findRegion("dice/face/morale1");
	public static final TextureRegion side_morale_2_minus_2_food = Main.atlas_3d.findRegion("dice/face/morale2foodminus2");
	public static final TextureRegion side_morale_2 = Main.atlas_3d.findRegion("dice/face/morale2");
	
	public static final TextureRegion side_fate_1 = Main.atlas_3d.findRegion("dice/face/fate1");
	public static final TextureRegion side_fateForFood = Main.atlas_3d.findRegion("dice/face/fate1foodminus1");
	public static final TextureRegion side_fateForWood = Main.atlas_3d.findRegion("dice/face/fate1woodminus1");
	public static final TextureRegion side_2fateForWoodAndFood = Main.atlas_3d.findRegion("dice/face/fate2woodminus1foodminus1");

    public static final TextureRegion lapel0 = Main.atlas_3d.findRegion("dice/lapel/0");
    public static final TextureRegion lapel1 = Main.atlas_3d.findRegion("dice/lapel/1");
    public static final TextureRegion lapel2 = Main.atlas_3d.findRegion("dice/lapel/2");
    public static final TextureRegion lapel3 = Main.atlas_3d.findRegion("dice/lapel/3");
    public static final TextureRegion lapel4 = Main.atlas_3d.findRegion("dice/lapel/4");
    public static final TextureRegion lapel5 = Main.atlas_3d.findRegion("dice/lapel/5");
	
	private static Map<String, TextureRegion[]> threeDTextures = new HashMap<>();
	private static TextureRegion[] makeFace(String name){
		TextureRegion base = Main.atlas_3d.findRegion("dice/face/"+name);
		TextureRegion highlight = Main.atlas_3d.findRegion("dice/face/"+name+"_highlight");
		return  new TextureRegion[]{base,side_sword};
	}
	
	public static TextureRegion[] get(String name){
		TextureRegion[] tr = threeDTextures.get(name);
		if(tr==null){
			tr = makeFace(name);
			threeDTextures.put(name, tr);
		}
		return tr;
	}
}
