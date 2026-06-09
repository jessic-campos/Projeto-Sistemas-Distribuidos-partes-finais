# Trabalho 3 - Pixel Art API

Reimplementacao do servico remoto de Pixel Art do Trabalho 2 usando API HTTP/JSON.

## Estrutura

- `servidor-api`: servidor Java com API HTTP/JSON.
- `cliente-java`: cliente em Java usando `HttpClient`.
- `cliente-python`: cliente em Python usando `urllib.request`.

## Requisitos atendidos

- Nao usa sockets diretamente no codigo da aplicacao cliente. A comunicacao e feita via API HTTP.
- Nao usa RMI.
- Servidor em projeto separado dos clientes.
- Tres objetos distribuidos no servidor:
  - `MuralResource`
  - `PixelResource`
  - `FerramentaResource`
- Entidades: `Pixel`, `Coordenada`, `Usuario`, `Artista`, `Administrador`, `Mural`, `Ferramenta`, `Pincel`, `Borracha`.
- Agregacao:
  - `Mural` tem `Usuario`.
  - `Mural` tem uma lista de `Pixel`.
  - `Pixel` tem `Coordenada`.
- Heranca:
  - `Artista` e `Administrador` sao `Usuario`.
  - `Pincel` e `Borracha` sao `Ferramenta`.
- Metodos remotos via API:
  - `POST /mural`
  - `POST /pixel`
  - `DELETE /pixel/{x}/{y}`
  - `GET /pixels`
  - `POST /pincel`
  - `POST /borracha`
  - `GET /mural?ansi=true`

## Como rodar no Windows / VS Code

Abra um terminal na pasta `servidor-api`:

```bash
mkdir bin
javac -encoding UTF-8 -d bin src\*.java
java -cp bin ServidorAPI
```

Em outro terminal, rode o cliente Java:

```bash
cd cliente-java
mkdir bin
javac -encoding UTF-8 -d bin src\*.java
java -cp bin ClienteJavaAPI
```

Em outro terminal, rode o cliente Python:

```bash
cd cliente-python
python cliente_python_api.py
```

## Endpoints da API

### Criar mural

`POST /mural`

```json
{"largura":10,"altura":10,"dono":"Victor"}
```

### Pintar pixel

`POST /pixel`

```json
{"x":3,"y":3,"cor":"AZUL"}
```

### Apagar pixel

`DELETE /pixel/3/3`

### Listar pixels

`GET /pixels`

### Aplicar pincel

`POST /pincel`

```json
{"x":1,"y":1,"cor":"VERMELHO"}
```

### Aplicar borracha

`POST /borracha`

```json
{"x":1,"y":1}
```

### Visualizar mural

`GET /mural?ansi=true`

## Observacao sobre cores ANSI

O mural usa blocos coloridos ANSI. No Windows, caso nao aparecam cores, use o terminal do VS Code, PowerShell moderno ou Windows Terminal.
