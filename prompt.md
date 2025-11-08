Atue como um Arquiteto de Software Java Sênior especialista em Java, Service, Controller e afins com mais de 20 anos de experiência.

Estou com o arquivo relevante pro erro aberto, o erro do mvn test é:
[ERROR] ApiTests.createReturns201AndLocation:40 Status expected:<201> but was:<405>
[ERROR] ApiTests.deleteReturns204:81 Status expected:<201> but was:<405>
[ERROR] ApiTests.listIsPaged:52 Status expected:<201> but was:<405>
[ERROR] ApiTests.statsCountsOverdueProperly:67 Status expected:<201> but was:<405>

Essa classe tem o http incorreto, usa um get para criar e deletar.

Envie o código para corrigir isso.



------------


Agora estou no Task Service, nela tem uma lógica incorreta para atrasados e o calculo ineficiente de histograma.
Corrija a comparação de datas e use Stream API com Collectors.groupingBy


--------

[ERROR] Failures:
[ERROR] ApiTests.deleteReturns204:86 Status expected:<404> but was:<204>
[ERROR] ApiTests.statsCountsOverdueProperly:72 JSON path "$.overdueCount"
Expected: a value equal to or greater than <1>
but: <0> was less than <1>
[ERROR] Errors:
[ERROR] ApiTests.listIsPaged:54 » Servlet Request processing failed: java.lang.IllegalArgumentException: Name for argument of type [br.fiap.arena.domain.TaskStatus] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.
[ERROR] Tests run: 4, Failures: 2, Errors: 1, Skipped: 0

Muito bem, agora com suas alterações estamos passando pelo erro enviado, corrija-o.

-----

Ainda está dando o seguinte erro:
[ERROR] Failures: 
[ERROR]   ApiTests.statsCountsOverdueProperly:72 JSON path "$.overdueCount"
Expected: a value equal to or greater than <1>
     but: <0> was less than <1>
[ERROR] Errors: 
[ERROR]   ApiTests.listIsPaged:54 » Servlet Request processing failed: java.lang.IllegalArgumentException: Name for argument of type [int] not specified, and parameter name information not available via reflection. Ensure that the compiler uses the '-parameters' flag.
[ERROR] Tests run: 4, Failures: 1, Errors: 1, Skipped: 0

Esse é o teste:
@Test
    void statsCountsOverdueProperly() throws Exception {
        var past = Map.of("title","Venceu ontem","priority",2,"status",TaskStatus.OPEN.name(),
                "dueDate", LocalDate.now().minusDays(1).toString());
        var future = Map.of("title","Vence amanhã","priority",2,"status",TaskStatus.OPEN.name(),
                "dueDate", LocalDate.now().plusDays(1).toString());
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(past)))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(future)))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/tasks/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overdueCount", greaterThanOrEqualTo(1)));
    }