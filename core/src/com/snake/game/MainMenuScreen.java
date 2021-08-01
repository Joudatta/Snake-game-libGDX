package com.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final Snake game;
    OrthographicCamera camera;
    private Texture playButtonUnclicked;
    private Texture playButtonClicked;


    public MainMenuScreen(final Snake game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        playButtonUnclicked = new Texture("PlayButtonUnclicked.png");
        playButtonClicked = new Texture("PlayButtonClicked.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.5f, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        resize(1280, 720);

        if (!((Gdx.input.getX() >= 548) && (Gdx.input.getX() <= 680) && (Gdx.input.getY() >= 212) && (Gdx.input.getY() <= 280))) {
            game.batch.begin();
            game.batch.draw(playButtonUnclicked, 550, 440);
            game.font.draw(game.batch, "Play", 600, 480);
            game.batch.end();
        }

        if (Gdx.input.getX() >= 548 && Gdx.input.getX() <= 680 && Gdx.input.getY() >= 212 && Gdx.input.getY() <= 280) {
            game.batch.begin();
            game.batch.draw(playButtonClicked, 550, 440);
            game.font.draw(game.batch, "Play", 600, 480);
            game.batch.end();

            if (Gdx.input.isTouched()) { //make this so that it starts the game once u click inside the play button
//                System.out.println("x = " + Gdx.input.getX() + ", y = " + Gdx.input.getY());
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        Gdx.graphics.setWindowedMode(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
