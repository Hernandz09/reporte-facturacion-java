package com.mta.chinook;

import java.sql.SQLException;
import java.util.List;

public class GeneradorReporte {
	public static void main(String[] args) {
		ReporteDAO dao = new ReporteDAO();
		ReporteConsola vista = new ReporteConsola();

		try {
			List<ReporteCliente> clientes = dao.obtenerFacturacionPorCliente();
			vista.imprimir(clientes);

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
