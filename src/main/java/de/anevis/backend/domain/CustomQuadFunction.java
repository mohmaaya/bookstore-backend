package de.anevis.backend.domain;

@FunctionalInterface
public interface CustomQuadFunction<S, T, U, V, R> {
    R apply(S s, T t, U u, V v);
}
