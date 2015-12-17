package djarroba.ludumdare34.level;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import djarroba.ludumdare34.entities.Dot;
import djarroba.ludumdare34.entities.Projectile;
import djarroba.ludumdare34.screens.GameScreen;
import djarroba.ludumdare34.units.Units;
import djarroba.ludumdare34.util.Colors;
import djarroba.ludumdare34.util.PhysicsCategories;

import java.util.ArrayList;

public class Level {

	Body body;
	TiledMap map;
	Vector2 spawnPosition;
	String name;
	public ArrayList<Dot> dots;
	GameScreen screen;

	public Level(TiledMap map, GameScreen screen) {
		this.name = (String) map.getProperties().get("name");
		this.map = map;
		this.screen = screen;

		this.dots = new ArrayList<Dot>();
		float spawnX = map.getLayers().get("Spawn").getObjects().get(0).getProperties().get("x", Float.class) / Units.PPU;
		float spawnY = map.getLayers().get("Spawn").getObjects().get(0).getProperties().get("y", Float.class) / Units.PPU;
		this.spawnPosition = new Vector2(spawnX, spawnY);

		BodyDef worldBodyDef = new BodyDef();
		worldBodyDef.type = BodyDef.BodyType.StaticBody;
		worldBodyDef.position.set(0, 0);

		body = screen.world.createBody(worldBodyDef);

		TiledMapTileLayer wallLayer = (TiledMapTileLayer) map.getLayers().get("Walls");
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 9; y++) {
				TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
				if(cell != null) {
					if(cell.getTile().getProperties().containsKey("blocked")) {
						FixtureDef fixtureDef = new FixtureDef();

						PolygonShape shape = new PolygonShape();
						shape.setAsBox(0.5f, 0.5f, new Vector2(x + 0.5f, y + 0.5f), 0);

						fixtureDef.shape = shape;
						fixtureDef.filter.categoryBits = PhysicsCategories.WORLD;

						body.createFixture(fixtureDef);

						shape.dispose();
					}
				}
			}
		}

		for(MapObject mapObject : map.getLayers().get("Dots").getObjects()) {
			float objectX = mapObject.getProperties().get("x", Float.class) / Units.PPU;
			float objectY = mapObject.getProperties().get("y", Float.class) / Units.PPU;

			Dot dot = new Dot(screen, new Vector2(objectX, objectY));
			screen.entityManager.add(dot);
			dots.add(dot);
		}
	}

	public void reset() {
		this.dots = new ArrayList<Dot>();

		for(MapObject mapObject : map.getLayers().get("Dots").getObjects()) {
			float objectX = mapObject.getProperties().get("x", Float.class) / Units.PPU;
			float objectY = mapObject.getProperties().get("y", Float.class) / Units.PPU;

			Dot dot = new Dot(screen, new Vector2(objectX, objectY));
			screen.entityManager.add(dot);
			dots.add(dot);
		}
	}

	public TiledMap getMap() {
		return map;
	}

	public Vector2 getSpawnPosition() {
		return spawnPosition;
	}

	public Dot getDotByBody(Body body) {
		for(Dot dot : dots) {
			if(body == dot.body) {
				return dot;
			}
		}
		return null;
	}


}
