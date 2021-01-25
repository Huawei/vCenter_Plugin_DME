/**
 * Undocumented experimental feature: inline editing.
 */
import { EventEmitter } from '@angular/core';
import { ClrStackView } from './stack-view';
export declare class StackControl {
    protected stackView: ClrStackView;
    model: any;
    modelChange: EventEmitter<any>;
    constructor(stackView: ClrStackView);
}
