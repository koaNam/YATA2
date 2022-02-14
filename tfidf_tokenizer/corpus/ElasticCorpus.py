from elasticsearch import Elasticsearch
from elasticsearch import helpers
import spacy
import re

from gensim.corpora import Dictionary


class ElasticCorpus:

    def __init__(self, host=None, port=None, username=None, password=None, index=None, debug_limit=None,
                 dictionary=Dictionary()):
        if host:
            self.elastic = Elasticsearch([{'host': host, 'port': port}], http_auth=(username, password))
            self.index = index
            self.debug_limit = debug_limit

        self.space = spacy.load('en_core_web_sm')
        self.dictionary = dictionary

    def __get_document_count(self):
        return self.elastic.count(index=self.index, body={'query': {"match_all": {}}})["count"]

    def __iter__(self):
        counter = 0

        if self.debug_limit:
            document_counter = self.debug_limit
        else:
            document_counter = self.__get_document_count()

        steps = document_counter // 100 if document_counter > 100 else 1
        for entry in helpers.scan(self.elastic, query={"query": {"match_all": {}}}, _source=["content"],
                                  index=self.index, size=2000):
            text = entry["_source"]["content"]
            text = re.sub('[^A-Za-z0-9 ]+', '', text)
            text = re.sub(' +', ' ', text)

            doc = self.space(text)
            tokens = [t.lemma_.upper() for t in doc if not t.is_stop]
            self.dictionary.add_documents([tokens])

            if counter % steps == 0:
                print(f"Progress: {(counter / document_counter) * 100} %")
            if self.debug_limit and self.debug_limit == counter:
                break

            counter = counter + 1
            yield self.dictionary.doc2bow(tokens)
