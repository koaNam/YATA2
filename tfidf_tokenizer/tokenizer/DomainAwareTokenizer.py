from gensim.corpora import Dictionary

from pathlib import Path

from gensim.models import TfidfModel

from corpus.ElasticCorpus import ElasticCorpus
from tokenizer.TfIdfTokenizer import TfIdfTokenizer
from Tokenizer import Tokenizer


class DomainAwareTokenizer(Tokenizer):

    def __init__(self):
        self.corpus = None
        self.default_tokenizer = None
        self.domain_tokenizer = None

    def fit(self, corpus):
        self.corpus = corpus

        print(f"Fitting default tokenizer")
        self.default_tokenizer = TfIdfTokenizer()
        self.default_tokenizer.fit(corpus.default_corpus)

        print(f"Fitting domain tokenizer")
        self.domain_tokenizer = TfIdfTokenizer()
        self.domain_tokenizer.fit(corpus.domain_corpus)

    def transform(self, input_text, value_count):
        default_result = dict(self.default_tokenizer.transform(input_text, -1))
        domain_result = dict(self.domain_tokenizer.transform(input_text, -1))

        recall_result = {}
        for token in domain_result:
            domain = domain_result[token]
            default = default_result[token] if token in default_result else 1
            recall_result[token] = default / (domain + default)

        result = list(recall_result.items())
        return sorted(result, key=lambda x: x[1], reverse=True)[:value_count]

    def save_model(self, filepath):
        Path(filepath).mkdir(parents=True, exist_ok=True)

        self.default_tokenizer.model.save(f"{filepath}/default_tok")
        self.domain_tokenizer.model.save(f"{filepath}/domain_tok")
        self.corpus.dictionary.save(f"{filepath}/dict")

    def load_model(self, filepath):
        default_model = TfidfModel.load(f"{filepath}/default_tok")
        domain_model = TfidfModel.load(f"{filepath}/domain_tok")
        dictionary = Dictionary.load(f"{filepath}/dict")

        self.default_tokenizer = TfIdfTokenizer(model=default_model, corpus=ElasticCorpus(dictionary=dictionary))
        self.domain_tokenizer = TfIdfTokenizer(model=domain_model, corpus=ElasticCorpus(dictionary=dictionary))
