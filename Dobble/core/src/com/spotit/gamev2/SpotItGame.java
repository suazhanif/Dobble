package com.spotit.gamev2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.spotit.gamev2.Commands.CommandQueue;
import com.spotit.gamev2.Commands.EndOfGameCommand;
import com.spotit.gamev2.Commands.ScorePointsCommand;

public class SpotItGame extends ApplicationAdapter {

	ShapeRenderer shapeRenderer;

	SpriteBatch batch;
	BitmapFont font;

	Player player;
	Deck deck;
	CardPair currCardPair;
	CommandQueue commandQueue;
	GameStateMachine gsm;

	OrthographicCamera camera;
	SpotItInputProcessor inputProcessor;

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();

		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("segoeUIblack_32.fnt"));
		font.setColor(Color.WHITE);

		player = new Player("Andrew");
		deck = new Deck("SpaceIcons.atlas", 6);
		currCardPair = null;
		commandQueue = new CommandQueue();
		gsm = new GameStateMachine(commandQueue, deck);

		camera = new OrthographicCamera(900f, 600f);
		inputProcessor = new SpotItInputProcessor(camera, commandQueue, gsm);
		Gdx.input.setInputProcessor(inputProcessor);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0x2b / 255f, 0x2b / 255f, 0x2b / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		switch (gsm.getState()) {
			case BEGIN:
				drawBeginScreen();
				break;
			case RUN:
				if (currCardPair != null) {
					drawRunScreen();
				}
				break;
			case END:
				drawEndScreen();
		}

	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
	}

	private void update() {
		if (deck.isEmpty()) {
			commandQueue.add(new EndOfGameCommand(gsm));
		}
		else if (currCardPair == null) {
			currCardPair = deck.pickCardPair();
		}
		else if (currCardPair.solved) {
			currCardPair = deck.pickCardPair();
			commandQueue.add(new ScorePointsCommand(player, 1));
		}
		else if (currCardPair != null) {
			arrangeSymbolSprites(200f, 0.5f);
		}

		inputProcessor.setCurrCardPair(currCardPair);
		commandQueue.tick();
	}

	private void drawBeginScreen() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(
				batch,
				"Press Space to Begin",
				camera.viewportWidth / -2f + 50f,
				camera.viewportHeight / 2f - 50f
		);
		batch.end();
	}

	private void drawRunScreen() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.circle(
				currCardPair.cardL.getCircle().x,
				currCardPair.cardL.getCircle().y,
				currCardPair.cardL.getCircle().radius
		);
		shapeRenderer.circle(
				currCardPair.cardR.getCircle().x,
				currCardPair.cardR.getCircle().y,
				currCardPair.cardR.getCircle().radius
		);
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.circle(
				currCardPair.cardL.getCircle().x,
				currCardPair.cardL.getCircle().y,
				currCardPair.cardL.getCircle().radius * 0.95f
		);
		shapeRenderer.circle(
				currCardPair.cardR.getCircle().x,
				currCardPair.cardR.getCircle().y,
				currCardPair.cardR.getCircle().radius * 0.95f
		);
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(
				batch,
				String.format("Score: %d", player.getScore()),
				camera.viewportWidth / -2f + 50f,
				camera.viewportHeight / 2f - 50f
		);
		for (Symbol symbol : currCardPair.getSymbols()) {
			symbol.getSprite().draw(batch);
		}
		batch.end();
	}

	private void arrangeSymbolSprites(float cardRadius, float symbolSizeFactor) {
		float angle = 0f; // degrees

		Card cardL = currCardPair.cardL;
		cardL.setCardPosition(new Vector2(camera.viewportWidth / -4f, 0f));
		cardL.setCardRadius(cardRadius);
		for (Symbol symbolL : cardL.getSymbols()) {
			symbolL.getSprite().setSize(
					cardL.getCircle().radius * symbolSizeFactor,
					cardL.getCircle().radius * symbolSizeFactor
			);
			symbolL.getSprite().setCenter(
					cardL.getCircle().x -  + cardL.getCircle().radius * 0.6f * MathUtils.cosDeg(angle),
					cardL.getCircle().y + cardL.getCircle().radius * 0.6f * MathUtils.sinDeg(angle)
			);
			angle += 360f / cardL.getSymbols().length;
		}

		Card cardR = currCardPair.cardR;
		cardR.setCardPosition(new Vector2(camera.viewportWidth / 4f, 0f));
		cardR.setCardRadius(cardRadius);
		for (Symbol symbolR : cardR.getSymbols()) {
			symbolR.getSprite().setSize(
					cardR.getCircle().radius * symbolSizeFactor,
					cardR.getCircle().radius * symbolSizeFactor
			);
			symbolR.getSprite().setCenter(
					cardR.getCircle().x + cardR.getCircle().radius * 0.6f * MathUtils.cosDeg(angle),
					cardR.getCircle().y + cardR.getCircle().radius * 0.6f * MathUtils.sinDeg(angle)
			);
			angle += 360f / cardR.getSymbols().length;
		}
	}

	private void drawEndScreen() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(
				batch,
				String.format("Final Score: %d", player.getScore()),
				camera.viewportWidth / -2f + 50f,
				camera.viewportHeight / 2f - 50f
		);
		font.draw(
				batch,
				"Press Space to Play Again",
				camera.viewportWidth / -2f + 50f,
				camera.viewportHeight / 2f - 100f
		);
		batch.end();
	}

}
