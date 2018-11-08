package com.egs.patViewer;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.stream.Stream;

public class PatFileFilter extends FileFilter {

    private static final String[] EXTENSIONS = {".pat", ".xls", ".opt", ".opg"};

    @Override
    public boolean accept(File f) {
        return Stream.of(EXTENSIONS).anyMatch(e -> f.getName().toLowerCase().endsWith(e)) || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "*.pat, *.xls, *.opt, *.opg";
    }
}
