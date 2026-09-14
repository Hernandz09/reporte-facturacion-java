# Sistema de Reportes de Facturación

Aplicación de consola en Java que genera reportes de facturación por cliente sobre una base de datos comercial, con filtros dinámicos definidos por el usuario.

Desarrollada como práctica de patrones de desarrollo ERP: reportes con criterios de selección variables, acceso a datos mediante SQL explícito y arquitectura en capas.

---

## El problema

Un área comercial necesita conocer el volumen de facturación por cliente dentro de un período, con la posibilidad de acotar la consulta por país y por monto mínimo facturado. El reporte debe mostrar, por cada cliente, la cantidad de facturas emitidas y el importe acumulado, además de un total general.

Los cuatro criterios son opcionales y combinables entre sí.

---

## Ejemplo de salida

```
REPORTE DE FACTURACION POR CLIENTE
==========================================================
ID    CLIENTE                        FACTURAS        TOTAL
----------------------------------------------------------
1     Luís Gonçalves                        7        39.62
10    Eduardo Martins                       7        37.62
11    Alexandre Rocha                       7        37.62
12    Roberto Almeida                       7        37.62
13    Fernanda Ramos                        7        37.62
----------------------------------------------------------
TOTAL GENERAL (5 clientes)                 35       190.10
```

---

## Arquitectura

El proyecto separa responsabilidades en capas, de modo que cada clase tenga una única razón para cambiar.

| Clase | Capa | Responsabilidad |
|---|---|---|
| `ReporteCliente` | Entidad | Representa una fila del reporte |
| `Filtros` | Entidad | Agrupa los criterios de selección |
| `ReporteDAO` | Acceso a datos | Construye y ejecuta la consulta |
| `ReporteConsola` | Presentación | Lee criterios e imprime resultados |
| `Main` | Entrada | Coordina el flujo |

La capa de presentación desconoce el origen de los datos y la capa de acceso a datos desconoce cómo se mostrarán. Sustituir la salida por consola por una interfaz web implicaría reemplazar una sola clase.

---

## Decisiones técnicas

**JDBC directo en lugar de un ORM.** El SQL se escribe y se mantiene de forma explícita. Un ORM como Hibernate oculta la consulta generada, lo que impide razonar sobre el plan de ejecución y sobre el rendimiento de los JOIN. En entornos ERP el control sobre la sentencia es un requisito, no una preferencia.

**`BigDecimal` para importes, nunca `double`.** Los tipos de punto flotante no representan de forma exacta valores decimales como 0.99, por lo que acumular importes produce discrepancias entre los subtotales y el total general. `BigDecimal` opera en base decimal y elimina ese error.

**`PreparedStatement` con marcadores de posición.** Los valores introducidos por el usuario nunca se concatenan dentro de la sentencia. La base de datos recibe primero la estructura y después los parámetros, de modo que un valor no puede reinterpretarse como instrucción. Esto neutraliza la inyección SQL.

**Filtros opcionales mediante construcción dinámica.** La sentencia se arma condicionalmente sobre una cláusula `WHERE 1=1`, agregando solo las condiciones cuyos criterios fueron informados. Los parámetros se acumulan en el mismo orden que los marcadores.

**El monto mínimo se filtra con `HAVING` y no con `WHERE`.** El criterio se aplica sobre el importe acumulado por cliente, un valor que no existe hasta después de la agrupación. Ubicarlo en el `WHERE` filtraría facturas individuales y produciría un resultado distinto.

**Rango de fechas con `>=` y `<` sobre el día siguiente.** La columna de fecha incluye componente horario. Usar `<=` sobre la fecha final excluiría los registros de ese día con hora distinta de cero.

**Objetos inmutables.** `ReporteCliente` y `Filtros` declaran sus campos como `final` y no exponen setters. Las validaciones se concentran en el constructor, de modo que si la instancia existe, su estado es válido.

---

## Correspondencia con desarrollo ABAP

El proyecto está diseñado como puente hacia el desarrollo backend sobre SAP. Cada componente tiene un equivalente directo:

| Este proyecto | ABAP / SAP |
|---|---|
| Clase `Filtros` | Estructura de criterios de selección (`SELECT-OPTIONS`) |
| POJO `ReporteCliente` | Estructura del Data Dictionary |
| `List<ReporteCliente>` | Tabla interna |
| `while (rs.next())` | `LOOP AT ... INTO` |
| `PreparedStatement` con `?` | Variables host (`@lv_pais`) en Open SQL |
| `BigDecimal` | Tipos `CURR` / `DEC` |
| `ReporteDAO` | Clase o módulo de función que encapsula el `SELECT` |
| Salida formateada por consola | Reporte ALV |

---

## Tecnologías

- Java 17
- SQLite con driver JDBC (`org.xerial:sqlite-jdbc`)
- Maven

Sin frameworks: el objetivo es el control explícito sobre el acceso a datos.

---

## Ejecución

Importar el proyecto en un IDE con soporte Maven (Eclipse, IntelliJ) 
y ejecutar la clase `com.mta.chinook.Main`.

Las dependencias se resuelven automáticamente desde el `pom.xml`. 
La base de datos de ejemplo se incluye en `db/` y no requiere 
instalación de ningún motor.

La base de datos de ejemplo se incluye en `db/` y no requiere instalación de ningún motor.

Al iniciar, la aplicación solicita los cuatro criterios de selección. Cualquiera puede dejarse en blanco para omitirlo.

---

## Estado

Primera etapa completa: reporte de facturación con filtros dinámicos.

Etapas previstas:

- **Carga masiva desde archivo CSV** con validación por registro, manejo transaccional y registro de errores.
- **Reglas de negocio** (descuentos por volumen, categorización de clientes) cubiertas con pruebas unitarias en JUnit.
