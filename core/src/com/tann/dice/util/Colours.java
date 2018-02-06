package com.tann.dice.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;

public class Colours {

    public static final Color dark;
    public static final Color light;
    public static final Color yellow;
    public static final Color orange;
    public static final Color red;
    public static final Color blue;
    public static final Color grey;
    public static final Color purple;

    public static final Color z_white = new Color(1,1,1,1);
    public static final Color z_black = new Color(0,0,0,1);
	public static final Color transparent = new Color(0,0,0,0);
	private static Pixmap p;
	static{
		Texture t = new Texture(Gdx.files.internal("misc/palette.png"));
		p = Draw.getPixmap(t);
		dark = palette(0, 0);
		light = palette(1, 0);
        yellow = palette(2, 0);
        orange = palette(3, 0);
		red = palette(4, 0);
		blue = palette(5, 0);
		grey = palette(6, 0);
		purple = palette(7, 0);
	}
	
	
	public static Color palette(int x, int y){
		return new Color(p.getPixel(x, y));
	}

	private static Color alphaCol = new Color();
	public static Color withAlpha(Color c, float alpha) {
	    alphaCol.set(c.r, c.g, c.b, alpha);
	    return alphaCol;
	}

	public static Color shiftedTowards(Color source, Color target, float amount) {
		if (amount > 1)
			amount = 1;
		if (amount < 0)
			amount = 0;
		float r = source.r + ((target.r - source.r) * amount);
		float g = source.g + (target.g - source.g) * amount;
		float b = source.b + (target.b - source.b) * amount;
		return new Color(r, g, b, 1);
	}

	public static Color multiply(Color source, Color target) {
		return new Color(source.r * target.r, source.g * target.g, source.b
				* target.b, 1);
	}

	private static Color make(int r, int g, int b) {
		return new Color((float) (r / 255f), (float) (g / 255f),
				(float) (b / 255f), 1);
	}

	public static Color monochrome(Color c) {
		float brightness = (c.r + c.g + c.b) / 3;
		return new Color(brightness, brightness, brightness, c.a);
	}

	public static boolean equals(Color a, Color b) {
		return a.a == b.a && a.r == b.r && a.g == b.g && a.b == b.b;
	}

	public static boolean wigglyEquals(Color a, Color aa) {
		float r = Math.abs(a.r - aa.r);
		float g = Math.abs(a.g - aa.g);
		float b = Math.abs(a.b - aa.b);
		float wiggle = .01f;
		return r < wiggle && g < wiggle && b < wiggle;
	}
	
	public static void setBatchColour(Batch batch, Color c, float a) {
		batch.setColor(c.r, c.g, c.b, a);
	}
	
	public static Vector3 v3(Color col){
		return new Vector3(col.r, col.g, col.b);
	}

    public static Color random() {
	    return new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1);
    }
}