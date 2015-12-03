package org;


import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Z3Exception;

public class Knuth {

	public static void main(String[] args) {
		createTree();
		solver();
	}

/* Beginn Bailleux nach Knuth ---------------------------------------------------------*/
	static int n = 4;
	static int r = 1;
	
	/*Erstellt den Baum für n und r nur zur Überprüfung -> wird später gelöscht*/
	public static void createTree() {
		int root = 1;
		int k1;
		int k2;
		System.out.println("Wurzel: " + root + ", ");
		for(int k = 1; k < n; k++){
			System.out.println("interner Knoten: " + k);
			k1 = 2*k;
			k2 = 2*k+1;
			System.out.println("Kind links: " + k1);
			System.out.println("Kind rechts: " + k2);
		}
	}
	
	/*Zählt die Blätter unter den jeweiligen Knoten t*/
	public static int calcT(int t){
		if(t >= n){
			return 1;
		}
		/*Überschreitet die Anzahl der möglichen Knoten*/
		if(t > 2 * n - 1){
			return 0;
		}
		/*berechnet die Anzahl der Blätter rekursiv für Knoten t*/
		return calcT(2 * t) + calcT(2 * t + 1);
	}
	
	/*erste Formel wird erstellt*/
	public static void createFormulaSecond(Context ctx, Solver solver) throws Z3Exception{
		for(int i=0; i <= calcT(2); i++){
			int j = r+1-i;
			if(j >=0 && j <= calcT(3)){
				//makeLiteral(not b^2_i or not b^3_j)
				//wird in x, bzw b umgewandelt in makeVariable
				solver.add(ctx.mkOr(ctx.mkNot(makeVariable(2, i, ctx)), ctx.mkNot(makeVariable(3, j, ctx))));
			}
		}
	}
	
	/*zweite Formel wird erstellt*/
	public static void createFormulaFirst(Context ctx, Solver solver) throws Z3Exception{
		for(int k=2; k<n; k++){
			//t2k in echtzeit berechnen
			for(int i = 0; i <= calcT(2*k); i++){
				for(int j = 0; j <= calcT(2*k+1); j++){
					if(i+j <= calcT(k) + 1 && i+j >= 1){
						//not b^2k_i or not b^2k+1_j or b^k_i+j
						//in x, bzw b umwandeln in makeVariable
						//TODO mkNot raus
						solver.add(ctx.mkOr(ctx.mkNot(makeVariable(2 * k, i, ctx)), ctx.mkNot(makeVariable(2*k+1, j, ctx)), makeVariable(k, i+j, ctx)));
					}
				}
			}
		}
	}
	
	/*Esetzt Ausdrücke bei Erfüllung der Bedingungen durch x */
	private static BoolExpr makeVariable(int upperIndex, int lowerIndex, Context ctx) throws Z3Exception{
	//TODO mkNot hier rein und darauf auch überprüfen. Ausdruck dann herauslöschen.
			//ist lowerIndex 0 oder r+1 soll der Ausdruck entfernt werden
			if(lowerIndex == 0){
				return ctx.mkFalse();
			}
					if(lowerIndex == r + 1){
				return  ctx.mkFalse();
			}
			// ersetzt b mit upperindex k > n durch x mit upperindex -n+1
			if(lowerIndex == 1 && upperIndex >=n){
				//x_upperIndex-n+1
				return ctx.mkBoolConst("x" + (upperIndex - n + 1));
			}else{
				//b^upperIndex_lowerIndex
				//ansonsten wird b nicht verändert
				return ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex);
			}
		}

	/* Ende Bailleux nach Knuth ---------------------------------------------------------*/
	
	//z3 Solver
	public static void solver(){
		Context ctx = null;
		try {
			ctx = new Context();
		    Solver solver=ctx.mkSolver("QF_LIA");
		    createFormulaFirst(ctx, solver);
		    createFormulaSecond(ctx, solver);
		    if (solver.check() == Status.UNSATISFIABLE) {
		    	 System.out.println("UNSAT");
		    }
		 else {
			 System.out.println("SAT");
		      Model m=solver.getModel();
		      System.out.println("Model is" + m);
		    }
		    solver.dispose();
		  }
		 catch (  Z3Exception ex) {
		    ex.printStackTrace();
		  }
		finally {
		ctx.dispose();
		}
	}		
}