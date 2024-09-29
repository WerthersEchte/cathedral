package de.fhkiel.ki.cathedral.utility;

import java.io.IOException;

public class CouldNotWriteFileRuntimeException extends RuntimeException{

  public CouldNotWriteFileRuntimeException(IOException e) {
    super(e);
  }
}
