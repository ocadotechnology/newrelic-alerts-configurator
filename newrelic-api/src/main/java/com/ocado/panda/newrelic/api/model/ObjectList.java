package com.ocado.panda.newrelic.api.model;

import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode(of = "items")
public abstract class ObjectList<T, LT extends ObjectList<T, LT>> {
    private final List<T> items;
    private final Function<List<T>, LT> constructor;

    protected ObjectList(List<T> items, Function<List<T>, LT> constructor) {
        this.items = items;
        this.constructor = constructor;
    }

    public final List<T> getList() {
        return items;
    }

    public final Optional<T> getSingle() {
        List<T> list = getList();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }
        throw new IllegalStateException("Expected single element in the list but found: " + list.size());
    }

    public final LT merge(ObjectList<T, LT> list) {
        List<T> mergedItems = Stream.concat(
                items.stream(),
                list.items.stream())
                .collect(Collectors.toList());
        return constructor.apply(mergedItems);
    }

    public final LT filter(Predicate<? super T> predicate) {
        List<T> filteredItems = items.stream().filter(predicate).collect(Collectors.toList());
        return constructor.apply(filteredItems);
    }
}
