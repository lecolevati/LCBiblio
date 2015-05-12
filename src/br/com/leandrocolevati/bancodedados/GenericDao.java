package br.com.leandrocolevati.bancodedados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Criado pelo Prof. M.Sc. Leandro Colevati dos Santos
 */
public class GenericDao {

	private Connection con;
	private String ip;
	private String user;
	private String password;
	private String database;
	private String sid;
	private boolean namedPipes;
	private Sgbd sgbd;

	/**
	 * Construtor para conex�o com SQL Server. Obrigat�rios:
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @param database
	 * @param namedPipes
	 *            (True : Pipes Nomeados, False : IP)
	 * @param sgbd
	 *            (Sgbd.SQLSERVER)
	 */
	public GenericDao(String ip, String user, String password, String database,
			boolean namedPipes, Sgbd sgbd) {
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.database = database;
		this.sgbd = sgbd;
		this.namedPipes = namedPipes;
	}

	/**
	 * Construtor para conex�o com ORACLE. Obrigat�rios:
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @param sid
	 * @param sgbd
	 *            (Sgbd.ORACLE)
	 */
	public GenericDao(String ip, String user, String password, String sid,
			Sgbd sgbd) {
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.sid = sid;
		this.sgbd = sgbd;
	}

	/**
	 * Construtor para conex�o com MySQL. Obrigat�rios:
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @param database
	 * @param sgbd
	 *            (Sgbd.SQLSERVER)
	 */
	public GenericDao(String ip, String user, String password, Sgbd sgbd,
			String database) {
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.database = database;
		this.sgbd = sgbd;
	}

	/**
	 * 
	 * @return conex�o com o SGBD selecionado ao instanciar GenericDao
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (ip.equals("localhost")) {
			ip = "127.0.0.1";
		}
		try {
			Class.forName(sgbd.getDriver());
			con = DriverManager.getConnection(geraStringConexao(), user,
					password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	/**
	 * 
	 * Fecha conex�o com o SGBD selecionado ao instanciar GenericDao
	 * 
	 * @throws SQLException
	 */
	public void fechaConexao(Connection con) throws SQLException {
		if (con != null)
			con.close();
		con = null;
	}

	private String geraStringConexao() throws SQLException {
		StringBuffer sb = new StringBuffer();
		if (sgbd.getValor() == 1) {
			if (database == null) {
				throw new SQLException("Database n�o informada");
			} else {
				sb.append("jdbc:jtds:sqlserver://");
				sb.append(ip);
				sb.append(":1433;DatabaseName=");
				sb.append(database);
				sb.append(";namedPipe=");
				if (namedPipes) {
					sb.append("true");
				} else {
					sb.append("false");
				}
			}
		} else {
			if (sgbd.getValor() == 2) {
				if (sid == null) {
					throw new SQLException("Oracle SID n�o informado");
				} else {
					sb.append("jdbc:oracle:thin:@");
					sb.append(ip);
					sb.append(":1521:");
					sb.append(sid);
				}
			} else {
				if (sgbd.getValor() == 3) {
					if (database == null) {
						throw new SQLException("Database n�o informada");
					} else {
						sb.append("jdbc:mysql://");
						sb.append(ip);
						sb.append(":3306/");
						sb.append(database);
					}
				} else {
					throw new SQLException("SGBD n�o suportado");
				}
			}
		}
		return sb.toString();

	}
}
