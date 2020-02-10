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
package com.graphqlio.gts.samples.uuid;

import com.graphqlio.uuid.domain.NsUrl;
import com.graphqlio.uuid.domain.TypeFormat;
import com.graphqlio.uuid.domain.UUIDDto;
import com.graphqlio.uuid.helpers.A2HS;
import com.graphqlio.uuid.helpers.UUIDHelper;

/**
 * sample for showing the use of pure uuid classes
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 * @author Torsten Kühnert
 */
public class SampleUUIDApplication {

  public static void main(String[] args) throws Exception {
    minimal();
    graphqlio();
  }

  private static void minimal() throws Exception {
    UUIDDto uuidDto = new UUIDDto();
    uuidDto.setVersionSid(5);
    uuidDto.setTypeFormatSid(TypeFormat.STD);
    uuidDto.setNsUrl(NsUrl.NS_URL);

    long[] sid = UUIDHelper.generateUUIDLongArray(uuidDto, uuidDto.getVersionSid());
    String resultUUID = A2HS.format(uuidDto.getTypeFormatSid().getTypeFormat(), sid);
    System.out.println("minimal resultUUID = " + resultUUID);
  }

  private static void graphqlio() throws Exception {
    String resultUUID =
        generateUUID(
            5, NsUrl.NS_URL, "http://engelschall.com/ns/graphql-query", "variables" + "query");
    System.out.println("graphqlio resultUUID = " + resultUUID);
  }

  private static String generateUUID(int version, NsUrl urlFormat, String url, String data)
      throws Exception {
    UUIDDto uUUIDOptions = new UUIDDto();
    uUUIDOptions.setData(url);
    uUUIDOptions.setNsUrl(urlFormat);
    uUUIDOptions.setVersion(version);
    uUUIDOptions.setTypeFormatNs(TypeFormat.STD);

    long[] uuid = UUIDHelper.generateUUIDLongArray(uUUIDOptions, uUUIDOptions.getVersion());
    String uuidFormat = A2HS.format(uUUIDOptions.getTypeFormatNs().getTypeFormat(), uuid);

    uUUIDOptions.setNs(uuid);
    uUUIDOptions.setUuidFormat(uuidFormat);
    uUUIDOptions.setData(data);
    uUUIDOptions.setVersionSid(version);
    uUUIDOptions.setTypeFormatSid(TypeFormat.STD);

    long[] sid = UUIDHelper.generateUUIDLongArray(uUUIDOptions, uUUIDOptions.getVersionSid());
    return A2HS.format(uUUIDOptions.getTypeFormatSid().getTypeFormat(), sid);
  }
}
