package model;

public class ScheduleModel {
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
