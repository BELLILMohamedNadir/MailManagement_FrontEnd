package com.example.gestiondecourrier.ui.pojo;

import java.util.Date;
import java.util.List;

public class Mail {

    private long id;
    private List<String> fileName;
    private List<byte[]> bytes;
    private Structure structure;
    private Category category;
    private Date entryDate;
    private Date departureDate;
    private Date entryDateReceived;
    private Date mailDate;
    private String internalReference;
    private String mailReference;
    private String objectReceived;
    private String object;
    private String recipient;
    private String priority;
    private String type;
    private String responseOf;
    private boolean favorite;
    private boolean archive;
    private boolean response;
    private boolean classed;
    private boolean trait;

    public Mail() {
    }

    public Mail(long id) {
        this.id = id;
    }

    public Mail(boolean response, boolean classed, boolean trait) {
        this.response = response;
        this.classed = classed;
        this.trait = trait;
    }

    public Mail(long id, List<String> fileName, List<byte[]> bytes, Structure structure, Category category_id, Date entryDate, Date departureDate, Date entryDateReceived, Date mailDate, String internalReference, String mailReference, String objectReceived, String object, String recipient, String priority, String type, String responseOf, boolean favorite, boolean archive, boolean response, boolean classed, boolean trait) {
        this.id = id;
        this.fileName = fileName;
        this.bytes = bytes;
        this.structure = structure;
        this.category = category_id;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.mailDate = mailDate;
        this.internalReference = internalReference;
        this.mailReference = mailReference;
        this.entryDateReceived = entryDateReceived;
        this.objectReceived = objectReceived;
        this.object = object;
        this.recipient = recipient;
        this.priority = priority;
        this.type = type;
        this.responseOf = responseOf;
        this.favorite = favorite;
        this.archive = archive;
        this.response = response;
        this.classed = classed;
        this.trait = trait;
    }

    public Mail(long id, List<String> fileName, List<byte[]> bytes, Structure structure, Category category_id, Date entryDate, Date departureDate, Date mailDate, String internalReference, String object, String recipient, String priority, String type, String responseOf, boolean favorite, boolean archive, boolean response, boolean classed, boolean trait) {
        this.id = id;
        this.fileName = fileName;
        this.bytes = bytes;
        this.structure = structure;
        this.category = category_id;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.mailDate = mailDate;
        this.internalReference = internalReference;
        this.object = object;
        this.recipient = recipient;
        this.priority = priority;
        this.type = type;
        this.responseOf = responseOf;
        this.favorite = favorite;
        this.archive = archive;
        this.response = response;
        this.classed = classed;
        this.trait = trait;
    }

    public Mail(Structure structure, Category category_id, String mailReference, String objectReceived) {
        this.structure = structure;
        this.category = category_id;
        this.mailReference = mailReference;
        this.objectReceived = objectReceived;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public List<byte[]> getBytes() {
        return bytes;
    }

    public void setBytes(List<byte[]> bytes) {
        this.bytes = bytes;
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

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getEntryDateReceived() {
        return entryDateReceived;
    }

    public void setEntryDateReceived(Date entryDateReceived) {
        this.entryDateReceived = entryDateReceived;
    }

    public Date getMailDate() {
        return mailDate;
    }

    public void setMailDate(Date mailDate) {
        this.mailDate = mailDate;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponseOf() {
        return responseOf;
    }

    public void setResponseOf(String responseOf) {
        this.responseOf = responseOf;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public boolean isClassed() {
        return classed;
    }

    public void setClassed(boolean classed) {
        this.classed = classed;
    }

    public boolean isTrait() {
        return trait;
    }

    public void setTrait(boolean trait) {
        this.trait = trait;
    }
}
