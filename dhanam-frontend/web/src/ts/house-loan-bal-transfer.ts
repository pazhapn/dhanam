/// <reference path="../defi/require.d.ts"/>
/// <reference path="../defi/bootstrap.d.ts"/>
/// <reference path="../defi/fuelux.d.ts"/>
/// <reference path="../defi/d3.d.ts"/>
/// <reference path="../defi/moment-timezone/moment-timezone.d.ts"/>

define(function (require) {
    moment = require("moment");
});

declare var requestId: string;

module Dhanam {
    
    /**
     * HouseLoanBalTransfer getFlight() is the starting point for the flight search GUI screen
     * 
     */
    export class HouseLoanBalTransfer {
        
        constructor() {
        }
        
        public initializeElements(): void{
            //$.off('fu.data-api');
            $("#usLoanFeeLabel").popover({placement: "auto", trigger: "hover", title: "Balance Transfer Fee",
                 content: "Usually its mentioned as 2% to 5% of the Bal Tx Amount." });
            $("#usLoanAmountSpin").spinbox({value: 5000, min: 10, max: 200000, step: 50, units: ['$'], decimalMark: ',', speed: "fast" });
            $("#usLoanFeeSpin").spinbox({value: 0, min: 0, max: 25, step: 0.01, units: ['%'] });
            $("#usLoanMonthsSpin").spinbox({value: 12, min: 1, max: 120, step: 1, units: ['$'] });
            //$("#indiaLoanAmount").spinbox({value: 5000000, min: 5000, max: 20000000, step: 5000, units: ['Rs'], decimalMark: ',', speed: "fast" });
            //$("#indiaLoanEMI").spinbox({value: 50000, min: 1000, max: 200000, step: 500, units: ['Rs'], decimalMark: ',', speed: "fast" });
            $("#indiaLoanAPRSpin").spinbox({value: 9.5, min: 5, max: 25, step: 0.01, units: ['%'] });
            $("#usSavingsAPRSpin").spinbox({value: 1.0, min: 0.01, max: 25, step: 0.01, units: ['%'] });
            $("#currentConversionRateSpin").spinbox({value: 66, min: 10, max: 120, step: 0.01, units: ['Rs'] });
            $("#futureConversionRateSpin").spinbox({value: 68, min: 10, max: 120, step: 0.01, units: ['Rs'] });
            $("#calculate").click(() => this.calculate());
        }
        
        public calculate(): void {
            var taxRate = 20;
            var amount: number = parseFloat($("#usLoanAmount").val());
            var feeApr: number = parseFloat($("#usLoanFee").val());
            var currentConversionRate: number = parseFloat($("#currentConversionRate").val());
            var futureConversionRate: number = parseFloat($("#futureConversionRate").val());
            var indiaLoanAPR: number = parseFloat($("#indiaLoanAPR").val());
            var usSavingsAPR: number = parseFloat($("#usSavingsAPR").val());
            var months: number = parseFloat($("#usLoanMonths").val());
            
            var initialLoanPaid: number = amount - ((amount * feeApr)/100);
            var initialIntSaved: number = ((initialLoanPaid * indiaLoanAPR) / 100) * months / 12;
            var monthlyInstallment: number = (amount/months);
            console.log("initialIntSaved ", monthlyInstallment, initialLoanPaid, initialIntSaved);
            var monthlySaved: number[] = new Array();
            var monthlyEarned: number[] = new Array();
            var totalMonthlySaved: number = 0, totalMonthlyEarned: number = 0;
            var totalFromInitialPaid: number = 0, totalFromMonthlyPaid: number = 0;
            var totalFromEndCurrentConvPaid: number = 0, totalFromEndFutureConvPaid: number = 0;
            var averageConvRate: number = (currentConversionRate + futureConversionRate)/2;
            console.log("averageConvRate ", currentConversionRate, futureConversionRate, (currentConversionRate + futureConversionRate), averageConvRate);
            for(var i=0; i < months; i++){
                monthlySaved[i] = ((monthlyInstallment * indiaLoanAPR) / 100) * ((months - i)/ 12);
                monthlyEarned[i] = ((monthlyInstallment * usSavingsAPR) / 100) * ((months - i)/ 12); 
                totalMonthlySaved += monthlySaved[i];
                totalMonthlyEarned += monthlyEarned[i];
            console.log("totalMonthlySaved ", totalMonthlySaved, totalMonthlyEarned);
            }
            totalFromInitialPaid = (initialLoanPaid + initialIntSaved) * currentConversionRate;
            totalFromMonthlyPaid = (amount + totalMonthlySaved ) * averageConvRate;
            totalFromEndCurrentConvPaid = (amount + (totalMonthlyEarned * (1 - taxRate/100))) * currentConversionRate;
            totalFromEndFutureConvPaid = (amount + (totalMonthlyEarned * (1 - taxRate/100))) * futureConversionRate;
            console.log("totalFromInitialPaid ", totalFromInitialPaid);
            console.log("totalFromMonthlyPaid ", totalFromMonthlyPaid, (amount + totalMonthlySaved ), averageConvRate);
            console.log("totalFromEndCurrentConvPaid ", totalFromEndCurrentConvPaid);
            console.log("totalFromEndFutureConvPaid ", totalFromEndFutureConvPaid);
        }
	}
}