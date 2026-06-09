# Trabalho 4 - Pixel Art com Comunicação Indireta

Disciplina: Sistemas Distribuídos  
Tema: Evolução do sistema Pixel Art API usando Comunicação Indireta  
Abordagem escolhida: **Opção C - Filas de Mensagens (Message Queues)**

## 1. Ideia da arquitetura

Nos trabalhos anteriores o sistema usava comunicação direta:

```text
Cliente Java/Python -> API -> Mural
```

No Trabalho 4, a arquitetura foi alterada para comunicação indireta usando uma fila persistente:

```text
Cliente Java/Python -> ServidorAPI -> Fila Persistente -> WorkerFila -> Mural
```

Agora a API não altera o mural diretamente. Ela apenas publica eventos em uma fila persistente. O componente `WorkerFila` consome os eventos de forma assíncrona e aplica as alterações no mural.

## 2. Componentes

### ServidorAPI

Processo produtor. Recebe requisições HTTP/JSON dos clientes e publica eventos na fila.

Endpoints:

| Método | Endpoint | Função |
|---|---|---|
| POST | `/mural` | Enfileira criação de mural |
| POST | `/pixel` | Enfileira pintura de pixel |
| DELETE | `/pixel/{x}/{y}` | Enfileira remoção de pixel |
| POST | `/pincel` | Enfileira pintura em área |
| POST | `/borracha` | Enfileira uso da borracha |
| GET | `/mural?ansi=true` | Consulta estado atual do mural |
| GET | `/pixels` | Lista pixels já processados |
| GET | `/fila/status` | Mostra quantidade de mensagens pendentes |

### FilaPersistente

Intermediário da comunicação indireta.

Arquivo usado:

```text
servidor-api/dados/fila_eventos.txt
```

Cada linha do arquivo representa um evento pendente em JSON. Isso garante que os eventos continuem salvos mesmo se o WorkerFila estiver desligado.

### WorkerFila

Processo consumidor. Lê os eventos da fila, aplica no mural e salva o estado em disco.

Arquivo de estado:

```text
servidor-api/dados/mural_estado.ser
```

## 3. Como executar

### 1. Compilar o servidor

```bash
cd servidor-api
javac -encoding UTF-8 -d bin src\*.java
```

No Linux/Mac:

```bash
javac -encoding UTF-8 -d bin src/*.java
```

### 2. Iniciar o ServidorAPI

```bash
java -cp bin ServidorAPI
```

### 3. Iniciar o WorkerFila em outro terminal

```bash
cd servidor-api
java -cp bin WorkerFila
```

### 4. Executar cliente Java

```bash
cd cliente-java
javac -encoding UTF-8 -d bin src\*.java
java -cp bin ClienteJavaAPI
```

### 5. Executar cliente Python

```bash
cd cliente-python
python cliente_python_api.py
```

## 4. Demonstração do desacoplamento temporal

Para demonstrar o requisito principal da fila de mensagens:

1. Inicie apenas o `ServidorAPI`.
2. Deixe o `WorkerFila` desligado.
3. Use o cliente Java ou Python para criar mural, pintar pixel, aplicar pincel etc.
4. Consulte a opção `8 - Status da fila`.
5. As mensagens devem ficar pendentes.
6. Agora inicie o `WorkerFila`.
7. O worker consumirá os eventos pendentes e atualizará o mural.
8. Consulte o mural no cliente com a opção `7 - Visualizar mural`.

Isso prova que o produtor consegue enviar mensagens mesmo sem o consumidor estar ativo.

## 5. Justificativa técnica

A opção escolhida foi **Message Queue**, pois ela se encaixa bem no sistema Pixel Art. As ações dos usuários, como criar mural, pintar pixel, aplicar pincel e apagar pixel, podem ser tratadas como eventos. Esses eventos são enviados para uma fila e processados depois.

A fila reduz o acoplamento entre quem envia a ação e quem processa. O cliente e a API não precisam conhecer o WorkerFila. A API apenas salva eventos na fila, e o Worker consome quando estiver disponível.

## 6. Análise de acoplamento e falhas

### Desacoplamento espacial

O cliente conhece apenas a API. A API não chama diretamente o WorkerFila por IP ou porta. A comunicação entre produtor e consumidor acontece por meio do arquivo de fila persistente.

### Desacoplamento temporal

Se o WorkerFila estiver desligado, os eventos continuam sendo aceitos pela API e ficam armazenados no arquivo `fila_eventos.txt`. Quando o WorkerFila volta, ele lê os eventos pendentes e os processa.

### Robustez

A fila é persistente em arquivo, então as mensagens não ficam apenas em memória. Caso o Worker seja finalizado, os eventos ainda não processados permanecem na fila.

### Overhead

A comunicação indireta adiciona custo de escrita e leitura em disco. Isso torna o processamento um pouco mais lento do que atualizar o mural diretamente. Porém, em troca, o sistema ganha resiliência, desacoplamento e capacidade de processamento assíncrono.

Uma forma de mitigar esse overhead seria processar eventos em lote, usar um broker real como RabbitMQ/ActiveMQ/Kafka ou manter cache em memória sincronizado com disco.

## 7. Relação com os requisitos do Trabalho 4

- Abordagem escolhida: Opção C - Filas de Mensagens.
- Produtores: clientes Java/Python e ServidorAPI.
- Intermediário: FilaPersistente.
- Consumidor: WorkerFila.
- Resiliência: mensagens aguardam em arquivo quando o consumidor está offline.
- Desacoplamento temporal: API aceita eventos sem Worker ativo.
- Desacoplamento espacial: API e Worker se comunicam pela fila, não por chamada direta.
