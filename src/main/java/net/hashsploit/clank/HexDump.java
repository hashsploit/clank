package net.hashsploit.clank;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

public class HexDump {
	private static final int ROW_BYTES = 16;
	private static final int ROW_QTR1 = 3;
	private static final int ROW_HALF = 7;
	private static final int ROW_QTR2 = 11;

	public static void dumpHexData(PrintStream out, String title, byte[] buf, int numBytes) {
		PrintWriter wrtr = new PrintWriter(new OutputStreamWriter(out));

		HexDump.dumpHexData(wrtr, title, buf, 0, numBytes);
	}

	public static void dumpHexData(PrintWriter out, String title, byte[] buf, int offset, int numBytes) {
		int rows, residue, i, j;
		byte[] save_buf = new byte[ROW_BYTES + 2];
		char[] hex_buf = new char[4];
		char[] idx_buf = new char[8];
		char[] hex_chars = new char[20];

		hex_chars[0] = '0';
		hex_chars[1] = '1';
		hex_chars[2] = '2';
		hex_chars[3] = '3';
		hex_chars[4] = '4';
		hex_chars[5] = '5';
		hex_chars[6] = '6';
		hex_chars[7] = '7';
		hex_chars[8] = '8';
		hex_chars[9] = '9';
		hex_chars[10] = 'A';
		hex_chars[11] = 'B';
		hex_chars[12] = 'C';
		hex_chars[13] = 'D';
		hex_chars[14] = 'E';
		hex_chars[15] = 'F';

		out.println(title + " - " + numBytes + " bytes.");
		rows = numBytes >> 4;
		residue = numBytes & 0x0000000F;
		for (i = 0; i < rows; i++) {
			int hexVal = (i * ROW_BYTES);
			idx_buf[0] = hex_chars[((hexVal >> 12) & 15)];
			idx_buf[1] = hex_chars[((hexVal >> 8) & 15)];
			idx_buf[2] = hex_chars[((hexVal >> 4) & 15)];
			idx_buf[3] = hex_chars[(hexVal & 15)];

			String idxStr = new String(idx_buf, 0, 4);
			out.print(idxStr + ": ");

			for (j = 0; j < ROW_BYTES; j++) {
				save_buf[j] = buf[offset + (i * ROW_BYTES) + j];

				hex_buf[0] = hex_chars[(save_buf[j] >> 4) & 0x0F];
				hex_buf[1] = hex_chars[save_buf[j] & 0x0F];

				out.print(hex_buf[0]);
				out.print(hex_buf[1]);
				out.print(' ');

				if (j == ROW_QTR1 || j == ROW_HALF || j == ROW_QTR2)
					out.print(" ");

				if (save_buf[j] < 0x20 || save_buf[j] > 0x7E)
					save_buf[j] = (byte) '.';
			}

			String saveStr = new String(save_buf, 0, j);
			out.println(" | " + saveStr + " |");
		}

		if (residue > 0) {
			int hexVal = (i * ROW_BYTES);
			idx_buf[0] = hex_chars[((hexVal >> 12) & 15)];
			idx_buf[1] = hex_chars[((hexVal >> 8) & 15)];
			idx_buf[2] = hex_chars[((hexVal >> 4) & 15)];
			idx_buf[3] = hex_chars[(hexVal & 15)];

			String idxStr = new String(idx_buf, 0, 4);
			out.print(idxStr + ": ");

			for (j = 0; j < residue; j++) {
				save_buf[j] = buf[offset + (i * ROW_BYTES) + j];

				hex_buf[0] = hex_chars[(save_buf[j] >> 4) & 0x0F];
				hex_buf[1] = hex_chars[save_buf[j] & 0x0F];

				out.print((char) hex_buf[0]);
				out.print((char) hex_buf[1]);
				out.print(' ');

				if (j == ROW_QTR1 || j == ROW_HALF || j == ROW_QTR2)
					out.print(" ");

				if (save_buf[j] < 0x20 || save_buf[j] > 0x7E)
					save_buf[j] = (byte) '.';
			}

			for ( /* j INHERITED */ ; j < ROW_BYTES; j++) {
				save_buf[j] = (byte) ' ';
				out.print("   ");
				if (j == ROW_QTR1 || j == ROW_HALF || j == ROW_QTR2)
					out.print(" ");
			}

			String saveStr = new String(save_buf, 0, j);
			out.println(" | " + saveStr + " |");
		}
	}

	public static void main(String[] args) {
		byte[] data = new byte[132];
		for (int i = 0; i < 132; ++i)
			data[i] = (byte) i;

		HexDump.dumpHexData(System.out, "Test HexDump", data, 132);
	}

}
