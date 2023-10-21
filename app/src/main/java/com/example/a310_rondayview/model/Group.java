package com.example.a310_rondayview.model;

import com.google.firebase.firestore.DocumentId;

import java.util.List;
import java.util.Objects;

public class Group {
    @DocumentId
    private String groupId;
    private String groupName;
    private List<String> userIdList;
    private List<String> eventIdList;

    public Group(){
    }

    public Group(String groupName, List<String> userIdList, List<String> eventIdList){
        this.groupName = groupName;
        this.userIdList = userIdList;
        this.eventIdList = eventIdList;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupName(String groupName) {this.groupName = groupName;}

    public String getGroupName(){return groupName;}

    public void setEventIdList(List<String> eventIdList) {
        this.eventIdList = eventIdList;
    }

    public List<String> getEventIdList() {
        return eventIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Group otherGroup = (Group) obj;
        return (groupId.equals(otherGroup.groupId) || (groupName.equals(otherGroup.groupName)));
    }

//    @Override
//    public int hashCode(){return Objects.hash(groupId, groupName);}

}
