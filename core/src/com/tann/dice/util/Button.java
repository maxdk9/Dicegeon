package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Button extends Lay{
	private TextureRegion region;
	private float imageScale;
	public Button(float width, float height, float imageScale, final TextureRegion region, Color backgroundColour, final Runnable runnable) {
		this.region=region;
		this.bg=backgroundColour;
		this.imageScale=imageScale;
		setRunnable(runnable);
		setSize(width, height);
	}
	
	public Button(float width, float height, float imageScale, final TextureRegion region, Color backgroundColour) {
		this.region=region;
		this.bg=backgroundColour;
		this.imageScale=imageScale;
		setSize(width, height);
	}

	public void setRunnable(final Runnable runnable){
		addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				runnable.run();
				return true;
			}
		});
	}
	
	public Button(float width, float height, final TextureRegion region, Color backgroundColour, final Runnable runnable) {
		this(width, height, 1, region, backgroundColour, runnable);
	}
	
	public void setBackgroundColour(Color col){
		bg=col;
	}

	Color bg, border;
	float borderSize;
	public void setBorder(Color bg, Color border, float size){
	    this.bg=bg; this.border=border; this.borderSize=size;
    }

    boolean square;
    public void setSquare(){
	    this.square = true;
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(bg);
		if(border==null) border=bg;
		Draw.fillActor(batch, this, bg, border, borderSize);
		batch.setColor(getColor());
        float imageWidth = getWidth()*imageScale;
        float imageHeight = getHeight()*imageScale;
        if(square){
            imageHeight = Math.min(imageHeight, imageHeight);
            imageWidth = imageHeight;
        }
		Draw.drawSize(batch, region,
				getX()+getWidth()/2-imageWidth/2,
				getY()+getHeight()/2-imageHeight/2,
				imageWidth, imageHeight);
		super.draw(batch, parentAlpha);
	}

    @Override
    public void layout() {

    }
}
