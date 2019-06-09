# mockito-java
Projeto de estudos de teste com mockito

#### Anota��o 1:
Quando voc� invoca um m�todo no mock que n�o foi previamente ensinado a responder algo, o Mockito faz o seguinte:

* Se o m�todo retorna um inteiro, double, ou um tipo primitivo qualquer, ele retornar� 0
* Se o m�todo retorna uma lista, o Mockito retornar� uma lista vazia
* Se o m�todo retorna uma outra classe qualquer, o Mockito retorna null.

#### Anota��o 2:

* Para checar se algum m�todo foi invocado, utilize verify. Exemplo: verficaSeMetodoAtualizarFoiInvocado() da classe EncerradorDeLeilaoTest. 
* Se foi invocado, utilize a op��o times para garantir a quantidade de vezes da invoca��o. Em caso de testar se n�o foi invocado utilize a op��o never. Exemplo com never: naoDeveEncerrarLeiloesQueComecaramOntem() da classe EncerradorDeLeilaoTest. 

Outras op��es:
* O m�todo atLeastOnce() vai garantir que o m�todo foi invocado no m�nimo uma vez. Ou seja, se ele foi invocado 1, 2, 3 ou mais vezes, o teste passa. Se ele n�o for invocado, o teste vai falhar.

* O m�todo atLeast(numero) funciona de forma an�loga ao m�todo acima, com a diferen�a de que passamos para ele o n�mero m�nimo de invoca��es que um m�todo deve ter.

* o m�todo atMost(numero) nos garante que um m�todo foi executado at� no m�ximo N vezes. Ou seja, se o m�todo tiver mais invoca��es do que o que foi passado para o atMost, o teste falha.

* Quando precisamos verificar mais de um mock em uma ordem espec�fica podemos utilizar o InOrder.Exemplo comentado em:
verficaSeMetodoAtualizarFoiInvocado() da classe EncerradorDeLeilaoTest.


#### Anota��o 3:

* Testar lan�amento de exce��es: doThrow
* Se quisermos verificar a execu��o de uma a��o para qualquer objeto podemos utilizar o any

Exemplo: doThrow(new RuntimeException()).when(daoFalso).atualiza(any(Leilao.class));

#### Anota��o 4:

* ArgumentCaptor possibilita capturar a inst�ncia que foi passada para o Mock