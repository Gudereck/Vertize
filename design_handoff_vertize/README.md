# Handoff: Vertize — app de finanças pessoais (Android)

## Visão geral
Redesenho completo do app **Vertize**: transações, orçamentos por categoria, histórico
de 6 meses, metas de economia e navegação por bottom nav. O pacote cobre 8 telas e o
fluxo interativo entre elas.

O app hoje já existe (Kotlin/Compose + Room) com um estado inicial vazio e tema
verde-teal. Este handoff propõe a versão preenchida das telas, um sistema visual
consistente e a hierarquia de informação de cada tela.

## Sobre os arquivos de design
Os arquivos deste pacote são **referências de design feitas em HTML** — protótipos que
mostram aparência e comportamento pretendidos. **Não são código de produção para copiar.**

A tarefa é **recriar esses designs no ambiente já existente do projeto** (Jetpack Compose,
Material 3, Room) usando os padrões, componentes e convenções que o codebase já tem.
Nada aqui deve ser portado como HTML/CSS.

Arquivos:
- `Vertize.dc.html` — documento de design: protótipo navegável (1a), as 8 telas lado a
  lado (1b) e 6 alternativas de componente (1c–1h).
- `VertizePhone.dc.html` — o app em si (todas as telas + lógica de estado do protótipo).

Abra `Vertize.dc.html` em um navegador para navegar pelo protótipo.

## Fidelidade
**Alta fidelidade (hifi).** Cores, tipografia, espaçamentos, raios e estados estão
definidos e devem ser reproduzidos fielmente.

**Uma ressalva importante:** este design foi feito sem acesso ao tema real do app. O app
atual usa verde-teal como cor de marca; o design propõe **roxo** (`#4A2F86` aprox.) como
cor de marca, com verde reservado para receitas e vermelho apenas para limite estourado.
Se o time quiser manter o teal, troque a cor de marca do design mantendo a mesma
estrutura e as mesmas regras semânticas (verde = receita, vermelho = estouro de limite).
Nesse caso o teal assume o papel do roxo: card de saldo, FAB central, anéis de meta.

---

## Design tokens

### Cores
Definidas em oklch no protótipo; hex aproximado ao lado.

| Token | Valor | Hex aprox. | Uso |
|---|---|---|---|
| `brand` | oklch(0.36 0.12 300) | #4A2F86 | FAB central, botões primários, mark do app |
| `brandCard` | oklch(0.34 0.115 300) | #452B7F | fundo do card de saldo |
| `brandActive` | oklch(0.42 0.15 300) | #5B37A6 | item ativo da bottom nav, anel de meta |
| `ink` | #16181C | — | texto primário |
| `ink70` | rgba(22,24,28,.70) | — | texto secundário forte |
| `ink50` | rgba(22,24,28,.50) | — | texto secundário |
| `ink38` | rgba(22,24,28,.38) | — | labels/eyebrow |
| `ink34` | rgba(22,24,28,.34) | — | ícone de nav inativo |
| `surface` | #FFFFFF | — | fundo |
| `surfaceSoft` | oklch(0.96 0.004 280) | #F4F4F5 | trilha de segmented control |
| `hairline` | rgba(22,24,28,.09–.14) | — | bordas de card |
| `positive` | oklch(0.50 0.14 155) | #17805A | receitas |
| `negative` | oklch(0.55 0.19 25) | #C74436 | limite estourado, badge de despesa |
| `warn` | oklch(0.72 0.14 55) | #D9903F | insight banner (fundo oklch(0.96 0.035 60)) |

Cores de categoria (usadas em monograma, anel e barra de progresso):

