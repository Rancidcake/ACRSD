package acrds;

public class Personnel {
    private int personnelId;
    private String name;
    private String personnelRank;
    private int teamId;

    public Personnel() {}

    public Personnel(int  Personnel_ID, String name, String Personnel_Rank, int Team_ID) {
        this.personnelId =  Personnel_ID;
        this.name = name;
        this.personnelRank = Personnel_Rank;
        this.teamId = teamId;
    }

    // Getters and setters omitted for brevity

    @Override
    public String toString() {
        return name + " - " + personnelRank + " (Team " + teamId + ")";
    }
}