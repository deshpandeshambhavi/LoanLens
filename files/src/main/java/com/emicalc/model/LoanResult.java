package com.emicalc.model;

import java.util.List;

public class LoanResult {
    // Core
    private double loanAmount;
    private double emi;
    private double totalInterest;
    private double totalPayment;
    private double totalUpfrontCost;
    private double effectiveCostOfLoan;   // total paid including upfront
    private int actualTenureMonths;       // with prepayments, may be less
    private double interestSavedByPrepayment;

    // Monthly breakdown
    private double processingFeeActual;
    private double downPaymentActual;

    // Affordability
    private boolean affordable;
    private double disposableIncome;
    private double emiToIncomeRatio;
    private String affordabilityRating;    // GREEN / YELLOW / RED
    private double maxAffordableLoan;
    private double maxAffordablePrice;

    // Amortization
    private List<AmortizationRow> schedule;

    // Comparison (optional)
    private LoanResult comparisonResult;

    // Yearly summaries for chart
    private List<YearlySummary> yearlySummaries;

    public static class YearlySummary {
        private int year;
        private double principalPaid;
        private double interestPaid;
        private double balance;

        public YearlySummary(int year, double principalPaid, double interestPaid, double balance) {
            this.year = year;
            this.principalPaid = Math.round(principalPaid * 100.0) / 100.0;
            this.interestPaid = Math.round(interestPaid * 100.0) / 100.0;
            this.balance = Math.round(balance * 100.0) / 100.0;
        }

        public int getYear() { return year; }
        public double getPrincipalPaid() { return principalPaid; }
        public double getInterestPaid() { return interestPaid; }
        public double getBalance() { return balance; }
    }

    // Getters and Setters
    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }

    public double getEmi() { return emi; }
    public void setEmi(double emi) { this.emi = emi; }

    public double getTotalInterest() { return totalInterest; }
    public void setTotalInterest(double totalInterest) { this.totalInterest = totalInterest; }

    public double getTotalPayment() { return totalPayment; }
    public void setTotalPayment(double totalPayment) { this.totalPayment = totalPayment; }

    public double getTotalUpfrontCost() { return totalUpfrontCost; }
    public void setTotalUpfrontCost(double totalUpfrontCost) { this.totalUpfrontCost = totalUpfrontCost; }

    public double getEffectiveCostOfLoan() { return effectiveCostOfLoan; }
    public void setEffectiveCostOfLoan(double effectiveCostOfLoan) { this.effectiveCostOfLoan = effectiveCostOfLoan; }

    public int getActualTenureMonths() { return actualTenureMonths; }
    public void setActualTenureMonths(int actualTenureMonths) { this.actualTenureMonths = actualTenureMonths; }

    public double getInterestSavedByPrepayment() { return interestSavedByPrepayment; }
    public void setInterestSavedByPrepayment(double interestSavedByPrepayment) { this.interestSavedByPrepayment = interestSavedByPrepayment; }

    public double getProcessingFeeActual() { return processingFeeActual; }
    public void setProcessingFeeActual(double processingFeeActual) { this.processingFeeActual = processingFeeActual; }

    public double getDownPaymentActual() { return downPaymentActual; }
    public void setDownPaymentActual(double downPaymentActual) { this.downPaymentActual = downPaymentActual; }

    public boolean isAffordable() { return affordable; }
    public void setAffordable(boolean affordable) { this.affordable = affordable; }

    public double getDisposableIncome() { return disposableIncome; }
    public void setDisposableIncome(double disposableIncome) { this.disposableIncome = disposableIncome; }

    public double getEmiToIncomeRatio() { return emiToIncomeRatio; }
    public void setEmiToIncomeRatio(double emiToIncomeRatio) { this.emiToIncomeRatio = emiToIncomeRatio; }

    public String getAffordabilityRating() { return affordabilityRating; }
    public void setAffordabilityRating(String affordabilityRating) { this.affordabilityRating = affordabilityRating; }

    public double getMaxAffordableLoan() { return maxAffordableLoan; }
    public void setMaxAffordableLoan(double maxAffordableLoan) { this.maxAffordableLoan = maxAffordableLoan; }

    public double getMaxAffordablePrice() { return maxAffordablePrice; }
    public void setMaxAffordablePrice(double maxAffordablePrice) { this.maxAffordablePrice = maxAffordablePrice; }

    public List<AmortizationRow> getSchedule() { return schedule; }
    public void setSchedule(List<AmortizationRow> schedule) { this.schedule = schedule; }

    public LoanResult getComparisonResult() { return comparisonResult; }
    public void setComparisonResult(LoanResult comparisonResult) { this.comparisonResult = comparisonResult; }

    public List<YearlySummary> getYearlySummaries() { return yearlySummaries; }
    public void setYearlySummaries(List<YearlySummary> yearlySummaries) { this.yearlySummaries = yearlySummaries; }
}
