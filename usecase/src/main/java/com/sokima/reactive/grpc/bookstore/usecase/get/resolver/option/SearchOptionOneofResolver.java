package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option;

import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public interface SearchOptionOneofResolver extends
        OneofResolver<GetBookRequest, SearchBookOption<?>> {
}
