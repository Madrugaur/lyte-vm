package virtualmachine;

public class MemorySegment {
	private int[] backing;
	private int size;
	public MemorySegment(int size) {
		this.size = size;
		backing = new int[size];
	}
	public void put(int i, int word) {
		if (i < size) backing[i] = word;
	}
	public int get(int i) {
		if (i < size) return backing[i];
		return -1;
	}
	public MemorySegment clone() {
		MemorySegment clone = new MemorySegment(this.size);
		clone.backing = this.backing;
		return clone;
	}
}
