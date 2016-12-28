package com.tazadum.glsl.compresion;

import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;

/**
 * Created by Erik on 2016-11-04.
 */
public class Compressor {
    private static byte[] buffer = new byte[65536];

    public static int compress(String content) {
        try {
            final Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
            compresser.reset();
            compresser.setInput(content.getBytes(StandardCharsets.UTF_8));
            compresser.finish();
            final int length = compresser.deflate(buffer);
            compresser.end();

            return length;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
