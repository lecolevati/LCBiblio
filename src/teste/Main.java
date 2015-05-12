package teste;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.leandrocolevati.arquivos.Arquivos;
import br.com.leandrocolevati.arquivos.EscreveArquivos;
import br.com.leandrocolevati.bancodedados.Crud;
import br.com.leandrocolevati.bancodedados.GenericDao;
import br.com.leandrocolevati.bancodedados.Sgbd;

public class Main {

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// Arquivos arq = new Arquivos("C:\\Users\\Leandro\\Desktop", "teste1.txt");
	// try {
	// arq.escreveArquivo("",EscreveArquivos.APPEND);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	// public static void main(String[] args) {
	// Arquivos arq = new Arquivos();
	// String[][] filtro = new String[2][2];
	// filtro[0][0] = "Arquivos Texto (.txt)";
	// filtro[0][1] = "txt";
	// filtro[1][0] = "Documentos Word (2007+) (.docx)";
	// filtro[1][1] = "docx";
	// File arquivo = arq.selecionaArquivo(null, false);
	// System.out.println(arquivo.getAbsolutePath());
	// }

	// public static void main(String[] args) {
	// GenericDao gDao = new GenericDao("localhost", "sa", "1234", "master",
	// true, Sgbd.SQLSERVER);
	// try {
	// Connection c = gDao.getConnection();
	// PreparedStatement ps = c.prepareStatement("SELECT GETDATE() as dt");
	// ResultSet rs = ps.executeQuery();
	// if (rs.next()){
	// System.out.println(rs.getString("dt"));
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
		// GenericDao gDao = new GenericDao("localhost", "SYSTEM", "1234",
		// "orcl", Sgbd.ORACLE);
		// try {
		// Connection c = gDao.getConnection();
		// PreparedStatement ps =
		// c.prepareStatement("SELECT SYSDATE AS dt FROM DUAL");
		// ResultSet rs = ps.executeQuery();
		// if (rs.next()){
		// System.out.println(rs.getString("dt"));
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// Crud crud = new Crud();
		// Notas notas = new Notas();
		// GenericDao gDao = new GenericDao("localhost", "sa", "1234",
		// "aulajoin10", true, Sgbd.SQLSERVER);
		// try {
		// Connection c = gDao.getConnection();
		// boolean testa = crud.verificaColunas(c, notas);
		// System.out.println(testa);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

		Crud crud = new Crud();
		Notas notas = new Notas();
		notas.setRa_aluno("1520007930");
		notas.setId_avaliacao(100002);
		notas.setId_materia(1);
//		notas.setNota(null);
		GenericDao gDao = new GenericDao("localhost", "sa", "1234",
				"aulajoin10", true, Sgbd.SQLSERVER);
		try {
			List<String> where = new ArrayList<String>();
			where.add("ra_aluno");
			where.add("id_materia");
			where.add("id_avaliacao");
			Connection c = gDao.getConnection();
			boolean insert = crud.delete(c, notas, where);
			System.out.println(insert);
		} catch (SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
