package com.tann.dice.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tann.dice.Main;

public class DicetopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled=true;
		config.width=1280;
		config.height=720;
		config.title="Dicegeons";
		new LwjglApplication(new Main(), config);
	}
}
