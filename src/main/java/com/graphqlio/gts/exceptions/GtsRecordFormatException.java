/**
 * *****************************************************************************
 *
 * <p>Design and Development by msg Applied Technology Research Copyright (c) 2019-2020 msg systems
 * ag (http://www.msg-systems.com/) All Rights Reserved.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * <p>****************************************************************************
 */
package com.graphqlio.gts.exceptions;

import java.util.Collections;
import java.util.List;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.language.SourceLocation;

/**
 * Exception to be thrown if Record format does not match specification
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */
public class GtsRecordFormatException extends GraphQLException implements GraphQLError {

  private static final long serialVersionUID = 1L;
  private List<SourceLocation> sourceLocations;

  public GtsRecordFormatException() {}

  public GtsRecordFormatException(String message) {
    super(message);
  }

  public GtsRecordFormatException(Throwable cause) {
    super(cause);
  }

  public GtsRecordFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public GtsRecordFormatException(String message, Throwable cause, SourceLocation sourceLocation) {
    super(message, cause);
    this.sourceLocations = Collections.singletonList(sourceLocation);
  }

  @Override
  public List<SourceLocation> getLocations() {
    return sourceLocations;
  }

  @Override
  public ErrorClassification getErrorType() {
    return ErrorType.ValidationError;
  }
}