| Categoria | oklch | Hex aprox. |
|---|---|---|
| Aluguel | 0.52 0.16 300 | #6B44B0 |
| Mercado | 0.55 0.15 250 | #3D6BC4 |
| Transporte | 0.52 0.11 210 | #3A6E90 |
| Restaurante | 0.60 0.13 62 | #96702A |
| Lazer | 0.55 0.14 155 | #1C8B62 |
| Saúde | 0.57 0.16 22 | #C05548 |
| Assinaturas | 0.50 0.07 285 | #5B5486 |
| Outros | 0.55 0.02 280 | #6D6C74 |
| Salário | 0.50 0.14 155 | #17805A |
| Freelance | 0.52 0.13 175 | #0F8578 |
| Investimentos | 0.50 0.12 200 | #24728F |
| Presente | 0.55 0.14 320 | #9B4D96 |

### Tipografia
- **UI:** Manrope — 400/500/600/700/800.
- **Números e títulos de tela:** Space Grotesk — 600/700.

Escala usada:

| Papel | Fonte | Tamanho | Peso | Tracking |
|---|---|---|---|---|
| Saldo (card) | Space Grotesk | 38sp | 700 | -0.03em |
| Valor no teclado (novo lançamento) | Space Grotesk | 42sp | 700 | -0.03em |
| % no anel grande da meta | Space Grotesk | 34sp | 700 | -0.03em |
| Título de tela ("Histórico", "Metas", "Mais") | Space Grotesk | 26sp | 700 | -0.03em |
| Nome da meta | Manrope | 16sp | 800 | -0.01em |
| Valor de item de lista | Space Grotesk | 15sp | 600 | — |
| Título de item de lista | Manrope | 14.5sp | 700 | — |
| Corpo / subtítulo | Manrope | 12.5–13.5sp | 600 | — |
| Eyebrow (caixa alta) | Manrope | 11.5sp | 700 | 0.06em |
| Label de bottom nav | Manrope | 10.5sp | 700 | — |

### Espaçamento e forma
- Padding horizontal de tela: **20dp**. Padding inferior do conteúdo: **108dp** (nav).
- Gap vertical entre blocos de uma tela: **18dp**.
- Raios: card de saldo **26dp**; cards de meta/orçamento **20–24dp**; banner **18–22dp**;
  linhas de lista **14–18dp**; chips e botões pequenos **11–13dp**; monograma **10–12dp**;
  FAB central **19dp** (rounded square, não círculo).
- Sombra do FAB: `0 10dp 22dp -8dp brand@80%`.
- Sombra do toast: `0 18dp 40dp -12dp rgba(22,24,28,.35)`.
- Alturas de barra de progresso: **7–8dp**, raio total.
- Altura mínima de toque: 44dp (todos os botões respeitam).

---

## Telas

### 1. Início — Transações (`home`)
**Objetivo:** ver saldo do mês e a lista de lançamentos; filtrar por tipo; abrir um
lançamento para editar.

**Layout** (coluna, gap 18dp, padding 20dp):
1. **Header** — mark do app (26dp, rounded square 8dp, brand, diamante branco 9dp
   rotacionado 45°) + wordmark "Vertize" (Space Grotesk 19sp/700, tracking -0.02em).
   À direita, avatar circular 34dp, fundo oklch(0.94 0.02 300), inicial em 13sp/800.
2. **Card de saldo** — fundo `brandCard`, raio 26dp, padding 22/22/18.
   - Eyebrow "SALDO DE SETEMBRO" 12.5sp/600, tracking 0.06em, branco@70%.
   - Valor Space Grotesk 38sp/700, branco.
   - Dois sub-cards em row (gap 10dp), fundo branco@12%, raio 16dp, padding 11/13:
     dot 7dp (verde claro oklch(0.78 0.16 155) / coral oklch(0.75 0.16 25)) + label
     11.5sp/700 branco@78% + valor Space Grotesk 17sp/600.
