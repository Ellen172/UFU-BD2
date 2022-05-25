--01-Nome e cidade de clientes que possuem algum emprestimo, ordenado pelo nome do cliente

SELECT DISTINCT cliente.nome_cliente, cliente.cidade_cliente
FROM emprestimo, cliente
WHERE cliente.nome_cliente=emprestimo.nome_cliente
ORDER BY cliente.nome_cliente;

--02-Nome e cidade de clientes que possuem emprestimo na PUC, ordenado pelo nome do cliente

SELECT DISTINCT cliente.nome_cliente, cliente.cidade_cliente
FROM cliente, emprestimo 
WHERE cliente.nome_cliente=emprestimo.nome_cliente
and emprestimo.nome_agencia='PUC'
ORDER BY cliente.nome_cliente;

--03-Nomes de Clientes com saldo entre 100 e 900

SELECT Cliente.nome_cliente
FROM Cliente, Deposito
WHERE Cliente.nome_cliente=Deposito.nome_cliente
AND Deposito.saldo_deposito between 100 and 900;

--04-Nomes de clientes, valores de depósitos e empréstimos na agencia PUC
SELECT D.nome_cliente, D.saldo_deposito, E.valor_emprestimo, E.nome_agencia
FROM deposito as D, emprestimo AS E
WHERE D.nome_agencia='PUC' 
and D.nome_agencia=E.nome_agencia 

﻿--05-Selecione os nomes dos clientes da cidade de Contagem com depósitos maiores do que R$ 3.000,00

select distinct cliente.nome_cliente, cliente.cidade_cliente, deposito.saldo_deposito
from cliente, deposito
where  cliente.cidade_cliente = 'Contagem'
and deposito.saldo_deposito > 3000;

--06-Um gerente pretende criar uma lista de clientes que correm o risco
--de se individar de maneira irreversível. Para tanto ele formulou
--a seguinte pesquisa:
--Selecione os clientes da cidade de Santa Luzia com depósitos
--menores do que R$ 1.000,00 e emprestimos maiores que R$ 1.000,00

select cliente.nome_cliente, cliente.cidade_cliente, 
       deposito.saldo_deposito, emprestimo.valor_emprestimo
from cliente, emprestimo, deposito
where cliente.nome_cliente = deposito.nome_cliente
and cliente.nome_cliente = emprestimo.nome_cliente 
and cliente.cidade_cliente = 'Santa Luzia'
and deposito.saldo_deposito < 1000
and emprestimo.valor_emprestimo > 1000


--07-Um gerente pretende criar uma lista de clientes que correm o risco
--de se individar de maneira irreversível. Para tanto ele formulou
--a seguinte pesquisa:
--Selecione os clientes da cidade de Santa Luzia com uma média de 
--depósitos menor do que a média de empréstimos

select c.nome_cliente , avg(d.saldo_deposito) as depositos , avg(e.valor_emprestimo) as emprestimos 
from cliente c , deposito d , emprestimo e 
where c.nome_cliente = d.nome_cliente
and c.nome_cliente = e.nome_cliente
and c.cidade_cliente = 'Santa Luzia'
group by c.nome_cliente 
having avg(d.saldo_deposito) < avg(e.valor_emprestimo)


--08-É preciso atualizar a informação do saldo do cliente na tabela cliente.
--para este propósito devemos levar em conta o saldo dos depósitos menos os
--saldos de empréstimos. o cálculo final deve ser armazenado na tabela conta.

UPDATE CONTA SET SALDO_CONTA = 0;

SELECT NOME_CLIENTE, SALDO_CONTA FROM CONTA ORDER BY SALDO_CONTA;

-- clientes que possuem emprestimos
select e.nome_cliente , e.numero_conta , e.nome_agencia  , sum(e.valor_emprestimo) as emprestimo
from emprestimo e 
group by e.nome_cliente , e.numero_conta , e.nome_agencia 
order by sum(e.valor_emprestimo)

-- cliente que possuem depositos
select d.nome_cliente , d.numero_conta , d.nome_agencia , sum(d.saldo_deposito) as deposito
from deposito d 
group by d.nome_cliente , d.numero_conta , d.nome_agencia 
order by sum(d.saldo_deposito)

-- update 1 = saldo deposito
update conta set saldo_conta = deposito.total
from 
	(select d.nome_cliente , d.numero_conta , d.nome_agencia , sum(d.saldo_deposito) as total
	from deposito d 
	group by d.nome_cliente , d.numero_conta , d.nome_agencia 
	order by sum(d.saldo_deposito)) 
	as deposito
where conta.nome_cliente = deposito.nome_cliente 
and conta.numero_conta = deposito.numero_conta 
and conta.nome_agencia = deposito.nome_agencia 

-- update 2 = menos emprestimo
update conta set saldo_conta = saldo_conta - emprestimo.total
from 
	(select e.nome_cliente , e.numero_conta , e.nome_agencia  , sum(e.valor_emprestimo) as total
	from emprestimo e 
	group by e.nome_cliente , e.numero_conta , e.nome_agencia 
	order by sum(e.valor_emprestimo))
	as emprestimo
where conta.nome_cliente = emprestimo.nome_cliente
and conta.numero_conta = emprestimo.numero_conta
and conta.nome_agencia = emprestimo.nome_agencia
