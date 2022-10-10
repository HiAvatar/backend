package com.fastcampus.finalproject.exception;

import java.io.IOException;

public class NoCreateUploadAudioFileException extends RuntimeException {
    public NoCreateUploadAudioFileException(IOException e) {super(e);}
}