3. **InsightBanner** — só aparece quando há variação relevante. Fundo
   oklch(0.96 0.035 60), borda oklch(0.88 0.06 60), raio 18dp, padding 13/14.
   Ícone 30dp (raio 9dp, fundo `warn`, triângulo branco). Texto 13sp/600 em
   oklch(0.35 0.06 60). Chevron "›" à direita. **Clicável → tela de orçamentos.**
   Copy: "Você já gastou 17% mais que no mesmo período de agosto. Lazer e Assinaturas
   passaram do limite." (positivo: "Seus gastos estão N% abaixo do mesmo período de
   agosto. Bom ritmo.")
4. **Filtro segmentado** — trilha `surfaceSoft`, raio 14dp, padding 4dp; 3 botões
   iguais, raio 11dp, 13sp/700. Ativo: fundo branco + sombra `0 1dp 3dp rgba(0,0,0,.12)`,
   texto `ink`. Inativo: transparente, `ink50`. Ordem: Tudo / Receitas / Despesas.
5. **Lista de lançamentos** — agrupada por dia, ordem decrescente.
   - Cabeçalho de dia: "05 de setembro", eyebrow 11.5sp/700 `ink38`, padding 14/2/8.
   - Linha (padding 9dp vertical, raio 14dp, hover oklch(0.97 0.003 280)):
     monograma 38dp (raio 12dp, fundo = cor da categoria a 5% de luminosidade alta,
     texto = cor da categoria, 12sp/800, 2 primeiras letras da categoria em caixa alta)
     · descrição 14.5sp/700 (ellipsis) + categoria 12.5sp/600 `ink50`
     · valor Space Grotesk 15sp/600: receita "+ R$ …" em `positive`, despesa "− R$ …" em `ink`.
   - **Toque na linha → tela de novo lançamento em modo edição.**

### 2. Novo/editar lançamento (`add`)
**Objetivo:** criar, editar ou excluir um lançamento. Tela cheia, **sem bottom nav.**

**Layout:**
1. **App bar** — "Cancelar" (14sp/700 `ink50`) · título 15sp/800 ("Novo lançamento" /
   "Editar lançamento") · "Salvar" (14sp/800 `brandActive`).
2. **Segmented Despesa / Receita** — mesmo componente do filtro. Trocar o tipo troca a
   lista de categorias e reseta a categoria selecionada (Mercado / Salário).
3. **Display do valor** — eyebrow "VALOR" + Space Grotesk 42sp/700. Receita em
   `positive`, despesa em `ink`. Formato pt-BR sempre com 2 decimais: "R$ 0,00".
4. **Chips de categoria** — flex-wrap, gap 7dp. Chip: raio 11dp, padding 8/12, 12.5sp/700.
   Selecionado: fundo = cor da categoria, texto branco, borda igual ao fundo.
   Não selecionado: fundo branco, texto `ink`@65%, borda rgba(22,24,28,.14).
   - Despesa: Aluguel, Mercado, Transporte, Restaurante, Lazer, Saúde, Assinaturas, Outros.
   - Receita: Salário, Freelance, Investimentos, Presente, Outros.
5. **Descrição + data** — row, gap 9dp. Inputs raio 14dp, borda rgba(22,24,28,.14),
   padding 12/13, 14sp/600. Data com largura fixa 118dp, formato dd/MM/yyyy.
   No app real, a data deve abrir o date picker do Material 3.
6. **Teclado numérico** — grid 3 colunas, gap 8dp. Teclas: 1–9, "00", 0, "⌫".
   Botão: raio 16dp, borda rgba(22,24,28,.1), fundo oklch(0.98 0.002 280),
   Space Grotesk 19sp/600, padding 14dp vertical.
   Dígitos entram por **centavos** (digitar 1,2,3 → R$ 1,23).
7. **Excluir lançamento** — só em modo edição. Botão largura cheia, raio 16dp,
   fundo oklch(0.97 0.02 25), borda oklch(0.88 0.07 25), texto oklch(0.5 0.17 25) 13.5sp/800.

