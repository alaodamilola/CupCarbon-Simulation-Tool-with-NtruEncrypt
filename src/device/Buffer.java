package device;

public class Buffer {
	
	private int size = 102400;
	private int index = 0;
	private boolean ready = false;
	private byte[] buffer;
	
	public Buffer() {
		setBuffer(new byte [size]);
	}
	
	public int getSize() {
		return size;
	}
	
	public void init() {
		index = 0 ;
		ready = false;	
		for(int i=0; i<size; i++) {
			buffer[i] = '\r';
		}
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	public int getIndex() {
		return index;
	}
	public void incIndex() {
		this.index++;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	} 
	
}
