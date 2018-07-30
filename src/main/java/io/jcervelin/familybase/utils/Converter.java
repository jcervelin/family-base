package io.jcervelin.familybase.utils;

@FunctionalInterface
public interface Converter<T, F> {

    T convert(F f);

}
