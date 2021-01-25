import { Injectable }  from "@angular/core";

import { ClientSdk, UserSession, WebPlatform } from "./vSphereClientSdkTypes";
import { clientSdkStub, webPlatformStub } from "./dev/webPlatformStub";


/**
 * Globals class bootstraps the API and pluginMode flag.
 * Do not use directly, use the GlobalsService API below instead!
 * Also, use app-config.ts for application configuration.
 */
export class Globals {
   readonly pluginMode: boolean;

   // Access to SDK 6.7 APIs.
   readonly clientSdk: ClientSdk;

   // Access to SDK 6.5 APIs.
   readonly webPlatform: WebPlatform;

   constructor() {
      // pluginMode is set to true when the app runs inside an iFrame, i.e. inside the vSphere Client
      this.pluginMode = (window.self !== window.parent);

      if (this.pluginMode) {
         this.clientSdk = (window.frameElement as any).htmlClientSdk;
         // We keep the 6.5 API around for convenience.  It is still supported in vSphere Client 6.7
         this.webPlatform = window.parent["vSphereClientSDK"].getWebPlatformApi(window);
      } else {
         this.clientSdk = clientSdkStub;
         this.webPlatform = webPlatformStub;
      }
   }
}

/**
 *  A service dealing with application globals which facilitate the mixed-mode
 *  approach of plugin development: dev mode (standalone app) and plugin mode.
 */
@Injectable()
export class GlobalsService {
   private readonly webContextPath: string;
   private readonly userSession: UserSession;

   // The locale id of the vSphere Client, or "en" by default in standalone
   public locale = "en";
   public loading: boolean = false; // 全局loading变量

   constructor(private globals: Globals) {
   }

   /**
    * @return true when the app runs as a plugin inside vSphere Client,
    *       false when it runs standalone during development.
    */
   public isPluginMode(): boolean {
      return this.globals.pluginMode;
   }

   /**
    * @return the ClientSdk object containing SDK 6.7 APIs
    */
   public getClientSdk(): ClientSdk {
      return this.globals.clientSdk;
   }

   /**
    * @return the WebPlatform object containing SDK 6.5 APIs
    */
   public getWebPlatform(): WebPlatform {
      return this.globals.webPlatform;
   }

   /**
    * @return the context path for this plugin object, i.e. /ui/myPluginName
    */
   public getWebContextPath(): string {
      return this.webContextPath;
   }

   public getUserSession(): UserSession {
      return this.userSession;
   }

   /**
    * Flag for checking both the dev mode or a local environment
    * wwweee
    * @returns true if the app is running standalone, or as a plugin in a local environment
    */
   isLocalhostDevMode(): boolean {
      return !this.isPluginMode() ||
         window["self"].parent.location.href.indexOf(" https://localhost:9443") === 0;
   }
}
