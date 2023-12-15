package com.example.gestiondecourrier.ui.pojo;

import java.util.List;

public class MailDetails {

    private User user;
    private Structure structure;
    private List<Category> category;
    private List<String> forStructure;
    private List<String> internalReference;
    private String entryDate,departureDate,mailDate,object
            ,recipient,priority,type,responseOf;
    private boolean favorite,archive,response,classed,trait;


    public MailDetails() {
    }

    public MailDetails(User user, Structure structure, List<Category> category, List<String> forStructure, List<String> internalReference, String entryDate, String departureDate, String mailDate, String object, String recipient, String priority, String type, String responseOf, boolean favorite, boolean archive, boolean response, boolean classed, boolean trait) {
        this.user = user;
        this.structure = structure;
        this.category = category;
        this.forStructure = forStructure;
        this.internalReference = internalReference;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.mailDate = mailDate;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<String> getForStructure() {
        return forStructure;
    }

    public void setForStructure(List<String> forStructure) {
        this.forStructure = forStructure;
    }

    public List<String> getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(List<String> internalReference) {
        this.internalReference = internalReference;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getMailDate() {
        return mailDate;
    }

    public void setMailDate(String mailDate) {
        this.mailDate = mailDate;
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
