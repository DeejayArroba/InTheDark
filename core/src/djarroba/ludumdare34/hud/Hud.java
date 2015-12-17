package djarroba.ludumdare34.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import djarroba.ludumdare34.screens.GameScreen;

public class Hud implements Disposable {

	GameScreen screen;

	public FitViewport viewport;

	public Stage stage;
	Skin skin;

	Table table;
	Table midTable;
	public Label dotsLeftLabel;
	public Label levelLabel;
	public Label timeLabel;
	public Label winLoseLabel;

	public Hud(GameScreen screen) {
		this.screen = screen;

		viewport = new FitViewport(16, 9);

		stage = new Stage();
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

		table = new Table();
		table.setFillParent(true);
		table.top().left();

		dotsLeftLabel = new Label("", skin);
		dotsLeftLabel.setFontScale(0.5f);
		table.add(dotsLeftLabel).pad(10).expandX().fillX();

		levelLabel = new Label("Level " + (screen.game.mapManager.getLevelIndex(screen.map)+1), skin);
		levelLabel.setFontScale(0.5f);
		table.add(levelLabel).pad(10).expandX().fillX();

		timeLabel = new Label("5s left", skin);
		timeLabel.setFontScale(0.5f);
		table.add(timeLabel).pad(10).expandX().fillX();

		midTable = new Table();
		midTable.setFillParent(true);
		midTable.top().left().center();


		winLoseLabel = new Label("", skin);
		winLoseLabel.setVisible(true);

		midTable.add(winLoseLabel).expand();

		stage.addActor(table);
		stage.addActor(midTable);
	}

	public void update(float delta) {
		viewport.apply();
		dotsLeftLabel.setText(screen.dotsLeft + " orbs left");
		timeLabel.setText(Math.round(screen.timeLeft * 100f)/100f + "s left");
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
