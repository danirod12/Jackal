package com.github.danirod12.jackal.client.controllers;

import com.github.danirod12.jackal.client.render.GameLoop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHandler implements KeyListener {

    private final GameLoop loop;

    public KeyboardHandler(GameLoop loop) {
        this.loop = loop;
        loop.getFrameRender().setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        loop.onKeyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

}
