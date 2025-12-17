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

### Criacionais
1. **Singleton**
   - **Local:** `MonitoramentoFacade`.
   - **Propósito:** Garante que apenas uma instância do motor de monitoramento exista, evitando conflitos de acesso aos arquivos e threads duplicadas.

2. **Factory Method**
   - **Local:** `UsuarioFactory`.
   - **Propósito:** Encapsula a lógica de criação de novos usuários, facilitando a manutenção caso novos tipos de usuários sejam adicionados.

### Estruturais
3. **Facade (Fachada)**
   - **Local:** `MonitoramentoFacade`.
   - **Propósito:** Simplifica a complexidade do sistema para a interface (Menu), unificando OCR, DAO e Alertas.

4. **Adapter (Adaptador)**
   - **Local:** `TesseractAdapter`.
   - **Propósito:** Adapta a biblioteca externa Tess4J para a interface `ILeitorImagem`, permitindo troca de tecnologia e pré-processamento.

### Comportamentais
5. **Observer (Observador)**
   - **Local:** `EmailNotificador`.
   - **Propósito:** Notifica automaticamente o módulo de e-mail quando o consumo excede o limite.

### Arquiteturais / Persistência
6. **DAO (Data Access Object)**
   - **Local:** `UsuarioArquivoDAO`.
   - **Propósito:** Abstrai a persistência em arquivo JSON, separando-a da regra de negócio.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Gerenciamento de Dependências:** Maven
* **OCR (Reconhecimento Óptico):** Tess4J (Wrapper do Tesseract)
* **Processamento de Imagem:** Java AWT / ImageIO
* **Persistência:** JSON (Google Gson)
* **E-mail:** Apache Commons Email

---

## 📂 Estrutura do Projeto

A organização dos pacotes segue estritamente os padrões de projeto implementados, garantindo alta coesão e baixo acoplamento:

```
br.edu.ifpb.monitoramento
├── adapter    # Padrão ADAPTER (TesseractAdapter)
├── dao        # Padrão DAO (UsuarioArquivoDAO)
├── facade     # Padrão FACADE (MonitoramentoFacade)
├── model      # Entidades do Domínio (Usuario, Hidrometro, Leitura)
├── observer   # Padrão OBSERVER (EmailNotificador)
├── view       # Interface CLI (MenuConsole)
└── Main.java  # Ponto de Entrada
```

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