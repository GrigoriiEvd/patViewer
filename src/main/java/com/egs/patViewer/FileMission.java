package com.egs.patViewer;

import org.apache.poi.ss.usermodel.CellType;

/**
 * Created by EGS on 05.10.2015.
 */
public class FileMission {
    private String name;
    private int nX;
    private int nY;
    private int sizeModulX;
    private int sizeModulY;
    private int Proxod;
    private int E;
    private int privWindowX;
    private int privWindowY;
    private int sizeWindowX;
    private int sizeWindowY;
    private int PFOKomplexX;
    private int PFOKomplexY;
    private int resultOffsetX;
    private int resultOffsetY;
    private int offsetNaFSX;
    private int offsetNaFSY;

    public void setName(String name) {
        this.name = name;
    }

    public void setnX(int nX) {
        this.nX = nX;
    }

    public void setnY(int nY) {
        this.nY = nY;
    }

    public void setProxod(int proxod) {
        Proxod = proxod;
    }

    public void setE(int e) {
        E = e;
    }

    public void setPrivWindowX(int privWindowX) {
        this.privWindowX = privWindowX;
    }

    public void setPrivWindowY(int privWindowY) {
        this.privWindowY = privWindowY;
    }

    public void setSizeWindowX(int sizeWindowX) {
        this.sizeWindowX = sizeWindowX;
    }

    public void setSizeWindowY(int sizeWindowY) {
        this.sizeWindowY = sizeWindowY;
    }

    public void setPFOKomplexX(int PFOKomplexX) {
        this.PFOKomplexX = PFOKomplexX;
    }

    public void setPFOKomplexY(int PFOKomplexY) {
        this.PFOKomplexY = PFOKomplexY;
    }

    public void setResultOffsetX(int resultOffsetX) {
        this.resultOffsetX = resultOffsetX;
    }

    public void setResultOffsetY(int resultOffsetY) {
        this.resultOffsetY = resultOffsetY;
    }

    public void setOffsetNaFSX(int offsetNaFSX) {
        this.offsetNaFSX = offsetNaFSX;
    }

    public void setOffsetNaFSY(int offsetNaFSY) {
        this.offsetNaFSY = offsetNaFSY;
    }

    public String getName() {

        return name;
    }

    public int getnX() {
        return nX;
    }

    public int getnY() {
        return nY;
    }

    public int getProxod() {
        return Proxod;
    }

    public int getE() {
        return E;
    }

    public int getPrivWindowX() {
        return privWindowX;
    }

    public int getPrivWindowY() {
        return privWindowY;
    }

    public int getSizeWindowX() {
        return sizeWindowX;
    }

    public int getSizeWindowY() {
        return sizeWindowY;
    }

    public int getPFOKomplexX() {
        return PFOKomplexX;
    }

    public int getPFOKomplexY() {
        return PFOKomplexY;
    }

    public int getResultOffsetX() {
        return resultOffsetX;
    }

    public int getResultOffsetY() {
        return resultOffsetY;
    }

    public int getOffsetNaFSX() {
        return offsetNaFSX;
    }

    public int getOffsetNaFSY() {
        return offsetNaFSY;
    }

    public void setSizeModulX(int sizeModulX) {
        this.sizeModulX = sizeModulX;
    }

    public void setSizeModulY(int sizeModulY) {
        this.sizeModulY = sizeModulY;
    }

    public int getSizeModulY() {
        return sizeModulY;
    }

    public int getSizeModulX() {
        return sizeModulX;
    }
}
