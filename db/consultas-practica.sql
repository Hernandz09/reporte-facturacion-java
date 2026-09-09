-- ============================================================
-- CONSULTAS DE PRÁCTICA — Base de datos de ventas (Chinook)
-- Autor: MTA
-- Motor: SQLite
-- ============================================================
-- Objetivo: practicar JOIN, GROUP BY y filtros como preparación
-- para el reporte de facturación de la Etapa 1.
-- ============================================================


-- ============================================================
-- NIVEL 1 — Un solo JOIN
-- ============================================================

-- 1. Título de cada álbum con el nombre de su artista.
-- Esperado: 347 filas (una por álbum).
-- El número coincide con el total de álbumes porque cada álbum
-- pertenece a exactamente un artista.
SELECT al.Title AS Album,
       ar.Name  AS Artista
FROM Album al
JOIN Artist ar ON al.ArtistId = ar.ArtistId;


-- 2. Nombre de cada canción con su género.
-- Esperado: 3.503 filas (una por canción).
-- OJO: el ON une GenreId con GenreId. Unir TrackId con GenreId
-- no da error pero devuelve solo 25 filas de datos falsos.
SELECT t.Name AS Cancion,
       g.Name AS Genero
FROM Track t
JOIN Genre g ON t.GenreId = g.GenreId;


-- 3. Facturas del año 2013 con el nombre del cliente.
-- Esperado: 80 filas.
-- Se usa "< del 1 de enero siguiente" en lugar de "<= 31/12"
-- porque InvoiceDate incluye la hora: una factura del 31/12 a
-- las 14:30 sería mayor que '2013-12-31' y se perdería.
SELECT i.InvoiceId,
       i.InvoiceDate,
       (c.FirstName || ' ' || c.LastName) AS NombreCliente,
       i.Total
FROM Invoice i
JOIN Customer c ON c.CustomerId = i.CustomerId
WHERE i.InvoiceDate >= '2013-01-01'
  AND i.InvoiceDate <  '2014-01-01'
ORDER BY i.InvoiceDate;


-- ============================================================
-- NIVEL 2 — Agrupaciones
-- ============================================================

-- 4. Cantidad de canciones por género, de mayor a menor.
-- Esperado: 25 filas. Rock encabeza con 1.297.
-- Se cuenta por TrackId y no por Name porque COUNT ignora
-- los valores nulos y el id nunca es nulo.
SELECT g.Name AS Genero,
       COUNT(t.TrackId) AS NumCanciones
FROM Genre g
JOIN Track t ON t.GenreId = g.GenreId
GROUP BY g.Name
ORDER BY NumCanciones DESC;


-- 5. Facturación total por país, de mayor a menor.
-- Esperado: 24 filas. USA encabeza con 523,06.
-- No necesita JOIN: el país está en la propia tabla Invoice.
-- El ORDER BY sí puede usar el alias porque se ejecuta al final.
SELECT BillingCountry AS Pais,
       SUM(Total) AS TotalFacturado
FROM Invoice
GROUP BY BillingCountry
ORDER BY TotalFacturado DESC;


-- 6. Artistas con más de 5 álbumes.
-- Esperado: 6 filas (Iron Maiden lidera con 21).
-- HAVING filtra grupos después de agrupar; WHERE no sirve aquí
-- porque se ejecuta antes del GROUP BY, cuando el COUNT aún no existe.
-- En GROUP BY y HAVING se repite la expresión completa y no el alias:
-- SQLite acepta el alias, pero PostgreSQL, Oracle y HANA no.
SELECT ar.Name AS Artista,
       COUNT(al.AlbumId) AS NumAlbums
FROM Artist ar
JOIN Album al ON ar.ArtistId = al.ArtistId
GROUP BY ar.Name
HAVING COUNT(al.AlbumId) > 5;


-- ============================================================
-- CONSULTAS DE VERIFICACIÓN
-- ============================================================
-- Sirven para detectar filas huérfanas que un INNER JOIN
-- descartaría en silencio.

-- Artistas sin ningún álbum. Esperado: 71.
-- Es IS NULL y no = NULL: NULL es ausencia de valor y no se
-- puede comparar con el operador de igualdad.
SELECT ar.Name AS Artista
FROM Artist ar
LEFT JOIN Album al ON ar.ArtistId = al.ArtistId
WHERE al.AlbumId IS NULL;


-- Cantidad de países distintos con facturación.
SELECT COUNT(DISTINCT BillingCountry) AS NumPaises
FROM Invoice;


-- Facturas cuyo Total de cabecera no coincide con la suma de
-- sus líneas. Esperado: 0 filas (la base está consistente).
-- Se compara con ABS(...) > 0.01 y no con != porque los importes
-- se almacenan en punto flotante: 0.99 no tiene representación
-- binaria exacta y sumarlo seis veces no da 5.94 exacto.
-- Por la misma razón, en Java hay que usar BigDecimal y no double.
SELECT i.InvoiceId,
       i.Total,
       SUM(il.UnitPrice * il.Quantity) AS CalculadoDesdeLineas
FROM Invoice i
JOIN InvoiceLine il ON i.InvoiceId = il.InvoiceId
GROUP BY i.InvoiceId, i.Total
HAVING ABS(i.Total - SUM(il.UnitPrice * il.Quantity)) > 0.01;
