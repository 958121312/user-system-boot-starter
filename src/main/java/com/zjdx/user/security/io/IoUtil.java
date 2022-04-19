package com.zjdx.user.security.io;



import com.zjdx.user.security.io.copy.ReaderWriterCopier;
import com.zjdx.user.security.io.copy.StreamCopier;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.CharBuffer;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 *
 * @author yuntian
 */
public class IoUtil  {
	/**
	 * 默认缓存大小 8192
	 */
	public static final int DEFAULT_BUFFER_SIZE = 2 << 12;
	// -------------------------------------------------------------------------------------- Copy start

	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小，拷贝后不关闭Reader
	 *
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer) throws IORuntimeException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader     Reader
	 * @param writer     Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IORuntimeException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(reader, writer, bufferSize, -1, streamProgress);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param count          最大长度
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
		return new ReaderWriterCopier(bufferSize, count, streamProgress).copy(reader, writer);
	}

	/**
	 * 拷贝流，使用默认Buffer大小，拷贝后不关闭流
	 *
	 * @param in  输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in         输入流
	 * @param out        输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param count          总拷贝长度
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
		return new StreamCopier(bufferSize, count, streamProgress).copy(in, out);
	}

	// -------------------------------------------------------------------------------------- getReader and getWriter start


	/**
	 * 从流中读取内容，读到输出流中，读取完毕后关闭流
	 *
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
		return read(in, true);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @param in      输入流
	 * @param isClose 读取完毕后是否关闭流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @since 5.5.3
	 */
	public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IORuntimeException {
		final FastByteArrayOutputStream out;
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			try {
				out = new FastByteArrayOutputStream(in.available());
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			out = new FastByteArrayOutputStream();
		}
		try {
			copy(in, out);
		} finally {
			if (isClose) {
				close(in);
			}
		}
		return out;
	}

	/**
	 * 从Reader中读取String，读取完毕后关闭Reader
	 *
	 * @param reader Reader
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader) throws IORuntimeException {
		return read(reader, true);
	}

	/**
	 * 从{@link Reader}中读取String
	 *
	 * @param reader  {@link Reader}
	 * @param isClose 是否关闭{@link Reader}
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader, boolean isClose) throws IORuntimeException {
		final StringBuilder builder = new StringBuilder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip());
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isClose) {
				IoUtil.close(reader);
			}
		}
		return builder.toString();
	}

	/**
	 * 从流中读取bytes，读取完毕后关闭流
	 *
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in) throws IORuntimeException {
		return readBytes(in, true);
	}

	/**
	 * 从流中读取bytes
	 *
	 * @param in      {@link InputStream}
	 * @param isClose 是否关闭输入流
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 * @since 5.0.4
	 */
	public static byte[] readBytes(InputStream in, boolean isClose) throws IORuntimeException {
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			final byte[] result;
			try {
				final int available = in.available();
				result = new byte[available];
				final int readLength = in.read(result);
				if (readLength != available) {
					throw new IOException(String.format("File length is %s but read %s!", available, readLength));
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			} finally {
				if (isClose) {
					close(in);
				}
			}
			return result;
		}

		// 未知bytes总量的流
		return read(in, isClose).toByteArray();
	}

	/**
	 * 读取指定长度的byte数组，不关闭流
	 *
	 * @param in     {@link InputStream}，为null返回null
	 * @param length 长度，小于等于0返回空byte数组
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in, int length) throws IORuntimeException {
		if (null == in) {
			return null;
		}
		if (length <= 0) {
			return new byte[0];
		}

		byte[] b = new byte[length];
		int readLength;
		try {
			readLength = in.read(b);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		if (readLength > 0 && readLength < length) {
			byte[] b2 = new byte[readLength];
			System.arraycopy(b, 0, b2, 0, readLength);
			return b2;
		} else {
			return b;
		}
	}


	// -------------------------------------------------------------------------------------- read end

	/**
	 * 文件转为{@link FileInputStream}
	 *
	 * @param file 文件
	 * @return {@link FileInputStream}
	 */
	public static FileInputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * byte[] 转为{@link ByteArrayInputStream}
	 *
	 * @param content 内容bytes
	 * @return 字节流
	 * @since 4.1.8
	 */
	public static ByteArrayInputStream toStream(byte[] content) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(content);
	}

	/**
	 * {@link ByteArrayOutputStream}转为{@link ByteArrayInputStream}
	 *
	 * @param out {@link ByteArrayOutputStream}
	 * @return 字节流
	 * @since 5.3.6
	 */
	public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BufferedInputStream}
	 * @since 4.0.10
	 */
	public static BufferedInputStream toBuffered(InputStream in) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in         {@link InputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedInputStream}
	 * @since 5.6.1
	 */
	public static BufferedInputStream toBuffered(InputStream in, int bufferSize) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in, bufferSize);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @return {@link BufferedOutputStream}
	 * @since 4.0.10
	 */
	public static BufferedOutputStream toBuffered(OutputStream out) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out        {@link OutputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedOutputStream}
	 * @since 5.6.1
	 */
	public static BufferedOutputStream toBuffered(OutputStream out, int bufferSize) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out, bufferSize);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader {@link Reader}
	 * @return {@link BufferedReader}
	 * @since 5.6.1
	 */
	public static BufferedReader toBuffered(Reader reader) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader     {@link Reader}
	 * @param bufferSize buffer size
	 * @return {@link BufferedReader}
	 * @since 5.6.1
	 */
	public static BufferedReader toBuffered(Reader reader, int bufferSize) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader, bufferSize);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer {@link Writer}
	 * @return {@link BufferedWriter}
	 * @since 5.6.1
	 */
	public static BufferedWriter toBuffered(Writer writer) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer     {@link Writer}
	 * @param bufferSize buffer size
	 * @return {@link BufferedWriter}
	 * @since 5.6.1
	 */
	public static BufferedWriter toBuffered(Writer writer, int bufferSize) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer, bufferSize);
	}

	/**
	 * 将{@link InputStream}转换为支持mark标记的流<br>
	 * 若原流支持mark标记，则返回原流，否则使用{@link BufferedInputStream} 包装之
	 *
	 * @param in 流
	 * @return {@link InputStream}
	 * @since 4.0.9
	 */
	public static InputStream toMarkSupportStream(InputStream in) {
		if (null == in) {
			return null;
		}
		if (false == in.markSupported()) {
			return new BufferedInputStream(in);
		}
		return in;
	}

	/**
	 * 转换为{@link PushbackInputStream}<br>
	 * 如果传入的输入流已经是{@link PushbackInputStream}，强转返回，否则新建一个
	 *
	 * @param in           {@link InputStream}
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackInputStream}
	 * @since 3.1.0
	 */
	public static PushbackInputStream toPushbackStream(InputStream in, int pushBackSize) {
		return (in instanceof PushbackInputStream) ? (PushbackInputStream) in : new PushbackInputStream(in, pushBackSize);
	}

	/**
	 * 将指定{@link InputStream} 转换为{@link InputStream#available()}方法可用的流。<br>
	 * 在Socket通信流中，服务端未返回数据情况下{@link InputStream#available()}方法始终为{@code 0}<br>
	 * 因此，在读取前需要调用{@link InputStream#read()}读取一个字节（未返回会阻塞），一旦读取到了，{@link InputStream#available()}方法就正常了。<br>
	 * 需要注意的是，在网络流中，是按照块来传输的，所以 {@link InputStream#available()} 读取到的并非最终长度，而是此次块的长度。<br>
	 * 此方法返回对象的规则为：
	 *
	 * <ul>
	 *     <li>FileInputStream 返回原对象，因为文件流的available方法本身可用</li>
	 *     <li>其它InputStream 返回PushbackInputStream</li>
	 * </ul>
	 *
	 * @param in 被转换的流
	 * @return 转换后的流，可能为{@link PushbackInputStream}
	 * @since 5.5.3
	 */
	public static InputStream toAvailableStream(InputStream in) {
		if (in instanceof FileInputStream) {
			// FileInputStream本身支持available方法。
			return in;
		}

		final PushbackInputStream pushbackInputStream = toPushbackStream(in, 1);
		try {
			final int available = pushbackInputStream.available();
			if (available <= 0) {
				//此操作会阻塞，直到有数据被读到
				int b = pushbackInputStream.read();
				pushbackInputStream.unread(b);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		return pushbackInputStream;
	}


	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param obj        写入的对象内容
	 * @throws IORuntimeException IO异常
	 * @since 5.3.3
	 */
	public static void writeObj(OutputStream out, boolean isCloseOut, Serializable obj) throws IORuntimeException {
		writeObjects(out, isCloseOut, obj);
	}

	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeObjects(OutputStream out, boolean isCloseOut, Serializable... contents) throws IORuntimeException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
				}
			}
			osw.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 从缓存中刷出数据
	 *
	 * @param flushable {@link Flushable}
	 * @since 4.2.2
	 */
	public static void flush(Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			} catch (Exception e) {
				// 静默刷出
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}

}
