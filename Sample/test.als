module banco

some sig Banco{
	contas: some Conta

}

abstract sig Conta{}

sig ContaCorrente, ContaPoupanca extends Conta{}

fact bankDetails{
	all c:Conta | one  c.~contas
	all b:Banco | #b.contas = 0
}

pred show[]{}

run show for 5
