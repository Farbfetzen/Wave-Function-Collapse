package farbfetzen.wavefunctioncollapse;

import static farbfetzen.wavefunctioncollapse.WaveFunctionCollapse.TILE_SIZE;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lombok.Getter;

class Tile {

    @Getter
    private final int gridX;

    @Getter
    private final int gridY;

    @Getter
    private final float displayX;

    @Getter
    private final float displayY;

    @Getter
    private boolean collapsed = false;

    @Getter
    private TileImage tileImage;

    @Getter
    private final Set<TileImage> candidates;

    Tile(final int gridX, final int gridY, final Set<TileImage> candidates) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.displayX = (float) gridX * TILE_SIZE;
        this.displayY = (float) gridY * TILE_SIZE;
        this.candidates = new HashSet<>(candidates);
    }

    void collapse(final Random random, final Neighbors neighbors) {
        this.collapsed = true;
        final TileImage candidate;
        if (candidates.size() == 1) {
            candidate = candidates.iterator().next();
        } else {
            candidate = candidates.toArray(TileImage[]::new)[random.nextInt(candidates.size())];
        }
        tileImage = candidate;
        pruneCandidates(neighbors);
    }

    private void pruneCandidates(final Neighbors neighbors) {
        if (neighbors.top() != null) {
            neighbors.top().candidates.retainAll(tileImage.getCompatibleTop());
        }
        if (neighbors.right() != null) {
            neighbors.right().candidates.retainAll(tileImage.getCompatibleRight());
        }
        if (neighbors.bottom() != null) {
            neighbors.bottom().candidates.retainAll(tileImage.getCompatibleBottom());
        }
        if (neighbors.left() != null) {
            neighbors.left().candidates.retainAll(tileImage.getCompatibleLeft());
        }
    }
}
