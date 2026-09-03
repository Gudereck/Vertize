package com.gustavo.financas.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J&\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\nJ\u000e\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u0016J\u0016\u0010!\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\u000bJ\u000e\u0010#\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR \u0010\u000e\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00120\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\rR\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00120\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\r\u00a8\u0006$"}, d2 = {"Lcom/gustavo/financas/ui/TransactionViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/gustavo/financas/data/TransactionRepository;", "budgetRepository", "Lcom/gustavo/financas/data/BudgetRepository;", "(Lcom/gustavo/financas/data/TransactionRepository;Lcom/gustavo/financas/data/BudgetRepository;)V", "despesasMesAtualPorCategoria", "Lkotlinx/coroutines/flow/StateFlow;", "", "", "", "getDespesasMesAtualPorCategoria", "()Lkotlinx/coroutines/flow/StateFlow;", "limitesPorCategoria", "saldo", "getSaldo", "statusOrcamentos", "", "Lcom/gustavo/financas/ui/BudgetStatus;", "getStatusOrcamentos", "transactions", "Lcom/gustavo/financas/data/Transaction;", "getTransactions", "addTransaction", "", "description", "amount", "type", "Lcom/gustavo/financas/data/TransactionType;", "category", "delete", "transaction", "setOrcamento", "limite", "updateTransaction", "app_debug"})
public final class TransactionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.gustavo.financas.data.TransactionRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.gustavo.financas.data.BudgetRepository budgetRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.gustavo.financas.data.Transaction>> transactions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Double> saldo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.lang.Double>> despesasMesAtualPorCategoria = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.lang.Double>> limitesPorCategoria = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.gustavo.financas.ui.BudgetStatus>> statusOrcamentos = null;
    
    public TransactionViewModel(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.TransactionRepository repository, @org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.BudgetRepository budgetRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.gustavo.financas.data.Transaction>> getTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Double> getSaldo() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.lang.Double>> getDespesasMesAtualPorCategoria() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.gustavo.financas.ui.BudgetStatus>> getStatusOrcamentos() {
        return null;
    }
    
    public final void addTransaction(@org.jetbrains.annotations.NotNull()
    java.lang.String description, double amount, @org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.TransactionType type, @org.jetbrains.annotations.NotNull()
    java.lang.String category) {
    }
    
    public final void updateTransaction(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.Transaction transaction) {
    }
    
    public final void delete(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.Transaction transaction) {
    }
    
    public final void setOrcamento(@org.jetbrains.annotations.NotNull()
    java.lang.String category, double limite) {
    }
}