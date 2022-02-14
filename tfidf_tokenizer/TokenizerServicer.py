from tokenizer.DomainAwareTokenizer import DomainAwareTokenizer
from protos.tfidf_tokenizer_pb2_grpc import TokenizerServicer


class TokenizerServicer(TokenizerServicer):

    def __init__(self, model_path):
        self.tokenizer = DomainAwareTokenizer()
        self.tokenizer.load_model(model_path)

    def transform(self, request, context):
        return self.tokenizer.transform(request.content, request.token_count)
