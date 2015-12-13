package djarroba.ludumdare34.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import djarroba.ludumdare34.MyGame;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(853, 480);
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return new MyGame();
	}

}