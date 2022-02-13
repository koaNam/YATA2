import abc


class Tokenizer(abc.ABC):

    @abc.abstractmethod
    def fit(self, corpus):
        pass

    @abc.abstractmethod
    def transform(self, input_text, value_count):
        return

    @abc.abstractmethod
    def save_model(self, filepath):
        return

    @abc.abstractmethod
    def load_model(self, filepath):
        return
