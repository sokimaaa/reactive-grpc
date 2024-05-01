package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;

public interface PartialMetadataOneof extends OneofResolver<PartialBookMetadata, SearchOption<String>> {
}
