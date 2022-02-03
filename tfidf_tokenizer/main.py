from ElasticCorpus import ElasticCorpus
from TfIdfTokenizer import TfIdfTokenizer


def create():
    corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "documents", debug_limit=10)
    tokenizer = TfIdfTokenizer()
    tokenizer.fit(corpus)

    tokenizer.save_model("tfidf_model_test_10")

    return tokenizer


def load():
    tokenizer = TfIdfTokenizer()
    tokenizer.load_model("tfidf_model_50k")
    return tokenizer


if __name__ == '__main__':
    #tokenizer = create()
    tokenizer = load()

    while True:
        text = input("Input:")
        print(tokenizer.transform(text, 10))
