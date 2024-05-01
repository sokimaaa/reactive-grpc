package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.option;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;

public interface PurchaseOptionOneof<P> extends OneofResolver<PurchaseBookRequest, PurchaseOption<P>> {
}
