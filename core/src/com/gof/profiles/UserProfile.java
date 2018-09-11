package com.gof.profiles;

import com.gof.game.SaveAndLoadable;

public class UserProfile extends SaveAndLoadable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7248397811160277883L;
	public String name;

	public UserProfile() {
		this.name = "Default";
	}

	public boolean showDebugInformationSide = false;
	public boolean showDebugInformationCoordinatesOnMapTiles = false;


	public static final transient String DATA = "data/";
	public static final transient String PROFILES = DATA + "profiles/";
	public static final transient String ENDING = ".profile";

	public static UserProfile load(String name) {
		return (UserProfile) SaveAndLoadable.loadFromInternal(PROFILES + name + ENDING, UserProfile.class);
	}

	public void save() {
		super.saveToInternal(PROFILES + name + ENDING);
	}

}