import os
import time
from playwright.sync_api import sync_playwright, TimeoutError as PlaywrightTimeoutError

# --- CONFIGURACIÓN ---
URL_BASE = "http://rigo-viajes.vercel.app"
DIRECTORIO_MANUAL = "manual_rigo_viajes"

def preparar_directorio():
    if not os.path.exists(DIRECTORIO_MANUAL):
        os.makedirs(DIRECTORIO_MANUAL)
        print(f"✔ Carpeta de documentación lista en: {DIRECTORIO_MANUAL}")

def generar_documentacion():
    preparar_directorio()

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False, slow_mo=400)
        page = browser.new_page(viewport={"width": 1280, "height": 720})

        try:
            # --- GENERACIÓN DE DATOS ÚNICOS ---
            sufijo_unico = str(int(time.time()))[-5:]
            correo_dinamico = f"favila_{sufijo_unico}@rigoviajes.com"
            nombre_empresa_dinamica = f"Transportes Rigo {sufijo_unico} S.L."
            cif_dinamico = f"B{sufijo_unico}678"

            print(f"➜ Generando ejecución con ID único: {sufijo_unico}")
            print(f"➜ Correo de prueba: {correo_dinamico}")

            # ---------------------------------------------------------
            # PASO 1: PANTALLA DE LOGIN Y NAVEGACIÓN A REGISTRO
            # ---------------------------------------------------------
            print("1. Accediendo al sistema...")
            page.goto(f"{URL_BASE}/login")
            page.wait_for_load_state("networkidle")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/01_pantalla_login.png")

            page.click("button.btn-secondary:has-text('Crear nuevo usuario')")
            page.wait_for_url(f"**/registro")

            # ---------------------------------------------------------
            # PASO 2: FORMULARIO DE REGISTRO
            # ---------------------------------------------------------
            print("2. Registrando nuevo usuario...")
            page.wait_for_selector("form")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/02_pantalla_registro.png")

            page.fill("input[formControlName='nombre']", f"Favila Test {sufijo_unico}")
            page.fill("input[formControlName='email']", correo_dinamico)
            page.fill("input[formControlName='password']", "PasswordSeguro123")
            page.fill("input[formControlName='rol']", "ADMIN")

            page.screenshot(path=f"{DIRECTORIO_MANUAL}/03_registro_rellenado.png")
            page.click("button.btn-registrar")

            page.wait_for_url(f"**/login")

            # ---------------------------------------------------------
            # PASO 3: INICIAR SESIÓN CON EL USUARIO
            # ---------------------------------------------------------
            print("3. Iniciando sesión...")
            page.fill("input[formControlName='nombreUsuario']", correo_dinamico)
            page.fill("input[formControlName='password']", "PasswordSeguro123")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/04_credenciales_login.png")

            page.click("button.btn-login")

            page.wait_for_url(f"**/empresas")
            page.wait_for_selector("table.empresas-table")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/05_dashboard_empresas.png")

            # ---------------------------------------------------------
            # PASO 4: CREAR NUEVA EMPRESA
            # ---------------------------------------------------------
            print("4. Abriendo modal de nueva empresa...")
            page.click("button.boton-flotante")
            page.wait_for_selector("mat-dialog-container")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/06_modal_nueva_empresa.png")

            print("   -> Rellenando Datos Generales...")
            page.fill("input[formControlName='nombre']", nombre_empresa_dinamica)
            page.fill("input[formControlName='cif']", cif_dinamico)
            page.fill("input[formControlName='telefono']", "600123456")
            page.fill("input[formControlName='precioKmDefecto']", "0.80")
            page.fill("input[formControlName='precioHoraEsperaDefecto']", "15")
            page.fill("input[formControlName='precioDietaDefecto']", "25")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/07_empresa_datos_generales.png")

            print("   -> Rellenando Dirección...")
            page.click("div[role='tab']:has-text('Dirección')")
            page.wait_for_timeout(500)

            page.fill("input[formControlName='calle']", "Av. Principal")
            page.fill("input[formControlName='numero']", "123")
            page.fill("input[formControlName='codigoPostal']", "28001")
            page.fill("input[formControlName='ciudad']", "Madrid")
            page.fill("input[formControlName='provincia']", "Madrid")
            page.fill("input[formControlName='pais']", "España")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/08_empresa_direccion.png")

            print("Guardando empresa...")
            page.click("mat-dialog-container button:has-text('Guardar')")

            page.wait_for_selector("mat-dialog-container", state="hidden")
            print(f"   -> Esperando a que '{nombre_empresa_dinamica}' aparezca en la tabla...")
            page.locator(f"text='{nombre_empresa_dinamica}'").wait_for(state="visible", timeout=10000)

            page.screenshot(path=f"{DIRECTORIO_MANUAL}/09_empresa_creada_exito.png")

            # ---------------------------------------------------------
            # PASO 5: ENTRAR DENTRO DE LA EMPRESA CREADA
            # ---------------------------------------------------------
            print("5. Entrando en Servicios de la empresa creada...")

            fila_empresa = page.locator("tr", has_text=nombre_empresa_dinamica)
            boton_nombre_empresa = fila_empresa.locator(
                "button", has_text=nombre_empresa_dinamica
            )
            boton_nombre_empresa.wait_for(state="visible")
            boton_nombre_empresa.click()

            page.wait_for_url("**/empresas/*/servicios")
            page.wait_for_selector("h2:has-text('Servicios de la empresa')")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/10_dashboard_interno_empresa.png")

            # Extraemos el ID de empresa desde la URL para validar Facturas más adelante.
            empresa_id = page.url.rstrip("/").split("/")[-2]

            # ---------------------------------------------------------
            # PASO 6: GESTIÓN DE SERVICIOS (CREAR / EDITAR / FACTURAR / ELIMINAR)
            # ---------------------------------------------------------
            print("6. Gestionando Servicios...")
            page.wait_for_selector("button:has-text('Añadir Servicios')")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/11_listado_servicios.png")

            print("   -> Creando Servicio en ventana emergente...")
            with page.expect_popup() as popup_crear:
                page.click("button:has-text('Añadir Servicios')")
            page_servicio = popup_crear.value
            page_servicio.wait_for_load_state("networkidle")
            page_servicio.wait_for_selector("form")

            page_servicio.fill("input[formcontrolname='tipoServicio']", "Transporte VIP")
            # El formulario ya trae fecha válida por defecto; evitamos forzar formato local.
            page_servicio.fill("input[formcontrolname='origen']", "Madrid")
            page_servicio.fill("input[formcontrolname='destino']", "Valencia")
            page_servicio.fill("input[formcontrolname='clienteFinal']", "Cliente Demo")

            page_servicio.click("div[role='tab']:has-text('Logística y Costes')")
            page_servicio.fill("input[formcontrolname='conductor']", "Conductor Prueba")
            page_servicio.fill("input[formcontrolname='matriculaVehiculo']", "1234-ABC")
            page_servicio.fill("input[formcontrolname='km']", "320")
            page_servicio.fill("input[formcontrolname='precioKm']", "0.80")
            page_servicio.click("label:has-text('Incluye Dieta')")
            page_servicio.fill("input[formcontrolname='precioDieta']", "25")
            page_servicio.fill("input[formcontrolname='horasEspera']", "1")
            page_servicio.fill("input[formcontrolname='importeEspera']", "15")

            page_servicio.click("div[role='tab']:has-text('Documentación y Notas')")
            page_servicio.fill("input[formcontrolname='albaran']", f"ALB-{sufijo_unico}")
            page_servicio.fill("input[formcontrolname='orden']", "1")
            page_servicio.fill(
                "textarea[formcontrolname='observaciones']",
                "Servicio creado por script de pruebas E2E.",
            )
            page_servicio.screenshot(path=f"{DIRECTORIO_MANUAL}/12_crear_servicio.png")
            page_servicio.locator(
                "button:has-text('Guardar Servicio'):not([disabled])"
            ).wait_for(state="visible")
            page_servicio.click("button:has-text('Guardar Servicio')")
            try:
                page_servicio.wait_for_event("close", timeout=20000)
            except PlaywrightTimeoutError:
                # La ventana no se cerró sola; la cerramos manualmente.
                try:
                    page_servicio.click("button:has-text('Cancelar')")
                except Exception:
                    pass

            page.locator("tr", has_text="Transporte VIP").wait_for(state="visible")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/13_servicio_creado.png")

            print("   -> Editando Servicio en ventana emergente...")
            fila_servicio = page.locator("tr", has_text="Transporte VIP")
            with page.expect_popup() as popup_editar:
                fila_servicio.locator("button.btn-editar").click()
            page_edicion = popup_editar.value
            page_edicion.wait_for_load_state("networkidle")
            page_edicion.click("div[role='tab']:has-text('Logística y Costes')")
            page_edicion.fill("input[formcontrolname='km']", "350")
            page_edicion.fill("input[formcontrolname='precioKm']", "0.90")
            page_edicion.screenshot(path=f"{DIRECTORIO_MANUAL}/14_editar_servicio.png")
            page_edicion.click("button:has-text('Actualizar Servicio')")
            try:
                page_edicion.wait_for_event("close", timeout=20000)
            except PlaywrightTimeoutError:
                try:
                    page_edicion.click("button:has-text('Cancelar')")
                except Exception:
                    pass

            page.wait_for_timeout(1000)

            print("   -> Generando Factura desde servicios seleccionados...")
            fila_servicio = page.locator("tr", has_text="Transporte VIP")
            fila_servicio.locator("input[type='checkbox']").check()
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/15_servicio_seleccionado_factura.png")

            page.click("button:has-text('Generar Factura')")
            page.wait_for_selector("mat-dialog-container")
            page.fill("mat-dialog-container input[type='number'] >> nth=0", "21")
            page.fill("mat-dialog-container input[type='number'] >> nth=1", "15")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/16_datos_factura.png")
            page.click("mat-dialog-container button:has-text('Generar')")
            page.wait_for_selector("mat-dialog-container", state="hidden")

            page.wait_for_timeout(1500)
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/17_factura_generada_desde_servicios.png")

            print("   -> Eliminando Servicio...")
            fila_servicio = page.locator("tr", has_text="Transporte VIP")
            fila_servicio.locator("button.btn-eliminar").click()
            page.wait_for_selector("mat-dialog-container")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/18_modal_confirmacion_servicio.png")
            page.click("mat-dialog-container button:has-text('Eliminar')")
            page.wait_for_selector("mat-dialog-container", state="hidden")

            # ---------------------------------------------------------
            # PASO 7: GESTIÓN DE FACTURAS (CONSULTA Y ELIMINACIÓN)
            # ---------------------------------------------------------
            print("7. Navegando a Facturas...")
            page.goto(f"{URL_BASE}/facturas/{empresa_id}")
            page.wait_for_load_state("networkidle")
            page.wait_for_selector("table.facturas-table")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/19_listado_facturas.png")

            print("   -> Eliminando la factura más reciente...")
            primera_factura = page.locator("table.facturas-table tbody tr").first
            primera_factura.wait_for(state="visible")
            numero_factura = primera_factura.locator("td").nth(0).inner_text().strip()
            primera_factura.locator("button:has(mat-icon:has-text('delete'))").click()
            page.wait_for_selector("mat-dialog-container")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/20_borrar_factura.png")
            page.click("mat-dialog-container button:has-text('Eliminar')")
            page.wait_for_selector("mat-dialog-container", state="hidden")

            # Espera breve para que refresque el datasource tras la eliminación
            page.wait_for_timeout(1500)
            page.locator(f"td:has-text('{numero_factura}')").wait_for(state="hidden")

            print("\n✔ ¡Manual completo generado con éxito!")

        except Exception as e:
            print(f"\n❌ Se produjo un error: {e}")
            page.screenshot(path=f"{DIRECTORIO_MANUAL}/ERROR_PANTALLA.png")

        finally:
            time.sleep(3)
            browser.close()

if __name__ == "__main__":
    generar_documentacion()
