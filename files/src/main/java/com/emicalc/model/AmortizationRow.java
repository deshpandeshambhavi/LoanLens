package com.emicalc.model;

public class AmortizationRow {
    private int month;
    private int year;
    private double openingBalance;
    private double emi;
    private double principal;
    private double interest;
    private double prepayment;
    private double closingBalance;
    private double cumulativeInterest;
    private double cumulativePrincipal;

    public AmortizationRow() {}

    public AmortizationRow(int month, int year, double openingBalance, double emi,
                           double principal, double interest, double prepayment,
                           double closingBalance, double cumulativeInterest, double cumulativePrincipal) {
        this.month = month;
        this.year = year;
        this.openingBalance = round(openingBalance);
        this.emi = round(emi);
        this.principal = round(principal);
        this.interest = round(interest);
        this.prepayment = round(prepayment);
        this.closingBalance = round(closingBalance);
        this.cumulativeInterest = round(cumulativeInterest);
        this.cumulativePrincipal = round(cumulativePrincipal);
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    public int getMonth() { return month; }
    public int getYear() { return year; }
    public double getOpeningBalance() { return openingBalance; }
    public double getEmi() { return emi; }
    public double getPrincipal() { return principal; }
    public double getInterest() { return interest; }
    public double getPrepayment() { return prepayment; }
    public double getClosingBalance() { return closingBalance; }
    public double getCumulativeInterest() { return cumulativeInterest; }
    public double getCumulativePrincipal() { return cumulativePrincipal; }
}
