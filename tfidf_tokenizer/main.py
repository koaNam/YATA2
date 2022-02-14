from concurrent import futures

import grpc

from tokenizer.DomainAwareTokenizer import DomainAwareTokenizer
from corpus.DomainWrappingCorpus import DomainWrappingCorpus
from corpus.ElasticCorpus import ElasticCorpus
from protos import tfidf_tokenizer_pb2_grpc

from TokenizerServicer import TokenizerServicer


def create():
    default_corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "documents", debug_limit=25)
    domain_corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "domain_documents", debug_limit=5)
    corpus = DomainWrappingCorpus(default_corpus, domain_corpus)

    tokenizer = DomainAwareTokenizer()
    tokenizer.fit(corpus)

    tokenizer.save_model("domain_tfidf_model_test")

    return tokenizer


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    tfidf_tokenizer_pb2_grpc.add_TokenizerServicer_to_server(TokenizerServicer("models/domain_tfidf_model_test"), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print("Server started")
    server.wait_for_termination()


if __name__ == '__main__':
    serve()