**Validação:** valor 0 bloqueia o salvamento e mostra o toast "Valor obrigatório /
Informe um valor maior que zero para salvar."

### 3. Categorias e orçamentos (`orc`)
**Objetivo:** ver a divisão do gasto do mês e ajustar o limite de cada categoria.

**Layout:**
1. **App bar** com "‹" (20sp/700 `ink`@55%) + título 15sp/800.
2. **Anel de gasto** — card com borda hairline, raio 24dp, padding 20dp.
   Donut SVG 180dp, raio do círculo 74dp, stroke 18dp, trilha oklch(0.95 0.003 280),
   girado -90°. Um arco por categoria, proporcional ao **total de despesas do mês**
   (não ao limite), na cor da categoria, ordenado do maior para o menor.
   Centro: eyebrow "GASTO NO MÊS" + valor Space Grotesk 25sp/700 + "de R$ <soma dos
   limites>" 12sp/700 `ink`@45%.
   Legenda abaixo: chips "cor · nome · %", 12sp/700, flex-wrap, gap 8/14dp.
3. **Lista de orçamentos** — um card por categoria, gap 9dp, raio 20dp, padding 15dp.
   - Row: monograma 32dp (raio 10dp, fundo = cor da categoria, texto branco 11.5sp/800)
     · nome 14sp/800 + "R$ gasto de R$ limite" (Space Grotesk 12.5sp/600 `ink50`)
     · stepper: dois botões 28dp, raio 9dp, borda hairline, "−" e "+" (± R$ 50 por toque).
   - **Barra de progresso** (margin-top 12dp, altura 8dp, raio 5dp, trilha
     rgba(22,24,28,.08)): largura = min(100%, gasto/limite).
     Cor: normal = cor da categoria; ≥ 85% = oklch(0.68 0.14 62) (âmbar);
     > 100% = `negative`.
   - **Estado estourado:** borda do card oklch(0.87 0.08 25), fundo
     oklch(0.985 0.012 25), e uma linha de aviso: quadrado 16dp `negative` com "!"
     branco + "Estourou o limite em R$ X" 12.5sp/700 em oklch(0.5 0.17 25).

**Notificação push:** sempre que um lançamento salvo ou um ajuste de limite fizer
`gasto > limite`, disparar notificação. Título "Orçamento de <Categoria> estourado",
corpo "R$ <gasto> de R$ <limite> — R$ <diferença> acima do limite."
No protótipo isso aparece como um toast in-app (ver "Toast" abaixo).

### 4. Histórico (`hist`)
1. Título "Histórico" + subtítulo "Saldo dos últimos 6 meses".
2. **Card do gráfico** (borda hairline, raio 24dp, padding 20/18/14):
   header "Saldo médio" 12.5sp/700 `ink50` + valor Space Grotesk 17sp/600.
   Gráfico de 6 colunas, altura 158dp, gap 10dp. Cada coluna: rótulo do mês
   (Space Grotesk 10.5sp/600 `ink`@55%) em cima, barra (raio 8/8/4/4, altura
   proporcional ao maior saldo, máx. 112dp), valor abreviado embaixo (11.5sp/700).
   Mês corrente: barra `brandActive`, rótulo `ink`. Outros: oklch(0.86 0.05 300),
   rótulo `ink`@45%.
3. **Detalhamento** — eyebrow "DETALHAMENTO" + um card por mês (mais recente primeiro),
   raio 18dp, padding 13/15: nome do mês 14sp/800 (largura 62dp) · coluna com
   "Receitas" em `positive` e "Despesas" em oklch(0.52 0.16 25) (12.5sp/600 label
   `ink50` / valor 700) · à direita "SALDO" eyebrow + valor Space Grotesk 15sp/600.

### 5. Metas (`metas`)
1. Header: título "Metas" + subtítulo "R$ X guardados em N metas". À direita, botão
   "Nova" (fundo `brand`, raio 13dp, padding 10/14, 13sp/800, branco).
