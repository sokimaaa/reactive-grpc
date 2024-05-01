package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;

public interface SearchOptionOneof<T> extends OneofResolver<GetBookRequest, SearchOption<T>> {
}
