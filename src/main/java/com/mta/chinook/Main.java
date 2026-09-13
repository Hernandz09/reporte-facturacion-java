package com.mta.chinook;

import java.sql.SQLException;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		ReporteDAO dao = new ReporteDAO();
		ReporteConsola vista = new ReporteConsola();

		try {
			Filtros filtros = new Filtros(null, null, "Brazil", null);
			List<ReporteCliente> clientes = dao.obtenerFacturacionPorCliente(filtros);
			vista.imprimir(clientes);

		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
