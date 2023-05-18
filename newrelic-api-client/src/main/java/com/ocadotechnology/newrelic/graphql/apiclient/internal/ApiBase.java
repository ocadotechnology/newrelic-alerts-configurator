package com.ocadotechnology.newrelic.graphql.apiclient.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.k0kubun.builder.query.graphql.GraphQLQueryBuilder;
import com.github.k0kubun.builder.query.graphql.builder.ObjectBuilder;
import com.github.k0kubun.builder.query.graphql.model.GraphQLObject;
import com.ocadotechnology.newrelic.graphql.apiclient.GraphqlApiError;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Query;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.Result;
import com.ocadotechnology.newrelic.graphql.apiclient.internal.transport.mutation.MutationResult;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.core.json.JsonWriteFeature.QUOTE_FIELD_NAMES;

@RequiredArgsConstructor
public abstract class ApiBase {
    private final QueryExecutor executor;

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(QUOTE_FIELD_NAMES.mappedFeature())
            .setSerializationInclusion(NON_NULL);

    protected Result execute(Query query) {
        return executor.execute(query);
    }

    protected GraphQLObject object(UnaryOperator<ObjectBuilder> custom) {
        return custom.apply(GraphQLQueryBuilder.object()).build();
    }


    protected Map<String, Object> params(String name1, Object value1, String name2, Object value2) {
        Map<String, Object> params = param(name1, value1);
        params.put(name2, value2);
        return params;
    }

    protected Map<String, Object> params(String name1, Object value1, String name2, Object value2, String name3, Object value3) {
        Map<String, Object> params = params(name1, value1, name2, value2);
        params.put(name3, value3);
        return params;
    }

    protected ParamValueWrapper complexParamValue(Map<String, Object> props) {
        try {
            return new ParamValueWrapper(OBJECT_MAPPER.writeValueAsString(props));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<String, Object> param(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }

    protected Result throwOnError(Result result) {
        if (result.hasErrors()) {
            throw new GraphqlApiError(result.getErrors().toString());
        }
        return result;
    }

    protected <T> Optional<T> throwOnError(Result result, Function<Result, Optional<? extends MutationResult<T>>> resultGetter) {
        return resultGetter.apply(throwOnError(result))
                .map(r -> {
                    if (r.hasErrors()) {
                        throw new GraphqlApiError(r.getErrors().toString());
                    }
                    return r.get();
                });
    }

    protected <T> Stream<T> asStream(Iterator<T> sourceIterator) {
        return StreamSupport.stream(((Iterable<T>) () -> sourceIterator).spliterator(), false);
    }

    @RequiredArgsConstructor
    protected static class ParamValueWrapper {
        private final String value;

        @Override
        public String toString() {
            return value;
        }
    }
}
