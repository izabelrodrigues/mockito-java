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