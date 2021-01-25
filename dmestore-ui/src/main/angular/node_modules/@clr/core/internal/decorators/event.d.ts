export interface EventOptions {
    /** indicate if event bubbles up through the DOM or not */
    bubbles?: boolean;
    /** indicate if event is cancelable */
    cancelable?: boolean;
    /** indicate if event can bubble across the boundary between the shadow DOM and the light DOM */
    composed?: boolean;
}
export declare class EventEmitter<T> {
    private target;
    private eventName;
    constructor(target: HTMLElement, eventName: string);
    emit(value: T, options?: EventOptions): void;
}
export declare function event(): (protoOrDescriptor: any, name: string) => any;
