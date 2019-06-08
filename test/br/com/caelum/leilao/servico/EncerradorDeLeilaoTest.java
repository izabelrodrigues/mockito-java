package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;

public class EncerradorDeLeilaoTest {

	/**
	 * Cria massa de teste de leiloes Antigos
	 * 
	 * @return
	 */
	private List<Leilao> criaLeiloesAntigos() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
		return Arrays.asList(leilao1, leilao2);
	}

	/**
	 * Cria massa de teste de Leiloes do dia anterior
	 * 
	 * @return
	 */
	private List<Leilao> criaLeiloesDiaAnterior() {
		Calendar ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(ontem).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(ontem).constroi();

		return Arrays.asList(leilao1, leilao2);
	}

	@Test
	public void deveEncerrarLeiloesQueComecaramHaMaisDeUmaSemanaAtras() {

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesAntigos = criaLeiloesAntigos();

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);

		encerrador.encerra();

		assertTrue(leiloesAntigos.get(0).isEncerrado());
		assertTrue(leiloesAntigos.get(1).isEncerrado());
		assertEquals(2, encerrador.getTotalEncerrados());
	}

	@Test
	public void naoDeveEncerrarLeiloesQueComecaramOntem() {
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesDiaAnterior = criaLeiloesDiaAnterior();

		when(daoFalso.correntes()).thenReturn(leiloesDiaAnterior);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);

		encerrador.encerra();

		assertFalse(leiloesDiaAnterior.get(0).isEncerrado());
		assertFalse(leiloesDiaAnterior.get(1).isEncerrado());
		assertEquals(0, encerrador.getTotalEncerrados());

	}

	@Test
	public void naoDeveEncerrarListaDeLeiloesVazia() {

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);

		when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso);

		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());
	}

}
