package farbfetzen.wavefunctioncollapse;

import static farbfetzen.wavefunctioncollapse.GridHelper.positionToIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import processing.core.PApplet;

public class WaveFunctionCollapse extends PApplet {

    static final int TILE_SIZE = 20;
    private static final int MILLIS_PER_STEP = 10;
    final Random random = new Random();
    private Tile[] tiles;
    private int gridWidth;
    private int gridHeight;
    private Set<TileImage> tileImages;
    private boolean done;
    private int previousMillis = 0;
    private boolean animating = false;

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
        tileImages = TileImage.createTileImages(this);
        gridWidth = width / TILE_SIZE;
        gridHeight = height / TILE_SIZE;
        reset();
    }

    @Override
    public void draw() {
        if (animating && !done) {
            final int currentMillis = millis();
            if (currentMillis - previousMillis >= MILLIS_PER_STEP) {
                previousMillis += MILLIS_PER_STEP;
                step();
            }
        }

        background(color(0, 0, 0));
        for (final Tile tile : tiles) {
            if (tile.isCollapsed()) {
                image(tile.getTileImage().getImage(), tile.getDisplayX(), tile.getDisplayY());
            }
        }
    }

    @Override
    public void keyPressed() {
        if (key == 'r') {
            reset();
        } else if (key == ' ') {
            animating = !animating;
            if (animating) {
                previousMillis = millis() - MILLIS_PER_STEP;
            }
        } else if (key == 's') {
            step();
        } else if (key == 'f') {
            while (!done) {
                step();
            }
        }
    }

    private void reset() {
        done = false;
        tiles = new Tile[gridWidth * gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                final int i = positionToIndex(x, y, gridWidth);
                tiles[i] = new Tile(x, y, tileImages);
            }
        }
    }

    private void step() {
        final Tile nextTile = findNext();
        if (nextTile == null) {
            println("The algorithm either finished or ran out of candidates for at least one position.");
            done = true;
            return;
        }
        final Neighbors neighbors = getNonCollapsedNeighbors(nextTile.getGridX(), nextTile.getGridY());
        nextTile.collapse(random, neighbors);
    }

    /**
     * Look for the tiles with the lowest number of candidates and return a random one from them.
     */
    // TODO: It should be enough to only check the tiles at the frontier.
    Tile findNext() {
        int minNumberOfCandidates = Integer.MAX_VALUE;
        final List<Tile> nextTiles = new ArrayList<>();
        for (final Tile tile : tiles) {
            if (tile.isCollapsed()) {
                continue;
            }
            final int numberOfCandidates = tile.getCandidates().size();
            if (numberOfCandidates == 0) {
                // A non-collapsed tile with no candidates means either the algorithm has finished
                // or it has reached a dead end.
                return null;
            }
            if (numberOfCandidates == minNumberOfCandidates) {
                nextTiles.add(tile);
            } else if (numberOfCandidates < minNumberOfCandidates) {
                minNumberOfCandidates = numberOfCandidates;
                nextTiles.clear();
                nextTiles.add(tile);
            }
        }
        if (nextTiles.isEmpty()) {
            return null;
        }
        if (nextTiles.size() == 1) {
            return nextTiles.get(0);
        }
        return nextTiles.get(random.nextInt(nextTiles.size()));
    }

    Neighbors getNonCollapsedNeighbors(final int x, final int y) {
        return new Neighbors(
                y > 0 ? tiles[positionToIndex(x, y - 1, gridWidth)] : null,
                x < gridWidth - 1 ? tiles[positionToIndex(x + 1, y, gridWidth)] : null,
                y < gridHeight - 1 ? tiles[positionToIndex(x, y + 1, gridWidth)] : null,
                x > 0 ? tiles[positionToIndex(x - 1, y, gridWidth)] : null
        );
    }
}
