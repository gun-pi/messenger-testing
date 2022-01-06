package com.messenger.modeservice;

import java.util.List;

public interface ModeService {

    List<String> getContentFromInput();

    void pullOutToOutput(String content);
}
