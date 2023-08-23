package de.anevis.backend.domain;

@FunctionalInterface
public interface CustomFunction<S, T, U, V, R> {
    R apply(S s, T t, U u, V v);
}
