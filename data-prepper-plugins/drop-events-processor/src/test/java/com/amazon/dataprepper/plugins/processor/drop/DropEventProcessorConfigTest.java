/*
 *
 *  * Copyright OpenSearch Contributors
 *  * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.amazon.dataprepper.plugins.processor.drop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class DropEventProcessorConfigTest {
    private DropEventProcessorConfig config;

    @BeforeEach
    void setUp() {
        config = new DropEventProcessorConfig();
    }

    @Test
    void getWhen() {
        assertThat(config.getDropWhen(), is(nullValue()));
    }

    @Test
    void getHandleFailedEventsOption() {
        assertThat(config.getHandleFailedEventsOption(), is(HandleFailedEventsOption.SKIP));
    }

}