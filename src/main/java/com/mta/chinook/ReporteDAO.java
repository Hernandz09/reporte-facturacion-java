package com.mta.chinook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    private static final String URL = "jdbc:sqlite:db/chinook.sqlite";

    public List<ReporteCliente> obtenerFacturacionPorCliente(Filtros filtros) throws SQLException {

        StringBuilder sql = new StringBuilder(
              "SELECT c.CustomerId, "
            + "       c.FirstName || ' ' || c.LastName AS Cliente, "
            + "       COUNT(i.InvoiceId) AS NumFacturas, "
            + "       SUM(i.Total) AS TotalFacturado "
            + "FROM Customer c "
            + "JOIN Invoice i ON c.CustomerId = i.CustomerId "
            + "WHERE 1=1 ");

        List<Object> parametros = new ArrayList<>();

        if (filtros.getFechaDesde() != null) {
            sql.append("AND i.InvoiceDate >= ? ");
            parametros.add(filtros.getFechaDesde().toString());
        }

        if (filtros.getFechaHasta() != null) {
            sql.append("AND i.InvoiceDate < ? ");
            parametros.add(filtros.getFechaHasta().plusDays(1).toString());
        }

        if (filtros.getPais() != null && !filtros.getPais().isBlank()) {
            sql.append("AND i.BillingCountry = ? ");
            parametros.add(filtros.getPais());
        }

        sql.append("GROUP BY c.CustomerId, c.FirstName, c.LastName ");

        if (filtros.getMontoMinimo() != null) {
            sql.append("HAVING SUM(i.Total) >= ? ");
            parametros.add(filtros.getMontoMinimo());
        }

        sql.append("ORDER BY SUM(i.Total) DESC");

        List<ReporteCliente> resultado = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL);
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new ReporteCliente(
                            rs.getInt("CustomerId"),
                            rs.getString("Cliente"),
                            rs.getInt("NumFacturas"),
                            rs.getBigDecimal("TotalFacturado")));
                }
            }
        }

        return resultado;
    }
}