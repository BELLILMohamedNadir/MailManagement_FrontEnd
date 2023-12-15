package com.example.gestiondecourrier.ui.pojo;

public class ReceivedMail {


    private Structure structure;
    private Category category;
    private String receivedCategory,mailReference,objectReceived;

    public ReceivedMail() {
    }

    public ReceivedMail(Structure structure, Category category, String receivedCategory, String mailReference, String objectReceived) {
        this.structure = structure;
        this.category = category;
        this.receivedCategory = receivedCategory;
        this.mailReference = mailReference;
        this.objectReceived = objectReceived;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMailReference() {
        return mailReference;
    }

    public void setMailReference(String mailReference) {
        this.mailReference = mailReference;
    }

    public String getObjectReceived() {
        return objectReceived;
    }

    public void setObjectReceived(String objectReceived) {
        this.objectReceived = objectReceived;
    }

    public String getReceivedCategory() {
        return receivedCategory;
    }

    public void setReceivedCategory(String receivedCategory) {
        this.receivedCategory = receivedCategory;
    }
}
