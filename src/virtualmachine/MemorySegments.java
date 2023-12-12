package virtualmachine;
import java.util.ArrayList;
import java.util.Arrays;

public class MemorySegments {
	private ArrayList<MemorySegment> arr;
	public MemorySegments() {
		arr = new ArrayList<MemorySegment>();
	}
	public int map(MemorySegment mem_seg) {
		arr.add(mem_seg);
		return arr.size() - 1;
	}
	public void unmap(int i) {
		arr.remove(i);
	}
	public int get(int i, int c) {
		return arr.get(i).get(c);
	}
	public void set(int i, MemorySegment memseg) {
		arr.set(i, memseg);
	}
	public MemorySegment get(int i) {
		return arr.get(i);
	}

}
