package com.sokima.reactive.grpc.bookstore.usecase.purchase.resolver;

import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;

public interface PurchaseOptionOneofResolver extends OneofResolver<PurchaseBookRequest, PurchaseOption<?>> {
}
