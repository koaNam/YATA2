from ElasticCorpus import ElasticCorpus
from TfIdfTokenizer import TfIdfTokenizer

if __name__ == '__main__':
    corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "documents", debug_limit=10)
    tokenizer = TfIdfTokenizer()
    tokenizer.fit(corpus)

    while True:
        text = input("Input:")
        print(tokenizer.tokens(text, 10))
