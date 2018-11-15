package com.egs.patViewer;

import java.util.List;

public class PatFile {

    private final String name;

    private final List<PatRectangle> list;

    public PatFile(String name, List<PatRectangle> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public List<PatRectangle> getList() {
        return list;
    }
}
