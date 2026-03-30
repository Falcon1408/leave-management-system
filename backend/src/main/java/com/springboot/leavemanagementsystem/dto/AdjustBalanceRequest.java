package springboot.leavemanagementsystem.dto;

public class AdjustBalanceRequest {

    private Integer newTotalAllocated;

    public Integer getNewTotalAllocated() {
        return newTotalAllocated;
    }

    public void setNewTotalAllocated(Integer newTotalAllocated) {
        this.newTotalAllocated = newTotalAllocated;
    }
}