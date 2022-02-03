import re

import spacy
from gensim.corpora import Dictionary
from gensim.models import TfidfModel

from pathlib import Path
from ElasticCorpus import ElasticCorpus


class TfIdfTokenizer:

    def __init__(self):
        self.space = spacy.load('en_core_web_sm')
        self.corpus = None
        self.model = None

    def fit(self, corpus):
        self.corpus = corpus
        self.model = TfidfModel(corpus)

    def transform(self, input_text, value_count):
        input_text = self.__preprocess_input(input_text)

        dictionary = self.corpus.dictionary
        result = self.model.__getitem__(dictionary.doc2bow(input_text))
        result = [(dictionary[r[0]], r[1]) for r in result]

        result.sort(key=lambda x: x[1], reverse=True)

        return result[:value_count]

    def save_model(self, filepath):
        Path(filepath).mkdir(parents=True, exist_ok=True)

        self.model.save(f"{filepath}/model")
        self.corpus.dictionary.save(f"{filepath}/dict")

    def load_model(self, filepath):
        self.model = TfidfModel.load(f"{filepath}/model")
        self.corpus = ElasticCorpus(dictionary=Dictionary.load(f"{filepath}/dict"))

    def __preprocess_input(self, text):
        text = re.sub('[^A-Za-z0-9 ]+', '', text)
        text = re.sub(' +', ' ', text)
        doc = self.space(text)
        tokens = [t.lemma_.upper() for t in doc if not t.is_stop]

        return tokens
