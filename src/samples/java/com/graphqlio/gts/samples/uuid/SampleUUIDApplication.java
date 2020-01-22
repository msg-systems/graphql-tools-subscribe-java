package com.graphqlio.gts.samples.uuid;

import com.graphqlio.uuid.domain.NsUrl;
import com.graphqlio.uuid.domain.TypeFormat;
import com.graphqlio.uuid.domain.UUIDDto;
import com.graphqlio.uuid.helpers.A2HS;
import com.graphqlio.uuid.helpers.UUIDHelper;

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
		String resultUUID = generateUUID(5, NsUrl.NS_URL, "http://engelschall.com/ns/graphql-query",
				"variables" + "query");
		System.out.println("graphqlio resultUUID = " + resultUUID);
	}

	private static String generateUUID(int version, NsUrl urlFormat, String url, String data) throws Exception {
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
