/// <reference path="jquery.d.ts" />

declare module JQuerySpinBox {

    interface SpinBoxOptions {
        value?: number;
        min?: number;
		max?: number;
		step?: number;
		limitToStep?: boolean;
		hold?: boolean;
		speed?: string;
		decimalMark?: string;
		disabled?: boolean;
		units?: any[];
	}
}

interface JQueryStatic {
	off(tag:string): JQuery;
}

interface JQuery {
    spinbox(options?: JQuerySpinBox.SpinBoxOptions): JQuery;
}