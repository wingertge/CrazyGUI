package com.octagon.crazygui;

public interface IMouseListener {
    void componentMouseDown(BaseComponent component, int x, int y, int button);
    void componentMouseUp(BaseComponent component, int x, int y, int button);
    void componentMouseDrag(BaseComponent component, int x, int y, int button, long time);
}
