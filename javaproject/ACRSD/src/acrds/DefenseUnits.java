package acrds;

public class DefenseUnits{
    private int unitId;
    private String unitName;
    private String commanderInCharge;
    private int orgId;

    public DefenseUnits(int unitId, String unitName, String commanderInCharge, int orgId) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.commanderInCharge = commanderInCharge;
        this.orgId = orgId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCommanderInCharge() {
        return commanderInCharge;
    }

    public void setCommanderInCharge(String commanderInCharge) {
        this.commanderInCharge = commanderInCharge;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return "DefenseUnit{" +
                "unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", commanderInCharge='" + commanderInCharge + '\'' +
                ", orgId=" + orgId +
                '}';
    }
}
