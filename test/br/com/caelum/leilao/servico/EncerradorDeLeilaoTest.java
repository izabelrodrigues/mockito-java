package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import br.com.caelum.leilao.infra.email.EnviadorDeEmail;

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
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();

		assertTrue(leiloesAntigos.get(0).isEncerrado());
		assertTrue(leiloesAntigos.get(1).isEncerrado());
		assertEquals(2, encerrador.getTotalEncerrados());
	}

	@Test
	public void naoDeveEncerrarLeiloesQueComecaramOntem() {
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesDiaAnterior = criaLeiloesDiaAnterior();
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		when(daoFalso.correntes()).thenReturn(leiloesDiaAnterior);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();

		assertFalse(leiloesDiaAnterior.get(0).isEncerrado());
		assertFalse(leiloesDiaAnterior.get(1).isEncerrado());
		assertEquals(0, encerrador.getTotalEncerrados());

		verify(daoFalso, never()).atualiza(leiloesDiaAnterior.get(0));
		verify(daoFalso, never()).atualiza(leiloesDiaAnterior.get(1));

	}

	@Test
	public void naoDeveEncerrarListaDeLeiloesVazia() {

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);

		when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());

		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());

	}

	@Test
	public void verficaSeMetodoAtualizarFoiInvocado() {
		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesAntigos = criaLeiloesAntigos();

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();

		/**
		 * Mocks a serem verificados
		 */
		InOrder inOrder = inOrder(daoFalso, carteiroFalso);

		Leilao leilao1 = leiloesAntigos.get(0);
		Leilao leilao2 = leiloesAntigos.get(1);

		/**
		 * Como a ordem dos mocks foi daoFalso e depois carteiroFalso, temos que fazer a
		 * verifica��o nessa mesma ordem. Caso contr�rio, o teste falhar�.
		 */
		inOrder.verify(daoFalso, times(1)).atualiza(leilao1);
		inOrder.verify(carteiroFalso, times(1)).envia(leilao1);

		inOrder.verify(daoFalso, times(1)).atualiza(leilao2);
		inOrder.verify(carteiroFalso, times(1)).envia(leilao2);

	}

	@Test
	public void deveContinuarAExecucaoMesmoQuandoDaoFalhaParaUmDosLeiloes() {

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesAntigos = criaLeiloesAntigos();

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		Leilao leilao1 = leiloesAntigos.get(0);
		Leilao leilao2 = leiloesAntigos.get(1);

		doThrow(new RuntimeException()).when(daoFalso).atualiza(leilao1);

		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();
		
		/**
		 * Foi lan�ado uma exce��o na atualiza��o do leilao1 mas o proximo leil�o tem que atualizar normalmente
		 */
		verify(daoFalso).atualiza(leilao2);
        verify(carteiroFalso).envia(leilao2);

	}
	
	@Test
	public void deveContinuarAExecucaoMesmoQuandoEnviadorDeEmailFalha() {

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesAntigos = criaLeiloesAntigos();

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		Leilao leilao1 = leiloesAntigos.get(0);
		Leilao leilao2 = leiloesAntigos.get(1);

		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		doThrow(new RuntimeException()).when(carteiroFalso).envia(leilao1);
		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();
		
		/**
		 * Foi lan�ado uma exce��o no envio de email do leilao1 mas o pr�ximo leil�o 
		 * tem que atualizar e enviar email normalmente
		 */
		verify(daoFalso).atualiza(leilao2);
        verify(carteiroFalso).envia(leilao2);

	}
	
	@Test
	public void deveContinuarAExecucaoMesmoQuandoDaoFalhaParaTodosLeiloes() {

		RepositorioDeLeiloes daoFalso = mock(RepositorioDeLeiloes.class);
		List<Leilao> leiloesAntigos = criaLeiloesAntigos();

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);

		doThrow(new RuntimeException()).when(daoFalso).atualiza(any(Leilao.class));
		
		EnviadorDeEmail carteiroFalso = mock(EnviadorDeEmail.class);

		EncerradorDeLeilao encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);

		encerrador.encerra();
		
		verify(carteiroFalso, never()).envia(any(Leilao.class));

	}

}
