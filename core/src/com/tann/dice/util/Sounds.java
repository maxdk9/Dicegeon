package com.tann.dice.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class Sounds {


	public static AssetManager am= new AssetManager();

    public static String[] clacks;
    public static String[] clocks;
	public static String[] lock;
	public static String[] unlock;

	public static String[] punches;
	public static String[] hits;
	public static String[] fwips;
	public static String[] blocks;
	public static String[] heals;
	public static String[] magic;

	public static String[] boost;
	public static String[] copy;
	public static String[] taunt;

	public static String[] death;

	public static String[] pip;
	public static String[] pop;
	public static String[] error;

	public static String[] levelup;


	public static void setup(){
		//sfx//
        clacks = makeSounds("dice/clack", 4);
        clocks = makeSounds("dice/clock", 4);
		lock = makeSounds("dice/lock", 1);
		unlock = makeSounds("dice/unlock", 1);

		fwips = makeSounds("combat/fwip", 5);
		hits = makeSounds("combat/hit", 6);
		punches = makeSounds("combat/punch", 5);
		blocks = makeSounds("combat/block", 4);
		heals = makeSounds("combat/heal", 4);
		magic = makeSounds("combat/mystic", 4);
		death = makeSounds("combat/death", 1);
		boost = makeSounds("combat/boost", 1);
		copy = makeSounds("combat/copy", 1);
		taunt = makeSounds("combat/taunt", 1);

		pip = makeSounds("ui/pip", 5);
		pop = makeSounds("ui/pop", 6);
		error = makeSounds("ui/error", 1);

		levelup = makeSounds("ui/levelup", 1);



		//stuff to attempt to load sounds properly//
		am.finishLoading();
		Array<Sound> sounds = new Array<>();
		am.getAll(Sound.class, sounds);
		for(Sound s:sounds)s.play(0);
		Array<Music> musics = new Array<>();
		am.getAll(Music.class, musics);
		for(Music m:musics){
			m.play();
			m.setVolume(1);
			m.stop();
		}
	}
	
	public static <T> T get(String name, Class<T> type){
		return am.get(name, type);

	}

    private static String makeSound(String path, Class type){
        am.load(path, type);
        return path;
    }

    private static String[] makeSounds(String path, int amount){
        return makeSounds(path,amount,".ogg");
    }

    private static String[] makeSounds(String path, int amount, String extension){
        String[] strings = new String[amount];
        for(int i=0;i<amount;i++){
            String s = "sfx/"+path+"_"+i+extension;
            makeSound(s, Sound.class);
            strings[i]=s;
        }
        return strings;
    }
	
	private static ArrayList<Fader> faders = new ArrayList<Sounds.Fader>();
	
	public static void fade(Music m, float targetVolume, float duration){
		faders.add(new Fader(m, targetVolume, duration));
	}
	
	public static void tickFaders(float delta){
		for(int i=faders.size()-1;i>=0;i--){
			Fader f = faders.get(i);
			f.tick(delta);
			if(f.done)faders.remove(f);
		}
	}
	
	private static Music previousMusic;
	private static Music currentMusic;
	public static void playMusic(String path){
        Music m = Sounds.get(path, Music.class);
        stopMusic();
		previousMusic=currentMusic;
		currentMusic=m;
		currentMusic.play();
		currentMusic.setLooping(true);
		updateMusicVolume();
	}

	public static void stopMusic(){
	    if(currentMusic!=null)  currentMusic.stop();
    }

	public static void updateMusicVolume(){
		if(currentMusic!=null)currentMusic.setVolume(Slider.music.getValue());
	}
	
	static class Fader{
		float startVolume;
		float targetVolume;
		Music music;
		boolean done;
		float duration;
		float ticks;
		public Fader(Music m, float targetVolume, float duration) {
			this.startVolume=m.getVolume();
			this.targetVolume=targetVolume;
			this.music=m;
			this.duration=duration;
		}
		public void tick(float delta){
			ticks+=delta;
			if(ticks>duration){
				ticks=duration;
				done=true;
			}
			float ratio = ticks/duration;
			float newVolume =startVolume+(targetVolume-startVolume)*ratio;
			music.setVolume(newVolume);
		}
	}

	static HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	public static void playSound(String string, float volume, float pitch) {
		Sound s = soundMap.get(string);
		if(s==null){
            s=get(string, Sound.class);
			soundMap.put(string, s);
		}
		s.play(Slider.SFX.getValue()*2*volume, pitch, 0);
	}

	static HashMap<String[], Long> timeLastPlayedMap = new HashMap<>();
	private static final long repeatTime = 50;

	public static void playSound(String[] strings){
		playSound(strings, 1, 1);
	}

	public static void playSound(String[] strings, float volume, float pitch){
        Long l = timeLastPlayedMap.get(strings);
        long now = System.currentTimeMillis();
        if(l!=null && now-l<repeatTime){
        	return;
		}
		timeLastPlayedMap.put(strings, now);
		playSound(strings[((int)(Math.random()* strings.length))], volume, pitch);
    }

	public static void playSoundDelayed(final String[] sound, final float volume, final float pitch, float delay){
		Tann.delay(new Runnable() {
			@Override
			public void run() {
				Sounds.playSound(sound, volume, pitch);
			}
		}, delay);
	}

}
