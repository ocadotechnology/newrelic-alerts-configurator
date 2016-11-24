package com.ocado.pandateam.newrelic.api.model;

import com.ocado.pandateam.newrelic.api.NewRelicApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ObjectList<T> {

    public abstract List<T> getList();

    public Optional<T> getSingle() throws NewRelicApiException {
        List<T> list = getList();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }
        throw new NewRelicApiException("Expected single element in the list but found: " + list.size());
    }

    public static <T> List<T> merge(ObjectList<T> list1, ObjectList<T> list2) {
        return Stream.concat(
                list1.getList().stream(),
                list2.getList().stream())
                .collect(Collectors.toList());
    }
}
