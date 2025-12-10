# Painel de Monitoramento de Hidrômetros (SHA)

Este projeto implementa uma solução centralizada para monitorar o consumo de água registrado por Simuladores de Hidrômetros Analógicos (SHAs), utilizando processamento de imagens (OCR) para leitura de dados.

## 📋 Status do Projeto (Barra de Progresso)

### 1. Documentação e Modelagem
- [x] Especificação de Requisitos Final (PDF)
- [x] Diagrama de Casos de Uso
- [x] Diagrama de Classes e Arquitetura

### 2. Funcionalidades Principais (Core)
- [ ] **Configuração e Estrutura**: Leitura de `config.properties` e Singleton Logger.
- [ ] **Leitura de Imagens (OCR)**: Integração com Tess4J e Padrão Adapter.
- [ ] **Agendador (Thread)**: Monitoramento automático de diretórios (Concorrência).
- [ ] **Mover Arquivos**: Lógica para mover imagens de `/entrada` para `/processados`.

### 3. Gestão de Dados (Fachada e DAO)
- [ ] **Entidades**: Implementação de Usuario, Hidrometro e Leitura.
- [ ] **Persistência**: Implementação do padrão DAO (Salvar em Arquivo/JSON).
- [ ] **Fachada**: Implementação da classe `MonitoramentoFacade`.

### 4. Alertas e Notificações
- [ ] **Lógica de Alerta**: Verificação de limite de consumo (Observer Pattern).
- [ ] **Envio de E-mail**: Integração com servidor SMTP simulado ou real.

### 5. Interface e Entrega
- [ ] **CLI/GUI**: Interface básica para iniciar/parar o monitoramento.
- [ ] **Vídeo de Demonstração**: SHAs em funcionamento e detecção pelo painel.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java JDK 17+
* Tesseract OCR instalado no sistema operacional (necessário para o Tess4J).
* Maven (para dependências).

### Configuração
1. Configure o arquivo `config.properties` na raiz com os caminhos dos diretórios dos SHAs.
2. Certifique-se de que as pastas dos SHAs existem.

### Execução


---
**Desenvolvido por:** CefrasMandú
**Professor:** Katyusco Santos
**Disciplina:** Padrões de Projeto - Eng. de Computação - IFPB-CG