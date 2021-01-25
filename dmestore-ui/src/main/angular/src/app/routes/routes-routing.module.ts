import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { environment } from '@env/environment';

import { AdminLayoutComponent } from '../theme/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from '../theme/auth-layout/auth-layout.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { StorageComponent } from './storage/storage.component';
import { AuthGuard } from '@core';
import {DetailComponent} from './storage/detail/detail.component';
import {ServicelevelComponent} from "./servicelevel/servicelevel.component";

const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      // { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        component: DashboardComponent,
        data: { title: 'Dashboard', titleI18n: 'dashboard' },
      },
      {
        path: 'storage',
        component: StorageComponent,
        data: { title: 'Storage', titleI18n: 'storage' },
      },
      {
        path: 'storage/detail',
        component: DetailComponent,
        data: { title: 'detail', titleI18n: 'detail' },
      },
      {
        path: 'vmfs',
        loadChildren: () => import('./vmfs/vmfs.module').then(m => m.VmfsModule),
        data: { title: 'VMFS', titleI18n: 'VMFS' },
      },
      { path: 'nfs',
        loadChildren: () => import('./nfs/nfs.module').then(m => m.NfsModule),
        data: { title: 'NFS', titleI18n: 'NFS' }
      },
      { path: 'servicelevel',
        loadChildren: () => import('./servicelevel/servicelevel.module').then(m => m.ServicelevelModule),
        data: { title: 'servicelevel', titleI18n: 'servicelevel' }
      },
      { path: 'bestpractice',
        loadChildren: () => import('./bestpractice/bestpractice.module').then(m => m.BestpracticeModule),
        data: { title: 'bestpractice', titleI18n: 'bestpractice' }
      },
      { path: 'iscsi',
        loadChildren: () => import('./iscsi/iscsi.module').then(m => m.IscsiModule),
        data: { title: 'iscsi', titleI18n: 'iscsi' }
      },
      { path: 'rdm',
        loadChildren: () => import('./rdm/rdm.module').then(m => m.RdmModule),
        data: { title: 'rdm', titleI18n: 'rdm' }
      },
      { path: 'applybp',
        loadChildren: () => import('./applybp/applybp.module').then(m => m.ApplybpModule),
        data: { title: 'applybp', titleI18n: 'applybp' }
      }
    ],
  },
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
    ],
  },
  { path: '**', redirectTo: 'dashboard' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      useHash: environment.useHash,
    }),
  ],
  exports: [RouterModule],
})
export class RoutesRoutingModule {}
