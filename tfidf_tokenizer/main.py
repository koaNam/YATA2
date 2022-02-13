from DomainAwareTokenizer import DomainAwareTokenizer
from DomainWrappingCorpus import DomainWrappingCorpus
from ElasticCorpus import ElasticCorpus
from TfIdfTokenizer import TfIdfTokenizer


def create():
    default_corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "documents", debug_limit=5)
    domain_corpus = ElasticCorpus("localhost", 9200, "elastic", "elastic", "domain_documents", debug_limit=5)
    corpus = DomainWrappingCorpus(default_corpus, domain_corpus)

    tokenizer = DomainAwareTokenizer()
    tokenizer.fit(corpus)

    tokenizer.save_model("domain_tfidf_model_test")

    return tokenizer


def load(name):
    tokenizer = DomainAwareTokenizer()
    tokenizer.load_model(name)
    return tokenizer


if __name__ == '__main__':
    #tokenizer = create()
    tokenizer = load("domain_tfidf_model_test")

    while True:
        text = input("Input:")
        print(tokenizer.transform(text, 25))


