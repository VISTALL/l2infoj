package jds.nio.buffer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.*;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.EnumSet;
import java.util.Set;

/**
 * A byte buffer used by MINA applications.
 * <p>
 * This is a replacement for {@link java.nio.ByteBuffer}. Please refer to
 * {@link java.nio.ByteBuffer} documentation for preliminary usage.  MINA does
 * not use NIO {@link java.nio.ByteBuffer} directly for two reasons:
 * <ul>
 * <li>It doesn't provide useful getters and putters such as
 * <code>fill</code>, <code>get/putString</code>, and
 * <code>get/putAsciiInt()</code> enough.</li>
 * <li>It is difficult to write variable-length data due to its fixed
 * capacity</li>
 * </ul>
 * </p>
 * <p/>
 * <h2>AlLocation</h2>
 * <p>
 * You can alLocationate a new heap buffer.
 * <pre>
 * IoBuffer buf = IoBuffer.alLocationate(1024, false);
 * </pre>
 * you can also alLocationate a new direct buffer:
 * <pre>
 * IoBuffer buf = IoBuffer.alLocationate(1024, true);
 * </pre>
 * or you can set the default buffer type.
 * <pre>
 * // AlLocationate heap buffer by default.
 * IoBuffer.setUseDirectBuffer(false);
 * // A new heap buffer is returned.
 * IoBuffer buf = IoBuffer.alLocationate(1024);
 * </pre>
 * </p>
 * <p/>
 * <h2>Wrapping existing NIO buffers and arrays</h2>
 * <p>
 * This class provides a few <tt>wrap(...)</tt> methods that wraps
 * any NIO buffers and byte arrays.
 * <p/>
 * <h2>AutoExpand</h2>
 * <p>
 * Writing variable-length data using NIO <tt>ByteBuffers</tt> is not really
 * easy, and it is because its size is fixed.  {@link NioBuffer} introduces
 * <tt>autoExpand</tt> property.  If <tt>autoExpand</tt> property is true, you
 * never get {@link java.nio.BufferOverflowException} or
 * {@link IndexOutOfBoundsException} (except when index is negative).
 * It automatically expands its capacity and limit value.  For example:
 * <pre>
 * String greeting = messageBundle.getMessage( "hello" );
 * IoBuffer buf = IoBuffer.alLocationate( 16 );
 * // Turn on autoExpand (it is off by default)
 * buf.setAutoExpand( true );
 * buf.putString( greeting, utf8encoder );
 * </pre>
 * The underlying {@link java.nio.ByteBuffer} is realLocationated by {@link NioBuffer} behind
 * the scene if the encoded data is larger than 16 bytes in the example above.
 * Its capacity will double, and its limit will increase to the last position
 * the string is written.
 * </p>
 * <p/>
 * <h2>AutoShrink</h2>
 * <p>
 * You might also want to decrease the capacity of the buffer when most
 * of the alLocationated memory area is not being used.  {@link NioBuffer} provides
 * <tt>autoShrink</tt> property to take care of this issue.  If
 * <tt>autoShrink</tt> is turned on, {@link NioBuffer} halves the capacity
 * of the buffer when {@link #compact()} is invoked and only 1/4 or less of
 * the current capacity is being used.
 * <p>
 * You can also {@link #shrink()} method manually to shrink the capacity of
 * the buffer.
 * <p>
 * The underlying {@link java.nio.ByteBuffer} is realLocationated by {@link NioBuffer} behind
 * the scene, and therefore {@link #buf()} will return a different
 * {@link java.nio.ByteBuffer} instance once capacity changes.  Please also note
 * {@link #compact()} or {@link #shrink()} will not decrease the capacity if
 * the new capacity is less than the {@link #minimumCapacity()} of the buffer.
 * <p/>
 * <h2>Derived Buffers</h2>
 * <p>
 * Derived buffers are the buffers which were created by
 * {@link #duplicate()}, {@link #slice()}, or {@link #asReadOnlyBuffer()}.
 * They are useful especially when you broadcast the same messages to
 * multiple {@link jds.nio.NioSession}s.  Please note that the buffer derived from and
 * its derived buffers are not both auto-expandable neither auto-shrinkable.
 * Trying to call {@link #setAutoExpand(boolean)} or {@link #setAutoShrink(boolean)}
 * with <tt>true</tt> parameter will raise an {@link IllegalStateException}.
 * </p>
 * <p/>
 * <h2>Changing Buffer AlLocation Policy</h2>
 * <p>
 * {@link NioBufferAllocator} interface lets you override the default buffer
 * management behavior.  There are two alLocationators provided out-of-the-box:
 * <ul>
 * <li>{@link SimpleBufferAllocator} (default)</li>
 * </ul>
 * You can implement your own alLocationator and use it by calling
 * {@link #setAllocator(NioBufferAllocator)}.
 * </p>
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public abstract class NioBuffer implements Comparable<NioBuffer>
{
	/**
	 * The alLocationator used to create new buffers
	 */
	private static NioBufferAllocator allocator = new SimpleBufferAllocator();

	/**
	 * A flag indicating which type of buffer we are using : heap or direct
	 */
	private static boolean useDirectBuffer = false;

	/**
	 * Returns the alLocationator used by existing and new buffers
	 */
	public static NioBufferAllocator getAllocator()
	{
		return allocator;
	}

	/**
	 * Sets the alLocationator used by existing and new buffers
	 */
	public static void setAllocator(NioBufferAllocator newAlLocationator)
	{
		if (newAlLocationator == null)
		{
			throw new NullPointerException("alLocationator");
		}

		NioBufferAllocator oldAlLocationator = allocator;

		allocator = newAlLocationator;

		if (null != oldAlLocationator)
		{
			oldAlLocationator.dispose();
		}
	}

	/**
	 * Returns <tt>true</tt> if and only if a direct buffer is alLocationated
	 * by default when the type of the new buffer is not specified.  The
	 * default value is <tt>false</tt>.
	 */
	public static boolean isUseDirectBuffer()
	{
		return useDirectBuffer;
	}

	/**
	 * Sets if a direct buffer should be alLocationated by default when the
	 * type of the new buffer is not specified.  The default value is
	 * <tt>false</tt>.
	 */
	public static void setUseDirectBuffer(boolean useDirectBuffer)
	{
		NioBuffer.useDirectBuffer = useDirectBuffer;
	}

	/**
	 * Returns the direct or heap buffer which is capable to store the
	 * specified amount of bytes.
	 * @param capacity the capacity of the buffer
	 * @see #setUseDirectBuffer(boolean)
	 */
	public static NioBuffer allocate(int capacity)
	{
		return allocate(capacity, useDirectBuffer);
	}

	/**
	 * Returns the buffer which is capable of the specified size.
	 * @param capacity the capacity of the buffer
	 * @param direct   <tt>true</tt> to get a direct buffer,
	 *                 <tt>false</tt> to get a heap buffer.
	 */
	public static NioBuffer allocate(int capacity, boolean direct)
	{
		if (capacity < 0)
		{
			throw new IllegalArgumentException("capacity: " + capacity);
		}

		return allocator.allocate(capacity, direct);
	}

	/**
	 * Wraps the specified NIO {@link java.nio.ByteBuffer} into MINA buffer.
	 */
	public static NioBuffer wrap(ByteBuffer nioBuffer)
	{
		return allocator.wrap(nioBuffer);
	}

	/**
	 * Wraps the specified byte array into MINA heap buffer.
	 */
	public static NioBuffer wrap(byte[] byteArray)
	{
		return wrap(ByteBuffer.wrap(byteArray));
	}

	/**
	 * Wraps the specified byte array into MINA heap buffer.
	 */
	public static NioBuffer wrap(byte[] byteArray, int offset, int length)
	{
		return wrap(ByteBuffer.wrap(byteArray, offset, length));
	}

	/**
	 * Normalizes the specified capacity of the buffer to power of 2,
	 * which is often helpful for optimal memory usage and performance.
	 * If it is greater than or equal to {@link Integer#MAX_VALUE}, it
	 * returns {@link Integer#MAX_VALUE}.  If it is zero, it returns zero.
	 */
	protected static int normalizeCapacity(int requestedCapacity)
	{
		switch (requestedCapacity)
		{
			case 0:
			case 1 << 0:
			case 1 << 1:
			case 1 << 2:
			case 1 << 3:
			case 1 << 4:
			case 1 << 5:
			case 1 << 6:
			case 1 << 7:
			case 1 << 8:
			case 1 << 9:
			case 1 << 10:
			case 1 << 11:
			case 1 << 12:
			case 1 << 13:
			case 1 << 14:
			case 1 << 15:
			case 1 << 16:
			case 1 << 17:
			case 1 << 18:
			case 1 << 19:
			case 1 << 21:
			case 1 << 22:
			case 1 << 23:
			case 1 << 24:
			case 1 << 25:
			case 1 << 26:
			case 1 << 27:
			case 1 << 28:
			case 1 << 29:
			case 1 << 30:
			case Integer.MAX_VALUE:
				return requestedCapacity;
		}

		int newCapacity = 1;
		while (newCapacity < requestedCapacity)
		{
			newCapacity <<= 1;
			if (newCapacity < 0)
			{
				return Integer.MAX_VALUE;
			}
		}
		return newCapacity;
	}

	/**
	 * Creates a new instance.  This is an empty constructor.
	 */
	protected NioBuffer()
	{
		// Do nothing
	}

	/**
	 * Declares this buffer and all its derived buffers are not used anymore
	 * so that it can be reused by some {@link NioBufferAllocator} implementations.
	 * It is not mandatory to call this method, but you might want to invoke this
	 * method for maximum performance.
	 */
	public abstract void free();

	/**
	 * Returns the underlying NIO buffer instance.
	 */
	public abstract ByteBuffer buf();

	/**
	 * @see java.nio.ByteBuffer#isDirect()
	 */
	public abstract boolean isDirect();

	/**
	 * returns <tt>true</tt> if and only if this buffer is derived from other buffer
	 * via {@link #duplicate()}, {@link #slice()} or {@link #asReadOnlyBuffer()}.
	 */
	public abstract boolean isDerived();

	/**
	 * @see java.nio.ByteBuffer#isReadOnly()
	 */
	public abstract boolean isReadOnly();

	/**
	 * Returns the minimum capacity of this buffer which is used to determine
	 * the new capacity of the buffer shrunk by {@link #compact()} and
	 * {@link #shrink()} operation.  The default value is the initial capacity
	 * of the buffer.
	 */
	public abstract int minimumCapacity();

	/**
	 * Sets the minimum capacity of this buffer which is used to determine
	 * the new capacity of the buffer shrunk by {@link #compact()} and
	 * {@link #shrink()} operation.  The default value is the initial capacity
	 * of the buffer.
	 */
	public abstract NioBuffer minimumCapacity(int minimumCapacity);

	/**
	 * @see java.nio.ByteBuffer#capacity()
	 */
	public abstract int capacity();

	/**
	 * Increases the capacity of this buffer.  If the new capacity is less than
	 * or equal to the current capacity, this method returns silently.  If the
	 * new capacity is greater than the current capacity, the buffer is
	 * realLocationated while retaining the position, limit, mark and the content
	 * of the buffer.
	 */
	public abstract NioBuffer capacity(int newCapacity);

	/**
	 * Returns <tt>true</tt> if and only if <tt>autoExpand</tt> is turned on.
	 */
	public abstract boolean isAutoExpand();

	/**
	 * Turns on or off <tt>autoExpand</tt>.
	 */
	public abstract NioBuffer setAutoExpand(boolean autoExpand);

	/**
	 * Returns <tt>true</tt> if and only if <tt>autoShrink</tt> is turned on.
	 */
	public abstract boolean isAutoShrink();

	/**
	 * Turns on or off <tt>autoShrink</tt>.
	 */
	public abstract NioBuffer setAutoShrink(boolean autoShrink);

	/**
	 * Changes the capacity and limit of this buffer so this buffer get
	 * the specified <tt>expectedRemaining</tt> room from the current position.
	 * This method works even if you didn't set <tt>autoExpand</tt> to
	 * <tt>true</tt>.
	 */
	public abstract NioBuffer expand(int expectedRemaining);

	/**
	 * Changes the capacity and limit of this buffer so this buffer get
	 * the specified <tt>expectedRemaining</tt> room from the specified
	 * <tt>position</tt>.
	 * This method works even if you didn't set <tt>autoExpand</tt> to
	 * <tt>true</tt>.
	 */
	public abstract NioBuffer expand(int position, int expectedRemaining);

	/**
	 * Changes the capacity of this buffer so this buffer occupies as less
	 * memory as possible while retaining the position, limit and the
	 * buffer content between the position and limit.  The capacity of the
	 * buffer never becomes less than {@link #minimumCapacity()}.
	 * The mark is discarded once the capacity changes.
	 */
	public abstract NioBuffer shrink();

	/**
	 * @see java.nio.Buffer#position()
	 */
	public abstract int position();

	/**
	 * @see java.nio.Buffer#position(int)
	 */
	public abstract NioBuffer position(int newPosition);

	/**
	 * @see java.nio.Buffer#limit()
	 */
	public abstract int limit();

	/**
	 * @see java.nio.Buffer#limit(int)
	 */
	public abstract NioBuffer limit(int newLimit);

	/**
	 * @see java.nio.Buffer#mark()
	 */
	public abstract NioBuffer mark();

	/**
	 * Returns the position of the current mark.  This method returns <tt>-1</tt> if no
	 * mark is set.
	 */
	public abstract int markValue();

	/**
	 * @see java.nio.Buffer#reset()
	 */
	public abstract NioBuffer reset();

	/**
	 * @see java.nio.Buffer#clear()
	 */
	public abstract NioBuffer clear();

	/**
	 * Clears this buffer and fills its content with <tt>NUL</tt>.
	 * The position is set to zero, the limit is set to the capacity,
	 * and the mark is discarded.
	 */
	public abstract NioBuffer sweep();

	/**
	 * double
	 * Clears this buffer and fills its content with <tt>value</tt>.
	 * The position is set to zero, the limit is set to the capacity,
	 * and the mark is discarded.
	 */
	public abstract NioBuffer sweep(byte value);

	/**
	 * @see java.nio.Buffer#flip()
	 */
	public abstract NioBuffer flip();

	/**
	 * @see java.nio.Buffer#rewind()
	 */
	public abstract NioBuffer rewind();

	/**
	 * @see java.nio.Buffer#remaining()
	 */
	public abstract int remaining();

	/**
	 * @see java.nio.Buffer#hasRemaining()
	 */
	public abstract boolean hasRemaining();

	/**
	 * @see java.nio.ByteBuffer#duplicate()
	 */
	public abstract NioBuffer duplicate();

	/**
	 * @see java.nio.ByteBuffer#slice()
	 */
	public abstract NioBuffer slice();

	/**
	 * @see java.nio.ByteBuffer#asReadOnlyBuffer()
	 */
	public abstract NioBuffer asReadOnlyBuffer();

	/**
	 * @see java.nio.ByteBuffer#hasArray()
	 */
	public abstract boolean hasArray();

	/**
	 * @see java.nio.ByteBuffer#array()
	 */
	public abstract byte[] array();

	/**
	 * @see java.nio.ByteBuffer#arrayOffset()
	 */
	public abstract int arrayOffset();

	/**
	 * @see java.nio.ByteBuffer#get()
	 */
	public abstract byte get();

	/**
	 * Reads one unsigned byte as a short integer.
	 */
	public abstract short getUnsigned();

	/**
	 * @see java.nio.ByteBuffer#put(byte)
	 */
	public abstract NioBuffer put(byte b);

	/**
	 * @see java.nio.ByteBuffer#get(int)
	 */
	public abstract byte get(int index);

	/**
	 * Reads one byte as an unsigned short integer.
	 */
	public abstract short getUnsigned(int index);

	/**
	 * @see java.nio.ByteBuffer#put(int, byte)
	 */
	public abstract NioBuffer put(int index, byte b);

	/**
	 * @see java.nio.ByteBuffer#get(byte[], int, int)
	 */
	public abstract NioBuffer get(byte[] dst, int offset, int length);

	/**
	 * @see java.nio.ByteBuffer#get(byte[])
	 */
	public abstract NioBuffer get(byte[] dst);

	/**
	 * TODO document me.
	 */
	public abstract NioBuffer getSlice(int index, int length);

	/**
	 * TODO document me.
	 */
	public abstract NioBuffer getSlice(int length);

	/**
	 * Writes the content of the specified <tt>src</tt> into this buffer.
	 */
	public abstract NioBuffer put(ByteBuffer src);

	/**
	 * Writes the content of the specified <tt>src</tt> into this buffer.
	 */
	public abstract NioBuffer put(NioBuffer src);

	/**
	 * @see java.nio.ByteBuffer#put(byte[], int, int)
	 */
	public abstract NioBuffer put(byte[] src, int offset, int length);

	/**
	 * @see java.nio.ByteBuffer#put(byte[])
	 */
	public abstract NioBuffer put(byte[] src);

	/**
	 * @see java.nio.ByteBuffer#compact()
	 */
	public abstract NioBuffer compact();

	/**
	 * @see java.nio.ByteBuffer#order()
	 */
	public abstract ByteOrder order();

	/**
	 * @see java.nio.ByteBuffer#order(java.nio.ByteOrder)
	 */
	public abstract NioBuffer order(ByteOrder bo);

	/**
	 * @see java.nio.ByteBuffer#getChar()
	 */
	public abstract char getChar();

	/**
	 * @see java.nio.ByteBuffer#putChar(char)
	 */
	public abstract NioBuffer putChar(char value);

	/**
	 * @see java.nio.ByteBuffer#getChar(int)
	 */
	public abstract char getChar(int index);

	/**
	 * @see java.nio.ByteBuffer#putChar(int, char)
	 */
	public abstract NioBuffer putChar(int index, char value);

	/**
	 * @see java.nio.ByteBuffer#asCharBuffer()
	 */
	public abstract CharBuffer asCharBuffer();

	/**
	 * @see java.nio.ByteBuffer#getShort()
	 */
	public abstract short getShort();

	/**
	 * Reads two bytes unsigned integer.
	 */
	public abstract int getUnsignedShort();

	/**
	 * @see java.nio.ByteBuffer#putShort(short)
	 */
	public abstract NioBuffer putShort(short value);

	/**
	 * @see java.nio.ByteBuffer#getShort()
	 */
	public abstract short getShort(int index);

	/**
	 * Reads two bytes unsigned integer.
	 */
	public abstract int getUnsignedShort(int index);

	/**
	 * @see java.nio.ByteBuffer#putShort(int, short)
	 */
	public abstract NioBuffer putShort(int index, short value);

	/**
	 * @see java.nio.ByteBuffer#asShortBuffer()
	 */
	public abstract ShortBuffer asShortBuffer();

	/**
	 * @see java.nio.ByteBuffer#getInt()
	 */
	public abstract int getInt();

	/**
	 * Reads four bytes unsigned integer.
	 */
	public abstract long getUnsignedInt();

	/**
	 * Relative <i>get</i> method for reading a medium int value.
	 * <p/>
	 * <p> Reads the next three bytes at this buffer's current position,
	 * composing them into an int value according to the current byte order,
	 * and then increments the position by three.</p>
	 * @return The medium int value at the buffer's current position
	 */
	public abstract int getMediumInt();

	/**
	 * Relative <i>get</i> method for reading an unsigned medium int value.
	 * <p/>
	 * <p> Reads the next three bytes at this buffer's current position,
	 * composing them into an int value according to the current byte order,
	 * and then increments the position by three.</p>
	 * @return The unsigned medium int value at the buffer's current position
	 */
	public abstract int getUnsignedMediumInt();

	/**
	 * Absolute <i>get</i> method for reading a medium int value.
	 * <p/>
	 * <p> Reads the next three bytes at this buffer's current position,
	 * composing them into an int value according to the current byte order.</p>
	 * @param index The index from which the medium int will be read
	 * @return The medium int value at the given index
	 * @throws IndexOutOfBoundsException If <tt>index</tt> is negative
	 *                                   or not smaller than the buffer's limit
	 */
	public abstract int getMediumInt(int index);

	/**
	 * Absolute <i>get</i> method for reading an unsigned medium int value.
	 * <p/>
	 * <p> Reads the next three bytes at this buffer's current position,
	 * composing them into an int value according to the current byte order.</p>
	 * @param index The index from which the unsigned medium int will be read
	 * @return The unsigned medium int value at the given index
	 * @throws IndexOutOfBoundsException If <tt>index</tt> is negative
	 *                                   or not smaller than the buffer's limit
	 */
	public abstract int getUnsignedMediumInt(int index);

	/**
	 * Relative <i>put</i> method for writing a medium int
	 * value.
	 * <p/>
	 * <p> Writes three bytes containing the given int value, in the
	 * current byte order, into this buffer at the current position, and then
	 * increments the position by three.</p>
	 * @param value The medium int value to be written
	 * @return This buffer
	 * @throws java.nio.BufferOverflowException
	 *          If there are fewer than three bytes
	 *          remaining in this buffer
	 * @throws java.nio.ReadOnlyBufferException
	 *          If this buffer is read-only
	 */
	public abstract NioBuffer putMediumInt(int value);

	/**
	 * Absolute <i>put</i> method for writing a medium int
	 * value.
	 * <p/>
	 * <p> Writes three bytes containing the given int value, in the
	 * current byte order, into this buffer at the given index.</p>
	 * @param index The index at which the bytes will be written
	 * @param value The medium int value to be written
	 * @return This buffer
	 * @throws IndexOutOfBoundsException If <tt>index</tt> is negative
	 *                                   or not smaller than the buffer's limit,
	 *                                   minus three
	 * @throws java.nio.ReadOnlyBufferException
	 *                                   If this buffer is read-only
	 */
	public abstract NioBuffer putMediumInt(int index, int value);

	/**
	 * @see java.nio.ByteBuffer#putInt(int)
	 */
	public abstract NioBuffer putInt(int value);

	/**
	 * @see java.nio.ByteBuffer#getInt(int)
	 */
	public abstract int getInt(int index);

	/**
	 * Reads four bytes unsigned integer.
	 */
	public abstract long getUnsignedInt(int index);

	/**
	 * @see java.nio.ByteBuffer#putInt(int, int)
	 */
	public abstract NioBuffer putInt(int index, int value);

	/**
	 * @see java.nio.ByteBuffer#asIntBuffer()
	 */
	public abstract IntBuffer asIntBuffer();

	/**
	 * @see java.nio.ByteBuffer#getLong()
	 */
	public abstract long getLong();

	/**
	 * @see java.nio.ByteBuffer#putLong(int, long)
	 */
	public abstract NioBuffer putLong(long value);

	/**
	 * @see java.nio.ByteBuffer#getLong(int)
	 */
	public abstract long getLong(int index);

	/**
	 * @see java.nio.ByteBuffer#putLong(int, long)
	 */
	public abstract NioBuffer putLong(int index, long value);

	/**
	 * @see java.nio.ByteBuffer#asLongBuffer()
	 */
	public abstract LongBuffer asLongBuffer();

	/**
	 * @see java.nio.ByteBuffer#getFloat()
	 */
	public abstract float getFloat();

	/**
	 * @see java.nio.ByteBuffer#putFloat(float)
	 */
	public abstract NioBuffer putFloat(float value);

	/**
	 * @see java.nio.ByteBuffer#getFloat(int)
	 */
	public abstract float getFloat(int index);

	/**
	 * @see java.nio.ByteBuffer#putFloat(int, float)
	 */
	public abstract NioBuffer putFloat(int index, float value);

	/**
	 * @see java.nio.ByteBuffer#asFloatBuffer()
	 */
	public abstract FloatBuffer asFloatBuffer();

	/**
	 * @see java.nio.ByteBuffer#getDouble()
	 */
	public abstract double getDouble();

	/**
	 * @see java.nio.ByteBuffer#putDouble(double)
	 */
	public abstract NioBuffer putDouble(double value);

	/**
	 * @see java.nio.ByteBuffer#getDouble(int)
	 */
	public abstract double getDouble(int index);

	/**
	 * @see java.nio.ByteBuffer#putDouble(int, double)
	 */
	public abstract NioBuffer putDouble(int index, double value);

	/**
	 * @see java.nio.ByteBuffer#asDoubleBuffer()
	 */
	public abstract DoubleBuffer asDoubleBuffer();

	/**
	 * Returns an {@link java.io.InputStream} that reads the data from this buffer.
	 * {@link java.io.InputStream#read()} returns <tt>-1</tt> if the buffer position
	 * reaches to the limit.
	 */
	public abstract InputStream asInputStream();

	/**
	 * Returns an {@link java.io.OutputStream} that appends the data into this buffer.
	 * Please note that the {@link java.io.OutputStream#write(int)} will throw a
	 * {@link java.nio.BufferOverflowException} instead of an {@link java.io.IOException}
	 * in case of buffer overflow.  Please set <tt>autoExpand</tt> property by
	 * calling {@link #setAutoExpand(boolean)} to prevent the unexpected runtime
	 * exception.
	 */
	public abstract OutputStream asOutputStream();

	/**
	 * Returns hexdump of this buffer.  The data and pointer are
	 * not changed as a result of this method call.
	 * @return hexidecimal representation of this buffer
	 */
	public abstract String getHexDump();

	/**
	 * Return hexdump of this buffer with limited length.
	 * @param lengthLimit The maximum number of bytes to dump from
	 *                    the current buffer position.
	 * @return hexidecimal representation of this buffer
	 */
	public abstract String getHexDump(int lengthLimit);

	////////////////////////////////
	// String getters and putters //
	////////////////////////////////

	/**
	 * Reads a <code>NUL</code>-terminated string from this buffer using the
	 * specified <code>decoder</code> and returns it.  This method reads
	 * until the limit of this buffer if no <tt>NUL</tt> is found.
	 */
	public abstract String getString(CharsetDecoder decoder) throws CharacterCodingException;

	/**
	 * Reads a <code>NUL</code>-terminated string from this buffer using the
	 * specified <code>decoder</code> and returns it.
	 * @param fieldSize the maximum number of bytes to read
	 */
	public abstract String getString(int fieldSize, CharsetDecoder decoder) throws CharacterCodingException;

	/**
	 * Writes the content of <code>in</code> into this buffer using the
	 * specified <code>encoder</code>.  This method doesn't terminate
	 * string with <tt>NUL</tt>.  You have to do it by yourself.
	 * @throws java.nio.BufferOverflowException
	 *          if the specified string doesn't fit
	 */
	public abstract NioBuffer putString(CharSequence val, CharsetEncoder encoder) throws CharacterCodingException;

	/**
	 * Writes the content of <code>in</code> into this buffer as a
	 * <code>NUL</code>-terminated string using the specified
	 * <code>encoder</code>.
	 * <p/>
	 * If the charset name of the encoder is UTF-16, you cannot specify
	 * odd <code>fieldSize</code>, and this method will append two
	 * <code>NUL</code>s as a terminator.
	 * <p/>
	 * Please note that this method doesn't terminate with <code>NUL</code>
	 * if the input string is longer than <tt>fieldSize</tt>.
	 * @param fieldSize the maximum number of bytes to write
	 */
	public abstract NioBuffer putString(CharSequence val, int fieldSize, CharsetEncoder encoder) throws CharacterCodingException;

	/**
	 * Reads a string which has a 16-bit length field before the actual
	 * encoded string, using the specified <code>decoder</code> and returns it.
	 * This method is a shortcut for <tt>getPrefixedString(2, decoder)</tt>.
	 */
	public abstract String getPrefixedString(CharsetDecoder decoder) throws CharacterCodingException;

	/**
	 * Reads a string which has a length field before the actual
	 * encoded string, using the specified <code>decoder</code> and returns it.
	 * @param prefixLength the length of the length field (1, 2, or 4)
	 */
	public abstract String getPrefixedString(int prefixLength, CharsetDecoder decoder) throws CharacterCodingException;

	/**
	 * Writes the content of <code>in</code> into this buffer as a
	 * string which has a 16-bit length field before the actual
	 * encoded string, using the specified <code>encoder</code>.
	 * This method is a shortcut for <tt>putPrefixedString(in, 2, 0, encoder)</tt>.
	 * @throws java.nio.BufferOverflowException
	 *          if the specified string doesn't fit
	 */
	public abstract NioBuffer putPrefixedString(CharSequence in, CharsetEncoder encoder) throws CharacterCodingException;

	/**
	 * Writes the content of <code>in</code> into this buffer as a
	 * string which has a 16-bit length field before the actual
	 * encoded string, using the specified <code>encoder</code>.
	 * This method is a shortcut for <tt>putPrefixedString(in, prefixLength, 0, encoder)</tt>.
	 * @param prefixLength the length of the length field (1, 2, or 4)
	 * @throws java.nio.BufferOverflowException
	 *          if the specified string doesn't fit
	 */
	public abstract NioBuffer putPrefixedString(CharSequence in, int prefixLength, CharsetEncoder encoder) throws CharacterCodingException;

	/**
	 * Writes the content of <code>in</code> into this buffer as a
	 * string which has a 16-bit length field before the actual
	 * encoded string, using the specified <code>encoder</code>.
	 * This method is a shortcut for <tt>putPrefixedString(in, prefixLength, padding, ( byte ) 0, encoder)</tt>.
	 * @param prefixLength the length of the length field (1, 2, or 4)
	 * @param padding	  the number of padded <tt>NUL</tt>s (1 (or 0), 2, or 4)
	 * @throws java.nio.BufferOverflowException
	 *          if the specified string doesn't fit
	 */
	public abstract NioBuffer putPrefixedString(CharSequence in, int prefixLength, int padding, CharsetEncoder encoder) throws CharacterCodingException;

	/**
	 * Writes the content of <code>in</code> into this buffer as a
	 * string which has a 16-bit length field before the actual
	 * encoded string, using the specified <code>encoder</code>.
	 * @param prefixLength the length of the length field (1, 2, or 4)
	 * @param padding	  the number of padded bytes (1 (or 0), 2, or 4)
	 * @param padValue	 the value of padded bytes
	 * @throws java.nio.BufferOverflowException
	 *          if the specified string doesn't fit
	 */
	public abstract NioBuffer putPrefixedString(CharSequence val, int prefixLength, int padding, byte padValue, CharsetEncoder encoder) throws CharacterCodingException;

	/**
	 * Reads a Java object from the buffer using the context {@link ClassLoader}
	 * of the current thread.
	 */
	public abstract Object getObject() throws ClassNotFoundException;

	/**
	 * Reads a Java object from the buffer using the specified <tt>classLoader</tt>.
	 */
	public abstract Object getObject(final ClassLoader classLoader) throws ClassNotFoundException;

	/**
	 * Writes the specified Java object to the buffer.
	 */
	public abstract NioBuffer putObject(Object o);

	/**
	 * Returns <tt>true</tt> if this buffer contains a data which has a data
	 * length as a prefix and the buffer has remaining data as enough as
	 * specified in the data length field.  This method is identical with
	 * <tt>prefixedDataAvailable( prefixLength, Integer.MAX_VALUE )</tt>.
	 * Please not that using this method can allow DoS (Denial of Service)
	 * attack in case the remote peer sends too big data length value.
	 * It is recommended to use {@link #prefixedDataAvailable(int, int)}
	 * instead.
	 * @param prefixLength the length of the prefix field (1, 2, or 4)
	 * @throws IllegalArgumentException if prefixLength is wrong
	 * @throws BufferDataException
	 *                                  if data length is negative
	 */
	public abstract boolean prefixedDataAvailable(int prefixLength);

	/**
	 * Returns <tt>true</tt> if this buffer contains a data which has a data
	 * length as a prefix and the buffer has remaining data as enough as
	 * specified in the data length field.
	 * @param prefixLength  the length of the prefix field (1, 2, or 4)
	 * @param maxDataLength the allowed maximum of the read data length
	 * @throws IllegalArgumentException if prefixLength is wrong
	 * @throws BufferDataException
	 *                                  if data length is negative or greater then <tt>maxDataLength</tt>
	 */
	public abstract boolean prefixedDataAvailable(int prefixLength, int maxDataLength);

	/////////////////////
	// IndexOf methods //
	/////////////////////

	/**
	 * Returns the first occurence position of the specified byte from the current position to
	 * the current limit.
	 * @return <tt>-1</tt> if the specified byte is not found
	 */
	public abstract int indexOf(byte b);

	//////////////////////////
	// Skip or fill methods //
	//////////////////////////

	/**
	 * Forwards the position of this buffer as the specified <code>size</code>
	 * bytes.
	 */
	public abstract NioBuffer skip(int size);

	/**
	 * Fills this buffer with the specified value.
	 * This method moves buffer position forward.
	 */
	public abstract NioBuffer fill(byte value, int size);

	/**
	 * Fills this buffer with the specified value.
	 * This method does not change buffer position.
	 */
	public abstract NioBuffer fillAndReset(byte value, int size);

	/**
	 * Fills this buffer with <code>NUL (0x00)</code>.
	 * This method moves buffer position forward.
	 */
	public abstract NioBuffer fill(int size);

	/**
	 * Fills this buffer with <code>NUL (0x00)</code>.
	 * This method does not change buffer position.
	 */
	public abstract NioBuffer fillAndReset(int size);

	//////////////////////////
	// Enum methods         //
	//////////////////////////

	/**
	 * Reads a byte from the buffer and returns the correlating enum constant defined
	 * by the specified enum type.
	 * @param <E>       The enum type to return
	 * @param enumClass The enum's class object
	 */
	public abstract <E extends Enum<E>> E getEnum(Class<E> enumClass);

	/**
	 * Reads a byte from the buffer and returns the correlating enum constant defined
	 * by the specified enum type.
	 * @param <E>       The enum type to return
	 * @param index	 the index from which the byte will be read
	 * @param enumClass The enum's class object
	 */
	public abstract <E extends Enum<E>> E getEnum(int index, Class<E> enumClass);

	/**
	 * Reads a short from the buffer and returns the correlating enum constant defined
	 * by the specified enum type.
	 * @param <E>       The enum type to return
	 * @param enumClass The enum's class object
	 */
	public abstract <E extends Enum<E>> E getEnumShort(Class<E> enumClass);

	/**
	 * Reads a short from the buffer and returns the correlating enum constant defined
	 * by the specified enum type.
	 * @param <E>       The enum type to return
	 * @param index	 the index from which the bytes will be read
	 * @param enumClass The enum's class object
	 */
	public abstract <E extends Enum<E>> E getEnumShort(int index, Class<E> enumClass);

	/**
	 * Reads an int from the buffer and returns the correlating enum constant defined
	 * by the specified enum type.
	 * @param <E>       The enum type to return
	 * @param enumClass The enum's class object
	 */
	public abstract <E extends Enum<E>> E getEnumInt(Class<E> enumClass);

	/**
	 * Reads an int from the buffer and returns the correlating enum constant defined
	 * by the specified enum type.
	 * @param <E>       The enum type to return
	 * @param index	 the index from which the bytes will be read
	 * @param enumClass The enum's class object
	 */
	public abstract <E extends Enum<E>> E getEnumInt(int index, Class<E> enumClass);

	/**
	 * Writes an enum's ordinal value to the buffer as a byte.
	 * @param e The enum to write to the buffer
	 */
	public abstract NioBuffer putEnum(Enum<?> e);

	/**
	 * Writes an enum's ordinal value to the buffer as a byte.
	 * @param index The index at which the byte will be written
	 * @param e	 The enum to write to the buffer
	 */
	public abstract NioBuffer putEnum(int index, Enum<?> e);

	/**
	 * Writes an enum's ordinal value to the buffer as a short.
	 * @param e The enum to write to the buffer
	 */
	public abstract NioBuffer putEnumShort(Enum<?> e);

	/**
	 * Writes an enum's ordinal value to the buffer as a short.
	 * @param index The index at which the bytes will be written
	 * @param e	 The enum to write to the buffer
	 */
	public abstract NioBuffer putEnumShort(int index, Enum<?> e);

	/**
	 * Writes an enum's ordinal value to the buffer as an integer.
	 * @param e The enum to write to the buffer
	 */
	public abstract NioBuffer putEnumInt(Enum<?> e);

	/**
	 * Writes an enum's ordinal value to the buffer as an integer.
	 * @param index The index at which the bytes will be written
	 * @param e	 The enum to write to the buffer
	 */
	public abstract NioBuffer putEnumInt(int index, Enum<?> e);

	//////////////////////////
	// EnumSet methods      //
	//////////////////////////

	/**
	 * Reads a byte sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * <p/>
	 * <p>Each bit is mapped to a value in the specified enum.  The least significant
	 * bit maps to the first entry in the specified enum and each subsequent bit maps
	 * to each subsequent bit as mapped to the subsequent enum value.</p>
	 * @param <E>       the enum type
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSet(Class<E> enumClass);

	/**
	 * Reads a byte sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param index	 the index from which the byte will be read
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSet(int index, Class<E> enumClass);

	/**
	 * Reads a short sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSetShort(Class<E> enumClass);

	/**
	 * Reads a short sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param index	 the index from which the bytes will be read
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSetShort(int index, Class<E> enumClass);

	/**
	 * Reads an int sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSetInt(Class<E> enumClass);

	/**
	 * Reads an int sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param index	 the index from which the bytes will be read
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSetInt(int index, Class<E> enumClass);

	/**
	 * Reads a long sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSetLong(Class<E> enumClass);

	/**
	 * Reads a long sized bit vector and converts it to an {@link java.util.EnumSet}.
	 * @param <E>       the enum type
	 * @param index	 the index from which the bytes will be read
	 * @param enumClass the enum class used to create the EnumSet
	 * @return the EnumSet representation of the bit vector
	 * @see #getEnumSet(Class)
	 */
	public abstract <E extends Enum<E>> EnumSet<E> getEnumSetLong(int index, Class<E> enumClass);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as a byte sized bit vector.
	 * @param <E> the enum type of the Set
	 * @param set the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSet(Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as a byte sized bit vector.
	 * @param <E>   the enum type of the Set
	 * @param index the index at which the byte will be written
	 * @param set   the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSet(int index, Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as a short sized bit vector.
	 * @param <E> the enum type of the Set
	 * @param set the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSetShort(Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as a short sized bit vector.
	 * @param <E>   the enum type of the Set
	 * @param index the index at which the bytes will be written
	 * @param set   the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSetShort(int index, Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as an int sized bit vector.
	 * @param <E> the enum type of the Set
	 * @param set the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSetInt(Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as an int sized bit vector.
	 * @param <E>   the enum type of the Set
	 * @param index the index at which the bytes will be written
	 * @param set   the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSetInt(int index, Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as a long sized bit vector.
	 * @param <E> the enum type of the Set
	 * @param set the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSetLong(Set<E> set);

	/**
	 * Writes the specified {@link java.util.Set} to the buffer as a long sized bit vector.
	 * @param <E>   the enum type of the Set
	 * @param index the index at which the bytes will be written
	 * @param set   the enum set to write to the buffer
	 */
	public abstract <E extends Enum<E>> NioBuffer putEnumSetLong(int index, Set<E> set);
}