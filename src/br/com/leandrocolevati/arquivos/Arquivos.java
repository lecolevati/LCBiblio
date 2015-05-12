package br.com.leandrocolevati.arquivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Arquivos {

	private String diretorio;
	private String nomeArquivo;

	public Arquivos() {
		super();
	}

	public Arquivos(String diretorio, String nomeArquivo) {
		this.diretorio = diretorio;
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * Com o diret�rio e o nome do arquivo passados como par�metros na inst�ncia
	 * da classe, o m�todo retorna uma String com o conte�do do arquivo.
	 * 
	 * @throws IOException
	 *             - Para arquivo e diret�rios inv�lidos.
	 */

	public String leArquivo() throws IOException {
		StringBuffer sb = new StringBuffer();
		validaDir();
		validaArq();
		File arquivo = new File(diretorio, nomeArquivo);
		FileInputStream fis = new FileInputStream(arquivo);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		while (br.readLine() != null) {
			sb.append(br.readLine());
			sb.append("\r\n");
		}
		br.close();
		isr.close();
		fis.close();
		return sb.toString();
	}

	/**
	 * Com o diret�rio e o nome do arquivo, passados como par�metros na
	 * inst�ncia da classe, o m�todo coloca necessita dos par�metros, conte�do
	 * do arquivo e op��o de escrita. O conte�do � o que ser� colocado no
	 * arquivo. Se a op��o de escrita for EscreveArquivos.APPEND, o arquivo �
	 * atualizado. Se a op��o de escrita for EscreveArquivos.OVERWRITE, o
	 * arquivo � sobrescrito.
	 * 
	 * @throws IOException
	 *             - Para arquivo e diret�rios inv�lidos.
	 * 
	 */
	public void escreveArquivo(String conteudo, EscreveArquivos opcaoEscrita)
			throws IOException {
		boolean opcao = opcaoEscrita.getValor();
		validaDir();
		File arquivo = new File(diretorio, nomeArquivo);
		if (!arquivo.exists()) {
			opcao = false;
			arquivo.createNewFile();
		}
		FileWriter fw = new FileWriter(arquivo, opcao);
		PrintWriter pw = new PrintWriter(fw);
		if (conteudo != null) {
			if (!conteudo.trim().equals("")) {
				if (opcao) {
					pw.write("\r\n");
				}
				pw.write(conteudo);
				pw.flush();
				pw.close();
				fw.close();
			} else {
				pw.close();
				fw.close();
				throw new IOException("Conte�do Inexistente !");
			}
		} else {
			pw.close();
			fw.close();
			throw new IOException("Conte�do Inexistente !");
		}

	}

	/**
	 * M�todo abre um JFileChooser para selecionar um arquivo, A matriz filtro,
	 * � um par�metro para definir os filtros de busca do JFileChooser
	 * 
	 * @param filtro
	 *            - Tipos de Arquivos {filtro[i][0] (Descri��o), filtro[i][1]
	 *            (Extens�o)}
	 * @param allFiles
	 *            - True : Permitir todos os arquivos
	 *            - False : N�o Permitir todos os arquivos
	 *            * Se filtro for null, allFiles = True por padr�o
	 * @return - Arquivo selecionado pelo usu�rio
	 */
	public File selecionaArquivo(String[][] filtro, boolean allFiles) {
		File arquivo = null;
		JFileChooser fc = new JFileChooser(System.getProperty("user.home", "."));
		if (filtro == null) {
			fc.setAcceptAllFileFilterUsed(true);
		} else {
			fc.setAcceptAllFileFilterUsed(allFiles);			
		}

		if (filtro != null) {
			fc.setAcceptAllFileFilterUsed(true);
			if (filtro.length > 0) {
				for (String[] linha : filtro) {
					fc.addChoosableFileFilter(new FileNameExtensionFilter(
							linha[0], linha[1]));
				}
			}
		}

		int res = fc.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			arquivo = fc.getSelectedFile();
		}

		return arquivo;
	}

	private void validaDir() throws IOException {
		File dir = new File(diretorio);
		if (!dir.exists()) {
			throw new IOException("Diret�rio Inv�lido !");
		}
	}

	private void validaArq() throws IOException {
		File arquivo = new File(diretorio, nomeArquivo);
		if (!arquivo.exists()) {
			throw new IOException("Arquivo Inv�lido !");
		}
	}

	// TODO m�todo listaDiretorios
	// TODO m�todo recursivo localizarArquivo

}
