package com.mta.chinook;

import java.math.BigDecimal;
import java.util.List;

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
}