2. **Card de meta** (largura cheia, borda hairline, raio 24dp, padding 18dp, gap 14dp):
   - Anel 56dp: SVG, raio 24, stroke 6dp, linecap round, girado -90°, trilha
     rgba(22,24,28,.1); arco = progresso. % no centro (Space Grotesk 13sp/700).
   - Nome 16sp/800 + badge "ALCANÇADA" quando concluída (fundo oklch(0.92 0.07 155),
     texto oklch(0.42 0.13 155), 10.5sp/800, raio 6dp).
   - Linha "R$ guardado de R$ alvo" Space Grotesk 14sp/600 `ink`@55%.
   - Barra de progresso 7dp.
   - Rodapé: "Faltam R$ X" · "R$ Y/mês" (12.5sp/600 `ink50`).
   - Meta concluída: anel e barra em `positive`, fundo oklch(0.99 0.01 155),
     rodapé "Meta concluída".
   - **Toque → detalhe da meta.**

### 6. Detalhe da meta (`meta`)
1. App bar "‹" + nome da meta.
2. **Anel grande** — 164dp, raio 72, stroke 14dp, trilha oklch(0.94 0.01 300),
   arco `brandActive`. Centro: % Space Grotesk 34sp/700 + valor guardado 12sp/700
   `ink`@45%. Abaixo: "Objetivo R$ X · até mmm/aaaa" 13sp/600 `ink`@55%.
3. **Dois cards** (row, gap 9dp, raio 18dp, padding 13dp): "FALTAM" e "IDEAL POR MÊS",
   eyebrow 11sp/700 `ink`@40% + valor Space Grotesk 17sp/600.
   `ideal = (alvo - guardado) / meses até a data alvo` (mínimo 1 mês).
4. **Registrar depósito** — card raio 20dp, padding 16dp: título 13.5sp/800 + row com
   input de valor (placeholder "R$ 0,00"), input de data (104dp) e botão "Add"
   (`brand`, raio 13dp, 13sp/800). Aceita "350,00" e "350.00".
5. **Calculadora "Se eu depositar por mês…"** — card fundo oklch(0.98 0.006 250),
   raio 20dp. Valor Space Grotesk 24sp/700 à esquerda, resultado "mmm/aaaa" à direita
   em oklch(0.45 0.14 250) 12.5sp/700. Slider R$ 100–3.000, passo 50, accent
   oklch(0.45 0.15 250). Nota: "Nesse ritmo faltam N depósitos para chegar aos R$ X."
   `meses = ceil(falta / aporte)`; data prevista = mês atual + meses.
6. **Gráfico de depósitos por mês** — card raio 20dp; barras (raio 7/7/3/3, altura
   máx. 76dp, cor oklch(0.62 0.13 300)), valor abreviado em cima, mês embaixo.
   Depósitos agregados por mês.
7. **Checkbox "Alcançada"** — linha clicável, raio 18dp, padding 14/15. Caixa 24dp
   raio 7dp, borda 2dp. Marcada: fundo e borda `positive`, "✓" branco, card com fundo
   oklch(0.98 0.015 155) e borda oklch(0.88 0.07 155).
8. **Histórico de depósitos** — linhas (padding 11dp, divisor rgba(22,24,28,.07)):
   data 13.5sp/600 `ink`@60% · valor Space Grotesk 14.5sp/600. Mais recente primeiro.

### 7. Mais (`mais`)
1. Título "Mais".
2. **Categorias e orçamentos** — linha ativa: ícone 36dp (raio 11dp, fundo
   oklch(0.94 0.03 300), anel 13dp com borda 3.5dp `brandActive`) · título 14.5sp/700
   + subtítulo dinâmico ("N categorias acima do limite" / "Todos os limites em dia")
   · chevron. **Clicável.**
