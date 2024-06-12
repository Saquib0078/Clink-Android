package com.visticsolution.posterbanao.model;

import java.util.ArrayList;
import java.util.List;

public class InvitationCategoryModel {

    String id, name, image, status, updated_at, created_at;
    List<InvitationModel> invitationcards = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<InvitationModel> getInvitationcards() {
        return invitationcards;
    }

    public void setInvitationcards(List<InvitationModel> invitationcards) {
        this.invitationcards = invitationcards;
    }
}
