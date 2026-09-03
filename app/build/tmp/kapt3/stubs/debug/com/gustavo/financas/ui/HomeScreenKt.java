package com.gustavo.financas.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0002\u001a\b\u0010\u0005\u001a\u00020\u0006H\u0003\u001a.\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00060\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0003\u001a@\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00060\u000b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00060\u0012H\u0007\u001a\u001a\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\f\u001a\u00020\rH\u0003\u001a<\u0010\u0019\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020 H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u001a,\u0010#\u001a\u00020\u00062\u0006\u0010$\u001a\u00020\u00142\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00060\u00122\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00060\u0012H\u0003\u001a\u001d\u0010\'\u001a\u0004\u0018\u00010\u00182\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00140)H\u0002\u00a2\u0006\u0002\u0010*\"\u0016\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006+"}, d2 = {"currencyFormat", "Ljava/text/NumberFormat;", "kotlin.jvm.PlatformType", "dateFormat", "Ljava/text/SimpleDateFormat;", "EmptyState", "", "FiltroPills", "selecionado", "Lcom/gustavo/financas/ui/Filtro;", "onSelecionar", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "HomeScreen", "viewModel", "Lcom/gustavo/financas/ui/TransactionViewModel;", "onAddClick", "Lkotlin/Function0;", "onEditClick", "Lcom/gustavo/financas/data/Transaction;", "onCategoriesClick", "InsightBanner", "variacaoMensal", "", "StatCard", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "", "valor", "accent", "Landroidx/compose/ui/graphics/Color;", "StatCard-xwkQ0AY", "(Landroidx/compose/ui/Modifier;Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;DJ)V", "TransactionRow", "transaction", "onEdit", "onDelete", "variacaoDespesasMes", "transactions", "", "(Ljava/util/List;)Ljava/lang/Double;", "app_debug"})
public final class HomeScreenKt {
    private static final java.text.NumberFormat currencyFormat = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat dateFormat = null;
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    com.gustavo.financas.ui.TransactionViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.gustavo.financas.data.Transaction, kotlin.Unit> onEditClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCategoriesClick) {
    }
    
    private static final java.lang.Double variacaoDespesasMes(java.util.List<com.gustavo.financas.data.Transaction> transactions) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FiltroPills(com.gustavo.financas.ui.Filtro selecionado, kotlin.jvm.functions.Function1<? super com.gustavo.financas.ui.Filtro, kotlin.Unit> onSelecionar, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void InsightBanner(double variacaoMensal, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyState() {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void TransactionRow(com.gustavo.financas.data.Transaction transaction, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
}