3. **Linhas futuras** (não clicáveis, fundo oklch(0.985 0.002 280), monograma cinza,
   badge "EM BREVE" em oklch(0.94 0.02 250) / oklch(0.48 0.1 250)):
   - CC · Cartões de crédito · "Ícones de banco prontos em BankVisuals"
   - CP · Contas a pagar · "Lembrete e notificação — plano desenhado"
   - EX · Exportar dados · "CSV do período" (sem badge)
   - NT · Notificações · "Orçamento estourado, lembretes" (sem badge)
4. **Ícones de banco prontos** — card de borda tracejada (raio 20dp, padding 16dp):
   título + nota "Aguardando a feature de cartões de crédito", e uma row de monogramas
   42dp (raio 13dp, texto branco 12.5sp/800):
   NU oklch(0.5 0.2 310) · IT oklch(0.62 0.17 55) · IN oklch(0.62 0.19 40) ·
   C6 oklch(0.3 0.01 280) · SA oklch(0.52 0.2 25) · SI oklch(0.5 0.13 155).
   **Estes são monogramas neutros de placeholder — no app, use os assets de
   `BankVisuals.kt`.**

### 8. Onboarding / estado vazio (`empty`)
Tela cheia, sem bottom nav, padding 30/26/40, gap 26dp.
1. Mark 52dp (raio 16dp, `brand`, diamante branco 18dp) + título "Bem-vindo ao\nVertize"
   (Space Grotesk 30sp/700, line-height 1.1) + parágrafo 14.5sp/600 `ink`@55%.
2. **Placeholder de ilustração** — bloco 212dp de altura, raio 22dp, fundo
   oklch(0.97 0.004 280) com hachura diagonal a 45° (rgba(22,24,28,.05)) e legenda
   monospace "ilustração de onboarding (3 telas, arte a definir)".
   **Substituir por arte real; não portar o SVG.**
3. Três bullets numerados (badge 22dp raio 7dp, fundo oklch(0.94 0.03 300), número
   `brandActive` 11sp/800) + texto 13.5sp/600 `ink`@65%:
   1. "Lance receitas e despesas em segundos, com categorias já prontas."
   2. "Defina um limite mensal por categoria e receba aviso quando estourar."
   3. "Crie metas de economia e acompanhe o quanto falta para chegar lá."
4. Rodapé: botão primário largura cheia "Adicionar primeiro lançamento" (`brand`,
   raio 17dp, padding 16dp, 14.5sp/800) + botão ghost "Explorar com dados de exemplo"
   (`ink50`, 13.5sp/700).

O estado vazio **dentro** da Home (quando o usuário já passou do onboarding e ainda não
tem lançamentos) é o que já existe no app hoje: ícone, "Nenhum lançamento por aqui",
"Toque no + para registrar seu salário ou um gasto." Mantenha essa copy.

---

## Bottom nav
Fixa, altura de conteúdo 9dp/20dp de padding, fundo branco@94% com blur 14dp,
borda superior rgba(22,24,28,.09). 5 slots de largura igual:

| Slot | Ícone (placeholder no protótipo) | Label |
|---|---|---|
| 1 | quadrado arredondado 17dp | Início |
| 2 | três barras verticais 4dp (9/16/12dp) | Histórico |
| 3 | **FAB** 54dp, raio 19dp, `brand`, "+" branco, elevado -16dp | — |
| 4 | diamante 15dp (quadrado rotacionado 45°) | Metas |
| 5 | três dots 4dp | Mais |

Ativo: `brandActive` (ícone e label). Inativo: `ink34`. Label 10.5sp/700.

**Os ícones do protótipo são formas geométricas de placeholder.** No app, use o icon set
do projeto (Material Symbols ou o set próprio): casa, gráfico de barras, +, alvo/bandeira,
menu. A **posição, o tamanho e o FAB elevado** é que devem ser reproduzidos.

Escondida em: `add` e `empty`.

