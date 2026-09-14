# SaveWise Roadmap

Documento de planejamento técnico para as próximas evoluções do projeto SaveWise,
desde a estabilização do sistema CLI atual até a migração para interface gráfica com JavaFX.

---

## Status Atual

A base do projeto está implementada e funcional com a seguinte arquitetura:

```
Main → MenuCLI → TransactionService → TransactionDAOImpl → ConnectionFactory → MariaDB
```

Camadas implementadas:
- **Model** — `Transaction.java` e `TransactionType.java`
- **Connection** — `ConnectionFactory.java` com `config.properties`
- **DAO** — `ITransactionDAO` e `TransactionDAOImpl` com JDBC
- **Service** — `TransactionService` com validações de negócio
- **UI (CLI)** — `MenuCLI` com loop de menu e Scanner
- **Main** — todas as camadas conectadas e funcionando

---

## Fase 1 — Estabilização do Sistema CLI

Hotfixes e melhorias de robustez antes de qualquer nova feature.

### 1.1 Tratamento de exceções no MenuCLI
- [x] Tratar `NumberFormatException` no loop principal do menu
- [x] Tratar `NumberFormatException` no método `deleteTransaction()`
- [x] Tratar `IllegalArgumentException` quando o usuário digitar um tipo inválido no lugar de `INCOME/EXPENSE`
- [ ] Exibir mensagem amigável em vez de encerrar o programa abruptamente

```java
// Exemplo de tratamento esperado em addTransaction()
try {
    TransactionType type = TransactionType.valueOf(scanner.nextLine().toUpperCase());
} catch (IllegalArgumentException e) {
    System.out.println("Invalid type. Please enter INCOME or EXPENSE.");
    return;
}
```

### 1.2 Tratamento de ID inexistente no delete
- [x] Verificar no `TransactionService` se o ID existe antes de deletar
- [x] Retornar mensagem clara ao usuário caso o ID não seja encontrado

```java
// Exemplo de verificação esperada no TransactionService
public void deleteTransaction(int id) {
    List<Transaction> all = transactionDAO.findAll();
    boolean exists = all.stream().anyMatch(t -> t.getId() == id);
    if (!exists) {
        throw new IllegalArgumentException("Transaction with ID " + id + " not found.");
    }
    transactionDAO.delete(id);
}
```

### 1.3 Filtro de transações por tipo e categoria
- [ ] Adicionar método `findByType(TransactionType type)` na interface `ITransactionDAO`
- [ ] Adicionar método `findByCategory(String category)` na interface `ITransactionDAO`
- [ ] Implementar os métodos no `TransactionDAOImpl` com queries SQL parametrizadas
- [ ] Expor os filtros no `TransactionService`
- [ ] Adicionar opções de filtro no `MenuCLI`

```java
// Contrato esperado na interface ITransactionDAO
List<Transaction> findByType(TransactionType type);
List<Transaction> findByCategory(String category);
```

### 1.4 Data personalizada no cadastro
- [x] Permitir que o usuário informe a data da transação manualmente
- [ ] Usar `LocalDate.parse()` com tratamento de `DateTimeParseException`
- [x] Manter `LocalDate.now()` como valor padrão caso o usuário não informe

---

## Fase 2 — Migração para JavaFX

Substituição do `MenuCLI` por uma interface gráfica moderna com JavaFX,
mantendo todas as camadas inferiores (`Service`, `DAO`, `Model`) intactas.

### 2.1 Configuração do ambiente JavaFX

- [x] Adicionar dependência do JavaFX no `pom.xml`

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21</version>
</dependency>
```

- [x] Adicionar o plugin Maven do JavaFX

```xml
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.savewise.Main</mainClass>
    </configuration>
</plugin>
```

### 2.2 Estrutura de pacotes JavaFX

Criar o pacote `com.savewise.ui.fx` para abrigar as novas telas,
mantendo o pacote `com.savewise.ui` com o `MenuCLI` para referência.

```
com.savewise
├── connection
│   └── ConnectionFactory.java
├── dao
│   ├── ITransactionDAO.java
│   └── TransactionDAOImpl.java
├── model
│   ├── Transaction.java
│   └── TransactionType.java
├── service
│   └── TransactionService.java
└── ui
    ├── MenuCLI.java              ← mantido como referência
    └── fx
        ├── MainApp.java          ← ponto de entrada JavaFX
        ├── controller
        │   ├── DashboardController.java
        │   └── TransactionController.java
        └── view
            ├── dashboard.fxml
            ├── dashboard.css
            ├── transaction-form.fxml
            └── transaction-form.css
```

### 2.3 Telas planejadas

#### Dashboard (tela principal)
- Exibição do saldo atual em destaque
- Tabela com todas as transações (`TableView`)
- Botões de ação: Adicionar, Deletar, Filtrar
- Atualização automática da tabela após cada operação

#### Formulário de transação
- Campos: descrição, valor, tipo (`ComboBox`), categoria, data (`DatePicker`)
- Validação visual de campos obrigatórios
- Botões: Salvar e Cancelar

### 2.4 Separação entre FXML e CSS

O JavaFX utiliza dois arquivos separados para cada tela:

- **`.fxml`** — define a estrutura dos componentes visuais (equivalente ao HTML)
- **`.css`** — define o estilo visual dos componentes (cores, fontes, espaçamentos)

Essa separação mantém o código Java limpo, focado apenas na lógica dos controllers.

### 2.5 Atualização do Main.java

```java
// O Main.java passará a estender Application do JavaFX
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fx/view/dashboard.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("SaveWise");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

## Fase 3 — Melhorias Futuras (Pós-JavaFX)

Evoluções planejadas após a interface gráfica estar estável.

- [ ] Gráfico de despesas por categoria (usando `PieChart` ou `BarChart` do JavaFX)
- [ ] Exportação de relatório em PDF
- [ ] Filtro por período (mês/ano)
- [ ] Suporte a múltiplos usuários com tela de login
- [ ] Backup automático do banco de dados

---

## Commits Sugeridos por Fase

### Fase 1
```
fix(ui): handle IllegalArgumentException for invalid TransactionType input
fix(service): validate transaction ID existence before deletion
feat(dao): add findByType and findByCategory query methods
feat(ui): add filter options by type and category in MenuCLI
feat(ui): add custom date input with DateTimeParseException handling
```

### Fase 2
```
chore(build): add JavaFX 21 dependencies and maven plugin to pom.xml
feat(ui/fx): add MainApp entry point extending JavaFX Application
feat(ui/fx): add DashboardController with TableView and balance display
feat(ui/fx): add TransactionController with form validation
feat(ui/fx): add dashboard.fxml and dashboard.css layout and styles
feat(ui/fx): add transaction-form.fxml and transaction-form.css
refactor(main): migrate entry point from MenuCLI to JavaFX MainApp
```
