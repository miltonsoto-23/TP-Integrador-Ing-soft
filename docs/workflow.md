# Git Workflow

**Trabajo Practico Final Ingeniería de Software**
**Proyecto:** Sistema de Monitoreo de habitación con control de acceso
**Desing&Code**
---

## 1. Rama de trabajo y Rama de Integración

Para garantizar la estabilidad del código y evitar colisiones directas entre el firmware y el backend, el repositorio se organiza bajo una jerarquía estricta de los cuatro siguientes niveles:

```text
main                    ← Versiones estables y entregables finales
 └── develop            ← Línea base de integración del Sprint
      └── epic/*        ← Módulos o requerimientos de alto nivel
           └── feat/*   ← Ramas de ejecución individual por desarrollador
```
* **Nivel 1: `main`**
  * **Tipología de Rama:** Producción / Release
  * **Propósito y Ciclo de Vida:** Contiene exclusivamente código funcional, auditado y listo para ser evaluado. Cada actualización aquí representa un hito o versión oficial del proyecto.
  * **Criterio de Fusión (Merge):** ❌ Solo recibe código desde `develop` vía PR.

* **Nivel 2: `develop`**
  * **Tipología de Rama:** Integración Continua
  * **Propósito y Ciclo de Vida:** Es el núcleo del trabajo colaborativo. Representa el estado más actualizado y estable del Sprint en curso.
  * **Criterio de Fusión (Merge):** ❌ Solo recibe código desde ramas inferiores (`epic/*` o `feat/*`) vía PR.

* **Nivel 3: `epic/*`**
  * **Tipología de Rama:** Bloques Estratégicos
  * **Propósito y Ciclo de Vida:** Ramas temporales de agrupación. Sirven para consolidar tareas que pertenecen a un mismo dominio grande antes de enviarlas al entorno general (ej. consolidar toda la telemetría antes de unirla con la web).
  * **Criterio de Fusión (Merge):** ❌ Solo recibe código desde ramas de ejecución (`feat/*` o `bug/*`) vía PR.

* **Nivel 4: `feat/*` y `bug/*`**
  * **Tipología de Rama:** Ejecución Individual
  * **Propósito y Ciclo de Vida:** Ramas efímeras, de vida corta, creadas para resolver un ticket específico de Jira. Son de uso estrictamente personal e intransferible.
  * **Criterio de Fusión (Merge):** No aplica (el desarrollo nace aquí).

``` mermaid
%%{init: { 'theme': 'base', 'themeVariables':{
    'git0': '#00b300',
    'git1': '#ff6600',
    'git2': '#cc0000',
    'git3': '#9900cc',
    'git4': '#ccaa00',
    'git5': '#555555',
    'git6': '#0066cc',
    'git7': '#006666'
},
'gitGraph': {'rotateCommitLabel': false} } }%%
gitGraph
    %% se pueden elegir temas como: 'dark', 'default', 'base', 'forest', 'neutral'
    commit id: "chore:SDR-01 init repo"
    %% Declaro development con orden 2 para dejar el espacio 1 para el hotfix
    branch development order: 2
    checkout development
    commit id: "chore:SDR-02 cfg_struct"

    branch epic/MonitoreoAmbiental order: 3
    checkout epic/MonitoreoAmbiental

    branch feat/SDR-41-cfg_ADC_sensor order: 4
    checkout feat/SDR-41-cfg_ADC_sensor
    commit id:"feat(edge):SDR-41 add cfg ADC for sensors"
    commit id:" "
    checkout epic/MonitoreoAmbiental
    merge feat/SDR-41-cfg_ADC_sensor id:"merge:SDR-41 sensors_cfg"

    branch bugfix/SDR-46-pin-validacion order: 5
    checkout bugfix/SDR-46-pin-validacion
    commit id: "fix(backend): SDR-46 corregir validacion de PIN"
    commit id:"  "
    checkout epic/MonitoreoAmbiental
    merge bugfix/SDR-46-pin-validacion id: "merge:SDR-46 pin_val_fix"

    branch feat/SDR-86-Logica_envio_datos order: 6
    checkout feat/SDR-86-Logica_envio_datos
    commit id: "feat(edge):SDR-86 agrego logica de envio de datos"
    commit id:"   "
    checkout epic/MonitoreoAmbiental
    merge feat/SDR-86-Logica_envio_datos id: "merge:SDR-86 send_data"

    checkout development
    merge epic/MonitoreoAmbiental
    %% release a main
    checkout main
    merge development id: "release: v1.1"

    %% Creo el hotfix desde main (despues de release)
    %% Uso "order:1" para que se dibuje justo debajo de main
    branch hotfix/SDR-34-crash-alarma order: 1
    checkout hotfix/SDR-34-crash-alarma
    commit id:"fix(edge):SDR-34 corregir crash alarma"

    %% Mergeo el hotfix a main
    checkout main
    merge hotfix/SDR-34-crash-alarma id: "hotfix: SDR-34 en produccion"

    checkout development
    %% Nota sobre GitFlow: Deberia mergear los hotfixes tambien a develop?
    %% para no perder el arreglo en la proxima version.
```
---

