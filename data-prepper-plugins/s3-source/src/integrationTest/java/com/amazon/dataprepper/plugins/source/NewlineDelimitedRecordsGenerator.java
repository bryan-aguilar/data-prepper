/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.source;

import com.amazon.dataprepper.model.event.Event;
import com.amazon.dataprepper.plugins.source.codec.Codec;
import com.amazon.dataprepper.plugins.source.codec.NewlineDelimitedCodec;
import com.amazon.dataprepper.plugins.source.codec.NewlineDelimitedConfig;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Generates records where each record is on a single line.
 */
class NewlineDelimitedRecordsGenerator implements RecordsGenerator {
    private final String KNOWN_HTTP_LINE = "GET /my/endpoint HTTP/1.1 200";
    private final Random random = new Random();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:hh:mm:ss");

    @Override
    public void write(final int numberOfRecords, final OutputStream outputStream) {
        try (final PrintWriter printWriter = new PrintWriter(outputStream)) {
            for (int i = 0; i < numberOfRecords; i++) {
                writeLine(printWriter);
            }
        }
    }

    @Override
    public Codec getCodec() {
        return new NewlineDelimitedCodec(new NewlineDelimitedConfig());
    }

    @Override
    public String getFileExtension() {
        return "txt";
    }

    @Override
    public void assertEventIsCorrect(final Event event) {
        final String message = event.get("message", String.class);
        assertThat(message, notNullValue());
        assertThat(message, containsString(KNOWN_HTTP_LINE));
    }

    private void writeLine(final PrintWriter printWriter) {
        final String dateString = dateTimeFormatter.format(LocalDateTime.now());
        final String line = "127.0.0.1 - - [" + dateString + "] " + KNOWN_HTTP_LINE + " " + random.nextInt(3000) + " \"http://localhost\" \"Mozilla/5.0\"";
        printWriter.write(line + "\n");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
