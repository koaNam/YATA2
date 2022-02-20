package de.koanam.yata2.streamtokenizer.service;

import java.util.Properties;

public interface TokenizerStream {

    void init(TfidfTokenizerService tokenizer, Properties props);

    void start();

}
