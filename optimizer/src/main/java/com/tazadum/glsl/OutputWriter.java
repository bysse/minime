package com.tazadum.glsl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Erik on 2016-10-24.
 */
public interface OutputWriter {
    String name();

    OutputStream outputStream() throws FileNotFoundException;


    class StdOutOutputWriter implements OutputWriter {
        @Override
        public String name() {
            return "sysout";
        }

        @Override
        public OutputStream outputStream() {
            return System.out;
        }
    }

    class FileOutputWriter implements OutputWriter {
        private final File file;
        private final FileOutputStream outputStream;

        public FileOutputWriter(String file) throws FileNotFoundException {
            this.file = new File(file);
            this.outputStream = new FileOutputStream(file);
        }

        @Override
        public String name() {
            return file.getName();
        }

        @Override
        public OutputStream outputStream() {
            return outputStream;
        }
    }
}
