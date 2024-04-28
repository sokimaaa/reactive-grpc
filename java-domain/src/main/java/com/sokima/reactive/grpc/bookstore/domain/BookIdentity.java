package com.sokima.reactive.grpc.bookstore.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface BookIdentity {

    /**
     * Business field that represents book title.
     *
     * @return the title value.
     */
    String title();

    /**
     * Business field that represents book author.
     *
     * @return the author value.
     */
    String author();

    /**
     * Business field that represents book edition.
     *
     * @return the edition value.
     */
    String edition();

    /**
     * Business field that represents book description.
     * <p/>
     * Potentially nullable field.
     *
     * @return the description value.
     */
    String description();
}
