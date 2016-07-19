package com.mycompany.soapservice;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.mycompany.consumer.QueueListener;

/**
 * Soap Service which calculates GCD of two numbers from queue.
 * @author mandar.namdas
 *
 */
@WebService
public class MySoapService {

	final static Logger log = Logger.getLogger(MySoapService.class);
	
	/* Maintain list of calculated GCD as in memory DB (hsqldb) implementation having some issues. */
	private static List<String> gcdList;
	
	private Connection conn;

	private Statement stmt;

	/**
	 * Method to calculate GCD of two numbers.
	 * @return GCD as integer
	 */
	@WebMethod
	public int gcd() {
		if (log.isDebugEnabled()) {
			log.debug("Start gcd");
		}
		QueueListener listener = new QueueListener();
		String message;
		try {
			message = listener.listenSingleMessage();
			setupDBConnection();
			String[] arr = message.split("-");
			BigInteger num1 = new BigInteger(arr[0]);
			BigInteger num2 = new BigInteger(arr[1]);
			/*if (stmt != null) {
				if (log.isDebugEnabled()) {
					log.debug("Inside if query connection is not null");
				}
				StringBuilder query = new StringBuilder();
				query.append("INSERT INTO GCD (num1,num2,gcd) VALUES (");
				query.append(num1 + ", ");
				query.append(num2 + ", ");
				query.append((num1.gcd(num2)).toString() + " )");
				if (log.isDebugEnabled()) {
					log.debug("Query String : "+query.toString());
				}
				stmt.executeQuery(query.toString());
			}*/
			if (gcdList == null) {
				gcdList = new ArrayList<String>();
			}
			gcdList.add((num1.gcd(num2)).toString());
			if (log.isDebugEnabled()) {
				log.debug("End gcd");
			}
			return (num1.gcd(num2)).intValue();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeDBConnection();
		}
		return 0;
	}

	/**
	 * Method to show list of GCD present in database.
	 * @return list of GCD
	 */
	@WebMethod
	public List<String> gcdList() {
		if (log.isDebugEnabled()) {
			log.debug("Start gcdList");
		}
		try {
			setupDBConnection();
			List<String> gcdValues = new ArrayList<String>();
			/*if (stmt != null) {
				StringBuilder query = new StringBuilder();
				query.append("SELECT gcd from GCD");
				ResultSet set = stmt.executeQuery(query.toString());
				while (set.next()) {
					gcdValues.add(set.getString(3));
				}
			}*/
			if (log.isDebugEnabled()) {
				log.debug("End gcdList");
			}
			return gcdList;
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeDBConnection();
		}
		return null;
	}

	/**
	 * Method to calculate sum of all GCD present in database.
	 * @return sum of GCD as integer
	 */
	@WebMethod
	public int getSum() {
		if (log.isDebugEnabled()) {
			log.debug("End getSum");
		}
		int sum = 0;
		try {
			/*if (stmt != null) {
				StringBuilder query = new StringBuilder();
				query.append("SELECT gcd from GCD");
				ResultSet set;

				set = stmt.executeQuery(query.toString());

				while (set.next()) {
					sum = sum + Integer.parseInt(set.getString(3));
				}
			}*/
			for (String gcd : gcdList) {
				sum = sum + Integer.parseInt(gcd); 
			}
			
			if (log.isDebugEnabled()) {
				log.debug("End getSum");
			}
			return sum;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDBConnection();
		}
		return sum;
	}

	/**
	 * Method to setup connection with hsqldb database.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void setupDBConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		conn = DriverManager.getConnection("jdbc:hsqldb:mydb", "sa", "");
		stmt = conn.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS GCD ( num1 integer, num2 integer, gcd integer)");
	}

	/**
	 * Method to close database connection.
	 */
	private void closeDBConnection() {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
}
