package com.example.demo.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/**
 * Mantem o registro na emissao da fatura 
 * @author colaborador1
 *
 */
public class Fatura {

	int numero;
	String CNPJdaContratada;
	String dataEmissao;
	String dataVencimento;
	String servicoContratado;
	float valor;
	Logger logger = LogManager.getLogger(this.getClass());

	public Fatura(int numero, String cnpj, String dataVencimento, String desc, String valor) {
		this.numero = numero;
		this.CNPJdaContratada = setCnpj(cnpj);
		this.dataEmissao = setDataEmissao();
		this.dataVencimento = setDataVencimento(dataVencimento);
		this.servicoContratado = setServicoContratado(desc);
		this.valor = setValorFatura(valor);
	}

	public Fatura() {
	}

	public int getNumero() {
		return numero;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public float getValor() {
		return valor;
	}
	/**
	 * atribui a data de emissao da fatura a data do sistema
	 * @return a data atual no formato texto
	 */
	private String setDataEmissao() {
		DateTime dataAtual = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		logger.info(">>>>>> setDataEmissao para data de hoje => " + dataAtual.toString(fmt));
		return dataAtual.toString(fmt);
	}
	
	/**
	 * atribui a data de vencimento verifica se a data eh valida
	 * @param data tipo texto no formato valido
	 * @return data tipo texto valida
	 */

	public String setDataVencimento(String data) {
        //É necessário ficar atento a situações excepcionais onde a execução ou não do restante dos operadores 
		//pode alterar o fluxo do programa e gerar um resultado não esperado
		if ((isValida(data) == true) && (dtVencMaiorDtAtual(getDataEmissao(), data) == true) && (ehDomingo(data) == false) ) {
			logger.info(">>>>>> setDataVencimento  => " + data);
			return data;
		} else {
			throw new IllegalArgumentException("Data invalida");
		}

	}
	/**
	 * verifica se a data foi agendada para um domingo
	 * @param data
	 * @return true ou false
	 */

	public boolean ehDomingo(String data) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
		DateTime umaData = fmt.parseDateTime(data);
		if (umaData.dayOfWeek().getAsText().equals("domingo")) {
			logger.info(">>>>>> ehdomingo => true ");
			return true;
		} else {
			return false;
		}

	}
	/**
	 * verifica se a data esta no formato dia e mes e ano valido
	 * @param data
	 * @return true ou false ou uma exception
	 */
	public boolean isValida(String data) {
		if (data != null) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setLenient(false);
			try {
				df.parse(data);
				return true;
			} catch (ParseException ex) {
				logger.info(">>>>>> isValida false=> " + ex.getMessage());
				return false;
			}
		} else {
			return false;
		}
	}
	/**
	 * compara a data atual com a data do vencimento 
	 * @param dataAtual
	 * @param dataVencimento
	 * @return true se a data de venc eh maior que a data atual ou false ou uma exception
	 */
	public boolean dtVencMaiorDtAtual(String dataAtual, String dataVencimento) {
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
			DateTime dtAtual = formatter.parseDateTime(dataAtual);
			DateTime dtVenc = formatter.parseDateTime(dataVencimento);

			Days d = Days.daysBetween(dtAtual, dtVenc);
			if (d.getDays() >= 0) {
				logger.info(">>>>>> dataVencMaiorDataAtual => true ");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.info(">>>>>> data de venc maior que data atual erro nao esperado =>" + e.getMessage());
			throw new IllegalArgumentException("Data invalida");
		}
	}
	/**
	 * atribui o valor da fatura, verifica se o valor da fatura é valido
	 * @param v 
	 * @return o valor no formato float ou uma exception
	 */
	public float setValorFatura(String v) {
		try {
			float temp = Float.parseFloat(v);
			if (temp > 0) {
				return Float.parseFloat(v);
			} else {
				throw new IllegalArgumentException("Valor invalido"); // float < ou igual a zero
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Valor invalido"); //literal nao numerico
		}
	}

	/**
	 * atribui o valor do cnpj, verifica se o cnpj eh valido 
	 * @param cnpj
	 * @return o cnpj validado ou uma excpetion
	 */
	public String setCnpj(String cnpj) {
		if ((cnpj !=  null) && cnpjIsValido(cnpj)) {
			return cnpj;
		} else {
			logger.info(">>>>>> setCnpj invalido ");
			throw new IllegalArgumentException("CNPJ invalido");
		}

	}

	public String getServicoContratado() {
		return servicoContratado;
	}
	/**
	 * atribui o texto do servico contratado
	 * @param servico
	 * @return o texto validado ou uma exeption 
	 */
	public String setServicoContratado(String servico) {
		if ((servico == null) || (servico.isBlank())) {
			logger.info(">>>>>> setServicoContratado invalido ");
			throw new IllegalArgumentException("Descriminacao do servico invalido");
		} else {
			logger.info(">>>>>> setServicoContratado valido ");
			return servico;
		}
	}
	/**
	 * valida o cnpj
	 * @param cnpj no tipo texto sem formatacao
	 * @return true/false ou uma exception
	 */

	public boolean cnpjIsValido(String cnpj) {
		char dig13, dig14;
		int sm, i, r, num, peso;
		if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222")
				|| cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
				|| cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
				|| cnpj.equals("99999999999999") || (cnpj.length() != 14)) {
			return (false);
		}
		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try { // Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int) (cnpj.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}
			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (cnpj.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}
			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);
			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
				return (true);
			else
				return (false);
		}

		catch (InputMismatchException erro) {
			erro.printStackTrace();
			return (false);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataEmissao, dataVencimento, numero, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fatura other = (Fatura) obj;
		return Objects.equals(dataEmissao, other.dataEmissao) && Objects.equals(dataVencimento, other.dataVencimento)
				&& numero == other.numero && Float.floatToIntBits(valor) == Float.floatToIntBits(other.valor);
	}

}