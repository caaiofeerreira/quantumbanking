# Quantum Banking - Cliente
---

## Funcionalidade: Registro de Conta

O sistema permite que novos clientes registrem suas contas através de um endpoint REST. O processo de registro envolve a validação dos dados do cliente e a criação de uma conta associada.


### Endpoint

- **POST /quantumbanking/register/account**: Este endpoint é utilizado para registrar um novo cliente e sua conta. O corpo da requisição deve conter as informações necessárias, como nome, CPF, telefone, e-mail, senha, endereço, tipo de cliente, número da conta e número da agência.

### Respostas

- **Sucesso**: Retorna uma mensagem de confirmação quando a conta é registrada com sucesso.
- **Erro**: Se a agência não for encontrada ou se os dados forem inválidos, uma mensagem de erro será retornada.

``` json
{
  "name": "Ana Paula Silva",
  "cpf": "123.456.789-00",
  "phone": "(21) 98888-1234",
  "email": "ana.paula@exemplo.com",
  "password": "12345678",
  "address": {
    "logradouro": "Avenida das Flores",
    "numero": "150",
    "complemento": "Casa 2",
    "bairro": "Jardim Primavera",
    "cidade": "Rio de Janeiro",
    "estado": "RJ",
    "cep": "22222-333"
  },
  "clientType": "FISICA",
  "accountNumber": "54321-1",
  "agencyNumber": "002"
}
```
- complemento - campo não obrigatório.
___

## Funcionalidade: Autenticação de Cliente

O sistema permite que clientes autenticados acessem suas contas através de um endpoint REST para login. O processo de autenticação envolve a verificação das credenciais do cliente e a geração de um token JWT para acesso seguro.

### Endpoint

- **POST /quantumbanking/account/login**: Este endpoint é utilizado para autenticar um cliente. O corpo da requisição deve conter as informações necessárias, como CPF e senha.

### Requisição

```json
{
	"cpf": "123.456.789-00",
	"password": "12345678"
}
```
___

## Funcionalidade: Atualização de Perfil do Cliente

O sistema permite que clientes atualizem suas informações pessoais através de um endpoint REST. O processo de atualização envolve a validação dos dados e a persistência das alterações no banco de dados.

### Endpoint

- **PUT /quantumbanking/account/update-profile**: Este endpoint é utilizado para atualizar o perfil de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com as novas informações.

### Requisição

```json
{
	"address": {
		"logradouro": "Rua Nova",
		"numero": "100",
		"complemento": "Apto 202",
		"bairro": "Centro",
		"cidade": "São Paulo",
		"estado": "SP",
		"cep": "01000-000"
	}
}
```
- complemento - campo não obrigatório.
---

## Funcionalidade: Registro de Chave Pix

O sistema permite que clientes registrem suas chaves Pix através de um endpoint REST. O processo de registro envolve a validação da chave e a persistência da informação no banco de dados.

### Endpoint

- **POST /quantumbanking/account/pix/register**: Este endpoint é utilizado para cadastrar uma nova chave Pix para um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com a chave Pix.

### Requisição

```json
{
    "pixKey": "pix@example.com"
}
```
---

## Funcionalidade: Atualização de Chave Pix

O sistema permite que clientes atualizem suas chaves Pix através de um endpoint REST. O processo de atualização envolve a validação da nova chave e a persistência da informação no banco de dados.

### Endpoint

- **PUT /quantumbanking/account/pix/update**: Este endpoint é utilizado para atualizar uma chave Pix de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com a nova chave Pix.

### Requisição

```json
{
    "pixKey": "nova_pix@example.com"
}
```
---

## Funcionalidade: Verificação de Saldo da Conta

O sistema permite que clientes verifiquem o saldo de suas contas através de um endpoint REST. O processo de verificação envolve a validação do token e a consulta do saldo no banco de dados.

### Endpoint

- **GET /quantumbanking/account/balance**: Este endpoint é utilizado para consultar o saldo da conta de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT.

### Respostas

- **Sucesso**: Retorna o saldo da conta formatado em moeda brasileira (R$).

**Erro**:
- Se houver um problema ao processar a solicitação ou se o token não for válido, uma mensagem de erro será retornada.

### Exceções

- **UnauthorizedAccessException**: Lançada quando não é possível validar o token ou encontrar o saldo da conta.
---

## Funcionalidade: Processamento de Depósito

O sistema permite que clientes realizem depósitos em suas contas através de um endpoint REST. O processo de depósito envolve a validação do valor e a atualização do saldo no banco de dados.

### Endpoint

- **POST /quantumbanking/account/deposit**: Este endpoint é utilizado para processar um depósito na conta de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com o valor a ser depositado.

### Requisição

```json
{
    "amount": 100.00
}
```

### Resposta

