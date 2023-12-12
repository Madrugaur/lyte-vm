package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import virtualmachine.OpCode;
import virtualmachine.VirtualMachine;


public class Compiler {
	private static int opcode_lsb = 28;
	private static int	reg_a_lsb = 6;
	private static int	reg_b_lsb = 3;
	private static int	reg_c_lsb = 0;
	private static int[] reg_lsbs = {6, 3, 0};
	private static int	special_register_lsb = 25;
	private static int	print_number_flag_lsb = 24;
	
	private static boolean INCOMING_LOAD = false;
	
	private static final String COMMENT = "//";
	private static int print_register = 7;
	public static void main(String[] args) throws Exception {
		File input = null;
		if (args.length == 0) {
			input = new File("C:\\Program Files (x86)\\Lyte\\lyte-sdk\\HelloWorld.lyte");
			/*System.out.println("Missing File!");
			System.exit(1);*/
		}
		else input = new File(args[0]);
		new Compiler(input);
	}
	private File input;
	public Compiler(File input) throws Exception {
		this.input = input;
		new VirtualMachine(parseAssembly());
	}
	
	private ArrayList<Integer> getBinary() throws FileNotFoundException{
		ArrayList<Integer> binaries = new ArrayList<Integer>();
		Scanner in = new Scanner(input);
		int count = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (!line.startsWith(COMMENT)) {
				if (INCOMING_LOAD) {
					binaries.add(Integer.parseInt(line));
					INCOMING_LOAD = false;
				}
				else {
					try {
						binaries.addAll(Arrays.asList(switchOnOperation(line)));						
					} catch (Exception e) {
						System.err.println(String.format("ERROR (@ ln:%d): unknown command: %s!", count, line));
						e.printStackTrace();
					}
				}
			}	
		}		
		in.close();
		return binaries;
	}
	private Integer[] switchOnOperation(String line) {
		String operation = line.substring(0, line.indexOf(' '));
		switch (operation) {
		//vitrual machine ops
		case "movc": return new Integer[] { movc(line) };
		case "segl": return new Integer[] { segl(line) };
		case "segs": return new Integer[] { segs(line) };
		case "add":  return new Integer[] { add(line) };
		case "mult": return new Integer[] { mult(line) };
		case "divi": return new Integer[] { divi(line) };
		case "nand": return new Integer[] { nand(line) };
		case "halt": return new Integer[] { halt() };
		case "map":  return new Integer[] { map(line) };
		case "umap": return new Integer[] { umap(line) };
		case "out":  return new Integer[] { out(line) };
		case "inpt": return new Integer[] { inpt(line) };
		case "lprg": return new Integer[] { lprg(line) };
		case "lval": return lval(line);
		//macros
		case "print_s": return print_s(line);
		case "sub": return new Integer[] { sub(line) };
		}
		return null;
	}
	private byte[] parseAssembly() throws IOException {
		ArrayList<Integer> list = getBinary();
		ByteBuffer bb = ByteBuffer.allocate(4 * list.size());
		for (Integer i : list) {
			bb.putInt(i);
		}
		return bb.array();
	}
	private Integer movc(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.MOVC.ordinal(), parseInts(components, 1));
	}
	private Integer segl(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.SEGL.ordinal(), parseInts(components, 1));
	}
	private Integer segs(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.SEGS.ordinal(), parseInts(components, 1));
	}
	private Integer add(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.ADD.ordinal(), parseInts(components, 1));
	}
	private Integer mult(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.MULT.ordinal(), parseInts(components, 1));
	}
	private Integer divi(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.DIVI.ordinal(), parseInts(components, 1));
	}
	private Integer nand(String line) {
		String[] components = line.split(" ");
		return getWord(OpCode.NAND.ordinal(), parseInts(components, 1));
	}
	private Integer halt() {
		int[] blank = {};
		return getWord(OpCode.HALT.ordinal(), blank);
	}
	private Integer map(String line){
		String[] components = line.split(" ");
		return getWord(OpCode.MAP.ordinal(), parseInts(components, 1));
	}
	private Integer umap(String line){
		String[] components = line.split(" ");
		return getWord(OpCode.UMAP.ordinal(), parseInts(components, 1));
	}
	private Integer inpt(String line){
		String[] components = line.split(" ");
		return getWord(OpCode.INPT.ordinal(), parseInts(components, 1));
	}
	private Integer lprg(String line){
		String[] components = line.split(" ");
		return getWord(OpCode.LPRG.ordinal(), parseInts(components, 1));
	}
	private Integer[] lval(String line){
		String[] components = line.split(" ");
		Integer[] words = initArray(2);
		words[0] |= pack(OpCode.LVAL.ordinal(), opcode_lsb);
		words[0] |= pack(Integer.parseInt(components[1]), reg_c_lsb);
		words[1] |= pack(Integer.parseInt(components[2]), 0);
		return words;
	}
	private Integer out(String line) {
		int word = 0x0;
		int[] components = parseInts(line.split(" "), 1);
		word |= pack(OpCode.OUT.ordinal(), opcode_lsb);
		word |= pack(components[1], special_register_lsb);
		word |= pack(components[2], print_number_flag_lsb);
		return word;
	}
	private Integer[] print_s(String line) {
		String message = line.substring(line.indexOf(" ") + 1);
		ArrayList<Integer> words = new ArrayList<Integer>(); 
		for (char c : message.toCharArray()) {
			words.addAll(Arrays.asList(lval("lval 7 " + (int)c)));
			words.add(out("out 7 " + (isNumber(c) ? "1" : "0")));
		}
		return words.toArray(new Integer[words.size()]);
	}
	private Integer sub(String line) {
		String[] comps = line.split(" ");
		return add("add " + comps[1] + " -" + comps[2]);
	}
	private boolean isNumber(char c) {
		return (c - '0') >= 0 && (c - '0') <= 9;
	}
	private int[] parseInts(String[] arr, int start) {
		int[] r = new int[arr.length];
		for (int i = start; i < arr.length; i++) {
			r[i] = Integer.parseInt(arr[i]);
		}
		return r;
	}
	private Integer getWord(int opcode, int[] vals) {
		int word = 0x0;
		word |= pack(opcode, opcode_lsb);
		for (int i = 0; i < vals.length; i++) {
			word |= pack(vals[i], reg_lsbs[i]);
		}
		return word;
	}
	private int pack(int val, int lsb) {
		return (val << lsb);
	}
	private Integer[] initArray(int size) {
		Integer[] temp = new Integer[size];
		for (int i = 0; i < size; i ++) {
			temp[i] = 0;
		}
		return temp;
	}
}
	