Alternativa avaliada (opção `1h` no documento de design): FAB rente à barra, sem
elevação, e aba ativa em pill de fundo. Vale considerar se a elevação do + conflitar com
o comportamento de scroll.

## Toast / notificação in-app
Card absoluto no topo (top 46dp, left/right 12dp), fundo branco@92% + blur 12dp, borda
hairline, raio 20dp, padding 12/14, sombra `0 18dp 40dp -12dp rgba(22,24,28,.35)`.
Mark 34dp + "Vertize" 12.5sp/800 + "agora" 11sp/600 `ink`@45% + título 13sp/700 +
corpo 12.5sp `ink`@60%. Entra com fade + translateY(-10dp) em 280ms ease.
Auto-dismiss em 4200ms.

No app real: notificação push do sistema para estouro de orçamento; snackbar do
Material 3 para validação de formulário.

---

## Estado e comportamento

### Estado necessário
- `screen` — home | hist | add | metas | meta | mais | orc | empty
- `filter` — tudo | receitas | despesas
- `transactions: List<Transaction>` — id, tipo (receita/despesa), categoria, descrição, valor, data
- `goals: List<Goal>` — id, nome, ícone, valorAlvo, dataAlvo, alcançada, depósitos
- `deposits: List<Deposit>` — goalId, valor, data
- `budgets: Map<Categoria, Limite>` — limite mensal por categoria
- `draft` — lançamento em edição (id opcional, tipo, categoria, descrição, centavos, data)
- `selectedGoalId`
- `calcMonthly` — valor do slider da calculadora

### Derivados (não persistir)
- `totalReceitas`, `totalDespesas`, `saldo` do mês corrente
- `gastoPorCategoria` → anel + barras de progresso
- `variaçãoVsMêsAnterior` = (despesasMêsAtual − despesasMesmoPeríodoMêsAnterior) / anterior
  → InsightBanner. **Comparar o mesmo período do mês** (dia 1 até hoje), não o mês fechado.
- por meta: `guardado`, `progresso`, `falta`, `idealPorMês`, `depósitosPorMês`

### Transições
- Filtro → refiltra a lista, sem mudar de tela.
- Toque em lançamento → `add` com `draft` preenchido.
- Salvar → upsert, volta para `home`; se a categoria estourou o limite, dispara notificação.
- Excluir → remove, volta para `home`.
- Stepper de limite → ±50; se `gasto > novo limite`, dispara notificação.
- Add depósito → cria Deposit, recalcula anel, barras e o gráfico de depósitos.
- Checkbox "Alcançada" → alterna o flag da meta.

### Persistência (Room)
Entidades: `Transaction`, `Category` (predefinidas por tipo), `Budget`, `Goal`,
`Deposit`. Tudo que o usuário cria/edita persiste; nada do que está listado em
"derivados" deve ir para o banco.

### Formatação
Sempre pt-BR, `R$ 1.234,56` (2 decimais, ponto de milhar). Valores abreviados nos
gráficos: `R$ 6.367` (sem centavos). Meses abreviados minúsculos: jan…dez.

---

## Fora de escopo neste handoff
- Fluxo de **criação** de meta (nome, ícone, valor, data alvo): não desenhado; hoje o
  botão "Nova" só dispara um aviso no protótipo.
- Cartões de crédito e Contas a pagar: apenas os pontos de entrada em "Mais".

## Assets
Nenhum asset binário. Fontes: Manrope e Space Grotesk (Google Fonts).
Todos os ícones, monogramas de banco e a ilustração de onboarding são **placeholders
geométricos** — substituir pelos assets do projeto (`BankVisuals.kt` para bancos) ou por
arte real.

## Arquivos
- `Vertize.dc.html` — documento de design (protótipo navegável + telas + alternativas)
- `VertizePhone.dc.html` — implementação do protótipo (todas as telas e a lógica de estado)
- `support.js` — runtime necessário para abrir os dois arquivos acima no navegador
