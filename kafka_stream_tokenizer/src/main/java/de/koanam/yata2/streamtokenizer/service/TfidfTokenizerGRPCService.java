package de.koanam.yata2.streamtokenizer.service;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tokenizer.TfidfTokenizer;
import tokenizer.TokenizerGrpc;
import de.koanam.yata2.streamtokenizer.util.ServiceException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TfidfTokenizerGRPCService implements TfidfTokenizerService {

    private static final Logger LOG = LoggerFactory.getLogger(TfidfTokenizerGRPCService.class);

    private TokenizerGrpc.TokenizerFutureStub tokenizer;

    public TfidfTokenizerGRPCService(String host, int port){
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public TfidfTokenizerGRPCService(ManagedChannelBuilder<?> channelBuilder) {
        ManagedChannel channel = channelBuilder.build();
        this.tokenizer = TokenizerGrpc.newFutureStub(channel);
    }

    @Override
    public List<String> transform(String content, int tokenCount) throws ServiceException{
        try {
            TfidfTokenizer.TransformRequest request = TfidfTokenizer.TransformRequest.newBuilder()
                    .setContent(content)
                    .setTokenCount(tokenCount)
                    .build();

            ListenableFuture<TfidfTokenizer.TransformReply> futureResponse = this.tokenizer.transform(request);
            TfidfTokenizer.TransformReply response = futureResponse.get();
            List<String> tokens = response.getTokensList().asByteStringList().stream().map(e -> e.toStringUtf8()).collect(Collectors.toList());

            LOG.info("Successfully got tokens from message");
            return tokens;
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceException("Error while calling gRPC", e);
        }
    }
}
