package farbfetzen.wavefunctioncollapse;

import static farbfetzen.wavefunctioncollapse.GridHelper.getBottomIndices;
import static farbfetzen.wavefunctioncollapse.GridHelper.getLeftIndices;
import static farbfetzen.wavefunctioncollapse.GridHelper.getRightIndices;
import static farbfetzen.wavefunctioncollapse.GridHelper.getTopIndices;
import static farbfetzen.wavefunctioncollapse.WaveFunctionCollapse.TILE_SIZE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

class TileImage {

    private static final int GRID_SIZE = 3;

    private final int id;

    @Getter
    private final PImage image;

    private final int[] topEdge = new int[GRID_SIZE];
    private final int[] rightEdge = new int[GRID_SIZE];
    private final int[] bottomEdge = new int[GRID_SIZE];
    private final int[] leftEdge = new int[GRID_SIZE];

    @Getter
    private final Set<TileImage> compatibleTop = new HashSet<>();

    @Getter
    private final Set<TileImage> compatibleRight = new HashSet<>();

    @Getter
    private final Set<TileImage> compatibleBottom = new HashSet<>();

    @Getter
    private final Set<TileImage> compatibleLeft = new HashSet<>();

    TileImage(final PApplet pApplet, final int id, final int[] pixels) {
        this.id = id;
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

        final int[] topIndices = getTopIndices(GRID_SIZE);
        for (int i = 0; i < topIndices.length; i++) {
            topEdge[i] = pixels[topIndices[i]];
        }
        final int[] rightIndices = getRightIndices(GRID_SIZE, GRID_SIZE);
        for (int i = 0; i < rightIndices.length; i++) {
            rightEdge[i] = pixels[rightIndices[i]];
        }
        final int[] bottomIndices = getBottomIndices(GRID_SIZE, pixels.length);
        for (int i = 0; i < bottomIndices.length; i++) {
            bottomEdge[i] = pixels[bottomIndices[i]];
        }
        final int[] leftIndices = getLeftIndices(GRID_SIZE, GRID_SIZE);
        for (int i = 0; i < leftIndices.length; i++) {
            leftEdge[i] = pixels[leftIndices[i]];
        }
    }

    static Set<TileImage> createTileImages(final PApplet pApplet) {
        final Set<TileImage> tileImages = new HashSet<>();
        final int colorA = pApplet.color(30, 120, 200);
        final int colorB = pApplet.color(200, 110, 30);
        int id = 0;

        // empty
        final int[] empty = new int[GRID_SIZE * GRID_SIZE];
        Arrays.fill(empty, colorA);
        tileImages.add(new TileImage(pApplet, ++id, empty));

        // top
        final int[] top = {
                colorA, colorB, colorA,
                colorB, colorB, colorB,
                colorA, colorA, colorA
        };
        tileImages.add(new TileImage(pApplet, ++id, top));

        // right
        final int[] right = {
                colorA, colorB, colorA,
                colorA, colorB, colorB,
                colorA, colorB, colorA
        };
        tileImages.add(new TileImage(pApplet, ++id, right));

        // bottom
        final int[] bottom = {
                colorA, colorA, colorA,
                colorB, colorB, colorB,
                colorA, colorB, colorA
        };
        tileImages.add(new TileImage(pApplet, ++id, bottom));

        // left
        final int[] left = {
                colorA, colorB, colorA,
                colorB, colorB, colorA,
                colorA, colorB, colorA
        };
        tileImages.add(new TileImage(pApplet, ++id, left));

        setCompatible(tileImages.toArray(TileImage[]::new));
        return tileImages;
    }

    private static void setCompatible(final TileImage[] tileImages) {
        for (int i = 0; i < tileImages.length; i++) {
            for (int j = i; j < tileImages.length; j++) {
                final TileImage a = tileImages[i];
                final TileImage b = tileImages[j];
                if (Arrays.equals(a.topEdge, b.bottomEdge)) {
                    a.compatibleTop.add(b);
                    b.compatibleBottom.add(a);
                }
                if (Arrays.equals(a.rightEdge, b.leftEdge)) {
                    a.compatibleRight.add(b);
                    b.compatibleLeft.add(a);
                }
                if (Arrays.equals(a.bottomEdge, b.topEdge)) {
                    a.compatibleBottom.add(b);
                    b.compatibleTop.add(a);
                }
                if (Arrays.equals(a.leftEdge, b.rightEdge)) {
                    a.compatibleLeft.add(b);
                    b.compatibleRight.add(a);
                }
            }
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return id == ((TileImage) other).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
