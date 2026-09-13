package com.mta.chinook;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Filtros {
	
	private final LocalDate fechaDesde;
	private final LocalDate fechaHasta;
	private final String pais;
	private final BigDecimal montoMinimo;
	
	public Filtros(LocalDate fechaDesde, LocalDate fechaHasta, String pais, BigDecimal montoMinimo) {
		if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
		    throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la final");
		}
		
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.pais = pais;
		this.montoMinimo = montoMinimo;
	}

	public LocalDate getFechaDesde() {
		return fechaDesde;
	}

	public LocalDate getFechaHasta() {
		return fechaHasta;
	}

	public String getPais() {
		return pais;
	}

	public BigDecimal getMontoMinimo() {
		return montoMinimo;
	}
	
}
