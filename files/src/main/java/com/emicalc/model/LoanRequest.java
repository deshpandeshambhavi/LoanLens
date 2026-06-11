package com.emicalc.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LoanRequest {

    @NotNull
    @Positive
    private Double propertyPrice;

    @NotNull
    @Min(0)
    private Double downPayment;         // absolute amount

    @NotNull
    @Min(0)
    private Double tradeInValue;        // trade-in / asset offset

    @NotNull
    @Positive
    private Double annualInterestRate;  // percent, e.g. 8.5

    @NotNull
    @Positive
    private Integer tenureMonths;       // total loan term in months

    @Min(0)
    private Double processingFee;       // upfront flat fee

    @Min(0)
    private Double processingFeePercent; // upfront % of loan

    @Min(0)
    private Double insuranceCost;       // upfront

    @Min(0)
    private Double otherUpfrontCosts;

    @Min(0)
    private Double monthlyPrepayment;   // extra monthly principal payment

    @Min(0)
    private Double annualLumpSum;       // annual lump-sum prepayment

    // Affordability
    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double maxEmiPercent;       // % of income user wants to keep EMI under

    // Comparison tenure (for side-by-side)
    private Integer compareTenureMonths;

    // Getters and Setters
    public Double getPropertyPrice() { return propertyPrice; }
    public void setPropertyPrice(Double propertyPrice) { this.propertyPrice = propertyPrice; }

    public Double getDownPayment() { return downPayment; }
    public void setDownPayment(Double downPayment) { this.downPayment = downPayment; }

    public Double getTradeInValue() { return tradeInValue; }
    public void setTradeInValue(Double tradeInValue) { this.tradeInValue = tradeInValue; }

    public Double getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(Double annualInterestRate) { this.annualInterestRate = annualInterestRate; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

    public Double getProcessingFee() { return processingFee != null ? processingFee : 0.0; }
    public void setProcessingFee(Double processingFee) { this.processingFee = processingFee; }

    public Double getProcessingFeePercent() { return processingFeePercent != null ? processingFeePercent : 0.0; }
    public void setProcessingFeePercent(Double processingFeePercent) { this.processingFeePercent = processingFeePercent; }

    public Double getInsuranceCost() { return insuranceCost != null ? insuranceCost : 0.0; }
    public void setInsuranceCost(Double insuranceCost) { this.insuranceCost = insuranceCost; }

    public Double getOtherUpfrontCosts() { return otherUpfrontCosts != null ? otherUpfrontCosts : 0.0; }
    public void setOtherUpfrontCosts(Double otherUpfrontCosts) { this.otherUpfrontCosts = otherUpfrontCosts; }

    public Double getMonthlyPrepayment() { return monthlyPrepayment != null ? monthlyPrepayment : 0.0; }
    public void setMonthlyPrepayment(Double monthlyPrepayment) { this.monthlyPrepayment = monthlyPrepayment; }

    public Double getAnnualLumpSum() { return annualLumpSum != null ? annualLumpSum : 0.0; }
    public void setAnnualLumpSum(Double annualLumpSum) { this.annualLumpSum = annualLumpSum; }

    public Double getMonthlyIncome() { return monthlyIncome != null ? monthlyIncome : 0.0; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Double getMonthlyExpenses() { return monthlyExpenses != null ? monthlyExpenses : 0.0; }
    public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public Double getMaxEmiPercent() { return maxEmiPercent != null ? maxEmiPercent : 40.0; }
    public void setMaxEmiPercent(Double maxEmiPercent) { this.maxEmiPercent = maxEmiPercent; }

    public Integer getCompareTenureMonths() { return compareTenureMonths; }
    public void setCompareTenureMonths(Integer compareTenureMonths) { this.compareTenureMonths = compareTenureMonths; }
}
