package com.tann.dice;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Images {

    public static final TextureRegion spellTab = Main.atlas.findRegion("spellTab");

    public static final TextureRegion magic = Main.atlas.findRegion("magic");
    public static final TextureRegion spell_shield = Main.atlas_3d.findRegion("dice/face/shield1");
    public static final TextureRegion spell_bow = Main.atlas_3d.findRegion("dice/face/arrow1");

	public static final TextureRegion roll = Main.atlas.findRegion("roll");
    public static final TextureRegion tick = Main.atlas.findRegion("tick");
    public static final TextureRegion skull = Main.atlas.findRegion("skull");

    public static final TextureRegion poison = Main.atlas.findRegion("buff/poison");
    public static final TextureRegion stealth = Main.atlas.findRegion("buff/stealth");

	public static final TextureRegion side_sword = Main.atlas_3d.findRegion("dice/face/sword1");

    public static final TextureRegion heart = Main.atlas.findRegion("heart");
    public static final TextureRegion heart_empty = Main.atlas.findRegion("heartEmpty");

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
		return  new TextureRegion[]{base,base};
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
