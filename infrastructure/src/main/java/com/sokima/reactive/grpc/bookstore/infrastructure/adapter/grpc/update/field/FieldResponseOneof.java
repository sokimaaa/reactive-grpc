package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.field;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;

public interface FieldResponseOneof extends OneofResolver<UpdateOption, BookField> {
}
