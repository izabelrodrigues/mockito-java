# mockito-java
Projeto de estudos de teste com mockito

#### Anotação 1:
Quando você invoca um método no mock que não foi previamente ensinado a responder algo, o Mockito faz o seguinte:

* Se o método retorna um inteiro, double, ou um tipo primitivo qualquer, ele retornará 0
* Se o método retorna uma lista, o Mockito retornará uma lista vazia
* Se o método retorna uma outra classe qualquer, o Mockito retorna null.