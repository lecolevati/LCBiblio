package br.com.leandrocolevati.bancodedados;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Crud {

	private boolean verificaTabela(Connection c, Object model)
			throws SQLException {
		boolean ver = false;
		String sql = "SELECT COUNT(*) FROM " + model.getClass().getSimpleName();

		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ver = true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new SQLException("Objeto e tabela devem ter o mesmo nome");
		}

		return ver;
	}

	private int contaAtributos(Connection c, Object model) throws SQLException {
		String conta = "select count(syscolumns.name) as tamanho from sysobjects, syscolumns where sysobjects.id = syscolumns.id and   sysobjects.xtype = 'u' and   sysobjects.name = ?";
		int numColunas = 0;
		PreparedStatement psConta = c.prepareStatement(conta);
		psConta.setString(1, model.getClass().getSimpleName());
		ResultSet rsConta = psConta.executeQuery();
		if (rsConta.next()) {
			numColunas = rsConta.getInt("tamanho");
		}
		rsConta.close();
		psConta.close();
		return numColunas;
	}

	private boolean verificaColunas(Connection c, Object model)
			throws SecurityException, SQLException {
		boolean ver = false;
		if (verificaTabela(c, model)) {
			List<String> listaAtributos = new ArrayList<String>();
			for (Method m : model.getClass().getMethods()) {
				if (m.getName().contains("set")) {
					listaAtributos.add(m.getName().substring(3,
							m.getName().length()));
				}
			}
			String sql = "select syscolumns.name as coluna from sysobjects, syscolumns where sysobjects.id = syscolumns.id and   sysobjects.xtype = 'u' and   sysobjects.name = ? order by syscolumns.name";

			try {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setString(1, model.getClass().getSimpleName());
				ResultSet rs = ps.executeQuery();
				if (contaAtributos(c, model) != listaAtributos.size()) {
					throw new SQLException(
							"Definições do objeto diferentes da tabela");
				} else {
					int contadorIguais = 0;
					while (rs.next()) {
						for (String s : listaAtributos) {
							if (rs.getString("coluna").toLowerCase()
									.equals(s.toLowerCase())) {
								contadorIguais++;
								break;
							}
						}
					}
					if (contadorIguais == listaAtributos.size()) {
						ver = true;
					}
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new SQLException(
						"Definições de Objeto e tabela incompatíveis");
			}
		}
		return ver;
	}

	private List<String> colunas(Connection c, Object model)
			throws SecurityException, SQLException {
		List<String> listaAtributos = new ArrayList<String>();
		if (verificaColunas(c, model)) {
			for (Method m : model.getClass().getMethods()) {
				if (m.getName().contains("set")) {
					listaAtributos.add(m.getName().substring(3,
							m.getName().length()));
				}
			}
		}
		return listaAtributos;
	}

	private List<Method> getGetters(Object model) {
		List<Method> listaGetters = new ArrayList<Method>();
		for (Method m : model.getClass().getMethods()) {
			if (m.getName().contains("get") && !m.getName().contains("Class")) {
				listaGetters.add(m);
			}
		}
		return listaGetters;
	}

	private int codigoStatement(String[] atributos, Method m) {
		int posicao = 0;
		for (int i = 0; i < atributos.length; i++) {
			if (atributos[i].toLowerCase().equals(
					m.getName().toLowerCase()
							.substring(3, m.getName().toLowerCase().length()))) {
				posicao = i + 1;
				break;
			}
		}
		return posicao;
	}

	/**
	 * Insere o conteúdo dos getters no Objeto model, na tabela de mesmo nome no
	 * SGBD definido no Connection c (Só funcionando com SQL SERVER)
	 * 
	 * @param c
	 *            - Conexão com o SGBD
	 * @param model
	 *            - Objeto com os dados (Seu nome deve ser igual ao da tabela,
	 *            bem como seus atributos)
	 * @return 
	 *            - TRUE (registro inserido)
	 *            - FALSE (nenhum registro inserido)
	 * @throws SecurityException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unused")
	public boolean insert(Connection c, Object model) throws SecurityException,
			SQLException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		boolean inserido = false;
		if (verificaColunas(c, model)) {
			List<String> lista = colunas(c, model);
			if (lista != null) {
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO ");
				sql.append(model.getClass().getSimpleName());
				sql.append(" (");
				int cont = 0;
				for (String s : lista) {
					sql.append(s);
					cont++;
					if (cont < lista.size()) {
						sql.append(",");
					}
				}
				sql.append(") VALUES (");
				cont = 0;
				for (String s : lista) {
					sql.append("?");
					cont++;
					if (cont < lista.size()) {
						sql.append(",");
					}
				}
				sql.append(")");
				PreparedStatement ps = c.prepareStatement(sql.toString());
				String[] atributos = lista.toArray(new String[0]);
				for (Method m : getGetters(model)) {
					int posicao = codigoStatement(atributos, m);
					if (posicao != 0){
						Object valor = m.invoke(model);
						ps.setObject(posicao, valor);
					}
				}
				ps.execute();
				ps.close();
				inserido = true;
			} else {
				throw new SQLException("Erro na Definição da Tabela");
			}
		}
		return inserido;
	}

	/**
	 * Deleta linha que atende a todas as condições citadas no List<String> where (Só funcionando com SQL SERVER)
	 * @param c
	 *            - Conexão com o SGBD
	 * @param model
	 *            - Objeto com os dados (Seu nome deve ser igual ao da tabela,
	 *            bem como seus atributos)
	 * @param where
	 *            - List<String> com os atributos que tem dados que são utilizados
	 *            na cláusula Where da query DELETE
	 *            *Só será excluída a linha que atenda a todas as condições.
	 * @return 
	 *            - TRUE (registro(s) excluídos(s))
	 *            - FALSE (nenhum registro(s) excluídos(s))
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public boolean delete(Connection c, Object model, List<String> where) throws SQLException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		boolean deletado = false;

		if (verificaColunas(c, model)) {
			List<String> lista = colunas(c, model);
			if (lista != null) {
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE ");
				sql.append(model.getClass().getSimpleName());
				sql.append(" WHERE ");
				
				int cont = 0;
				for (String s : where) {
					sql.append(s + " = ?");
					if (cont < where.size() - 1){
						sql.append(" AND ");
					}
					cont++;
				}
				PreparedStatement ps = c.prepareStatement(sql.toString());
				String[] atributos = where.toArray(new String[0]);
				for (Method m : getGetters(model)) {
					int posicao = codigoStatement(atributos, m);
					if (posicao != 0){
						Object valor = m.invoke(model);
						ps.setObject(posicao, valor);
					}
				}
				ps.execute();
				ps.close();
				deletado = true;
			} else {
				throw new SQLException("Erro na Definição da Tabela");
			}
		}

		return deletado;
	}
}
