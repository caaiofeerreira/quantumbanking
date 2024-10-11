# Quantum Banking - Gerente
---
## Funcionalidade: Registro de Gerente

O sistema permite que novos gerentes registrem suas contas através de um endpoint REST. O processo de registro envolve a validação dos dados do gerente e a criação de uma conta associada.

### Endpoint

- **POST /quantumbanking/register/manager**: Este endpoint é utilizado para registrar um novo gerente. O corpo da requisição deve conter as informações necessárias, como nome, CPF, telefone, e-mail, senha, endereço e número da agência.

### Requisição

```json
{
  "name": "Carlos Eduardo",
  "cpf": "123.456.789-00",
  "phone": "(21) 98888-1234",
  "email": "carlos.eduardo@exemplo.com",
  "password": "senhaSegura123",
  "address": {
    "logradouro": "Rua das Palmeiras",
    "numero": "200",
    "complemento": "Apto 12",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01010-010"
  },
  "agencyNumber": "003"
}
```
- complemento - campo não obrigatório.

### Resposta

- Sua conta foi registrada com sucesso.


`Erro:` Se a agência não for encontrada ou se os dados forem inválidos, uma mensagem de erro será retornada.

``` json
{
  "error": "Agência não encontrada. Agências registradas: 001, 002, 003"
}

```

``` json
{
  "error": "Dados inválidos. Por favor, verifique as informações e tente novamente."
}

```

---

## Funcionalidade: Login de Gerente

O sistema permite que gerentes se autentiquem através de um endpoint REST. O processo de login envolve a verificação das credenciais e a geração de um token JWT para acesso.

### Endpoint

- **POST /quantumbanking/manager/login**: Este endpoint é utilizado para autenticar um gerente. O corpo da requisição deve conter as informações necessárias, como CPF e senha.

### Requisição

```json
{
  "cpf": "123.456.789-00",
  "password": "senhaSegura123"
}
```

### Resposta

```json
{
  "token": "seu.token.jwt.aqui"
}

```

`Erro:`
```json
{
  "error": "Credenciais inválidas fornecidas. Verifique seu login e senha e tente novamente."
}
```
---

## Funcionalidade: Atualização de Perfil do Gerente

O sistema permite que gerentes atualizem suas informações pessoais através de um endpoint REST. O processo de atualização envolve a validação dos dados e a persistência das alterações no banco de dados.

### Endpoint

- **PUT /quantumbanking/account/update-profile**: Este endpoint é utilizado para atualizar o perfil de um gerente autenticado. A requisição deve incluir um cabeçalho de autorização com um token JWT e um corpo com as novas informações.

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

## Funcionalidade: Consultar Solicitações de Empréstimos

O sistema permite que gerentes consultem as solicitações de empréstimos através de um endpoint REST. Esse endpoint é protegido, requerendo a autenticação adequada.

### Endpoint

- **GET /quantumbanking/dashboard/loan/requests**: Este endpoint é utilizado para obter as solicitações de empréstimos feitas pelas contas associadas à agência do gerente autenticado.


### Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de solicitações de empréstimos.

```json
[
  {
    "id": "1",
    "accountId": "12345",
    "amount": 10000,
    "status": "PENDING",
    "requestedAt": "2024-10-01T12:00:00Z"
  },
  {
    "id": "2",
    "accountId": "67890",
    "amount": 5000,
    "status": "APPROVED",
    "requestedAt": "2024-10-02T15:30:00Z"
  }
]
```
`Erro:`

```json
{
  "error": "A agência ainda não possui contas cadastradas."
}
```
```json
{
  "error": "Nenhum pedido de empréstimo registrado."
}
```
```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```
---


## Funcionalidade: Consultar Contas de Clientes

O sistema permite que gerentes consultem as contas de clientes associadas à sua agência através de um endpoint REST. Este endpoint é protegido e requer autenticação.

### Endpoint

- **GET /quantumbanking/dashboard/accounts**: Este endpoint é utilizado para obter a lista de contas de clientes associadas à agência do gerente autenticado.


### Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de contas de clientes.

```json
[
  {
    "id": "1",
    "accountNumber": "123456",
    "clientName": "João Silva",
    "balance": 1500.00,
    "status": "ATIVA"
  },
  {
    "id": "2",
    "accountNumber": "789012",
    "clientName": "Maria Oliveira",
    "balance": 2500.00,
    "status": "ATIVA"
  }
]
```

`Erro:`

```json
{
  "error": "A agência ainda não possui clientes cadastrados."
}
```

```json
{
  "error": "Erro ao obter contas"
}
```
---

## Funcionalidade: Consultar Solicitações de Encerramento de Contas

O sistema permite que gerentes consultem as solicitações de encerramento de contas de clientes através de um endpoint REST. Este endpoint é protegido e requer autenticação.

### Endpoint

- **GET /quantumbanking/dashboard/accounts-closure**: Este endpoint é utilizado para obter a lista de contas de clientes que possuem pedidos de encerramento pendente.


### Cabeçalho da Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de contas com pedidos de encerramento pendente.

```json
[
  {
    "request": 1,
    "accountDto": {
      "id": 2,
      "number": "12345-3",
      "balance": 0.00,
      "agencyNumber": "001"
    }
  },
  {
    "request": 2,
    "accountDto": {
      "id": 4,
      "number": "12345-5",
      "balance": 0.00,
      "agencyNumber": "001"
    }
  }
]
```

`Erro:`

```json
{
  "error": "Nenhuma conta com pedido de encerramento pendente foi encontrada."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```
---

## Funcionalidade: Finalizar Solicitações de Encerramento de Contas

O sistema permite que gerentes finalizem solicitações de encerramento de contas de clientes através de um endpoint REST. Este endpoint é protegido e requer autenticação.

### Endpoint

- **PUT /quantumbanking/dashboard/finalizer-account**: Este endpoint é utilizado para aprovar ou reprovar um pedido de encerramento de conta.


### Cabeçalho da Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Corpo da Requisição

O corpo da requisição deve seguir a seguinte estrutura JSON:

```json
{
  "id": "1",
  "approve": true
}
```

### Resposta
- Sucesso: Retorna um status 200 OK quando a operação é realizada com sucesso.

`Erro:`

```json
{
  "error": "Pedido de encerramento não encontrado."
}
```

```json
{
  "error": "Conta associada ao pedido não encontrada."
}
```

```json
{
  "error": "Você não tem permissão para acessar esta conta."
}
```

```json
{
  "error": "Operação inválida: somente contas com status PENDENTE podem ser atualizadas."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```
---

## Funcionalidade: Consultar Contas Encerradas de Clientes

O sistema permite que gerentes consultem as contas encerradas de clientes associadas à sua agência através de um endpoint REST. Este endpoint é protegido e requer autenticação.

### Endpoint

- **GET /quantumbanking/dashboard/closed-accounts**: Este endpoint é utilizado para obter a lista de contas de clientes que foram encerradas.


### Cabeçalho da Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de contas encerradas.

```json
[
  {
    "id": "1",
    "accountNumber": "123456",
    "clientName": "João Silva",
    "closureDate": "2024-10-01T12:00:00Z",
    "status": "ENCERRADA"
  },
  {
    "id": "2",
    "accountNumber": "789012",
    "clientName": "Maria Oliveira",
    "closureDate": "2024-10-02T15:30:00Z",
    "status": "ENCERRADA"
  }
]
```

`Erro:`

```json
{
  "error": "Nenhuma conta encerrada foi encontrada."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```

---

## Funcionalidade: Consultar Empréstimos Pendentes

O sistema permite que gerentes consultem as solicitações de empréstimos pendentes através de um endpoint REST. Esse endpoint é protegido e requer autenticação.

### Endpoint

- **GET /quantumbanking/dashboard/loan/pending**: Este endpoint é utilizado para obter as solicitações de empréstimos que estão pendentes, feitas pelas contas associadas à agência do gerente autenticado.

### Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de solicitações de empréstimos pendentes.

```json
[
  {
    "id": "1",
    "accountId": "12345",
    "amount": 10000,
    "status": "PENDENTE",
    "requestedAt": "2024-10-01T12:00:00Z"
  },
  {
    "id": "2",
    "accountId": "67890",
    "amount": 5000,
    "status": "PENDENTE",
    "requestedAt": "2024-10-02T15:30:00Z"
  }
]
```

`Erro:`
```json
{
  "error": "A agência ainda não possui contas cadastradas."
}
```
```json
{
  "error": "Nenhum pedido de empréstimo PENDENTE registrado."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}

```
---

## Funcionalidade: Consultar Empréstimos Cancelados

O sistema permite que gerentes consultem as solicitações de empréstimos cancelados através de um endpoint REST. Esse endpoint é protegido e requer autenticação.

### Endpoint

- **GET /quantumbanking/dashboard/loan/canceled**: Este endpoint é utilizado para obter as solicitações de empréstimos que foram canceladas, feitas pelas contas associadas à agência do gerente autenticado.

### Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de solicitações de empréstimos cancelados.

```json
[
  {
    "id": "1",
    "accountId": "12345",
    "amount": 10000,
    "status": "CANCELADO",
    "requestedAt": "2024-10-01T12:00:00Z"
  },
  {
    "id": "2",
    "accountId": "67890",
    "amount": 5000,
    "status": "CANCELADO",
    "requestedAt": "2024-10-02T15:30:00Z"
  }
]
```

`Erro:`
```json
{
  "error": "A agência ainda não possui contas cadastradas."
}
```

```json
{
  "error": "Nenhum pedido de empréstimo CANCELADO registrado."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```
---

## Funcionalidade: Consultar Empréstimos Aprovados

O sistema permite que gerentes consultem as solicitações de empréstimos aprovados através de um endpoint REST. Este endpoint é protegido e requer autenticação.

### Endpoint

- **GET /quantumbanking/dashboard/loan/approved**: Este endpoint é utilizado para obter as solicitações de empréstimos que foram aprovadas, feitas pelas contas associadas à agência do gerente autenticado.

### Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Respostas

- **Sucesso**: Retorna uma lista de solicitações de empréstimos aprovados.

```json
[
  {
    "id": "1",
    "accountId": "12345",
    "amount": 10000,
    "status": "APROVADO",
    "requestedAt": "2024-10-01T12:00:00Z"
  },
  {
    "id": "2",
    "accountId": "67890",
    "amount": 5000,
    "status": "APROVADO",
    "requestedAt": "2024-10-02T15:30:00Z"
  }
]
```

`Erro:`
```json
{
  "error": "A agência ainda não possui contas cadastradas."
}
```

```json
{
  "error": "Nenhum pedido de empréstimo APROVADO registrado."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```
---

## Funcionalidade: Aprovar Solicitações de Empréstimos

O sistema permite que gerentes aprovem solicitações de empréstimos através de um endpoint REST. Este endpoint é protegido e requer autenticação.

### Endpoint

- **PUT /quantumbanking/dashboard/loan/approve**: Este endpoint é utilizado para aprovar ou cancelar um pedido de empréstimo específico.


### Cabeçalho da Requisição

- **Authorization**: Token JWT do gerente autenticado.

### Requisição

```json
{
  "id": "1",
  "approve": true
}
```

### Resposta
- Sucesso: Retorna um status 200 OK quando a operação é realizada com sucesso.

`Erro:`

```json
{
  "error": "Pedido de empréstimo não encontrado."
}
```

```json
{
  "error": "Conta do cliente não encontrada."
}
```

```json
{
  "error": "Você não tem permissão para acessar esta conta."
}
```

```json
{
  "error": "Operação inválida: somente empréstimos com status PENDENTE podem ser atualizados."
}
```

```json
{
  "error": "Erro ao processar o token ou verificar permissões."
}
```