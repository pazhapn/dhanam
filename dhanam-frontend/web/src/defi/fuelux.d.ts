/// <reference path="jquery.d.ts" />

declare module JQuerySpinBox {

    interface SpinBoxOptions {
        value?: string|number;
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
	off(tag:string): void;
}

interface JQuery {
    spinbox(options?: JQuerySpinBox.SpinBoxOptions): JQuery;
}