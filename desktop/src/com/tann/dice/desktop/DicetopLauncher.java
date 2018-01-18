package com.tann.dice.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.tann.dice.Main;

import java.io.*;
import java.util.Scanner;

public class DicetopLauncher {
	public static void main (String[] arg)  {
        checkPack("../../images_3d", "imagehash3d.txt", true);
        checkPack("../../images", "imagehash2d.txt", false);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled=true;
		config.width=1280;
		config.height=720;
		config.samples=1;
		config.title="Dicegeon";
        config.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), config);
	}

	private static void checkPack(String dir, String file, boolean threeD){
        int total = hash(new File(dir));
        int c = 0;
        try {
            File f= new File(file);
            if(!f.exists()){
                PrintWriter pw = new PrintWriter(f);
                pw.print(-1);
                pw.close();
            }
            Scanner s = new Scanner(f);
            c = s.nextInt();
            if (total != c){
                PrintWriter pw = new PrintWriter(f);
                pw.print(total);
                pw.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(total!=c){
            packImages(threeD);
        }

    }

	private static int hash(File f){
        int total = 0;
	    if(f.isDirectory()) {
            for (File content :f.listFiles()){
                total += hash(content);
            }
        }
        if(!f.isDirectory()) {
            try {
                FileInputStream in = new FileInputStream(f.getPath());
                int c;
                while ((c = in.read()) != -1) {
                    total += c;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    private static void packImages(boolean threeD){
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.silent = true;
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
	    if(threeD){
            settings.minWidth = 2048;
            settings.minHeight = 2048;
            settings.paddingX = 2;
            settings.paddingY = 2;
            settings.combineSubdirectories = true;
            settings.filterMag = Texture.TextureFilter.MipMap;
            settings.filterMin = Texture.TextureFilter.MipMap;
            TexturePacker.process(settings, "../../images_3d", "3d", "atlas_image");
        }
        else{
            settings.combineSubdirectories = false;
            settings.filterMag = Texture.TextureFilter.Nearest;
            settings.filterMin = Texture.TextureFilter.Nearest;
            TexturePacker.process(settings, "../../images", "2d", "atlas_image");
        }
    }
}
