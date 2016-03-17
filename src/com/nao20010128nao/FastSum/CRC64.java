package com.nao20010128nao.FastSum;

import java.nio.ByteBuffer;
import java.util.zip.Checksum;

public class CRC64 implements Checksum {

	private static final long POLY = 0xC96C5795D7870F42L;
	private static final long CRC_TABLE[] = new long[256];

	private long crc = -1;

	static {
		for (int b = 0; b < CRC_TABLE.length; ++b) {
			long r = b;
			for (int i = 0; i < 8; ++i) {
				if ((r & 1) == 1)
					r = r >>> 1 ^ POLY;
				else
					r >>>= 1;
			}

			CRC_TABLE[b] = r;
		}
	}

	public CRC64() {
	}

	public void update(byte b) {
		crc = CRC_TABLE[(b ^ (int) crc) & 0xFF] ^ crc >>> 8;
	}

	public void update(byte[] buf) {
		update(buf, 0, buf.length);
	}

	@Override
	public void update(byte[] buf, int off, int len) {
		int end = off + len;

		while (off < end)
			crc = CRC_TABLE[(buf[off++] ^ (int) crc) & 0xFF] ^ crc >>> 8;
	}

	@Override
	public long getValue() {
		return ~crc;
	}

	@Override
	public void update(int b) {
		// TODO 自動生成されたメソッド・スタブ
		update(ByteBuffer.allocate(4).putInt(b).array(), 0, 4);
	}

	@Override
	public void reset() {
		// TODO 自動生成されたメソッド・スタブ
		crc = -1;
	}
}