package com.tann.dice.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.Main;

public class DicetopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled=true;
		config.width=Main.width;
		config.height=Main.height;
		config.samples=10;
		config.title="Dicegeon";
        config.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new Main(), config);
	}
}
