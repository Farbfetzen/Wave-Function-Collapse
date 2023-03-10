package farbfetzen.wavefunctioncollapse;

import static farbfetzen.wavefunctioncollapse.WaveFunctionCollapse.TILE_SIZE;

import java.util.Arrays;

import lombok.Getter;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

class Tile {

    @Getter
    private final PImage image;

    Tile(final PApplet pApplet, final int[] pixels) {
        final PGraphics pg = pApplet.createGraphics(3, 3);
        pg.beginDraw();
        pg.loadPixels();
        pg.pixels = pixels;
        pg.updatePixels();
        pg.endDraw();
        final PGraphics img = pApplet.createGraphics(TILE_SIZE, TILE_SIZE);
        img.beginDraw();
        img.image(pg, 0, 0, TILE_SIZE, TILE_SIZE);
        img.endDraw();
        image = img;
    }

    static Tile[] createTiles(final PApplet pApplet) {
        final Tile[] tiles = new Tile[5];
        final int colorA = pApplet.color(30, 120, 200);
        final int colorB = pApplet.color(200, 110, 30);

        // empty
        final int[] empty = new int[9];
        Arrays.fill(empty, colorA);
        tiles[0] = new Tile(pApplet, empty);

        // top
        final int[] top = {
                colorA, colorB, colorA,
                colorB, colorB, colorB,
                colorA, colorA, colorA
        };
        tiles[1] = new Tile(pApplet, top);

        // right
        final int[] right = {
                colorA, colorB, colorA,
                colorA, colorB, colorB,
                colorA, colorB, colorA
        };
        tiles[2] = new Tile(pApplet, right);

        // bottom
        final int[] bottom = {
                colorA, colorA, colorA,
                colorB, colorB, colorB,
                colorA, colorB, colorA
        };
        tiles[3] = new Tile(pApplet, bottom);

        // left
        final int[] left = {
                colorA, colorB, colorA,
                colorB, colorB, colorA,
                colorA, colorB, colorA
        };
        tiles[4] = new Tile(pApplet, left);

        return tiles;
    }
}
