package org.medipi.ui;

import javafx.beans.property.SimpleBooleanProperty;

public class BufferTile extends Tile {
    public BufferTile(SimpleBooleanProperty bpop) {
        super(bpop, 1, 1);
    }

    public BufferTile(SimpleBooleanProperty bpop, int widthUnits, int heightUnits) {
        super(bpop, widthUnits, heightUnits);
    }
}
