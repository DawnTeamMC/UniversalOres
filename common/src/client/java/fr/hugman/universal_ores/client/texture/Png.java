package fr.hugman.universal_ores.client.texture;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Writes PNG files in memory, for textures served by a pack rather than drawn in an atlas.
 */
public final class Png {
    private static final byte[] SIGNATURE = {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};
    private static final int BIT_DEPTH = 8;
    private static final int COLOR_TYPE_RGBA = 6;

    private Png() {
    }

    /**
     * @param pixels the pixels (ARGB), row by row
     * @return a PNG file of 8-bit RGBA pixels
     */
    public static byte[] encode(int width, int height, int[] pixels) {
        if (pixels.length != width * height) {
            throw new IllegalArgumentException("A " + width + "x" + height + " image can't have " + pixels.length + " pixels");
        }
        try {
            ByteArrayOutputStream header = new ByteArrayOutputStream();
            DataOutputStream headerData = new DataOutputStream(header);
            headerData.writeInt(width);
            headerData.writeInt(height);
            headerData.writeByte(BIT_DEPTH);
            headerData.writeByte(COLOR_TYPE_RGBA);
            headerData.writeByte(0); // Deflate
            headerData.writeByte(0); // Adaptive filtering
            headerData.writeByte(0); // Not interlaced

            ByteArrayOutputStream compressed = new ByteArrayOutputStream();
            try (DeflaterOutputStream deflater = new DeflaterOutputStream(compressed, new Deflater(Deflater.BEST_COMPRESSION))) {
                byte[] row = new byte[1 + width * 4];
                for (int y = 0; y < height; y++) {
                    // Each row starts with its filter: none
                    row[0] = 0;
                    for (int x = 0; x < width; x++) {
                        int pixel = pixels[y * width + x];
                        row[1 + x * 4] = (byte) (pixel >>> 16);
                        row[2 + x * 4] = (byte) (pixel >>> 8);
                        row[3 + x * 4] = (byte) pixel;
                        row[4 + x * 4] = (byte) (pixel >>> 24);
                    }
                    deflater.write(row);
                }
            }

            ByteArrayOutputStream png = new ByteArrayOutputStream();
            png.write(SIGNATURE);
            DataOutputStream data = new DataOutputStream(png);
            writeChunk(data, "IHDR", header.toByteArray());
            writeChunk(data, "IDAT", compressed.toByteArray());
            writeChunk(data, "IEND", new byte[0]);
            return png.toByteArray();
        } catch (IOException e) {
            // Only written in memory
            throw new UncheckedIOException(e);
        }
    }

    private static void writeChunk(DataOutputStream output, String type, byte[] data) throws IOException {
        byte[] typeBytes = type.getBytes(StandardCharsets.US_ASCII);
        CRC32 crc = new CRC32();
        crc.update(typeBytes);
        crc.update(data);
        output.writeInt(data.length);
        output.write(typeBytes);
        output.write(data);
        output.writeInt((int) crc.getValue());
    }
}
