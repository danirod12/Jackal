package com.github.danirod12.jackal.client.controllers;

import java.awt.event.KeyEvent;

public interface KeyboardExecutor {

    /**
     * String contains only one char
     */
    public void onKeyTyped(KeyEvent key);

}
