package com.foodbridge.dashboard.dto.response;

public class DashboardResponse {

    private long availableDonations;
    private long myDonations;
    private long claimedDonations;
    private long completedDonations;

    public DashboardResponse() {
        super();
    }

    public DashboardResponse(
            long availableDonations,
            long myDonations,
            long claimedDonations,
            long completedDonations) {

        this.availableDonations = availableDonations;
        this.myDonations = myDonations;
        this.claimedDonations = claimedDonations;
        this.completedDonations = completedDonations;
    }

    public long getAvailableDonations() {
        return availableDonations;
    }

    public void setAvailableDonations(long availableDonations) {
        this.availableDonations = availableDonations;
    }

    public long getMyDonations() {
        return myDonations;
    }

    public void setMyDonations(long myDonations) {
        this.myDonations = myDonations;
    }

    public long getClaimedDonations() {
        return claimedDonations;
    }

    public void setClaimedDonations(long claimedDonations) {
        this.claimedDonations = claimedDonations;
    }

    public long getCompletedDonations() {
        return completedDonations;
    }

    public void setCompletedDonations(long completedDonations) {
        this.completedDonations = completedDonations;
    }
}