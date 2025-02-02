/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazon.dataprepper.plugins.source.loghttp.codec;

import com.linecorp.armeria.common.HttpData;

import java.io.IOException;

/**
 * Codec parses the content of HTTP request into custom Java type.
 * <p>
 */
public interface Codec<T> {
    /**
     * parse the request into custom type
     *
     * @param httpData The content of the original HTTP request
     * @return The target data type
     */
    T parse(HttpData httpData) throws IOException;
}
