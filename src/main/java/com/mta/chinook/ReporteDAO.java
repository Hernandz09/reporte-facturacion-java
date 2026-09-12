package com.mta.chinook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    private static final String URL = "jdbc:sqlite:db/chinook.sqlite";

    public List<ReporteCliente> obtenerFacturacionPorCliente() throws SQLException {

        String sql = "SELECT c.CustomerId, "
                   + "       c.FirstName || ' ' || c.LastName AS Cliente, "
                   + "       COUNT(i.InvoiceId) AS NumFacturas, "
                   + "       SUM(i.Total) AS TotalFacturado "
                   + "FROM Customer c "
                   + "JOIN Invoice i ON c.CustomerId = i.CustomerId "
                   + "GROUP BY c.CustomerId, c.FirstName, c.LastName "
                   + "ORDER BY SUM(i.Total) DESC";

        List<ReporteCliente> resultado = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                ReporteCliente cliente = new ReporteCliente(
                        rs.getInt("CustomerId"),
                        rs.getString("Cliente"),
                        rs.getInt("NumFacturas"),
                        rs.getBigDecimal("TotalFacturado")
                );
                resultado.add(cliente);
            }
        }

        return resultado;
    }
}