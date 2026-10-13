package fr.hugman.universal_ores.client.texture;

import java.util.ArrayList;
import java.util.List;

/**
 * The frames of an animated texture. Like Minecraft reads them, frames are laid out from left to right, then from top
 * to bottom.
 */
public final class TextureFrames {
    private TextureFrames() {
    }

    /**
     * @return the pixels (ARGB) of each frame
     */
    public static List<int[]> split(int[] pixels, int width, int height, int frameWidth, int frameHeight) {
        if (frameWidth <= 0 || frameHeight <= 0 || width % frameWidth != 0 || height % frameHeight != 0) {
            throw new IllegalArgumentException("A " + width + "x" + height + " texture can't hold " + frameWidth + "x" + frameHeight + " frames");
        }
        int columns = width / frameWidth;
        int count = columns * (height / frameHeight);
        List<int[]> frames = new ArrayList<>(count);
        for (int frame = 0; frame < count; frame++) {
            int left = (frame % columns) * frameWidth;
            int top = (frame / columns) * frameHeight;
            int[] framePixels = new int[frameWidth * frameHeight];
            for (int y = 0; y < frameHeight; y++) {
                System.arraycopy(pixels, (top + y) * width + left, framePixels, y * frameWidth, frameWidth);
            }
            frames.add(framePixels);
        }
        return frames;
    }

    /**
     * Stacks frames of the same size from top to bottom.
     */
    public static int[] stack(List<int[]> frames, int frameWidth, int frameHeight) {
        int frameSize = frameWidth * frameHeight;
        int[] pixels = new int[frameSize * frames.size()];
        for (int frame = 0; frame < frames.size(); frame++) {
            if (frames.get(frame).length != frameSize) {
                throw new IllegalArgumentException("Frame " + frame + " has " + frames.get(frame).length + " pixels instead of " + frameSize);
            }
            System.arraycopy(frames.get(frame), 0, pixels, frame * frameSize, frameSize);
        }
        return pixels;
    }
}
