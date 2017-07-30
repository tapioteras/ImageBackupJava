package fi.teras.imagebackup;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configuration {
    public static final Map<Integer, String> MONTH_FOLDER_NAME_MAP = Collections.unmodifiableMap(
            Stream.of(new AbstractMap.SimpleEntry<>(0, "tammi"), new AbstractMap.SimpleEntry<>(1, "helmi"), new AbstractMap.SimpleEntry<>(2, "maalis"),
                    new AbstractMap.SimpleEntry<>(3, "huhti"), new AbstractMap.SimpleEntry<>(4, "touko"), new AbstractMap.SimpleEntry<>(5, "kesa"),
                    new AbstractMap.SimpleEntry<>(6, "heina"), new AbstractMap.SimpleEntry<>(7, "elo"), new AbstractMap.SimpleEntry<>(8, "syys"),
                    new AbstractMap.SimpleEntry<>(9, "loka"), new AbstractMap.SimpleEntry<>(10, "marras"), new AbstractMap.SimpleEntry<>(11, "joulu"))
                    .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

}
