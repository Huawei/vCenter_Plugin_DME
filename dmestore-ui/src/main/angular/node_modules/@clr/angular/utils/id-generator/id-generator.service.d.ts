import { InjectionToken } from '@angular/core';
export declare const UNIQUE_ID: InjectionToken<string>;
export declare function uniqueIdFactory(): string;
export declare const UNIQUE_ID_PROVIDER: {
    provide: InjectionToken<string>;
    useFactory: typeof uniqueIdFactory;
};
