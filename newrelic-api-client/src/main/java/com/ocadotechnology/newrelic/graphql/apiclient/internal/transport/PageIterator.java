package com.ocadotechnology.newrelic.graphql.apiclient.internal.transport;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class PageIterator<C, W extends Page<List<C>>> implements Iterator<List<C>> {
    private W current;

    private final Function<String, Optional<W>> fetch;

    @Override
    public boolean hasNext() {
        return isNull(current) || nonNull(current.getNextCursor());
    }

    @Override
    public List<C> next() {
        if (hasNext()) {
            current = fetch.apply(Optional.ofNullable(current).map(Page::getNextCursor).orElse("")).get();
            return Optional.ofNullable(current.getContent()).orElseGet(ArrayList::new);
        }
        throw new IllegalStateException("Cannot get next");
    }
}
