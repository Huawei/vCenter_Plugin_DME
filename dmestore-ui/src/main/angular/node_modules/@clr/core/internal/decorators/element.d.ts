export declare const customElement: (tagName: string) => (classOrDescriptor: Constructor<HTMLElement> | ClassDescriptor) => any;
interface ClassDescriptor {
    kind: 'class';
    elements: ClassElement[];
    finisher?: <T>(classDef: Constructor<T>) => undefined | Constructor<T>;
}
interface ClassElement {
    kind: 'field' | 'method';
    key: PropertyKey;
    placement: 'static' | 'prototype' | 'own';
    initializer?: Function;
    extras?: ClassElement[];
    finisher?: <T>(classDef: Constructor<T>) => undefined | Constructor<T>;
    descriptor?: PropertyDescriptor;
}
export declare type Constructor<T> = {
    new (...args: any[]): T;
};
export {};
