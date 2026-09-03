package com.gustavo.financas.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\u001a$\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00060\nH\u0003\u001a\u001e\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0010H\u0007\u001a\u001e\u0010\u0011\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u0013\u001a\u00020\u000bH\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"GAP_GRAUS", "", "currencyFormat", "Ljava/text/NumberFormat;", "kotlin.jvm.PlatformType", "CategoriaRow", "", "status", "Lcom/gustavo/financas/ui/BudgetStatus;", "onSalvarLimite", "Lkotlin/Function1;", "", "CategoriesScreen", "viewModel", "Lcom/gustavo/financas/ui/TransactionViewModel;", "onBack", "Lkotlin/Function0;", "DespesasRing", "", "total", "app_debug"})
public final class CategoriesScreenKt {
    private static final java.text.NumberFormat currencyFormat = null;
    private static final float GAP_GRAUS = 6.0F;
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CategoriesScreen(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.ui.TransactionViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DespesasRing(java.util.List<com.gustavo.financas.ui.BudgetStatus> status, double total) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoriaRow(com.gustavo.financas.ui.BudgetStatus status, kotlin.jvm.functions.Function1<? super java.lang.Double, kotlin.Unit> onSalvarLimite) {
    }
}