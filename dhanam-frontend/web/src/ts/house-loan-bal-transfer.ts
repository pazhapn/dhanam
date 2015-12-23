/// <reference path="../defi/require.d.ts"/>
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
            $("#usLoanAmount").spinbox({
                value: 10,
                min: 5,
                max: 100,
                step: 1,
                units: ['USD']
            });
        }
	}
}