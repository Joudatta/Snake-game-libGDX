package com.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class GameScreen implements Screen {

    final Snake game;
    Texture snakeBlockImage;
    Texture appleImage;
    Array<Rectangle> snakeChains;
    Rectangle apple;
    OrthographicCamera camera;

    Random rand = new Random();
    private static int windowWidth;
    private static int windowHeight;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private int prevX, prevY;
    private int direction = rand.nextInt(4);
    private int prevDirection = direction;
    boolean isAppleEaten = false;


    public GameScreen(final Snake game) {
        this.game = game;
        snakeBlockImage = new Texture(Gdx.files.internal("Snake_block.png"));
        appleImage = new Texture(Gdx.files.internal("apple.png"));
        windowWidth = Gdx.graphics.getWidth()/4;
        windowHeight = Gdx.graphics.getHeight()/4;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);

        snakeChains = new Array<>();
        System.out.println(snakeChains.size);
        Rectangle snakeUnit = new Rectangle();
        snakeUnit.x = windowWidth/2;
        snakeUnit.y = windowHeight/2;
        snakeUnit.width = 16;
        snakeUnit.height = 16;
        snakeChains.add(snakeUnit);
        if (snakeChains.size == 0) {
            System.out.println("Size is 0!");
        }

        apple = new Rectangle();
        apple.x = rand.nextInt(windowWidth);
        System.out.println(windowWidth);
        apple.y = rand.nextInt(windowHeight);
        System.out.println(windowHeight);
        apple.width = 16;
        apple.height = 16;
    }

    private void spawnSnakeBody() {

        Rectangle snakeUnit = new Rectangle();
        snakeUnit.width = 16;
        snakeUnit.height = 16;
        System.out.println(snakeChains.size);

        if (direction == LEFT) {
            snakeUnit.x = snakeChains.get(snakeChains.size-1).x + 256;
        }
        else if (direction == RIGHT) {
            snakeUnit.x = snakeChains.get(snakeChains.size-1).x - 256;
        }
        else if (direction == UP) {
            snakeUnit.y = snakeChains.get(snakeChains.size-1).y -256;
        }
        else if (direction == DOWN) {
            snakeUnit.y = snakeChains.get(snakeChains.size-1).y + 256;
        }
        else {
            return;
        }
        snakeChains.add(snakeUnit);
    }

    private void move() {
        prevDirection = direction;

        for (int i = snakeChains.size-1; i > 0; i--) {
            snakeChains.get(i).x = snakeChains.get(i-1).x;
            snakeChains.get(i).y = snakeChains.get(i-1).y;// to be continued
        }

        //NOTE: FOR SOME REASON, THE SIZE OF THE BODY OF THE SNAKE DEPENDS ON THE RATE A WHICH THE HEAD OF THE SNAKE IS MOVING,
        //TRY TO FIND A WAY TO SEPARATE THE SPEED OF THE SNAKE FROM THE SIZE OF THE SNAKE
        if (direction == LEFT) {
            snakeChains.get(0).x -= 64 * Gdx.graphics.getDeltaTime();
        }
        else if (direction == RIGHT) {
            snakeChains.get(0).x += 64 * Gdx.graphics.getDeltaTime();
        }
        else if (direction == UP) {
            snakeChains.get(0).y += 64 * Gdx.graphics.getDeltaTime();
        }
        else if (direction == DOWN ) {
            snakeChains.get(0).y -= 64 * Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        for (Rectangle snakeUnit: snakeChains) {
            game.batch.draw(snakeBlockImage, snakeUnit.x, snakeUnit.y, 16, 16);
        }
        game.batch.draw(appleImage, apple.x, apple.y, apple.width, apple.height);
        game.batch.end();

        if (snakeChains.get(0).overlaps(apple)) {

            isAppleEaten = true;
            if (snakeChains.get(0).x >= windowWidth/2 && snakeChains.get(0).y >= windowHeight/2) { //Q1
                apple.x = rand.nextInt(windowWidth/2);
                apple.y = rand.nextInt(windowHeight/2);
            }
            else if (snakeChains.get(0).x < windowWidth/2 && snakeChains.get(0).y >= windowHeight/2) { //Q2
                apple.x = rand.nextInt(windowWidth/2) + windowWidth/2;
                apple.y = rand.nextInt(windowHeight/2);
            }
            else if (snakeChains.get(0).x < windowWidth/2 && snakeChains.get(0).y < windowHeight/2) { //Q3
                apple.x = rand.nextInt(windowWidth/2) + windowWidth/2;
                apple.y = rand.nextInt(windowHeight/2) + windowHeight/2;
            }
            else if (snakeChains.get(0).x >= windowWidth/2 && snakeChains.get(0).y < windowHeight/2) { //Q4
                apple.x = rand.nextInt(windowWidth/2);
                apple.y = rand.nextInt(windowHeight/2) + windowHeight/2;
            }

            spawnSnakeBody();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && prevDirection != RIGHT) {
            direction = LEFT;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D) && prevDirection != LEFT) {
            direction = RIGHT;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.W) && prevDirection != DOWN) {
            direction = UP;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && prevDirection != UP) {
            direction = DOWN;
        }

        move();

        if (snakeChains.get(0).x < 0 || snakeChains.get(0).x > windowWidth || snakeChains.get(0).y < 0 || snakeChains.get(0).y > windowHeight) {
            game.setScreen(new MainMenuScreen(game));
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
        snakeBlockImage.dispose();
        appleImage.dispose();
        game.batch.dispose();
        game.dispose();
    }
}
