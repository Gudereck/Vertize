package com.gustavo.financas.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u000eJ\u001e\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/gustavo/financas/data/BudgetRepository;", "", "dao", "Lcom/gustavo/financas/data/BudgetDao;", "(Lcom/gustavo/financas/data/BudgetDao;)V", "allBudgets", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/gustavo/financas/data/Budget;", "getAllBudgets", "()Lkotlinx/coroutines/flow/Flow;", "remove", "", "budget", "(Lcom/gustavo/financas/data/Budget;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setLimite", "category", "", "limite", "", "(Ljava/lang/String;DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class BudgetRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.gustavo.financas.data.BudgetDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.gustavo.financas.data.Budget>> allBudgets = null;
    
    public BudgetRepository(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.BudgetDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.gustavo.financas.data.Budget>> getAllBudgets() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setLimite(@org.jetbrains.annotations.NotNull()
    java.lang.String category, double limite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object remove(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.data.Budget budget, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}