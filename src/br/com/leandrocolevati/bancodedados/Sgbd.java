package br.com.leandrocolevati.bancodedados;

public enum Sgbd {

	SQLSERVER(1), ORACLE(2), MYSQL(3), ACCESS(4);
	private final int valor;
	private String nome;
	private String driver;

	Sgbd(int opcao) {
		nome = "";
		driver = "";
		valor = opcao;
		switch (valor) {
		case 1:
			nome = "SQL Server";
			driver = "net.sourceforge.jtds.jdbc.Driver";
			break;
		case 2:
			nome = "Oracle";
			driver = "oracle.jdbc.driver.OracleDriver";
			break;
		case 3:
			nome = "MySQL";
			driver = "org.gjt.mm.mysql.Driver";
			break;
		case 4:
			nome = "Access";
			driver = "sun.jdbc.odbc.JdbcOdbcDriver";
			break;		
			
		default:
			nome = "Sql Server";
			driver = "net.sourceforge.jtds.jdbc.Driver";
			break;
		}
	}

	public int getValor() {
		return valor;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getDriver(){
		return driver;
	}

	@Override
	public String toString() {
		return nome;
	}
	
	
}
