# Convención de Commits

Para mantener un historial de control de versiones legible, estructurado y que facilite la trazabilidad con la gestión ágil del proyecto, el equipo adopta el estándar de **Conventional Commits** adaptado para requerir de forma estricta la vinculación con Jira.

## 1. Regla General

Todo mensaje de *commit* debe seguir obligatoriamente la siguiente estructura:

`tipo(alcance): CLAVE-JIRA descripción corta`

### Desglose de la estructura:
* **`tipo`**: Indica la naturaleza del cambio. Los valores permitidos son:
  * `feat`: Una nueva característica o funcionalidad.
  * `fix`: Corrección de un error o *bug*.
  * `docs`: Cambios exclusivos en la documentación (`docs/`, `README.md`).
  * `refactor`: Cambio de código que no corrige un error ni añade una función (ej. aplicar un patrón de diseño).
  * `test`: Adición o corrección de pruebas.
  * `chore`: Tareas de mantenimiento, actualización de dependencias o configuración.
* **`CLAVE-JIRA`**: Es **obligatorio** incluir la clave del ticket de la tarea asociada (ej. `SDR-11`).
* **`descripción corta`**: Explicación concisa de lo que hace el *commit*. Debe estar escrita en modo imperativo (ej. "agregar", no "agregado" ni "agrega"), todo en minúsculas y sin punto final.

### Reglas Adicionales:
* El mensaje de la primera línea no debe superar los 72 caracteres.
* Si el cambio requiere mayor explicación técnica, se debe dejar una línea en blanco después de la descripción corta y redactar el cuerpo del *commit* debajo.

---

## 2. Ejemplos Válidos

A continuación, se presentan ejemplos de mensajes de *commit* que cumplen estrictamente con la convención y relacionan la tarea con Jira:

**Ejemplo 1**

feat(edge): SDR-11 inicializar pines I2C para pantalla LCD

**Ejemplo 2**
fix(backend): SDR-22 corregir timeout en conexion Singleton a PostgreSQL

Se incrementó el tiempo de espera del pool de conexiones de 5s a 15s para evitar caídas durante picos de telemetría.

**Ejemplo 3**
refactor(api): SDR-18 delegar calculo estadistico al strategy pattern

**Ejemplo 4**
docs: SDR-05 agregar diagrama de componentes y justificacion de patrones
