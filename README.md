# Painel de Monitoramento de Hidrômetros (SHA)

Este projeto implementa uma solução centralizada para monitorar o consumo de água registrado por Simuladores de Hidrômetros Analógicos (SHAs), utilizando processamento de imagens (OCR) para leitura de dados.

## 📋 Status do Projeto (Barra de Progresso)

### 1. Documentação e Modelagem
- ✅ Especificação de Requisitos Final (PDF)
- ✅ Diagrama de Casos de Uso
- ✅ Diagrama de Classes e Arquitetura

### 2. Funcionalidades Principais (Core)
- ✅ **Configuração e Estrutura**: Estrutura Maven e dependências configuradas.
- ✅ **Leitura de Imagens (OCR)**: Integração com Tess4J, tratamento de imagem (pré-processamento) e filtro de ruído.
- ✅ **Agendador (Thread)**: Monitoramento automático de diretórios usando `ScheduledExecutorService`.
- ✅ **Mover Arquivos**: Lógica robusta para mover imagens processadas ou com erro (evita loops).

### 3. Gestão de Dados (Fachada e DAO)
- ✅ **Entidades**: Implementação de Usuario, Hidrometro e Leitura.
- ✅ **Persistência**: Implementação do padrão DAO com persistência em JSON (Gson) e adaptador para datas.
- ✅ **Fachada**: Implementação da classe `MonitoramentoFacade` centralizando a lógica.

### 4. Alertas e Notificações
- ✅ **Lógica de Alerta**: Verificação de limite de consumo (limite mensal vs. leitura).
- ✅ **Envio de E-mail**: Implementação do Padrão Observer para notificação de alertas via E-mail.

### 5. Interface e Entrega
- ✅ **CLI**: Interface de Linha de Comando (Menu) para cadastro e consultas.
- ✅ **Vídeo de Demonstração**: SHAs em funcionamento e detecção pelo painel.

---

## 🧩 Padrões de Projeto Utilizados

Este sistema foi arquitetado utilizando padrões de projeto clássicos para garantir desacoplamento e manutenibilidade. Abaixo estão os locais onde cada padrão foi aplicado:

### 1. Facade (Fachada)
* **Propósito:** Simplificar a interface de uso do sistema, escondendo a complexidade dos subsistemas de OCR, Banco de Dados, Agendamento e Notificações.
* **Localização:** `br.edu.ifpb.monitoramento.facade.MonitoramentoFacade`
* **Uso:** A classe `Main` e o `MenuConsole` interagem apenas com a Facade, sem conhecer as regras de negócio internas.

### 2. Adapter (Adaptador)
* **Propósito:** Isolar o sistema da biblioteca externa de OCR (Tess4J). Permite trocar a tecnologia de reconhecimento visual sem alterar o restante do código.
* **Localização:** `br.edu.ifpb.monitoramento.adapter.TesseractAdapter` (Implementa `ILeitorImagem`).
* **Uso:** Adapta a chamada da biblioteca Tesseract e adiciona pré-processamento de imagem (escala de cinza/zoom) para atender à interface esperada pelo sistema.

### 3. Observer (Observador)
* **Propósito:** Permitir que o sistema de monitoramento notifique interessados (como o módulo de envio de e-mails) quando um evento crítico ocorre (limite de consumo excedido), sem acoplamento rígido.
* **Localização:** `br.edu.ifpb.monitoramento.observer.EmailNotificador` (Implementa `IObservadorAlerta`).
* **Uso:** A Facade atua como o *Subject* notificando a lista de observadores quando uma leitura ultrapassa o limite configurado.

### 4. DAO (Data Access Object)
* **Propósito:** Abstrair e encapsular o acesso aos dados, separando a lógica de negócio da lógica de persistência (arquivo JSON).
* **Localização:** `br.edu.ifpb.monitoramento.dao.UsuarioArquivoDAO` (Implementa `UsuarioDAO`).
* **Uso:** Gerencia a leitura e escrita no arquivo `banco_usuarios.json`, utilizando adaptadores do Gson para tipos complexos (`LocalDateTime`).

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java JDK 17** ou superior.
* **Maven** (geralmente incluso no IntelliJ).
* **Conexão com a Internet** (para baixar as dependências do `pom.xml` na primeira execução).

### 🛠️ Configuração do Ambiente (Passo a Passo)

1. **Dependências Maven**:
    - Abra o projeto no IntelliJ.
    - Aguarde o Maven baixar as bibliotecas listadas no `pom.xml` (Tess4J, GSON, Commons Email).
    - Caso não baixe automaticamente, clique no ícone "Reload All Maven Projects" na aba lateral do Maven.

2. **Configuração do OCR (Tesseract)**:
    - O projeto requer um arquivo de treinamento para ler os números.
    - Crie uma pasta chamada `tessdata` na **raiz** do projeto (no mesmo nível do `pom.xml` e da pasta `src`).
    - Baixe o arquivo `eng.traineddata` (Recomendado para números) neste link oficial: [GitHub Tesseract Data](https://github.com/tesseract-ocr/tessdata/blob/main/eng.traineddata).
    - Coloque o arquivo `eng.traineddata` dentro da pasta `tessdata`.

3. **Ambiente de Simulação (SHAs)**:
    - O sistema monitora pastas locais. Certifique-se de que as pastas configuradas no cadastro do hidrômetro existam no seu computador.

### ▶️ Execução

1. Localize a classe principal: `src/main/java/br/edu/ifpb/monitoramento/Main.java`.
2. Clique com o botão direito no arquivo e selecione **"Run Main.main()"**.
3. O **Menu Interativo** aparecerá no console.
4. Utilize a **Opção 4** para iniciar o monitoramento em segundo plano.
5. **Para testar**: Copie uma imagem de hidrômetro (`.jpg`) para a pasta de um SHA cadastrado. O sistema detectará, processará e moverá o arquivo.

---

**Desenvolvido por:** Cefras Mandú

**Professor:** Katyusco Santos

**Disciplina:** Padrões de Projeto - Eng. de Computação - IFPB-CG