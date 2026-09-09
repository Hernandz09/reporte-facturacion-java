package com.mta.chinook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;

public class PruebaConexion {
	public static void main(String[] args) {
		ReporteDAO dao = new ReporteDAO();

		try {
			List<ReporteCliente> clientes = dao.obtenerFacturacionPorCliente();
			System.out.println("Clientes obtenidos: " + clientes.size());
			System.out.println("Primero: " + clientes.get(0).getNombre());
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}

