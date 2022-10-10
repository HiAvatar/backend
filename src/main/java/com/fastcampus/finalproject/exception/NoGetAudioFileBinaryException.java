package com.fastcampus.finalproject.exception;

import java.io.IOException;

public class NoGetAudioFileBinaryException extends RuntimeException {
    public NoGetAudioFileBinaryException(IOException e) {super(e);}
}
