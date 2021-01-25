export interface CustomPropertyConfig {
    type: unknown;
    required?: 'error' | 'warning';
    requiredMessage?: string;
}
export declare type PropertyConfig = PropertyDeclaration<unknown, unknown> & CustomPropertyConfig;
/**
 * https://developers.google.com/web/fundamentals/web-components/best-practices
 */
export declare function getDefaultOptions(propertyKey: string, options?: PropertyConfig): PropertyDeclaration;
export declare function requirePropertyCheck(protoOrDescriptor: any, name: string, options?: PropertyConfig): void;
/**
 * lit-element @property decorator with custom defaults specific to Clarity.
 * https://lit-element.polymer-project.org/guide/properties#property-options
 *
 * A property decorator which creates a LitElement property which reflects a
 * corresponding attribute value. A PropertyDeclaration may optionally be
 * supplied to configure property features.
 *
 * @ExportDecoratedItems
 */
export declare function property(options?: PropertyConfig): (protoOrDescriptor: any, name: string) => any;
export interface PropertyDeclaration<Type = unknown, TypeHint = unknown> {
    noAccessor?: boolean;
    attribute?: boolean | string;
    type?: TypeHint;
    reflect?: boolean;
    converter?: ((value: string | null, type?: TypeHint) => Type) | {
        fromAttribute?(value: string | null, type?: TypeHint): Type;
        toAttribute?(value: Type, type?: TypeHint): unknown;
    };
    hasChanged?(value: Type, oldValue: Type): boolean;
}
/**
 * lit-element @internalProperty decorator with custom defaults specific to Clarity.
 *
 * This is used for communication between internal component properties
 * that are not exposed as part of the public component API.
 *
 * A internalProperty decorator which creates a LitElement property which will
 * trigger a re-render when set but not allow the value to be updated through
 * public attributes.
 *
 * @ExportDecoratedItems
 */
export declare function internalProperty(options?: PropertyConfig): (protoOrDescriptor: any, name: string) => any;
