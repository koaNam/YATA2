from argparse import ArgumentParser
from datetime import datetime
from concurrent import futures

import grpc

from tokenizer.DomainAwareTokenizer import DomainAwareTokenizer
from corpus.DomainWrappingCorpus import DomainWrappingCorpus
from corpus.ElasticCorpus import ElasticCorpus
from protos import tfidf_tokenizer_pb2_grpc

from TokenizerServicer import TokenizerServicer


def init_argparser():
    parser = ArgumentParser(description="Start a tokenizer that can be accessed via a gRPC API")
    parser.add_argument("--port", help='Port for the gRPC API')
    parser.add_argument("--model", help='Path to the previously trained model')

    return parser


def create():
    start = datetime.now()
    print(f"Starting: {start}")
    default_corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "documents", debug_limit=20000)
    domain_corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "domain_documents",)
    corpus = DomainWrappingCorpus(default_corpus, domain_corpus)

    tokenizer = DomainAwareTokenizer()
    tokenizer.fit(corpus)

    end = datetime.now()
    print(f"Finished (wie Wikingercrews): {end}")
    print(f"Took: {end - start}")
    tokenizer.save_model("models/whiskey_review_model")

    return tokenizer


def serve(port, model):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    tfidf_tokenizer_pb2_grpc.add_TokenizerServicer_to_server(TokenizerServicer(f"{model}"), server)
    #server.add_insecure_port(f'[::]:{port}')
    server.add_insecure_port('[::]:50051')
    server.start()
    print(f"Server started on port {port} with model '{model}'")
    server.wait_for_termination()


if __name__ == '__main__':
    argparser = init_argparser()
    args = argparser.parse_args()
    serve(args.port, args.model)

    #create()
