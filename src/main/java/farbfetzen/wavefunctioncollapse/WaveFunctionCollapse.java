package farbfetzen.wavefunctioncollapse;

import processing.core.PApplet;

public class WaveFunctionCollapse extends PApplet {

    static final int TILE_SIZE = 100;
    Tile[] tiles;

    public static void main(final String[] args) {
        PApplet.main(WaveFunctionCollapse.class, args);
    }

    @Override
    public void settings() {
        size(800, 600);
        noSmooth();
    }

    @Override
    public void setup() {
        tiles = Tile.createTiles(this);
    }

    @Override
    public void draw() {
        background(color(0, 0, 0));
        for (int i = 0; i < tiles.length; i++) {
            image(tiles[i].getImage(), (float) i * TILE_SIZE, 0);
        }
    }
}
