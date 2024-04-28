package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.partial;

import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public interface PartialMetadataOneofResolver extends OneofResolver<PartialBookMetadata, SearchBookOption<String>> {
}
