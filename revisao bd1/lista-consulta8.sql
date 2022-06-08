--IMPLEMENTE UM GATILHO (TRIGGER) QUE ATUALIZE A TABELA CONTA, PARA O CAMPO SALDO_CONTA, 
--SEMPRE QUE UMA NOVA LINHA FOR INSERIDA NA TABELA DE DEPÓSITO OU EMPRÉSTIMO.

select * from conta;

create trigger nova_linha
on conta, emprestimo
for update 
as 
begin 
	declare
	saldo_conta int;
	select d.saldo_deposito, e.valor_emprestimo 
		from deposito d natural full join emprestimo e; 
	update conta set saldo_conta = ;
end
