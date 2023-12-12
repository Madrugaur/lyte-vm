package virtualmachine;

public class Instruction {
	private static final int width = 3;
	private static final int opcode_lsb = 28;
	private static final int regA_lsb = 6;
	private static final int regAprime_lsb = 25;
	private static final int regB_lsb = 3;
	private static final int regC_lsb = 0;
	private static final int print_numberflag_lsb = 24;
	private int word;
	public Instruction(int word) {
		this.word = word;
	}
	public int getOpCode() {
		int code =  get(4, opcode_lsb);
		return code;
	}
	public int getRegA() {
		return get(width, regA_lsb);
	}
	public int getRegB() {
		return get(width, regB_lsb);
	}
	public int getRegC() {
		return get(width, regC_lsb);
	}
	public int getRegAPrime() {
		return get(width, regAprime_lsb);
	}
	public int getValue() {
		return word;
	}
	public int printNumber() {
		return get(1, print_numberflag_lsb);
	}
	private int get(int width, int lsb) {		
		return ((word & (one_list(width) << lsb)) >> lsb) & one_list(width);
	}
	private int one_list(int n) {
		return (0x1 << n) - 1;
	}
}
