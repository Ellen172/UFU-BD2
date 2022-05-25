--01-Nomes das Ag�ncias com dep�sitos (nome pode aparecer repetido)

SELECT nome_agencia FROM deposito;

--02-Nomes das Ag�ncias com dep�sitos (nome sem repeti��o)

SELECT DISTINCT nome_agencia FROM deposito;

--03-Nomes de Clientes com dep�sitos e empr�stimos ao mesmo tempo;

SELECT DISTINCT nome_cliente FROM deposito
intersect 
SELECT DISTINCT nome_cliente FROM emprestimo

--04-Nomes de Clientes com dep�sitos e empr�stimos ao mesmo tempo na ag�ncia PUC;

SELECT DISTINCT nome_cliente FROM deposito WHERE nome_agencia= 'PUC'
intersect 
SELECT DISTINCT nome_cliente FROM emprestimo WHERE nome_agencia='PUC'

--Alternativa2 para incluir a condi��o uma �nica vez
SELECT DISTINCT nome_cliente FROM 
	(	SELECT DISTINCT nome_cliente, nome_agencia FROM deposito
		intersect 
		select DISTINCT nome_cliente, nome_agencia FROM emprestimo
	) AS relatorio
WHERE relatorio.nome_agencia = 'PUC'

--Alternativa3 para usar outro operador (IN)
SELECT DISTINCT nome_cliente FROM deposito WHERE nome_agencia= 'PUC'
AND nome_cliente in (SELECT DISTINCT nome_cliente FROM emprestimo WHERE nome_agencia='PUC');

--05-Nomes de Clientes com dep�sitos, mas sem empr�stimos na ag�ncia PUC;

SELECT nome_cliente FROM deposito WHERE nome_agencia='PUC'  
except 
SELECT nome_cliente from emprestimo WHERE nome_agencia='PUC'

SELECT DISTINCT nome_cliente from deposito WHERE nome_agencia='PUC' 
AND nome_cliente NOT IN (SELECT DISTINCT nome_cliente FROM emprestimo WHERE nome_agencia='PUC');

--06-Clientes que possuem dep�sitos ou empr�stimos na agencia da PUC

SELECT DISTINCT nome_cliente from deposito where nome_agencia='PUC'
UNION
SELECT DISTINCT nome_cliente FROM emprestimo WHERE nome_agencia='PUC'

--07-Nomes de Clientes de Belo Horizonte

SELECT nome_cliente
FROM Cliente
WHERE cidade_cliente='Belo Horizonte';

--08-Nomes de clientes que tem 'Santos' no nome

SELECT nome_cliente from cliente
WHERE nome_cliente LIKE '%Santos%';

--09-Nomes de clientes que tem terminam com 'Souza' ou 'Sousa'

SELECT nome_cliente
FROM cliente
WHERE nome_cliente like '%Sou_a';

--10-Total de dep�sitos para cada nome de cliente

SELECT nome_cliente, sum(1)
FROM deposito GROUP BY nome_cliente;

--11-Nomes de clientes e dep�sitos que est�o entre 
--R$ 3.000,00 e R$ 4.000,00

SELECT nome_cliente, saldo_deposito
FROM deposito 
WHERE saldo_deposito >= 3000 
AND  saldo_deposito <= 4000

SELECT nome_cliente, saldo_deposito
FROM deposito 
WHERE saldo_deposito BETWEEN 3000 and 4000

--12-Nomes de clientes e a soma de dep�sitos de cada nome, 
--quando a soma for maior que R$ 5.000,00

SELECT nome_cliente, sum(saldo_deposito)
FROM deposito GROUP BY nome_cliente
HAVING sum(saldo_deposito) > 5000

--13-Nomes de clientes e a soma de dep�sitos de cada nome,
--quando a soma estiver entre R$ 1.000,00 e R$ 4.000,00,
--em ordem pela soma

SELECT nome_cliente, sum(saldo_deposito)
FROM deposito group by nome_cliente
HAVING sum(saldo_deposito) BETWEEN 1000 AND 4000
ORDER BY sum(saldo_deposito)


--14-Nomes de clientes e a soma de dep�sitos de cada nome,
--quando a soma estiver maior do que a m�dia de dep�sitos,
--em ordem pela soma

SELECT nome_cliente, sum(saldo_deposito)
FROM deposito GROUP BY nome_cliente
HAVING sum(saldo_deposito) > (select avg(saldo_deposito) from deposito)
ORDER BY sum(saldo_deposito)


--15-Fa�a uma listagem da soma de dep�sitos feitos
--por cada cliente em cada ag�ncia. Ordene o resultado
--pelo nome do cliente e depois pelo nome da ag�ncia.

SELECT nome_agencia, nome_cliente, sum(saldo_deposito)
FROM deposito 
GROUP BY nome_cliente, nome_agencia
ORDER BY nome_cliente, nome_agencia

--16- Selecione os nomes dos clientes e a soma de dep�sitos
--feitos por eles em cada ag�ncia que foram maiores que todos
--os dep�sitos efetuados na ag�ncia 'Pampulha'.

--Conjunto1:
SELECT nome_cliente, nome_agencia, sum(saldo_deposito) 
FROM deposito GROUP BY nome_cliente, nome_agencia 

--Conjunto2: 
SELECT sum(saldo_deposito) FROM deposito 
WHERE nome_agencia='Pampulha' GROUP BY nome_agencia

--Conjunto1 HAVING sum > ALL Conjunto2
SELECT nome_cliente, nome_agencia, sum(saldo_deposito) 
FROM deposito GROUP BY nome_cliente, nome_agencia 
HAVING sum(saldo_deposito) > ALL
	(SELECT sum(saldo_deposito) FROM deposito 
	WHERE nome_agencia='Pampulha' GROUP BY nome_agencia);