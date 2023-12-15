package com.example.gestiondecourrier.ui.pojo;

import java.util.Date;
import java.util.List;

public class MailResponse {

    private long id;
    private List<String> fileName;
    private List<String> paths;
    private List<byte[]> bytes;
    private String forStructure;
    private String sender;
    private String category;
    private String receivedCategory;
    private Date entryDate;
    private Date departureDate;
    private Date entryDateReceived;
    private Date mailDate;
    private String entryDateToShow;
    private String entryDateReceivedToShow;
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
    private boolean initializedFavorite;
    private boolean initializedArchive;
    private boolean initializedTrait;

    private int placeInTheViewModel;


    public MailResponse() {
    }

    public MailResponse(long id, List<String> fileName, List<String> paths, List<byte[]> bytes, String forStructure, String sender, String category, String receivedCategory, Date entryDate, Date departureDate, Date entryDateReceived, Date mailDate, String entryDateToShow, String getEntryDateReceivedToShow, String internalReference, String mailReference, String objectReceived, String object, String recipient, String priority, String type, String responseOf, boolean favorite, boolean archive, boolean response, boolean classed, boolean trait, boolean initializedFavorite, boolean initializedArchive, boolean initializedTrait) {
        this.id = id;
        this.fileName = fileName;
        this.paths = paths;
        this.bytes = bytes;
        this.forStructure = forStructure;
        this.sender = sender;
        this.category = category;
        this.receivedCategory = receivedCategory;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.entryDateReceived = entryDateReceived;
        this.mailDate = mailDate;
        this.entryDateToShow = entryDateToShow;
        this.entryDateReceivedToShow = getEntryDateReceivedToShow;
        this.internalReference = internalReference;
        this.mailReference = mailReference;
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
        this.initializedFavorite = initializedFavorite;
        this.initializedArchive = initializedArchive;
        this.initializedTrait = initializedTrait;
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

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public List<byte[]> getBytes() {
        return bytes;
    }

    public void setBytes(List<byte[]> bytes) {
        this.bytes = bytes;
    }

    public String getForStructure() {
        return forStructure;
    }

    public void setForStructure(String forStructure) {
        this.forStructure = forStructure;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getReceivedCategory() {
        return receivedCategory;
    }

    public void setReceivedCategory(String receivedCategory) {
        this.receivedCategory = receivedCategory;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isInitializedFavorite() {
        return initializedFavorite;
    }

    public void setInitializedFavorite(boolean initializedFavorite) {
        this.initializedFavorite = initializedFavorite;
    }

    public boolean isInitializedArchive() {
        return initializedArchive;
    }

    public void setInitializedArchive(boolean initializedArchive) {
        this.initializedArchive = initializedArchive;
    }

    public boolean isInitializedTrait() {
        return initializedTrait;
    }

    public void setInitializedTrait(boolean initializedTrait) {
        this.initializedTrait = initializedTrait;
    }

    public String getEntryDateToShow() {
        return entryDateToShow;
    }

    public void setEntryDateToShow(String entryDateToShow) {
        this.entryDateToShow = entryDateToShow;
    }

    public String getEntryDateReceivedToShow() {
        return entryDateReceivedToShow;
    }

    public void setEntryDateReceivedToShow(String entryDateReceivedToShow) {
        this.entryDateReceivedToShow = entryDateReceivedToShow;
    }

    public int getPlaceInTheViewModel() {
        return placeInTheViewModel;
    }

    public void setPlaceInTheViewModel(int placeInTheViewModel) {
        this.placeInTheViewModel = placeInTheViewModel;
    }
}