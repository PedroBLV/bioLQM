package org.colomoto.biolqm.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Provide OutputStream objects to export filters.
 *
 * @author Aurelien Naldi
 */
public class OutputStreamProvider {

    private final File parent;
    private final String filename;

    public OutputStreamProvider(String filename) {
        this(new File(filename));
    }
    public OutputStreamProvider(File file) {
        this(file.getParentFile(), file.getName());
    }
    public OutputStreamProvider(File parent, String filename) {
        this.parent = parent;
        this.filename = filename;
    }

    /**
     * Get a single stream. When saving to a file it will have the selected filename.
     *
     * @return the output stream
     * @throws IOException if creating the stream failed
     */
    public OutputStream getOutputStream() throws IOException {
        return getOutputStream("$f");
    }

    /**
     * Get a custom stream, specifying a pattern.
     * The pattern enables to change the file extension, write multiple files to a sub-folder...
     * Available patterns:
     * <ul>
     * <li>$f: the selected filename</li>
     * </ul>
     * @param pattern a naming pattern for the new stream
     * @return an output stream associated to the desired pattern
     * @throws IOException if creating the stream failed
     */
    public OutputStream getOutputStream(String pattern) throws IOException  {
        File f = parent;
        String[] path = pattern.split("/");
        for (String s: path) {
            s = s.replace("$f", filename);
            f = new File(f, s);
        }

        return new FileOutputStream(f);
    }
}
