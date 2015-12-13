package djarroba.ludumdare34.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import djarroba.ludumdare34.screens.GameScreen;

public class Hud {

	GameScreen screen;

	public FitViewport viewport;

	public Stage stage;
	Skin skin;

	Table table;
	public Label dotsLeftLabel;
	public Label winLoseLabel;

	public Hud(GameScreen screen) {
		this.screen = screen;


		viewport = new FitViewport(16, 9, screen.camera);

		stage = new Stage();
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

		table = new Table();
		table.setFillParent(true);
		table.top().left();

		dotsLeftLabel = new Label("", skin);
		table.add(dotsLeftLabel).pad(10).expandX().fillX();

		table.row();

		winLoseLabel = new Label("", skin);
		winLoseLabel.setVisible(true);

		table.add(winLoseLabel).center().expand().center();

		stage.addActor(table);
	}

	public void update(float delta) {
		dotsLeftLabel.setText(screen.dotsLeft + "");
		viewport.apply();
		stage.act(delta);
		stage.draw();
	}

}
