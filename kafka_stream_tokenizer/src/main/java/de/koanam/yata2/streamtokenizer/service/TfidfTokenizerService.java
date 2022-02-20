package de.koanam.yata2.streamtokenizer.service;

import de.koanam.yata2.streamtokenizer.util.ServiceException;

import java.util.List;

public interface TfidfTokenizerService {

    List<String> transform(String content, int tokenCount) throws ServiceException;

}
