package com.gentlemansoftware.pixelworld.menu;

import java.util.LinkedList;
import java.util.List;

import com.gentlemansoftware.pixelworld.game.Main;
import com.gentlemansoftware.pixelworld.profiles.UserProfile;
import com.gentlemansoftware.pixelworld.simplemenu.SimpleMenu;
import com.gentlemansoftware.pixelworld.simplemenu.SimpleMenuComponent;
import com.gentlemansoftware.pixelworld.simplemenu.SimpleMenuNameTypes;
import com.gentlemansoftware.pixelworld.simplemenu.SimpleMenuRunnableItem;
import com.gentlemansoftware.pixelworld.simplemenu.SimpleMenuVarholder;
import com.gentlemansoftware.pixelworld.world.WorldToPNG;

public class OptionMenu extends SimpleMenu {
	
	Menu audioMenu; 
	Menu debugMenu;

	public OptionMenu(MenuHandler handler, Menu parent) {
		super(handler, parent, "Options", null);
		UserProfile profile = this.handler.user.profile;
		debugMenu = new SimpleMenuVarholder(handler, this, "Debug Options",profile.debugProfile.getVars());
		audioMenu = new SimpleMenuVarholder(handler, this, "Audio Options",profile.soundProfile.getVars());
		
		this.setContent(initMenuComponents());
	}

	public List<SimpleMenuComponent> initMenuComponents() {
		List<SimpleMenuComponent> menuComponents = new LinkedList<SimpleMenuComponent>();

		Runnable quitRunnable = new Runnable() {
			public void run() {
				WorldToPNG.saveToImage(Main.getInstance().titleScreenWorld);
			}
		};

		SimpleMenuRunnableItem WorldToPNGItem = new SimpleMenuRunnableItem("World To PNG", SimpleMenuNameTypes.SUB,
				quitRunnable);
		
		menuComponents.add(debugMenu);
		menuComponents.add(audioMenu);
		menuComponents.add(WorldToPNGItem);

		menuComponents.add(parent);

		return menuComponents;
	}

}
