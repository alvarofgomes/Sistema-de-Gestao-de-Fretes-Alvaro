<div align="center">

# 🚚 Sistema de Gestão de Fretes

### Plataforma web completa para gestão operacional de transportadoras

*Desenvolvido com Java Web clássico — arquitetura em camadas, relatórios PDF e notificações automáticas via WhatsApp.*

---

![Java](https://img.shields.io/badge/Java-8-007396?style=flat-square&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=flat-square&logo=postgresql&logoColor=white)
![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-9-F8DC75?style=flat-square&logo=apachetomcat&logoColor=black)
![JasperReports](https://img.shields.io/badge/JasperReports-6.20.6-darkgreen?style=flat-square)
![Twilio](https://img.shields.io/badge/Twilio-WhatsApp-F22F46?style=flat-square&logo=twilio&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)

</div>

---

## 📌 Objetivo do Projeto

O **Sistema de Gestão de Fretes** foi desenvolvido para atender às necessidades operacionais de pequenas e médias transportadoras, centralizando o controle de todo o ciclo logístico — desde o cadastro de clientes, motoristas e veículos até a emissão, rastreamento e entrega de fretes.

---

## 🛠️ Tecnologias Utilizadas

### Backend
| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 8 | Linguagem principal |
| Servlet | 3.1 | Controllers da aplicação |
| JSP | 2.3 | Camada de visualização |
| JDBC | — | Acesso ao banco de dados |
| Apache Tomcat | 9 | Servidor de aplicação |

### Frontend
| Tecnologia | Finalidade |
|---|---|
| HTML5 | Estrutura das páginas |
| CSS3 | Estilização com variáveis e design system |
| JavaScript | Interatividade, máscaras e validações |
| JSTL | Lógica de exibição nas JSPs |

### Banco de Dados
| Tecnologia | Finalidade |
|---|---|
| PostgreSQL | Banco de dados relacional principal |
| JDBC puro | Conexão e execução de queries |

### Relatórios
| Tecnologia | Finalidade |
|---|---|
| JasperReports 6.20.6 | Geração de relatórios em PDF |

### Integrações
| Tecnologia | Finalidade |
|---|---|
| Twilio WhatsApp API | Notificações automáticas de status |

---

## 🏗️ Arquitetura do Projeto

O sistema segue uma arquitetura em camadas bem definida, garantindo separação de responsabilidades e facilidade de manutenção:

```
JSP  →  Servlet (Controller)  →  BO (Business Object)  →  DAO  →  PostgreSQL
```

### Responsabilidade de cada camada

| Camada | Responsabilidade |
|---|---|
| **JSP** | Apenas exibe dados e envia formulários. Sem regra de negócio. |
| **Servlet (Controller)** | Recebe a requisição, chama o BO e redireciona ou encaminha para a JSP. |
| **BO (Business Object)** | Concentra todas as regras de negócio, validações e orquestrações. |
| **DAO (Data Access Object)** | Único responsável pelo acesso ao banco. Contém todo o SQL. |
| **PostgreSQL** | Persiste os dados da aplicação. |

### Pacotes da aplicação

```
br.com.sistema_frete
├── controller        # Servlets — ponto de entrada das requisições
├── BO                # Regras de negócio e validações
├── DAO               # Acesso ao banco de dados (SQL)
├── model             # Entidades do sistema (POJOs)
├── enums             # Enumerações de status e tipos
├── exception         # Hierarquia de exceções (NegocioException, etc.)
├── filter            # Filtros de autenticação e encoding
├── util              # Utilitários (ConnectionFactory, HashUtil, etc.)
└── service           # Serviços externos (WhatsAppService)
```

---

## ✅ Funcionalidades Principais

### 🔐 Autenticação e Controle de Acesso
- Login com validação de credenciais e hash SHA-256 para senhas
- Logout com invalidação de sessão
- Controle de acesso por perfil via `AuthenticationFilter`
- Três perfis: **ADMIN**, **OPERADOR** e **CLIENTE**
- Redirecionamento automático por perfil após login
- Cadastro público de novos clientes via portal

### 👥 Clientes
- Cadastro completo com validação de CNPJ (dígito verificador)
- Edição com verificação de fretes vinculados
- Listagem paginada com filtro por nome/razão social
- Criação automática de login de acesso ao portal no cadastro

### 🧑‍✈️ Motoristas
- Cadastro com validação de CPF (dígito verificador)
- Controle de CNH: número, categoria e validade
- Regras de status: ATIVO, SUSPENSO, INATIVO
- Inativação bloqueada quando há frete ativo
- Validação de CNH vencida na emissão de fretes

### 🚛 Veículos
- Controle de disponibilidade em tempo real
- Capacidade de carga em kg e volume em m³
- Status operacional: DISPONÍVEL, EM VIAGEM, EM MANUTENÇÃO, INATIVO
- Validação de placa nos formatos antigo e Mercosul

### 📦 Fretes
- Emissão com geração automática de número sequencial (FRT-AAAA-NNNNN)
- Validações completas: motorista ativo, CNH válida, veículo disponível, capacidade
- Fluxo de status: EMITIDO → SAÍDA CONFIRMADA → EM TRÂNSITO → ENTREGUE
- Transações JDBC manuais garantindo integridade dos dados
- Cancelamento somente de fretes com status EMITIDO

### 📍 Ocorrências
- Registro de ocorrências por tipo: SAÍDA DO PÁTIO, EM ROTA, TENTATIVA DE ENTREGA, ENTREGA REALIZADA, AVARIA, EXTRAVIO, OUTROS
- Atualização automática do status do frete conforme ocorrência registrada
- Validação de sequência cronológica das ocorrências
- Dados do recebedor na entrega (nome e documento)
- Histórico completo exibido na tela de detalhe do frete

### 🖥️ Portal do Cliente
- Dashboard com KPIs: fretes em aberto, em trânsito, entregues, em atraso e solicitações pendentes
- Listagem dos próprios fretes com filtro por status
- Geração de relatório PDF dos próprios fretes por período
- Solicitação de novos fretes diretamente pelo portal
- Acesso isolado — cliente nunca visualiza dados de outros clientes

### 📋 Solicitação de Frete
- Cliente solicita frete com dados de origem, destino e carga
- ADMIN/OPERADOR aprova ou recusa com motivo
- Solicitação aprovada pode ser convertida em frete oficial com um clique
- Formulário de novo frete pré-preenchido com dados da solicitação
- Status: PENDENTE → APROVADA → CONVERTIDA / RECUSADA / CANCELADA

### 📊 Relatórios (JasperReports)
| Relatório | Descrição |
|---|---|
| Fretes em Aberto | Lista fretes EMITIDO, SAÍDA CONFIRMADA e EM TRÂNSITO com dias de atraso |
| Romaneio de Carga | Fretes de um motorista em uma data, com totais de peso e volumes |
| Fretes por Cliente no Período | Extrato de fretes de um cliente com valor total consolidado |
| Relatório do Portal | Cliente gera seu próprio PDF de fretes por período |

### 📱 Notificações WhatsApp (Twilio)
Envio automático de mensagens ao cliente nos seguintes eventos:

| Evento | Quando dispara |
|---|---|
| 🔵 Frete Emitido | Após emissão do frete |
| 🟡 Em Trânsito | Quando ocorrência EM_ROTA é registrada |
| 🟢 Entregue | Após ocorrência ENTREGA_REALIZADA |

> A falha no envio do WhatsApp **nunca cancela** a operação principal do frete.

---

## 🔄 Fluxo Operacional do Sistema

```
👤 Cliente acessa o portal
        │
        ▼
📝 Solicita um frete (cidade, carga, peso)
        │
        ▼
👨‍💼 ADMIN/OPERADOR analisa a solicitação
        │
   ┌────┴────┐
   ▼         ▼
✅ Aprova   ❌ Recusa (com motivo)
   │
   ▼
🚚 Gera o frete oficial
   (motorista, veículo, valores, data)
        │
        ▼
📱 Cliente recebe WhatsApp: "Frete Emitido"
        │
        ▼
🏁 Admin confirma saída do veículo
        │
        ▼
🛣️ Motorista registra ocorrência EM ROTA
        │
        ▼
📱 Cliente recebe WhatsApp: "Em Trânsito"
        │
        ▼
📦 Motorista registra entrega com recebedor
        │
        ▼
📱 Cliente recebe WhatsApp: "Entregue!"
        │
        ▼
🖥️ Cliente acompanha tudo pelo portal
```

---

## ⭐ Diferenciais do Projeto

| Diferencial | Descrição |
|---|---|
| 🔔 **Notificações WhatsApp** | Integração real com Twilio via HttpURLConnection — sem SDK externo |
| 📊 **Relatórios PDF** | Três relatórios profissionais gerados com JasperReports 6.20.6 |
| 🖥️ **Portal do Cliente** | Área exclusiva com dashboard, KPIs e relatórios próprios |
| 🔄 **Workflow completo** | Do pedido do cliente à entrega, com rastreamento em cada etapa |
| 🔐 **Controle por perfil** | ADMIN, OPERADOR e CLIENTE com rotas isoladas por filtro |
| 🏗️ **Arquitetura limpa** | JSP → Servlet → BO → DAO sem mistura de responsabilidades |
| 💾 **Transações JDBC** | Commit e rollback manuais garantindo integridade de dados críticos |
| 📋 **Solicitação de frete** | Fluxo completo de aprovação com conversão automática para frete |

---

## 📁 Estrutura de Pastas

```
sistema-gestao-fretes/
│
├── sql/
│   └── schema.sql                    # Script completo do banco
│
├── src/main/java/br/com/sistema_frete/
│   ├── BO/                           # Business Objects (regras de negócio)
│   │   ├── ClienteBO.java
│   │   ├── FreteBO.java
│   │   ├── MotoristaBO.java
│   │   ├── SolicitacaoFreteBO.java
│   │   ├── UsuarioBO.java
│   │   └── VeiculoBO.java
│   │
│   ├── controller/                   # Servlets (Controllers)
│   │   ├── ClienteServlet.java
│   │   ├── FreteServlet.java
│   │   ├── LoginServlet.java
│   │   ├── OcorrenciaServlet.java
│   │   ├── PortalClienteServlet.java
│   │   ├── PortalClienteRelatorioServlet.java
│   │   ├── RelatorioFreteServlet.java
│   │   ├── RelatorioRomaneioServlet.java
│   │   ├── RelatorioClienteServlet.java
│   │   ├── SolicitacaoFreteServlet.java
│   │   └── UsuarioServlet.java
│   │
│   ├── DAO/                          # Data Access Objects (SQL)
│   │   ├── ClienteDAO.java
│   │   ├── FreteDAO.java
│   │   ├── MotoristaDAO.java
│   │   ├── OcorrenciaDAO.java
│   │   ├── SolicitacaoFreteDAO.java
│   │   ├── UsuarioDAO.java
│   │   └── VeiculoDAO.java
│   │
│   ├── model/                        # Entidades (POJOs)
│   │   ├── Cliente.java
│   │   ├── Frete.java
│   │   ├── Motorista.java
│   │   ├── Ocorrencia.java
│   │   ├── SolicitacaoFrete.java
│   │   ├── Usuario.java
│   │   └── Veiculo.java
│   │
│   ├── enums/                        # Enumerações
│   │   ├── cliente/StatusCliente.java
│   │   ├── frete/FreteStatus.java
│   │   ├── motorista/StatusMotorista.java
│   │   ├── ocorrencia/TipoOcorrencia.java
│   │   ├── solicitacao/StatusSolicitacaoFrete.java
│   │   ├── usuario/PerfilUsuario.java
│   │   ├── usuario/StatusUsuario.java
│   │   └── veiculo/StatusVeiculo.java
│   │
│   ├── exception/                    # Exceções customizadas
│   │   ├── CadastroException.java
│   │   ├── FreteException.java
│   │   └── NegocioException.java
│   │
│   ├── filter/                       # Filtros Servlet
│   │   ├── AuthenticationFilter.java
│   │   └── EncodingFilter.java
│   │
│   ├── util/                         # Utilitários
│   │   ├── ConnectionFactory.java
│   │   ├── GeradorNumeroFrete.java
│   │   ├── HashUtil.java
│   │   ├── ValidadorCNPJ.java
│   │   └── ValidadorCPF.java
│   │
│   └── service/                      # Serviços externos
│       └── WhatsAppService.java
│
├── src/main/resources/
│   └── db.properties                 # Configuração do banco
│
└── src/main/webapp/
    ├── assets/
    │   └── css/style.css             # Design system com variáveis CSS
    ├── includes/
    │   └── header.jsp                # Header com navegação por perfil
    ├── relatorios/
    │   ├── relatorio_fretes_abertos.jrxml
    │   ├── relatorio_romaneio.jrxml
    │   └── relatorio_cliente_periodo.jrxml
    ├── WEB-INF/
    │   ├── lib/                      # JARs (PostgreSQL, JasperReports)
    │   └── web.xml
    └── *.jsp                         # Páginas da aplicação
```

---

## 🗄️ Banco de Dados

O sistema utiliza **PostgreSQL** como banco de dados relacional.

### Tabelas principais

| Tabela | Descrição |
|---|---|
| `cliente` | Empresas remetentes e destinatárias |
| `motorista` | Motoristas com CNH e vínculo |
| `veiculo` | Frota com capacidade e status |
| `frete` | Fretes emitidos com todo o ciclo |
| `ocorrencia_frete` | Histórico de ocorrências por frete |
| `usuario` | Usuários do sistema com perfil e hash de senha |
| `solicitacao_frete` | Solicitações de frete feitas pelos clientes |

### Diagrama de relacionamentos

```
cliente ──< frete (remetente)
cliente ──< frete (destinatário)
motorista ──< frete
veiculo ──< frete
frete ──< ocorrencia_frete
cliente ──< usuario
cliente ──< solicitacao_frete
usuario ──< solicitacao_frete (analise)
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java 8 (JDK)
- Apache Tomcat 9
- PostgreSQL 13+
- Eclipse IDE (ou IntelliJ)
- JARs necessários em `WEB-INF/lib/`:
  - `postgresql-42.x.x.jar`
  - `jasperreports-6.20.6.jar`
  - `commons-beanutils-1.9.4.jar`
  - `commons-collections-3.2.2.jar`
  - `commons-logging-1.2.jar`
  - `itext-2.1.7.jar`

### Passo a passo

**1. Clonar o repositório**
```bash
git clone https://github.com/alvarofgomes/sistema-gestao-fretes.git
cd sistema-gestao-fretes
```

**2. Criar o banco de dados**
```sql
CREATE DATABASE sistema_gestao_frete;
```

**3. Executar o script SQL**
```bash
psql -U postgres -d sistema_gestao_frete -f sql/schema.sql
```

**4. Configurar a conexão com o banco**

Edite o arquivo `src/main/resources/db.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/sistema_gestao_frete
db.user=postgres
db.password=sua_senha
db.driver=org.postgresql.Driver
```

**5. Adicionar os JARs ao Build Path**

No Eclipse: clique com botão direito no projeto → Build Path → Configure Build Path → Libraries → Add External JARs → selecione todos os JARs da pasta `WEB-INF/lib/`.

**6. Configurar o servidor Tomcat**

Adicione o projeto ao Tomcat 9 no Eclipse e inicie o servidor.

**7. Acessar a aplicação**
```
http://localhost:8080/sistema_frete/login
```

**Credenciais padrão:**
| Login | Senha | Perfil |
|---|---|---|
| `admin` | `admin123` | ADMIN |

---

## 📱 Configuração do Twilio (WhatsApp)

### Sandbox do Twilio

1. Crie uma conta gratuita em [twilio.com](https://www.twilio.com)
2. Acesse **Messaging → Try it out → Send a WhatsApp message**
3. Envie a mensagem de ativação exibida para o número `+1 415 523 8886` no WhatsApp
4. Anote seu **Account SID** e **Auth Token**

### Configurar no projeto

Edite o arquivo `WhatsAppService.java`:
```java
private static final String ACCOUNT_SID   = "ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
private static final String AUTH_TOKEN    = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
private static final String FROM_WHATSAPP = "whatsapp:+14155238886";
```

> ⚠️ O sandbox expira após 72h sem uso. Reative enviando qualquer mensagem para o número Twilio antes da demonstração.

---

## 🔮 Melhorias Futuras

- [ ] Dashboard com gráficos de desempenho (Chart.js ou D3.js)
- [ ] Upload e armazenamento de comprovantes de entrega
- [ ] Geolocalização do veículo em tempo real
- [ ] API REST para integração com sistemas externos
- [ ] Migração para Spring Boot com Spring Security
- [ ] Deploy em nuvem (AWS, Railway ou Render)
- [ ] Envio de e-mail automático além do WhatsApp
- [ ] Aplicativo mobile para motoristas
- [ ] Módulo financeiro com emissão de notas

---

## 👤 Autor

<div align="center">

**Alvaro Gomes**

[![GitHub](https://img.shields.io/badge/GitHub-alvarofgomes-181717?style=flat-square&logo=github)](https://github.com/alvarofgomes)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Álvaro%20Gomes-0077B5?style=flat-square&logo=linkedin)](https://linkedin.com/in/alvarofgomes)

</div>

---

## 📝 Conclusão

O **Sistema de Gestão de Fretes** representa a consolidação dos conhecimentos adquiridos em desenvolvimento Java Web, cobrindo desde a camada de persistência com JDBC puro até integrações com serviços externos como Twilio WhatsApp API.

O projeto demonstra a aplicação prática de conceitos fundamentais de engenharia de software — separação de responsabilidades, tratamento de exceções, transações de banco de dados, controle de acesso e geração de relatórios — em um contexto de negócio real e complexo.

A arquitetura em camadas adotada (JSP → Servlet → BO → DAO) foi mantida rigorosamente ao longo de todo o desenvolvimento, garantindo um código organizado, testável e de fácil manutenção.

---

<div align="center">

*Desenvolvido com ☕ Java e dedicação*

</div>