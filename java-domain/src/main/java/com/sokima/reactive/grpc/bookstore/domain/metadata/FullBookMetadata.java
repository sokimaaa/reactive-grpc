package com.sokima.reactive.grpc.bookstore.domain.metadata;

import org.immutables.value.Value;

@Value.Immutable
public interface FullBookMetadata {
    /**
     * @return the title value.
     */
    String title();

    /**
     * @return the author value.
     */
    String author();

    /**
     * @return the edition value.
     */
    String edition();
}
