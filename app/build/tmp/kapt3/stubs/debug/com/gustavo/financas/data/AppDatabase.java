package com.gustavo.financas.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lcom/gustavo/financas/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "budgetDao", "Lcom/gustavo/financas/data/BudgetDao;", "transactionDao", "Lcom/gustavo/financas/data/TransactionDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.gustavo.financas.data.Transaction.class, com.gustavo.financas.data.Budget.class}, version = 2, exportSchema = false)
@androidx.room.TypeConverters(value = {com.gustavo.financas.data.Converters.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.gustavo.financas.data.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.gustavo.financas.data.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.gustavo.financas.data.TransactionDao transactionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.gustavo.financas.data.BudgetDao budgetDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/gustavo/financas/data/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/gustavo/financas/data/AppDatabase;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.gustavo.financas.data.AppDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}