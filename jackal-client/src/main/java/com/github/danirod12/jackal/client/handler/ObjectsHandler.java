package com.github.danirod12.jackal.client.handler;

import com.github.danirod12.jackal.client.Jackal;
import com.github.danirod12.jackal.client.controllers.KeyboardExecutor;
import com.github.danirod12.jackal.client.controllers.MouseExecutor;
import com.github.danirod12.jackal.client.objects.AppObject;
import com.github.danirod12.jackal.client.objects.RenderObject;
import com.github.danirod12.jackal.client.objects.input.ChatObject;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Consumer;

public class ObjectsHandler {

    private ConcurrentSkipListMap<Integer, Collection<AppObject>> map = new ConcurrentSkipListMap<>(Comparator.naturalOrder());

    public synchronized ChatObject getChat() {

        if(map.containsKey(RenderLayer.CHAT.getLayer())) {

            Collection<AppObject> collection = map.get(RenderLayer.CHAT.getLayer());
            return collection.size() == 0 ? null : (ChatObject) collection.iterator().next();

        }
        return null;

    }

    public synchronized Collection<RenderObject> getRenderObjects() {

        Collection<RenderObject> objects = new ArrayList<>();
        for (Map.Entry<Integer, Collection<AppObject>> collection : map.entrySet()) {
            if(collection.getKey() == RenderLayer.NOT_RENDER.getLayer()) continue;
            objects.addAll(this.findAll(RenderObject.class, collection.getValue()));
        }
        return objects;

    }

    public synchronized Collection<MouseExecutor> getMouseExecutors() {
        return this.findAll(MouseExecutor.class);
    }

    public synchronized Collection<KeyboardExecutor> getKeyboardExecutors() {

//        Collection<KeyboardExecutor> objects = new ArrayList<>();
//        for (Map.Entry<Integer, Collection<AppObject>> collection : map.entrySet()) {
//            for(AppObject object : collection.getValue()) {
//                if(object instanceof KeyboardExecutor)
//                    objects.add((KeyboardExecutor) object);
//            }
//        }
//        return objects;
        return this.findAll(KeyboardExecutor.class);

    }

    public synchronized Collection<AppObject> getLayerCopy(RenderLayer layer) {
        return getLayerCopy(layer.getLayer());
    }

    public synchronized Collection<AppObject> getLayerCopy(int layer) {
        return map.containsKey(layer) ? new ArrayList<>(map.get(layer)) : new ArrayList<>();
    }

    public synchronized Collection<AppObject> getLayer(RenderLayer layer, boolean create) {
        return getLayer(layer.getLayer(), create);
    }

    public synchronized Collection<AppObject> getLayer(int layer, boolean create) {
        if(map.containsKey(layer)) return map.get(layer);
        Collection<AppObject> collection = new ArrayList<>();
        if(create) map.put(layer, collection);
        return collection;
    }

    public synchronized void add(RenderLayer layer, AppObject object) {
        add(layer.getLayer(), object);
    }

    public synchronized void add(int layer, AppObject object) {
        Collection<AppObject> collection = getLayer(layer, true);
        collection.add(object);
    }

    public synchronized void remove(AppObject object) {
        map.forEach((k, v) -> v.remove(object));
    }

    public synchronized void remove(RenderLayer layer) {
        remove(layer.getLayer());
    }

    public synchronized void remove(int layer) {
        map.remove(layer);
    }

    public synchronized void removeAll() {
        map.clear();
        Jackal.getGameLoop().unselectObject();
    }

    public synchronized void forEach(Consumer<AppObject> action) {
        map.values().forEach(n -> new ArrayList<>(n).forEach(action));
    }

    public synchronized <T> Collection<T> findAll(Class<? extends T> type) {
        Collection<T> collection = new ArrayList<>();
        for (Collection<AppObject> objects : map.values())
            collection.addAll(findAll(type, objects));
        return collection;
    }

    public synchronized <T> Collection<T> findAll(Class<? extends T> type, Collection<AppObject> objects) {
        Collection<T> collection = new ArrayList<>();
        for (AppObject object : objects)
            if (type.isInstance(object))
                collection.add((T) object);
        return collection;
    }

}
