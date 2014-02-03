package de.koch.uim_project.database.importdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.PreparedStatement;

public class Main {
	public static Connection connection;

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/uim_projekt", "root", "root");
			PreparedStatement pstmt = connection
					.prepareStatement("INSERT INTO `uim_projekt`.`slogans` (`slogan`, `year`, `game_title`, `source`) VALUES (?, ?, ?, ?);");

			BufferedReader br = new BufferedReader(new FileReader(new File(
					"files/GameSlogansS2Final.csv")));
			String line = br.readLine();
			while (line != null) {
				String[] tmp = line.split(";");
				if (tmp.length == 4) {
					pstmt.setString(1, tmp[0].toLowerCase());
					if (tmp[2].equals("")) {
						pstmt.setNull(2, java.sql.Types.INTEGER);
					} else {
						pstmt.setInt(2, Integer.parseInt(tmp[2]));
					}
					pstmt.setString(3, tmp[1].toLowerCase());
					pstmt.setString(4, tmp[3].toLowerCase());
					pstmt.executeUpdate();
				}
				line = br.readLine();
			}

			pstmt.close();
			connection.close();
		} catch (Exception e) {
			try {
				connection.rollback();
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
	}
}
