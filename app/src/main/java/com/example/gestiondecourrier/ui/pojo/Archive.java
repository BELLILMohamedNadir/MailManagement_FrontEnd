package com.example.gestiondecourrier.ui.pojo;

public class Archive {

    private ArchivePk archivedPk;
    private boolean archive;

    public Archive() {
    }

    public Archive(ArchivePk archivedPk, boolean archive) {
        this.archivedPk = archivedPk;
        this.archive = archive;
    }

    public ArchivePk getArchivedPk() {
        return archivedPk;
    }

    public void setArchivedPk(ArchivePk archivedPk) {
        this.archivedPk = archivedPk;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }
}
