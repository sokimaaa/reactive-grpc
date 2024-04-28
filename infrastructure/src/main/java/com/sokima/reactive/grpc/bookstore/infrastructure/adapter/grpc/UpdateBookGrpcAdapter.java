package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.sokima.reactive.grpc.bookstore.domain.helper.FieldOption;
import com.sokima.reactive.grpc.bookstore.proto.*;
import com.sokima.reactive.grpc.bookstore.usecase.update.UpdateBookFacade;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
public class UpdateBookGrpcAdapter extends ReactorUpdateBookUseCaseGrpc.UpdateBookUseCaseImplBase {

    private final UpdateBookFacade updateBookFacade;

    public UpdateBookGrpcAdapter(final UpdateBookFacade updateBookFacade) {
        this.updateBookFacade = updateBookFacade;
    }

    @Override
    public Mono<UpdateBookResponse> updateBook(final UpdateBookRequest request) {
        return updateBookFacade.updateField(request)
                .map(response -> UpdateBookResponse.newBuilder()
                        .setChecksum(Checksum.newBuilder().setValue(response.checksum()).build())
                        .setNewBookField(bookField(response.newField()))
                        .setOldBookField(bookField(response.oldField()))
                        .build());
    }

    private BookField bookField(final BookFieldOption option) {
        final var builder = BookField.newBuilder();
        if (FieldOption.DESCRIPTION.name().equals(option.field())) {
            return builder.setDescription(option.value())
                    .build();
        }
        return builder.build();
    }
}
