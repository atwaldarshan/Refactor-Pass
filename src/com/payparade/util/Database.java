package com.payparade.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class Database {
	static Logger logger = Logger.getLogger(Database.class);
	private DataSource dataSource;
	private static Connection connection;

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		Database.connection = connection;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		try {
			connection = dataSource.getConnection();
		} catch (SQLException sql) {
			sql.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static HashMap<String, ArrayList<String>> pkCache_ = null;

	public static boolean isKey(String table, String column) {
		ArrayList<String> keyNames = null;
		String tableName = table.toUpperCase();
		String columnName = column.toUpperCase();
		logger.info("isKey(" + tableName + "." + column + ")");
		ArrayList<String> keys = getKeyFields(tableName);
		return keys.contains(columnName);
	}

	public static ArrayList<String> getKeyFields(String tableName) {
		ArrayList<String> keyNames = null;
		tableName = tableName.toUpperCase();
		logger.info("getKeyFields(" + tableName + ")");
		Connection con = getConnection();

		synchronized (pkCache_) {
			keyNames = pkCache_.get(tableName);
			if (keyNames == null || keyNames.isEmpty()) {
				Connection conn = null;
				DatabaseMetaData md = null;
				keyNames = new ArrayList<String>();
				try {
					md = con.getMetaData();
					ResultSet table = md.getTables(null, null, tableName, null);
					if (table.next()) {
						logger.trace("getKeyFields() found: "
								+ table.getString("TABLE_SCHEM") + "."
								+ table.getString("TABLE_NAME"));

						ResultSet primaryKeys = md.getPrimaryKeys(null, null,
								table.getString("TABLE_NAME"));
						while (primaryKeys.next()) {
							String primaryKeyColumn = primaryKeys
									.getString("COLUMN_NAME");
							keyNames.add(primaryKeyColumn.toUpperCase());
							logger.trace("Primary Key Column: "
									+ primaryKeyColumn);
						}
					}
					pkCache_.put(tableName, keyNames);
				} catch (SQLException e) {
					logger.error("SQL ex " + e.getMessage());
				} finally {
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {/* do nothing */
						}
					}
				}
				conn = null;
			}
		}
		return keyNames;
	}

}
