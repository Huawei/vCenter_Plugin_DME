import { CdsControlMessage } from '../control-message/control-message.element';
import { CdsControl } from '../control/control.element';
export declare type ValidityStateKey = 'valueMissing' | 'badInput' | 'customError' | 'patternMismatch' | 'rangeOverflow' | 'rangeUnderflow' | 'stepMismatch' | 'tooLong' | 'tooShort' | 'typeMismatch' | 'valid';
export declare function syncHTML5Validation(control: CdsControl, messages: CdsControlMessage[]): void;
