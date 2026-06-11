package com.emicalc.service;

import com.emicalc.model.AmortizationRow;
import com.emicalc.model.LoanRequest;
import com.emicalc.model.LoanResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanCalculatorService {

    /**
     * Calculate full loan result including amortization schedule,
     * affordability analysis, and optional comparison tenure.
     */
    public LoanResult calculate(LoanRequest req) {
        // --- Derive loan amount ---
        double loanAmount = req.getPropertyPrice()
                - req.getDownPayment()
                - req.getTradeInValue();

        if (loanAmount <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive after down payment and trade-in.");
        }

        // --- Processing fee ---
        double processingFeeFlat   = req.getProcessingFee();
        double processingFeePct    = (req.getProcessingFeePercent() / 100.0) * loanAmount;
        double processingFeeActual = processingFeeFlat + processingFeePct;

        // --- Total upfront cost ---
        double totalUpfront = req.getDownPayment()
                + req.getTradeInValue()   // trade-in offsets but user pays this implicitly
                + processingFeeActual
                + req.getInsuranceCost()
                + req.getOtherUpfrontCosts();

        // --- EMI ---
        double monthlyRate = req.getAnnualInterestRate() / 100.0 / 12.0;
        int n = req.getTenureMonths();
        double emi = computeEmi(loanAmount, monthlyRate, n);

        // --- Amortization with prepayments ---
        AmortizationData data = buildAmortization(loanAmount, monthlyRate, n, emi,
                req.getMonthlyPrepayment(), req.getAnnualLumpSum());

        // --- Base result (no prepayment) to compute interest saved ---
        double baseInterest = (emi * n) - loanAmount;
        double interestSaved = baseInterest - data.totalInterest;

        LoanResult result = new LoanResult();
        result.setLoanAmount(round(loanAmount));
        result.setEmi(round(emi));
        result.setTotalInterest(round(data.totalInterest));
        result.setTotalPayment(round(data.totalPaid));
        result.setTotalUpfrontCost(round(totalUpfront));
        result.setEffectiveCostOfLoan(round(data.totalPaid + processingFeeActual
                + req.getInsuranceCost() + req.getOtherUpfrontCosts()));
        result.setActualTenureMonths(data.actualMonths);
        result.setInterestSavedByPrepayment(round(Math.max(0, interestSaved)));
        result.setProcessingFeeActual(round(processingFeeActual));
        result.setDownPaymentActual(round(req.getDownPayment()));
        result.setSchedule(data.rows);
        result.setYearlySummaries(buildYearlySummaries(data.rows));

        // --- Affordability ---
        computeAffordability(result, req, emi, loanAmount, monthlyRate);

        // --- Comparison tenure ---
        if (req.getCompareTenureMonths() != null && req.getCompareTenureMonths() > 0) {
            LoanRequest compareReq = cloneWithTenure(req, req.getCompareTenureMonths());
            LoanResult compareResult = calculateSingle(compareReq, loanAmount,
                    processingFeeActual, totalUpfront);
            result.setComparisonResult(compareResult);
        }

        return result;
    }

    private LoanResult calculateSingle(LoanRequest req, double loanAmount,
                                       double processingFeeActual, double totalUpfront) {
        double monthlyRate = req.getAnnualInterestRate() / 100.0 / 12.0;
        int n = req.getTenureMonths();
        double emi = computeEmi(loanAmount, monthlyRate, n);

        AmortizationData data = buildAmortization(loanAmount, monthlyRate, n, emi,
                req.getMonthlyPrepayment(), req.getAnnualLumpSum());

        double baseInterest = (emi * n) - loanAmount;
        double interestSaved = baseInterest - data.totalInterest;

        LoanResult r = new LoanResult();
        r.setLoanAmount(round(loanAmount));
        r.setEmi(round(emi));
        r.setTotalInterest(round(data.totalInterest));
        r.setTotalPayment(round(data.totalPaid));
        r.setTotalUpfrontCost(round(totalUpfront));
        r.setEffectiveCostOfLoan(round(data.totalPaid + processingFeeActual
                + req.getInsuranceCost() + req.getOtherUpfrontCosts()));
        r.setActualTenureMonths(data.actualMonths);
        r.setInterestSavedByPrepayment(round(Math.max(0, interestSaved)));
        r.setProcessingFeeActual(round(processingFeeActual));
        r.setDownPaymentActual(round(req.getDownPayment()));
        r.setSchedule(data.rows);
        r.setYearlySummaries(buildYearlySummaries(data.rows));
        return r;
    }

    private double computeEmi(double principal, double monthlyRate, int months) {
        if (monthlyRate == 0) return principal / months;
        double factor = Math.pow(1 + monthlyRate, months);
        return (principal * monthlyRate * factor) / (factor - 1);
    }

    private static class AmortizationData {
        List<AmortizationRow> rows = new ArrayList<>();
        double totalInterest = 0;
        double totalPaid = 0;
        int actualMonths = 0;
    }

    private AmortizationData buildAmortization(double principal, double monthlyRate,
                                                int maxMonths, double emi,
                                                double monthlyPrepayment, double annualLumpSum) {
        AmortizationData data = new AmortizationData();
        double balance = principal;
        double cumulativeInterest = 0;
        double cumulativePrincipal = 0;
        int month = 0;

        while (balance > 0.01 && month < maxMonths) {
            month++;
            int year = (int) Math.ceil(month / 12.0);

            double interestPart = balance * monthlyRate;
            double principalPart = emi - interestPart;
            principalPart = Math.min(principalPart, balance);

            // Annual lump sum prepayment at end of each year
            double lumpSum = 0;
            if (annualLumpSum > 0 && month % 12 == 0) {
                lumpSum = Math.min(annualLumpSum, balance - principalPart);
            }

            // Monthly prepayment
            double extraPayment = Math.min(monthlyPrepayment, balance - principalPart);
            double totalPrepayment = Math.max(0, extraPayment + lumpSum);

            double opening = balance;
            balance = Math.max(0, balance - principalPart - totalPrepayment);

            cumulativeInterest += interestPart;
            cumulativePrincipal += principalPart + totalPrepayment;
            data.totalInterest += interestPart;
            data.totalPaid += emi + totalPrepayment;

            data.rows.add(new AmortizationRow(
                    month, year, opening, emi,
                    principalPart, interestPart, totalPrepayment,
                    balance, cumulativeInterest, cumulativePrincipal
            ));
        }

        data.actualMonths = month;
        return data;
    }

    private List<LoanResult.YearlySummary> buildYearlySummaries(List<AmortizationRow> rows) {
        List<LoanResult.YearlySummary> summaries = new ArrayList<>();
        int currentYear = 1;
        double yearPrincipal = 0, yearInterest = 0;
        double lastBalance = 0;

        for (AmortizationRow row : rows) {
            if (row.getYear() != currentYear) {
                summaries.add(new LoanResult.YearlySummary(currentYear, yearPrincipal, yearInterest, lastBalance));
                currentYear = row.getYear();
                yearPrincipal = 0;
                yearInterest = 0;
            }
            yearPrincipal += row.getPrincipal() + row.getPrepayment();
            yearInterest += row.getInterest();
            lastBalance = row.getClosingBalance();
        }
        // last year
        summaries.add(new LoanResult.YearlySummary(currentYear, yearPrincipal, yearInterest, lastBalance));
        return summaries;
    }

    private void computeAffordability(LoanResult result, LoanRequest req,
                                       double emi, double loanAmount, double monthlyRate) {
        if (req.getMonthlyIncome() <= 0) {
            result.setAffordabilityRating("UNKNOWN");
            return;
        }

        double income     = req.getMonthlyIncome();
        double expenses   = req.getMonthlyExpenses();
        double disposable = income - expenses;
        double ratio      = (emi / income) * 100.0;
        double threshold  = req.getMaxEmiPercent();

        result.setDisposableIncome(round(disposable));
        result.setEmiToIncomeRatio(round(ratio));

        if (ratio <= threshold * 0.75) {
            result.setAffordabilityRating("GREEN");
            result.setAffordable(true);
        } else if (ratio <= threshold) {
            result.setAffordabilityRating("YELLOW");
            result.setAffordable(true);
        } else {
            result.setAffordabilityRating("RED");
            result.setAffordable(false);
        }

        // Max affordable loan based on income threshold
        double maxEmi         = income * (threshold / 100.0);
        double maxLoan        = computeMaxLoan(maxEmi, monthlyRate, req.getTenureMonths());
        double maxPrice       = maxLoan + req.getDownPayment() + req.getTradeInValue();
        result.setMaxAffordableLoan(round(maxLoan));
        result.setMaxAffordablePrice(round(maxPrice));
    }

    private double computeMaxLoan(double maxEmi, double monthlyRate, int n) {
        if (monthlyRate == 0) return maxEmi * n;
        double factor = Math.pow(1 + monthlyRate, n);
        return maxEmi * (factor - 1) / (monthlyRate * factor);
    }

    private LoanRequest cloneWithTenure(LoanRequest original, int newTenure) {
        LoanRequest clone = new LoanRequest();
        clone.setPropertyPrice(original.getPropertyPrice());
        clone.setDownPayment(original.getDownPayment());
        clone.setTradeInValue(original.getTradeInValue());
        clone.setAnnualInterestRate(original.getAnnualInterestRate());
        clone.setTenureMonths(newTenure);
        clone.setProcessingFee(original.getProcessingFee());
        clone.setProcessingFeePercent(original.getProcessingFeePercent());
        clone.setInsuranceCost(original.getInsuranceCost());
        clone.setOtherUpfrontCosts(original.getOtherUpfrontCosts());
        clone.setMonthlyPrepayment(original.getMonthlyPrepayment());
        clone.setAnnualLumpSum(original.getAnnualLumpSum());
        clone.setMonthlyIncome(original.getMonthlyIncome());
        clone.setMonthlyExpenses(original.getMonthlyExpenses());
        clone.setMaxEmiPercent(original.getMaxEmiPercent());
        return clone;
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
