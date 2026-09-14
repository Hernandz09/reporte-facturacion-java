package com.mta.chinook;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReporteConsola {

	public void imprimir(List<ReporteCliente> clientes) {

		BigDecimal totalGeneral = BigDecimal.ZERO;
		int facturasTotales = 0;

		System.out.println();
		System.out.println("REPORTE DE FACTURACION POR CLIENTE");
		System.out.println("=".repeat(58));
		System.out.printf("%-5s %-28s %10s %12s%n", "ID", "CLIENTE", "FACTURAS", "TOTAL");
		System.out.println("-".repeat(58));

		for (ReporteCliente c : clientes) {
			System.out.printf("%-5d %-28s %10d %12.2f%n", c.getCustomerId(), c.getNombre(), c.getNumFacturas(),
					c.getTotalFacturado());

			totalGeneral = totalGeneral.add(c.getTotalFacturado());
			facturasTotales += c.getNumFacturas();
		}
		System.out.println("-".repeat(58));
		System.out.printf("%-34s %10d %12.2f%n", "TOTAL GENERAL (" + clientes.size() + " clientes)", facturasTotales,
				totalGeneral);
		System.out.println();
	}

	public Filtros leerFiltros() {
		Scanner sc = new Scanner(System.in);
		
		LocalDate desde = leerFecha(sc, "Fecha desde (AAAA-MM-DD): ");
		LocalDate hasta = leerFecha(sc, "Fecha hasta (AAAA-MM-DD): ");
		
		System.out.println("=== FILTROS DEL REPORTE ===");
		System.out.println("Deje en blanco para omitir un filtro.");
		System.out.println();

		System.out.print("Pais: ");
		String pais = sc.nextLine().trim();
		if (pais.isBlank()) {
		    pais = null;
		}
		
		return new Filtros(desde, hasta, pais, null);

	}
	
	private LocalDate leerFecha(Scanner sc, String mensaje) {
		while (true) {
			System.out.print(mensaje);
			String entrada = sc.nextLine().trim();

			if (entrada.isBlank()) {
				return null;
			}

			try {
				return LocalDate.parse(entrada);
			} catch (DateTimeParseException e) {
				System.out.println("Formato invalido. Use AAAA-MM-DD, por ejemplo 2013-01-31.");
			}
		}
	}

}
