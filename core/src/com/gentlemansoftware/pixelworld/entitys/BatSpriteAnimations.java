package com.gentlemansoftware.pixelworld.entitys;

import com.badlogic.gdx.graphics.Texture;
import com.gentlemansoftware.pixelworld.game.ResourceLoader;
import com.gentlemansoftware.pixelworld.physics.WorldTime;

public class BatSpriteAnimations {
	
	private static final String ENTITYTYPENAME = "bat";

	public static Texture getTexture(MotionState motion,WorldTime time){
		switch(motion){
		case STOP : return getSittingTexture(time);
		case MOVING : break;
		}
		return getTexture("bat_fly-0");
	}
	
	public static float getAnimationTime(WorldTime time){
		float animationTimeInTicks = time.getTicksPerSecond()*sittingAnimationTime;
		float percentage = (time.getTicks()%(animationTimeInTicks))/animationTimeInTicks;
		return percentage;
	}
	
	public static float sittingAnimationTime = .5f;
	private static Texture getSittingTexture(WorldTime time){
		float percentage = getAnimationTime(time);
		if(percentage<0.25f) return getTexture("bat_fly-0");
		if(percentage<0.5f) return getTexture("bat_fly-1");
		if(percentage<0.75f) return getTexture("bat_fly-2");
		return getTexture("bat_fly-3");
	}
	
	private static Texture getTexture(String name){
		return ResourceLoader.getInstance().getEntity(ENTITYTYPENAME,name); 
	}

}
