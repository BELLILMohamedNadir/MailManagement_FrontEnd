package com.example.gestiondecourrier.ui.pojo;

import android.graphics.Bitmap;

public class PdfBitmap {

    private Folder folder;
    private Bitmap bitmap;

    public PdfBitmap(Folder folder, Bitmap bitmap) {
        this.folder = folder;
        this.bitmap = bitmap;
    }

    public Folder getPdf() {
        return folder;
    }

    public void setPdf(Folder folder) {
        this.folder = folder;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
