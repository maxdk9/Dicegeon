package com.tann.dice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tann.dice.util.Draw;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	public static TextureAtlas atlas;
	public static float ticks;
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	@Override
	public void create () {
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("atlas_image.atlas"));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.1f, .085f, .07f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setColor(new Color(.8f, .3f, .2f, 1));
		int steps = 10;
		float x = WIDTH*.7f;
		float xVariance = WIDTH*.02f;
		for(int y=0;y<steps;y++){
			Draw.drawLine(batch, x+xVariance*((y%2==1)?1:-1), Main.HEIGHT/steps*y, x+xVariance*(((y+1)%2==1)?1:-1), Main.HEIGHT/steps*(y+1), 2);
		}

		Draw.fillRectangle(batch, 50,50,50,50);
		batch.end();
	}
}
