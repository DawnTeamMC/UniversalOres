package fr.hugman.universal_ores.client.texture;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextureFramesTest {
    @Test
    void framesAreReadLeftToRightThenTopToBottom() {
        // A 4x4 texture holding four 2x2 frames, each filled with its index
        int[] pixels = {
                0, 0, 1, 1,
                0, 0, 1, 1,
                2, 2, 3, 3,
                2, 2, 3, 3
        };
        List<int[]> frames = TextureFrames.split(pixels, 4, 4, 2, 2);
        assertEquals(4, frames.size());
        for (int frame = 0; frame < 4; frame++) {
            assertArrayEquals(new int[]{frame, frame, frame, frame}, frames.get(frame));
        }
    }

    @Test
    void stackedFramesSplitBack() {
        List<int[]> frames = List.of(new int[]{1, 2, 3, 4}, new int[]{5, 6, 7, 8}, new int[]{9, 10, 11, 12});
        int[] stacked = TextureFrames.stack(frames, 2, 2);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, stacked);
        List<int[]> split = TextureFrames.split(stacked, 2, 6, 2, 2);
        for (int frame = 0; frame < frames.size(); frame++) {
            assertArrayEquals(frames.get(frame), split.get(frame));
        }
    }

    @Test
    void framesMustFitTheTexture() {
        assertThrows(IllegalArgumentException.class, () -> TextureFrames.split(new int[16 * 20], 16, 20, 16, 16));
    }
}
