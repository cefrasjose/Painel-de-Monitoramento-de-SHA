# Painel de Monitoramento de Hidrômetros (SHA)

Este projeto implementa uma solução centralizada para monitorar o consumo de água registrado por Simuladores de Hidrômetros Analógicos (SHAs), utilizando processamento de imagens (OCR) para leitura de dados.

## 📋 Status do Projeto (Barra de Progresso)

### 1. Documentação e Modelagem
- ✅ Especificação de Requisitos Final (PDF)
- ✅ Diagrama de Casos de Uso
- ✅ Diagrama de Classes e Arquitetura

### 2. Funcionalidades Principais (Core)
- ⬜ **Configuração e Estrutura**: Leitura de `config.properties` e Singleton Logger.
- ⬜ **Leitura de Imagens (OCR)**: Integração com Tess4J e Padrão Adapter.
- ⬜ **Agendador (Thread)**: Monitoramento automático de diretórios (Concorrência).
- ⬜ **Mover Arquivos**: Lógica para mover imagens de `/entrada` para `/processados`.

### 3. Gestão de Dados (Fachada e DAO)
- ⬜ **Entidades**: Implementação de Usuario, Hidrometro e Leitura.
- ⬜ **Persistência**: Implementação do padrão DAO (Salvar em Arquivo/JSON).
- ✅ **Fachada**: Implementação da classe `MonitoramentoFacade`.

### 4. Alertas e Notificações
- ⬜ **Lógica de Alerta**: Verificação de limite de consumo (Observer Pattern).
- ⬜ **Envio de E-mail**: Integração com servidor SMTP simulado ou real.

### 5. Interface e Entrega
- ⬜ **CLI/GUI**: Interface básica para iniciar/parar o monitoramento.
- ✅ **Vídeo de Demonstração**: SHAs em funcionamento e detecção pelo painel.

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
   
   *A estrutura final deve ficar assim:*


    MeuProjeto/
    ├── pom.xml
    ├── src/
    └── tessdata/
    └── eng.traineddata

3. **Ambiente de Simulação (SHAs)**:
- O sistema monitora pastas locais que simulam os hidrômetros.
- Crie as seguintes pastas no seu computador (ou ajuste os caminhos na classe `MonitoramentoFacade`):
  - `C:/temp/sha01` (Para o Hidrômetro 01)
  - `C:/temp/sha02` (Para o Hidrômetro 02)

### ▶️ Execução

1. Localize a classe principal: `src/main/java/br/edu/ifpb/monitoramento/Main.java`.
2. Clique com o botão direito no arquivo e selecione **"Run Main.main()"**.
3. Acompanhe o **Console** do IntelliJ.
- O sistema exibirá a mensagem `[AGENDADOR] Verificando...` a cada 5 segundos.
4. **Para testar**:
- Copie uma imagem de hidrômetro (`.jpg`) para dentro de uma das pastas criadas (ex: `C:/temp/sha01`).
- O console exibirá imediatamente um alerta de detecção e processará o arquivo.

---

**Desenvolvido por:** Cefras Mandú
**Professor:** Katyusco Santos
**Disciplina:** Padrões de Projeto - Eng. de Computação - IFPB-CG