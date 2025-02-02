/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.source.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum OnErrorOption {
    DELETE_MESSAGES("delete_messages"),
    RETAIN_MESSAGES("retain_messages");

    private static final Map<String, OnErrorOption> OPTIONS_MAP = Arrays.stream(OnErrorOption.values())
            .collect(Collectors.toMap(
                    value -> value.option,
                    value -> value
            ));

    private final String option;

    OnErrorOption(final String option) {
        this.option = option;
    }

    @JsonCreator
    static OnErrorOption fromOptionValue(final String option) {
        return OPTIONS_MAP.get(option);
    }
}
