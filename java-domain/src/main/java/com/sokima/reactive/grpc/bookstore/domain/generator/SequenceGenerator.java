package com.sokima.reactive.grpc.bookstore.domain.generator;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

abstract class SequenceGenerator {

    private static final AtomicLong sequenceCounter = new AtomicLong(System.currentTimeMillis());
    private static final long _13DIGITS = 10000000000000L;

    protected SequenceGenerator() {
        throw new UnsupportedOperationException("Instantiation of util class is forbidden.");
    }

    protected static String generateChecksum(
            final String value
    ) {
        return generateChecksum(value.getBytes(StandardCharsets.UTF_8));
    }

    protected static String generateChecksum(
            final byte[] bytes
    ) {
        return DigestUtils.md5Hex(bytes);
    }

    protected static long generate13SequenceNumber() {
        return sequenceCounter.incrementAndGet() % _13DIGITS;
    }
}
