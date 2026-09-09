package com.mta.chinook;

import java.math.BigDecimal;

public class ReporteCliente {
	private int customerId;
	private String nombre;
	private int numFacturas;
	private BigDecimal totalFacturado;

	public ReporteCliente(int customerId, String nombre, int numFacturas, BigDecimal totalFacturado) {
		this.customerId = customerId;
		this.nombre = nombre;
		this.numFacturas = numFacturas;
		this.totalFacturado = totalFacturado;
	}

	public String getNombre() {
		return nombre;
	}

	public int getCustomerId() {
		return customerId;
	}

	public int getNumFacturas() {
		return numFacturas;
	}

	public BigDecimal getTotalFacturado() {
		return totalFacturado;
	}
}