```json
{
	"id": "4e2aad7a-766d-4d07-aeee-17b037bdb70a",
	"amount": 100.00,
	"createdAt": "2024-09-30T13:52:20.5423898"
}
```
---

## Funcionalidade: Processamento de Saque

O sistema permite que clientes realizem saques de suas contas através de um endpoint REST. O processo de saque envolve a validação do valor e a atualização do saldo no banco de dados.

### Endpoint

- **POST /quantumbanking/account/withdraw**: Este endpoint é utilizado para processar um saque na conta de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com o valor a ser sacado.

### Requisição

```json
{
    "amount": 10.00
}
```

### Resposta

```json
{
    "id": "4057562e-e333-4ed6-9bcf-ed0c2c1e32d1",
	"amount": 10.00,
	"createdAt": "2024-09-30T13:52:32.0723356"
}
```
---

## Funcionalidade: Transferência Interna entre Contas

O sistema permite que clientes realizem transferências internas entre suas contas através de um endpoint REST. O processo de transferência envolve a validação das contas de origem e destino, além da atualização dos saldos no banco de dados.

### Endpoint

- **POST /quantumbanking/account/transfer-internal**: Este endpoint é utilizado para processar uma transferência interna entre contas de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com os detalhes da transferência.

### Requisição

```json
{
    "accountDestiny": "12345-6",
    "amount": 10.00
}
```

### Resposta

```json
{
	"id": "3c2af12e-e2f1-4f6f-8c7e-4e717a58e91c",
	"accountOriginId": 2,
	"accountDestinyId": 1,
	"amount": 10.00,
	"type": "TRANSFER",
	"createdAt": "2024-09-30T13:53:01.6686737"
}

```
---

## Funcionalidade: Transferência Externa entre Contas

O sistema permite que clientes realizem transferências externas para contas de outros bancos através de um endpoint REST. O processo de transferência envolve a validação das informações fornecidas e a atualização do saldo no banco de dados.

### Endpoint

- **POST /quantumbanking/account/transfer-external**: Este endpoint é utilizado para processar uma transferência externa de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com os detalhes da transferência.

### Requisição

```json
{
    "name": "João Silva",
    "accountDestiny": "98765-4",
    "amount": 10.00,
    "accountType": "CORRENTE",
    "agencyNumber": "001",
    "bankCode": "123",
    "document": "123.456.789-01"
}
```

### Resposta

```json
{
	"id": "ab31df6b-9c24-4907-a5bb-2cd8a33c0cfb",
	"name": "João Silva",
	"accountOrigin": 2,
	"accountDestiny": "98765-4",
	"amount": 10.00,
	"transactionType": "TRANSFER",
	"agencyNumber": "001",
	"bankCode": "123",
	"document": "123.456.789-01",
	"accountType": "CORRENTE"
}

```
---

## Funcionalidade: Transferência via Pix

O sistema permite que clientes realizem transferências via Pix através de um endpoint REST. O processo de transferência envolve a validação da chave Pix do destinatário e a atualização dos saldos no banco de dados.

### Endpoint

- **POST /quantumbanking/account/pix**: Este endpoint é utilizado para processar uma transferência via Pix de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com os detalhes da transferência.

### Requisição

```json
{
  "pixType": "email",
  "pixKey": "ana.beatriz@exemplo.com",
  "amount": 10.00
}

```
---

## Funcionalidade: Solicitação de Empréstimo

O sistema permite que clientes solicitem empréstimos através de um endpoint REST. O processo de solicitação envolve a validação da conta do cliente e o cálculo do valor total do empréstimo com base nas taxas de juros aplicáveis.

### Endpoint

- **POST /quantumbanking/account/loan**: Este endpoint é utilizado para solicitar um empréstimo de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com os detalhes do pedido.

### Requisição

```json
{
    "amount": 1000.00,
    "installment": 10
}
```

### Resposta

```json
{
	"id": 1,
	"amount": 1000.00,
	"installment": 10,
	"totalLoanCost": 1100.00,
	"status": "PENDENTE"
}

```
---

## Funcionalidade: Solicitação de Fechamento de Conta

O sistema permite que clientes solicitem o fechamento de suas contas através de um endpoint REST. O processo de solicitação envolve a validação da conta e a atualização do seu status.

### Endpoint

- **POST /quantumbanking/account/account-closure**: Este endpoint é utilizado para solicitar o fechamento de uma conta de um cliente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT.

### Respostas

- **Sucesso**: Retorna um objeto que contém informações sobre a solicitação de fechamento registrada.

`Erro:`
- Se a conta não puder ser fechada devido a uma validação falha, uma mensagem de erro será retornada.

### Exceções

- **ValidateException**: Lançada quando a validação para fechamento da conta falha.
- **UnauthorizedAccessException**: Lançada quando a solicitação não é autorizada ou ocorre um erro durante o processamento.
