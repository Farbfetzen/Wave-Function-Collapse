package farbfetzen.wavefunctioncollapse;

import java.util.stream.IntStream;

import lombok.experimental.UtilityClass;

@UtilityClass
class GridHelper {

    static int positionToIndex(final int x, final int y, final int width) {
        return x + width * y;
    }

    static int[] getTopIndices(final int width) {
        return IntStream.range(0, width).toArray();
    }

    static int[] getRightIndices(final int width, final int height) {
        return IntStream.rangeClosed(1, height).map(i -> i * width - 1).toArray();
    }

    static int[] getLeftIndices(final int width, final int height) {
        return IntStream.range(0, height).map(i -> i * width).toArray();
    }

    static int[] getBottomIndices(final int width, final int length) {
        return IntStream.range(length - width, length).toArray();
    }

}
