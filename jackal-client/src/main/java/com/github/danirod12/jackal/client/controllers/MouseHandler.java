package com.github.danirod12.jackal.client.controllers;

import com.github.danirod12.jackal.client.render.GameLoop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener {

    private final GameLoop loop;

    public MouseHandler(GameLoop loop) {
        this.loop = loop;
    }

    private boolean released = true;
    private MouseExecutor drag_session = null;

    @Override
    public void mouseClicked(MouseEvent e) {
        a(e.getX(), e.getY());
    }

    public void a(int a, int b) {
        this.drag_session = loop.onMouseClick(a, b);
        released = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(released) a(e.getX(), e.getY());
        if(drag_session != null)
            drag_session.onMouseClick(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        loop.onMouseMove(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {
        drag_session = null;
        released = true;
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

}
