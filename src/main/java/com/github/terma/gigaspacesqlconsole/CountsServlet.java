package com.github.terma.gigaspacesqlconsole;

public class CountsServlet extends JsonServlet<CountsRequest> {

    @Override
    protected Object doJson(CountsRequest request) throws Exception {
        return Counts.counts(request);
    }

    @Override
    protected Class getRequestClass() {
        return CountsRequest.class;
    }

}
