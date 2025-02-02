/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.source;

import com.amazon.dataprepper.model.event.Event;
import com.amazon.dataprepper.plugins.source.codec.Codec;
import com.amazon.dataprepper.plugins.source.codec.JsonCodec;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

class JsonRecordsGenerator implements RecordsGenerator {

    public static final String EVENT_VERSION_FIELD = "eventVersion";
    public static final String EVENT_VERSION_VALUE = "1.0";
    public static final int KNOWN_FIELD_COUNT_PER_EVENT = 7;
    private final JsonFactory jsonFactory = new JsonFactory();

    @Override
    public void write(final int numberOfRecords, final OutputStream outputStream) throws IOException {
        try (final JsonGenerator jsonGenerator = jsonFactory
                .createGenerator(outputStream, JsonEncoding.UTF8)) {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeArrayFieldStart("Records");

            for (int i = 0; i < numberOfRecords; i++) {
                writeSingleRecord(jsonGenerator);
            }

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }

    @Override
    public Codec getCodec() {
        return new JsonCodec();
    }

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    public void assertEventIsCorrect(final Event event) {

        final Map<String, Object> messageMap = event.get("message", Map.class);
        assertThat(messageMap, notNullValue());
        assertThat(messageMap.size(), greaterThanOrEqualTo(KNOWN_FIELD_COUNT_PER_EVENT));
        assertThat(messageMap.get(EVENT_VERSION_FIELD), equalTo(EVENT_VERSION_VALUE));
    }

    private void writeSingleRecord(final JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField(EVENT_VERSION_FIELD, EVENT_VERSION_VALUE);
        jsonGenerator.writeStringField("eventTime", Instant.now().toString());
        jsonGenerator.writeStringField("eventSource", "ec2.amazonaws.com");
        jsonGenerator.writeStringField("eventName", "StartInstances");
        jsonGenerator.writeStringField("awsRegion", "us-east-1");
        jsonGenerator.writeStringField("sourceIPAddress", "ec2-api-tools 1.6.12.2");
        jsonGenerator.writeStringField("userAgent", "us-east-1");

        jsonGenerator.writeObjectFieldStart("userIdentity");
        jsonGenerator.writeStringField("type", "IAMUser");
        jsonGenerator.writeStringField("principalId", UUID.randomUUID().toString());
        final String userName = UUID.randomUUID().toString();
        jsonGenerator.writeStringField("arn", "arn:aws:iam::123456789012:user/" + userName);
        jsonGenerator.writeStringField("accountId", "123456789012");
        jsonGenerator.writeStringField("userName", userName);
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
