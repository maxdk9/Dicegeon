package com.tann.dice.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DicePack {
  public static void main(String[] args){
    Settings settings = new Settings();
    settings.combineSubdirectories = false;
    settings.maxWidth=2048;
    settings.maxHeight=2048;
    settings.filterMag= Texture.TextureFilter.Linear;
    settings.filterMin= Texture.TextureFilter.Linear;
    TexturePacker.process(settings, "../images", "../android/assets", "atlas_image");


    settings.minWidth=2048;
    settings.minHeight=2048;
    settings.paddingX=2;
    settings.paddingY=2;
    settings.combineSubdirectories=true;
      settings.filterMag= Texture.TextureFilter.MipMap;
      settings.filterMin= Texture.TextureFilter.MipMap;
    TexturePacker.process(settings, "../images_3d", "../android/assets/3d", "atlas_image");
  }

}
