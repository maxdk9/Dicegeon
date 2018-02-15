package com.tann.dice;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Images {

    public static final TextureRegion spellTab = Main.atlas.findRegion("spellTab");

    public static final TextureRegion magic = Main.atlas.findRegion("icon/magic");
    public static final TextureRegion magicBigger = Main.atlas.findRegion("icon/magicBigger");
    public static final TextureRegion magicHover = Main.atlas.findRegion("ui/magicHover");
    public static final TextureRegion magicButt = Main.atlas.findRegion("ui/magicButton");
    public static final TextureRegion spellBorder = Main.atlas.findRegion("spell/border");
    public static final TextureRegion spellBorderBig = Main.atlas.findRegion("spell/borderBig");

	public static final TextureRegion roll = Main.atlas.findRegion("ui/roll");
    public static final TextureRegion tick = Main.atlas.findRegion("ui/tick");
    public static final TextureRegion skull = Main.atlas.findRegion("icon/skull");

    public static final TextureRegion poison = Main.atlas.findRegion("buff/poison");
    public static final TextureRegion doubleDamage = Main.atlas.findRegion("buff/doubledamage");
    public static final TextureRegion stealth = Main.atlas.findRegion("buff/stealth");
    public static final TextureRegion flameWard = Main.atlas.findRegion("buff/flameWard");
    public static final TextureRegion regen = Main.atlas.findRegion("buff/regen");

	public static final TextureRegion side_sword = Main.atlas_3d.findRegion("reg/face/sword");

    public static final TextureRegion heart = Main.atlas.findRegion("icon/hp");
    public static final TextureRegion heart_empty = Main.atlas.findRegion("icon/hp_empty");

    public static final TextureRegion background = Main.atlas.findRegion("background2");

    public static final TextureRegion patch = Main.atlas.findRegion("patch/test");

    public static final TextureRegion portrait = Main.atlas.findRegion("portrait/goblin");

}
