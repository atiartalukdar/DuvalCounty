package model;

import java.io.Serializable;

public class ScheduleModel implements Serializable {
    String name;
    String level;
    String uniqueID;
    String parent;
    String createdAt;

    public ScheduleModel() {
    }

    public ScheduleModel(String name, String level, String uniqueID, String parent, String createdAt) {
        this.name = name;
        this.level = level;
        this.uniqueID = uniqueID;
        this.parent = parent;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getParent() {
        return parent;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ScheduleModel{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", uniqueID='" + uniqueID + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }
}
