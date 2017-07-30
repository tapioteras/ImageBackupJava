package fi.teras.imagebackup.copier;

import fi.teras.imagebackup.resolver.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Copier {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.imagebackup.Copier");

    private static void copy(Path from, Path to) throws IOException {
        if (!Files.exists(to)) {
            Files.createDirectories(to);
        }

        copyFile(from, to);
    }

    public static void copy(Path to, Integer year, Integer month, Path imagePath) throws IOException {
        try {
            Path imageTo = Paths.get(to.toString(), year.toString(), Resolver.resolveMonthPart(month), imagePath.getFileName().toString());
            copy(imagePath, imageTo);
        } catch (NullPointerException e) {
            log.error("null value at copy operation: ", e);
        }
    }

    private static void copyFile(Path file, Path to) throws IOException {
        log.debug("writing file stream to " + to + "...");
        Files.copy(file, to, StandardCopyOption.REPLACE_EXISTING);
    }
}