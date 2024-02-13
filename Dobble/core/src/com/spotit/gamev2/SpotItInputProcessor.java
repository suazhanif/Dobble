package com.spotit.gamev2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.spotit.gamev2.Commands.CommandQueue;
import com.spotit.gamev2.Commands.SymbolClickedCommand;

public class SpotItInputProcessor implements InputProcessor {

    private final OrthographicCamera camera;
    private final CommandQueue commandQueue;
    private CardPair currCardPair;
    private GameStateMachine gsm;

    public SpotItInputProcessor(OrthographicCamera camera, CommandQueue commandQueue, GameStateMachine gsm) {
        super();
        this.camera = camera;
        this.commandQueue = commandQueue;
        currCardPair = null;
        this.gsm = gsm;
    }

    public void setCurrCardPair(CardPair currCardPair) {
        this.currCardPair = currCardPair;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.printf("%s Key Down\n", Input.Keys.toString(keycode));
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.printf("%s Key Up\n", Input.Keys.toString(keycode));
        gsm.handleKeyInput(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 cursor = new Vector2(
                MathUtils.map(
                        0,
                        Gdx.graphics.getWidth(),
                        camera.viewportWidth / -2f,
                        camera.viewportWidth / 2f,
                        screenX
                ),
                MathUtils.map(
                        0,
                        Gdx.graphics.getHeight(),
                        camera.viewportHeight / 2f,
                        camera.viewportHeight / -2f,
                        screenY
                )
        );
        System.out.printf("touch down at (x:%.2f, y:%.2f)\n", cursor.x, cursor.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 cursor = new Vector2(
                MathUtils.map(
                        0,
                        Gdx.graphics.getWidth(),
                        camera.viewportWidth / -2f,
                        camera.viewportWidth / 2f,
                        screenX
                ),
                MathUtils.map(
                        0,
                        Gdx.graphics.getHeight(),
                        camera.viewportHeight / 2f,
                        camera.viewportHeight / -2f,
                        screenY
                )
        );
        System.out.printf("touch up at (x:%.2f, y:%.2f)\n", cursor.x, cursor.y);

        if (button == Input.Buttons.LEFT && pointer == 0 && currCardPair != null) {
            for (Symbol symbol : currCardPair.getSymbols()) {
                if (symbol.getSprite().getBoundingRectangle().contains(cursor)) {
                    commandQueue.add(new SymbolClickedCommand(symbol, currCardPair));
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
