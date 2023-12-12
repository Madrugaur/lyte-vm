package virtualmachine;
import java.io.IOException;

public class VirtualMachine {
	private byte[] source;
	
	private int[] registers;
	private MemorySegments memory_segments;
	private int program_counter = 0x0;
	private int num_reg = 8;
	private boolean loading = false;
	private int loading_reg = -1;
	
	public static final int EOF_TOKEN = -1;
	public VirtualMachine(byte[] source) throws Exception {
		this.source = source;
		assert(source.length % 4 == 0);
		
		registers = new int[num_reg];
		memory_segments = new MemorySegments();
		
		parseAllIntructions();
		
		run();

	}
	public void run() throws Exception {
		int word;
		while ((word = this.memory_segments.get(0, program_counter)) != EOF_TOKEN) {
			if (word == EOF_TOKEN) break;
			executeInstruction(new Instruction(word));
			program_counter++; 
		}
	}
	static int i = 0;
	private void executeInstruction(Instruction instruct) throws Exception {
		if (loading) {
			exActualLoad(instruct.getValue());
			return;
		}

		OpCode opcode = OpCode.get(instruct.getOpCode());
 		switch(opcode) {
 		case ADD:
 		{
 			int ara = instruct.getRegA();
 			int arb = instruct.getRegB();
 			int arc = instruct.getRegC();
 			exAddOp(ara, arb, arc);
 			break;
 		}
 		case LVAL:
 		{
 			int lr = instruct.getRegC();
 			exLoadOp(lr);
 			break;
 		}
 		case OUT:
 		{
 			int pr = instruct.getRegAPrime();
 			int flag = instruct.printNumber();
 			exPrintOp(pr, flag);
 			break;
 		}
 		case MOVC:
 		{
 			int ra = instruct.getRegA();
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exConditionalMove(ra,rb,rc);
 			break;
 		}
 		case SEGL:
 		{
 			int ra = instruct.getRegA();
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exSegmentedLoad(ra, rb,rc);
 			break;
 		}
 		case SEGS:
 		{
 			int ra = instruct.getRegA();
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exSegmentedStore(ra,rb,rc);
 			break;
 		}
 		case MULT:
 		{
 			int ra = instruct.getRegA();
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exMultiplication(ra,rb,rc);
 			break;
 		}
 		case DIVI:
 		{
 			int ra = instruct.getRegA();
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exDivision(ra,rb,rc);
 			break;
 		}
 		case NAND:
 		{
 			int ra = instruct.getRegA();
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exNAND(ra,rb,rc);
 			break;
 		}
 		case HALT:
 		{
 			exHalt();
 		}
 		case MAP:
 		{
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exMapSegment(rb,rc);
 			break;
 		}
 		case UMAP:
 		{
 			int rc = instruct.getRegC();
 			exUnmapSegment(rc);
 			break;
 		}
 		case INPT:
 		{
 			int rc = instruct.getRegC();
 			exInput(rc);
 			break;
 		}
 		case LPRG:
 		{
 			int rb = instruct.getRegB();
 			int rc = instruct.getRegC();
 			exLoadProgram(rb,rc);
 			break;
 		}
 		default:
 			throw new Exception();
 		}
	}
	private void exConditionalMove(int ra, int rb, int rc) {
		this.validate_regester_access(ra,rb,rc);
		if (registers[rc] != 0) registers[ra] = registers[rb];
	}
	private void exSegmentedLoad(int ra, int rb, int rc) {
		this.validate_regester_access(ra,rb,rc);
		registers[ra] = memory_segments.get(registers[rb], registers[ra]);
	}
	private void exSegmentedStore(int ra, int rb, int rc) {
		this.validate_regester_access(ra,rb,rc);
		memory_segments.get(registers[ra]).put(registers[rb], registers[rb]);
	}
	private void exMultiplication(int ra, int rb, int rc) {
		this.validate_regester_access(ra,rb,rc);
		registers[ra] = (int) ((registers[rb] * registers[rc]) % (Math.pow(2, 32)));
	}
	private void exDivision(int ra, int rb, int rc) {
		this.validate_regester_access(ra,rb,rc);
		registers[ra] = Math.floorDiv(registers[rb], registers[rc]);
	}
	private void exNAND(int ra, int rb, int rc) {
		this.validate_regester_access(ra,rb,rc);
		registers[ra] = ~(registers[rb] | registers[rc]);
	}
	private void exHalt() {
		System.exit(1);
	}
	private void exMapSegment(int rb, int rc) {
		this.validate_regester_access(rb,rc);
		MemorySegment mem_seg = new MemorySegment(registers[rc]);
		registers[rb] = memory_segments.map(mem_seg);
	}
	private void exUnmapSegment(int rc) {
		this.validate_regester_access(rc);
		memory_segments.unmap(registers[rc]);
	}
	private void exInput(int rc) throws IOException {
		this.validate_regester_access(rc);
		registers[rc] = System.in.read();
	}
	private void exLoadProgram(int rb, int rc) {
		this.validate_regester_access(rb,rc);
		if (registers[rb] != 0) {
			this.memory_segments.set(0, memory_segments.get(registers[rb]));
		}
		program_counter = registers[rc];
	}
	private void exActualLoad(int value) {
		registers[loading_reg] = value;
		loading = false;
	}
	private void exAddOp(int ra, int rb, int rc) {
		this.validate_regester_access(ra, rb, rc);
		registers[ra] = registers[rb] + registers[rc];	
	}
	private void exLoadOp(int r) {
		assert(r < num_reg);
		loading = true;
		loading_reg = r;
	}
	private void exPrintOp(int r, int flag) {
		this.validate_regester_access(r);
		if (flag == 1) {
			System.out.print(registers[r]);
		}else {
			int val = registers[r];
			if (val < 256) {
				System.out.print((char) val);
			}
		}
	}
	private void parseAllIntructions() {
		int word_count = source.length / 4;
		MemorySegment mem_seg = new MemorySegment(word_count);
		for (int i = 0, curr_word = 0; i < source.length; i += 4, curr_word++) {
			int word = 0x0;
			for (int k = i; k < i + 4; k++) {
				word |= (source[k] & 0xFF) << (32 - (8 * (k + 1)));
			} 
			mem_seg.put(curr_word, word);
		}
		memory_segments.map(mem_seg);
	}
	private void validate_regester_access(int... reg_indices) {
		for (int reg_index : reg_indices) {
			assert(reg_index < num_reg);
		}
	}
}
