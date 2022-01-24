package com.github.danirod12.jackal.client.objects;

import com.github.danirod12.jackal.client.Jackal;

import java.awt.*;

public interface AppObject {

    void tick();

    default void destroy() { Jackal.getGameLoop().getObjectsHandler().remove(this); }

}
