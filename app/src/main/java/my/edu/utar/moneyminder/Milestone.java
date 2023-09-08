package my.edu.utar.moneyminder;

public class Milestone {
    private double targetAmount;
    private boolean milestoneReached;
    public Milestone(double targetAmount) {
        this.targetAmount = targetAmount;
        this.milestoneReached = false;
    }




    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
        this.milestoneReached = false;
    }

    public double getTargetAmount() {
        return targetAmount; // Add this method to retrieve the target amount
    }

    public boolean isMilestoneReached(double currentAmount) {
        if (!milestoneReached && currentAmount >= targetAmount) {
            milestoneReached = true;
            return true;
        }
        return false;
    }

    public boolean isMilestoneFullyReached(double currentAmount) {
        return currentAmount >= targetAmount;
    }
}

