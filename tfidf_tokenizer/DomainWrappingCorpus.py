from gensim.corpora import Dictionary


class DomainWrappingCorpus:

    def __init__(self, default_corpus, domain_corpus):
        self.dictionary = Dictionary()

        default_corpus.dictionary = self.dictionary
        self.default_corpus = default_corpus

        domain_corpus.dictionary = self.dictionary
        self.domain_corpus = domain_corpus
