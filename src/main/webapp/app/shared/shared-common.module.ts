import { NgModule } from '@angular/core';

import { Testk8SSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [Testk8SSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [Testk8SSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class Testk8SSharedCommonModule {}