## 2. Forma de Integración por PR

La integración de código entre las distintas ramas está estrictamente controlada mediante **Pull Requests**. Queda completamente prohibido realizar *pushes* directos a las ramas de integración (`develop` o `main`).

### A. Integración Continua (`feat/*` o `bug/*` ➔ `develop`)
Cada vez que finaliza el trabajo de un ticket, el código debe integrarse a la rama de desarrollo compartida cumpliendo estas condiciones:
* **Vinculación con Jira:** El título y la descripción del PR deben contener obligatoriamente la clave del ticket (ej. `SDR-15: Implementar Singleton para BD`).
* **Code Review:** Se requiere la revisión y aprobación explícita de al menos **dos (2) miembros del equipo** distintos al autor.
* **Resolución de Conflictos:** El autor de la rama de trabajo es el único responsable de actualizarla contra `develop` (hacer *pull* o *rebase*) y resolver cualquier conflicto localmente antes de solicitar el merge.
* **Estrategia de Fusión:** Se utilizará **Squash and Merge**. Esto comprime todos los *commits* intermedios (pruebas, correcciones de tipografía) en un único *commit* limpio y cohesivo sobre la historia de `develop`.

### B. Entregables de Producción (`develop` ➔ `main`)
La integración hacia la rama principal de producción ocurre únicamente al finalizar el Sprint, cuando el sistema de monitoreo y acceso es completamente estable.
* **Consenso del Equipo:** Dado que el grupo consta de seis integrantes, pasar código a producción requiere la aprobación de al menos **cuatro** desarrolladores además del responsable del PR.
* **Estrategia de Fusión:** Se utilizará un **Merge Commit** estándar para preservar el nodo histórico de unión, acompañado inmediatamente por la creación de un *tag* de versionado semántico (ej. `v1.0.0` o `v1.1.0`).
* **Validación Estricta:** Antes de aprobar, el código del microcontrolador en C debe compilar sin errores y los servicios del backend deben poder levantar la conexión a PostgreSQL de forma exitosa.

## 3. Relación entre Ramas y Jira (Trazabilidad)

Para garantizar la trazabilidad entre el código escrito y la gestión del proyecto en Jira, el repositorio utiliza una convención de nombres estricta que vincula automáticamente las ramas con los tickets.

### A. Nomenclatura de Ramas
Toda rama debe contener la clave única del ticket de Jira (`SDR-NN`) en su nombre.
* **Formato Épicas:** `epic/<clave-jira>` (Ejemplo: `epic/SDR-10`)
* **Formato Tareas/Bugs:** `<tipo>/<clave-jira>-<descripcion-corta>`
  * Ejemplo Tarea: `feat/SDR-11-configurar-adc-sensores`
  * Ejemplo Bug: `bug/SDR-22-error-lectura-teclado`

### B. Vinculación Automática
* **Commits:** Al incluir la clave (ej. `SDR-11`) en el mensaje del commit, Jira lo enlazará automáticamente al ticket, permitiendo ver el código desde el tablero ágil.
* **Pull Requests:** El título del PR debe comenzar con la clave del ticket. Cuando el PR se apruebe e integre (Merge), el ticket en Jira pasará automáticamente a la columna "Done" o "Cerrado".
