package com.nao20010128nao.FastSum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class Main {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		// TODO 自動生成されたメソッド・スタブ
		if (args.length == 0) {
			System.err.println("Error!");
			return;
		}

		File f = new File(args[0]);

		FileInputStream fis = new FileInputStream(f);
		MessageDigest md5, sha1, sha256;
		CRC32 crc32;
		CRC64 crc64;
		crc32 = new CRC32();
		crc64 = new CRC64();
		md5 = MessageDigest.getInstance("MD5");
		sha1 = MessageDigest.getInstance("SHA-1");
		sha256 = MessageDigest.getInstance("SHA-256");

		byte[] buffer = new byte[32768 * 4];
		final long fSize = f.length();
		long size = 0;
		int lastRead = -1;

		while (true) {
			lastRead = fis.read(buffer);
			if (lastRead <= 0) {
				fis.close();
				break;
			}

			size += lastRead;

			crc32.update(buffer, 0, lastRead);
			crc64.update(buffer, 0, lastRead);
			md5.update(buffer, 0, lastRead);
			sha1.update(buffer, 0, lastRead);
			sha256.update(buffer, 0, lastRead);

			System.out.println(size + "/" + fSize + " (" + percentage(size, fSize) + "%)");
		}

		System.out.println("CRC32   :" + dump(ByteBuffer.allocate(4).putInt((int) crc32.getValue()).array()));
		System.out.println("CRC64   :" + dump(ByteBuffer.allocate(8).putLong(crc64.getValue()).array()));
		System.out.println("MD5     :" + dump(md5.digest()));
		System.out.println("SHA-1   :" + dump(sha1.digest()));
		System.out.println("SHA-256 :" + dump(sha256.digest()));

	}

	static String percentage(long size, long fSize) {
		BigDecimal sBD = BigDecimal.valueOf(size);
		BigDecimal fBD = BigDecimal.valueOf(fSize);
		return sBD.divide(fBD, 20, RoundingMode.DOWN).multiply(BigDecimal.valueOf(100)).intValue() + "";
	}

	static String dump(byte[] array) {
		StringBuilder sb = new StringBuilder(array.length * 2);

		for (byte b : array) {
			sb.append(Character.forDigit(b >> 4 & 0xf, 16));
			sb.append(Character.forDigit(b & 0xf, 16));
		}

		return sb.toString();
	}
}
