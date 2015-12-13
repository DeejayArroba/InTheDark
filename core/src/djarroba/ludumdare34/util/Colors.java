package djarroba.ludumdare34.util;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class Colors {

	public static final Color player = new Color(0f, 0.2f, 0.8f, 0.6f);
	public static final Color particlePlayer = new Color(0f, 0.2f, 0.8f, 0.3f);
	public static final Color particleProjectile = new Color(0f, 0.2f, 0.8f, 0.2f);
	public static final Color particleDot = new Color(0.8f, 0.8f, 0.8f, 0.2f);

	public static Color getColor(String str) {
		if(str != null) {
			if(str.equals("red"))
				return new Color(1, 0, 0, 1);
			if(str.equals("green"))
				return new Color(0, 1, 0, 1);
			if(str.equals("blue"))
				return new Color(0, 0, 1, 1);
		}
		return new Color(1, 1, 1, 1);
	}

	public static Color randomColor(float alpha) {
		Random random = new Random();
		float r = random.nextFloat();
		float g = random.nextFloat();
		float b = random.nextFloat();

		return new Color(r, g, b, alpha);
	}

}
