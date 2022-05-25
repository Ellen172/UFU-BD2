--Fun��es de agrega��o:

--Encontre a soma total de dep�sitos para cada cliente

select nome_cliente, sum(saldo_deposito) 
from deposito
group by nome_cliente

--Encontre a soma total de dep�sitos para cada cliente 
--e ordene pela ordem descendente dos dep�sitos

select nome_cliente, sum(saldo_deposito) 
from deposito
group by nome_cliente
order by sum(saldo_deposito) desc 

--Conte quantos dep�sitos realizou cada cliente

select nome_cliente, count(1)
from deposito
group by nome_cliente

--Conte quantos dep�sitos realizou cada cliente e ordene 
--pelo ordem descendente da quantidade e depositos

select nome_cliente, count(1)
from deposito
group by nome_cliente
order by count(1) desc

--Encontre a soma total de dep�sitos para cada cliente,
--e tamb�m a quantidade de dep�sitos. Mostre o resultado
--ordenado primeiro pela ordem ascendente da quantidade de dep�sitos
--e depois pela soma total descendente de dep�sitos de cada cliente

select nome_cliente, sum(saldo_deposito), count(1) 
from deposito
group by nome_cliente
order by count(1), sum(saldo_deposito) desc

--Encontre o n�mero de depositantes em cada ag�ncia
select nome_agencia, count(nome_cliente)
from deposito
group by nome_agencia

--Mas a cl�usula anterior est� errada porque um cliente
--pode fazer mais de um dep�sito por agencia. Veja:
select nome_agencia, nome_cliente, count(nome_cliente)
from deposito group by nome_agencia, nome_cliente

--Solu��o:
select nome_agencia, count(distinct nome_cliente)
from deposito
group by nome_agencia 

--Encontre o saldo m�dio de dep�sitos de cada ag�ncia

select nome_agencia, avg(saldo_deposito)
from deposito
group by nome_agencia

--Encontre o saldo m�dio de dep�sitos de cada ag�ncia
--mas mostre apenas as agencias e saldo m�dio que forem
--maiores do que R$ 1.200,00

select nome_agencia, avg(saldo_deposito)
from deposito
group by nome_agencia
having  avg(saldo_deposito)> 1200

--Selecione o valor do maior dep�sito

select max(saldo_deposito)
from deposito

--Selecione o valor do maior, da m�dia e do menor dep�sito

select max(saldo_deposito), avg(saldo_deposito), min(saldo_deposito)
from deposito

--Selecione o valor do maior, da m�dia 
--e do menor dep�sito, todos por agencia

select nome_agencia, max(saldo_deposito), 
avg(saldo_deposito), min(saldo_deposito)
from deposito
group by nome_agencia


--Selecione o nome do cliente que fez o maior deposito

select nome_cliente, saldo_deposito
from deposito
where saldo_deposito = (select max(saldo_deposito) from deposito)


