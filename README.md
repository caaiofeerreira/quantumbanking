# Quantum Banking
## Sistema Bancário em Java Spring Boot
___

<img src = "Quantum Banking - Logo.png" alt="Logo Quantum Banking">

Este projeto é um avanço significativo desde meu primeiro projeto em Java (CodeBank), que tinha algumas funcionalidades (consultar saldo, deposito, saque e transferência). Com o tempo e o aprofundamento dos meus conhecimentos, estou utilizando o Java Spring Boot para criar uma aplicação mais robusta e funcional. Estou aberto a feedback e sugestões da comunidade para aprimorar ainda mais o projeto.

### | Tecnologias Utilizadas:
- Java Spring Boot para desenvolvimento da aplicação.
- JWT para segurança.
- MySQL: Sistema de gerenciamento de banco de dados utilizado (via MySQL Connector/J).
- Lombok: Para simplificar o código e reduzir a verbosidade em classes Java.
- Springdoc OpenAPI: Para documentação automática da API usando OpenAPI.
- Commons Validator: Biblioteca para validação de dados.
___

### | Funcionalidades Implementadas:

- Registro de Clientes e Contas: Criação e gerenciamento de clientes e suas contas.
- Registro de Gerentes: Adição e gerenciamento de gerentes do banco.
- Autenticação com JWT: Sistema de autenticação seguro.
- Atualização de Perfil: Permite que clientes e gerentes atualizem suas informações.

`Cliente - Serviços:`
- Consultar Saldo: Permite ao cliente verificar o saldo disponível em sua conta de forma rápida e fácil.
- Depósito: O cliente pode realizar depósitos em sua conta, com opções para diferentes métodos.
- Saque: O cliente pode retirar valores de sua conta, com acesso a limites e condições de saque.
- Transferência Bancária: Disponibiliza opções para transferências internas (entre contas da mesma instituição) e externas (para outras instituições).
- Pix: Permite ao cliente realizar transações instantâneas utilizando a chave Pix, facilitando pagamentos e transferências.
- Registro e Atualização de Chave Pix: O cliente pode registrar uma nova chave Pix ou atualizar uma chave existente.
- Encerramento da Conta: O cliente pode solicitar o encerramento de sua conta de forma simples e rápida.


`Gerente:`
- O gerente tem acesso a todas as contas registradas em sua agência, facilitando a gestão.
- Gerenciar Pedidos de Encerramento: O gerente pode visualizar e gerenciar os pedidos de encerramento de contas feitos pelos clientes.
- Análise de Encerramento de Conta: O gerente avalia se o pedido de encerramento de uma conta deve ser aceito ou não, garantindo a conformidade com as políticas da agência.
- Acesso a Contas Encerradas: O gerente pode consultar informações sobre contas que já foram encerradas, permitindo um melhor acompanhamento e análise de históricos.
---

### | Novas Funcionalidades Implementadas:

`Cliente:`
- Solicitar Empréstimo: O cliente pode solicitar um empréstimo e ficará aguardando a aprovação do gerente.

`Gerente:`
- Visualizar Solicitações de Empréstimo: O gerente tem acesso a todos os pedidos de empréstimos feitos pelos clientes.
- Solicitações Pendentes: O gerente pode visualizar todas as solicitações de empréstimos que ainda estão aguardando aprovação.
- Solicitações Canceladas: O gerente pode acessar e revisar todos os pedidos de empréstimos que foram cancelados.
- Solicitações Aprovadas: O gerente pode visualizar todos os empréstimos que foram aprovados.
- Aprovar ou Rejeitar Empréstimos: O gerente tem a capacidade de aprovar ou rejeitar os pedidos de empréstimos com base nas informações fornecidas.