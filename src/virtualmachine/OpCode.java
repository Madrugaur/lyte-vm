package virtualmachine;

public enum OpCode {
	MOVC(0), SEGL(1), SEGS(2), ADD(3), MULT(4), DIVI(5), NAND(6), HALT(7), MAP(8), UMAP(9), OUT(10), INPT(11), LPRG(12), LVAL(13);
	int value = -1;
	OpCode(int i){
		value = i;
	}
	public static OpCode get(int i) {
		assert(i < OpCode.values().length);
		return OpCode.values()[i];
	}
}
