package net.tracystacktrace.stackem.tools;

/**
 * A simple equivalent of {@link java.util.function.Function} but with support of throwing a {@link Exception}
 *
 * @param <T> Input argument type
 * @param <R> Return argument type
 * @param <E> Throwable type
 */
@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {
    R apply(T a) throws E;
}
