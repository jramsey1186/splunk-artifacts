package com.dirtdilly.loggenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        String root = "/Users/johnramsey/workspace/shared/shakespeare/";
        String category = "history"; //comedy, history, tragedy
        final String format = "no-labels"; //json, no-labels, pipe

        Path export = Paths.get(root, category + ".log"); //log, json, csv
        if (Files.exists(export)) {
            Files.delete(export);
        }
        Files.createFile(export);
        Files.writeString(export, parseFilesInFolder(root, category, format));
    }

    private static String parseFilesInFolder(String root, String directory, final String format) throws NoSuchAlgorithmException {

        var path = Paths.get(root, directory);
        final Pattern actPattern = Pattern.compile("<H3>(?<act>ACT\\s\\w*)</h3>", Pattern.CASE_INSENSITIVE);
        final Pattern scenePattern = Pattern.compile("<h3>(?<scene>SCENE\\s.*)</h3>");
        final Pattern speechPattern = Pattern.compile("<A NAME=(?<speech>speech\\d*)><b>(?<character>[\\w\\s]*)</b></a>", Pattern.CASE_INSENSITIVE);
        final Pattern linePattern = Pattern.compile("<A NAME=(?<lineNumber>\\d\\.\\d*\\.\\d*)>(?<spoken>.[\\w\\s\\-\\.\\,\\;\\:\\?\\!\\']*)</a><br>", Pattern.CASE_INSENSITIVE);
        final Pattern endquote = Pattern.compile("</blockquote>", Pattern.CASE_INSENSITIVE);
        var folder = path.toFile();
        AtomicReference<String> title = new AtomicReference<>("");
        AtomicReference<String> act = new AtomicReference<>("");
        AtomicReference<String> scene = new AtomicReference<>("");
        AtomicReference<String> speech = new AtomicReference<>("");
        AtomicReference<String> character = new AtomicReference<>("");
        AtomicReference<String> lineNumber = new AtomicReference<>("");
        AtomicReference<String> spoken = new AtomicReference<>("");
        AtomicReference<String> type = new AtomicReference<>("");
        AtomicReference<Integer> previousHash = new AtomicReference<>(0);
        AtomicReference<Integer> index = new AtomicReference<>(0);
        AtomicReference<Instant> instant = new AtomicReference<>(Instant.now());
        StringBuilder builder = new StringBuilder();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        long startFrom = Instant.now().toEpochMilli();
        if(format == "pipe") {
            builder.append(header());
        }
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            Arrays.stream(files).forEach(file -> {
                if (file.isFile()) {
                    title.set(file.getName().substring(0, file.getName().indexOf(':')));
                    clearRowData(speech, character, lineNumber, spoken, type);
                    act.set("");
                    instant.set(Instant.ofEpochMilli(startFrom));
                    try {
                        var lines = Files.readAllLines(file.toPath());
                        lines.stream().forEach(line -> {
                            index.getAndUpdate(integer -> ++integer);
                            Matcher actMatcher = actPattern.matcher(line);
                            Matcher sceneMatcher = scenePattern.matcher(line);
                            Matcher speechMatcher = speechPattern.matcher(line);
                            Matcher lineMatcher = linePattern.matcher(line);
                            if (actMatcher.find()) {
                                act.set(actMatcher.group("act"));
                                clearRowData(speech, character, lineNumber, spoken, type);
                            }
                            if (sceneMatcher.find()) {
                                scene.set(sceneMatcher.group("scene"));
                            }
                            if (speechMatcher.find()) {
                                speech.set(speechMatcher.group("speech"));
                                character.set(speechMatcher.group("character"));
                            }
                            if (lineMatcher.find()) {
                                lineNumber.set(lineMatcher.group("lineNumber"));
                                String s = lineMatcher.group("spoken");
                                spoken.set(s);

                                if (s.contains("!")) {
                                    type.set("EMPHATIC");
                                } else if (s.contains("?")) {
                                    type.set("QUESTION");
                                }
                                if (s.contains("?") && s.contains("!")) {
                                    type.getAndUpdate(x -> x + "EMPHATIC, QUESTION");
                                }
                                if (!s.contains("!") && !s.contains("?")) {
                                    type.set("NEUTRAL");
                                }
                            }
                            Random random = new SecureRandom();
                            random.ints(1, 100000);
                            long increment = Math.abs(random.nextInt() / 100000);
                            instant.getAndUpdate(instant1 -> instant1.plusMillis(increment));
                            byte[] md5 = messageDigest.digest((title.get() + speech.get()).getBytes());
                            String output = noLabels(title, act, scene, speech, character, lineNumber, spoken, type, md5);
                            int hash = output.hashCode();
                            if (hash != previousHash.get() && lineNumber.get() != "") {
                                if (format == "no-labels") {
                                    builder.append(index.get() + " - " + instant.get().toEpochMilli() + output + "\n");
                                }
                                if(format == "json") {
                                    builder.append(toJson(index, instant, title, act, scene, speech,character,lineNumber,spoken,type, md5));
                                }
                                if(format == "pipe") {
                                    builder.append(piped(index, instant, title, act, scene, speech,character,lineNumber,spoken,type, md5));
                                }

                            }
                            previousHash.set(hash);
                            if (endquote.matcher(line).find()) {
                                clearRowData(speech, character, lineNumber, spoken, type);
                            }

                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return builder.toString();
    }

    private static String noLabels(AtomicReference<String> title,
                                   AtomicReference<String> act,
                                   AtomicReference<String> scene,
                                   AtomicReference<String> speech,
                                   AtomicReference<String> character,
                                   AtomicReference<String> lineNumber,
                                   AtomicReference<String> spoken,
                                   AtomicReference<String> type,
                                   byte[] md5) {
        String output = " - " + title.get() + " - " + act.get() +
                " - " + scene.get() + " - " + Base64.getEncoder().encodeToString(md5) +
                " - " + speech.get() + " - " + character.get() +
                " - " + lineNumber.get() + " - " + spoken.get().trim() + " - " + type.get().trim();
        return output;
    }

    private static String toJson(AtomicReference<Integer> index,
                                 AtomicReference<Instant> instant,
                                 AtomicReference<String> title,
                                 AtomicReference<String> act,
                                 AtomicReference<String> scene,
                                 AtomicReference<String> speech,
                                 AtomicReference<String> character,
                                 AtomicReference<String> lineNumber,
                                 AtomicReference<String> spoken,
                                 AtomicReference<String> type,
                                 byte[] md5) {

        return String.format("{\"index\": %d, \"timestamp\": %d, " +
                        "\"title\": \"%s\", \"act\": \"%s\", \"scene\": \"%s\", " +
                        "\"speech\": \"%s\", \"character\": \"%s\", \"lineNumber\": \"%s\", \"dialog\": \"%s\", \"type\": \"%s\", \"md5\": \"%s\"}\n"
                , index.get(), instant.get().toEpochMilli(), title.get(), act.get(), scene.get(), speech.get(), character.get(), lineNumber.get(), spoken.get(), type.get(), Base64.getEncoder().encodeToString(md5));

    }
    
    private static String header() {
        return "index|timestamp|title|act|scene|speech|character|lineNumber|dialog|type|md5\n";
    }

    private static String piped(AtomicReference<Integer> index,
                                AtomicReference<Instant> instant,
                                AtomicReference<String> title,
                                AtomicReference<String> act,
                                AtomicReference<String> scene,
                                AtomicReference<String> speech,
                                AtomicReference<String> character,
                                AtomicReference<String> lineNumber,
                                AtomicReference<String> spoken,
                                AtomicReference<String> type,
                                byte[] md5) {

        return String.format("%d|%d|%s|%s|%s|%s|%s|%s|%s|%s|%s\n"
                , index.get(), instant.get().toEpochMilli(), title.get(), act.get(), scene.get(), speech.get(), character.get(), lineNumber.get(), spoken.get(), type.get(), Base64.getEncoder().encodeToString(md5));

    }

    private static void clearRowData(AtomicReference<String> speech, AtomicReference<String> character, AtomicReference<String> lineNumber, AtomicReference<String> spoken, AtomicReference<String> type) {
        lineNumber.set("");
        speech.set("");
        spoken.set("");
        character.set("");
        type.set("");
    }
}
