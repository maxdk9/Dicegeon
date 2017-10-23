package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;

public class ImageActor extends BasicLay{
	
	public TextureRegion tr;
	private Border b;
	float scale =1;
    public ImageActor(TextureRegion tr, float width, float height) {
        this.tr=tr;
        setSize(width, height);
    }

    public void setImageScale(float scale){
        this.scale=scale;
    }

    public void setBorder(Border b){
        this.b=b;
    }

    public ImageActor(TextureRegion tr) {
        this(tr, tr.getRegionWidth(), tr.getRegionHeight());
    }

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(b!=null){
		    b.draw(batch, this);
        }
        batch.setColor(getColor());
        if(flash){
            float freq = 3.5f;
            batch.setColor(Colours.withAlpha(getColor(), (float)(Math.sin(Main.ticks*freq)+1)/2));
        }
        if(scale!=1){
            Draw.drawCenteredScaled(batch, tr, getX()+getWidth()/2, getY()+getWidth()/2, getWidth()/tr.getRegionWidth()*scale, getHeight()/tr.getRegionHeight()*scale);
        }
        else{
            Draw.drawSize(batch, tr, getX(), getY(), getWidth(), getHeight());
        }

    }


    @Override
    public void layout() {

    }

    boolean flash;
    public void flash(boolean flash) {
        System.out.println("flash");
        this.flash=flash;
    }

}
