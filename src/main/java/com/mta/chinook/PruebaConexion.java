package com.mta.chinook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class PruebaConexion {
	public static void main(String[] args) {
        String url = "jdbc:sqlite:db/chinook.sqlite";
        String sql = "SELECT COUNT(*) AS Total FROM Customer";
        
        try (Connection con = DriverManager.getConnection(url);
        	 Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            	 
            System.out.println("Clientes en la base: " + rs.getInt("Total"));
             
        } catch (SQLException e) {
            System.out.println("Fallo la conexion: " + e.getMessage());
        }
    }
}

