import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiciosFactuComponent } from './servicios-factu.component';

describe('ServiciosFactuComponent', () => {
  let component: ServiciosFactuComponent;
  let fixture: ComponentFixture<ServiciosFactuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ServiciosFactuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiciosFactuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
