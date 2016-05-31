package com.valarion.gameengine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class DuplicatedPrintStream extends PrintStream {
	PrintStream original;
	String prefix = "";

	public DuplicatedPrintStream(File file, PrintStream original) throws FileNotFoundException {
		super(file);
		this.original = original;
	}

	public DuplicatedPrintStream(File file, String csn, PrintStream original)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		this.original = original;
	}

	public DuplicatedPrintStream(OutputStream out, PrintStream original) {
		super(out);
		this.original = original;
	}

	public DuplicatedPrintStream(OutputStream out, boolean autoFlush, PrintStream original) {
		super(out, autoFlush);
		this.original = original;
	}

	public DuplicatedPrintStream(OutputStream out, boolean autoFlush, String encoding, PrintStream original)
			throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
		this.original = original;
	}

	public DuplicatedPrintStream(String fileName, PrintStream original) throws FileNotFoundException {
		super(fileName);
		this.original = original;
	}

	public DuplicatedPrintStream(String fileName, String csn, PrintStream original)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		this.original = original;
	}
	
	public DuplicatedPrintStream(File file, PrintStream original, String prefix) throws FileNotFoundException {
		super(file);
		this.original = original;
		this.prefix = prefix;
	}

	public DuplicatedPrintStream(File file, String csn, PrintStream original, String prefix)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		this.original = original;
		this.prefix = prefix;
	}

	public DuplicatedPrintStream(OutputStream out, PrintStream original, String prefix) {
		super(out);
		this.original = original;
		this.prefix = prefix;
	}

	public DuplicatedPrintStream(OutputStream out, boolean autoFlush, PrintStream original, String prefix) {
		super(out, autoFlush);
		this.original = original;
		this.prefix = prefix;
	}

	public DuplicatedPrintStream(OutputStream out, boolean autoFlush, String encoding, PrintStream original, String prefix)
			throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
		this.original = original;
		this.prefix = prefix;
	}

	public DuplicatedPrintStream(String fileName, PrintStream original, String prefix) throws FileNotFoundException {
		super(fileName);
		this.original = original;
		this.prefix = prefix;
	}

	public DuplicatedPrintStream(String fileName, String csn, PrintStream original, String prefix)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		this.original = original;
		this.prefix = prefix;
	}

	public void write(byte[] buf, int off, int len) {
		//super.write(prefix.getBytes(),0,prefix.getBytes().length);
		super.write(buf, off, len);
		original.write(buf, off, len);
	}

	public void write(int b) {
		super.write(b);
		original.write(b);
	}

	public void close() {
		super.close();
	}

	public void flush() {
		super.flush();
		original.flush();
	}
	
	public PrintStream getOriginal() {
		return original;
	}
}
