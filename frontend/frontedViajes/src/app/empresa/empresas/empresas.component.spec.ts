import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Empresas } from './empresas.component';

describe('Empresas', () => {
  let component: Empresas;
  let fixture: ComponentFixture<Empresas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Empresas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Empresas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
