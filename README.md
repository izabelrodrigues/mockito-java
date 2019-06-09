# mockito-java
Projeto de estudos de teste com mockito

#### Anotação 1:
Quando você invoca um método no mock que não foi previamente ensinado a responder algo, o Mockito faz o seguinte:

* Se o método retorna um inteiro, double, ou um tipo primitivo qualquer, ele retornará 0
* Se o método retorna uma lista, o Mockito retornará uma lista vazia
* Se o método retorna uma outra classe qualquer, o Mockito retorna null.

#### Anotação 2:

* Para checar se algum método foi invocado, utilize verify. Exemplo: verficaSeMetodoAtualizarFoiInvocado() da classe EncerradorDeLeilaoTest. 
* Se foi invocado, utilize a opção times para garantir a quantidade de vezes da invocação. Em caso de testar se não foi invocado utilize a opção never. Exemplo com never: naoDeveEncerrarLeiloesQueComecaramOntem() da classe EncerradorDeLeilaoTest. 

Outras opções:
* O método atLeastOnce() vai garantir que o método foi invocado no mínimo uma vez. Ou seja, se ele foi invocado 1, 2, 3 ou mais vezes, o teste passa. Se ele não for invocado, o teste vai falhar.

* O método atLeast(numero) funciona de forma análoga ao método acima, com a diferença de que passamos para ele o número mínimo de invocações que um método deve ter.

* o método atMost(numero) nos garante que um método foi executado até no máximo N vezes. Ou seja, se o método tiver mais invocações do que o que foi passado para o atMost, o teste falha.

* Quando precisamos verificar mais de um mock em uma ordem específica podemos utilizar o InOrder.Exemplo comentado em:
verficaSeMetodoAtualizarFoiInvocado() da classe EncerradorDeLeilaoTest.


#### Anotação 3:

* Testar lançamento de exceções: doThrow
* Se quisermos verificar a execução de uma ação para qualquer objeto podemos utilizar o any

Exemplo: doThrow(new RuntimeException()).when(daoFalso).atualiza(any(Leilao.class));

#### Anotação 4:

* ArgumentCaptor possibilita capturar a instância que foi passada para o Mock