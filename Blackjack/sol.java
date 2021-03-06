import java.util.*;
import java.io.*;

class State{
	public String player, dealer;
	public int stateValue;

	public State(String pla, String dea){
		player = pla;
		dealer = dea;
	}

	public String getPlayer(){
		return player;
	}

	public String getPlayerHand(){
		String hand = "";

		if(player.charAt(0)=='A' || player.charAt(0)=='X' || (player.length()!=1 && player.charAt(0)==player.charAt(1) && player.charAt(0)!='1')){
			return player;
		}else{
			if(player.length()==1){
				if(player.charAt(0)-'0'<=4){
					if(player.charAt(0)=='2'){
						return "AA";
					}else if(player.charAt(0)=='3'){
						return "A2";	
					}else{
						return "A3";
					}
				}else{
					hand = "2" + Integer.toString(player.charAt(0)-'0'-2);
				}
			}else{
				if(player.charAt(1)=='6'){
					return "X6";
				}else if(player.charAt(1)=='9'){
					return "X9";
				}else if(player.charAt(1)=='8'){
					return "X8";
				}else{
					hand = "8"+Integer.toString(Integer.parseInt(player)-8);
				}
			}
		}

		return hand;
	}

	public String getDealer(){
		return dealer;
	}
}

public class sol{
	static double p = 0;
	static double q = 0;

	public static int stateToInt(State state){
		String pla = state.getPlayer();
		String dea = state.getDealer();
		int dealer_int = 0;
		int index = 0;

		if(dea.charAt(0)=='A'){
			dealer_int = 9;
		}else if(dea.equals("X")){
			dealer_int = 8;
		}else{
			dealer_int = dea.charAt(0)-'0'-2;
		}

		if(pla.equals("XX")){
			index = 340 + dealer_int;
		}else if(pla.length()==1){
			index = 10*(pla.charAt(0)-'0'-2) + dealer_int;
		}else{
			if(pla.charAt(0)=='A'){
				if(pla.charAt(1)=='A'){
					index = 350+dealer_int;
				}else{
					index = 180 + 10*(pla.charAt(1)-'0'-2) + dealer_int;
				}
			}else if(pla.charAt(0)=='1'){
				index = 10*(Integer.parseInt(pla)-2) + dealer_int;
			}else{
				index = 260 + 10*(pla.charAt(0)-'0'-2) + dealer_int;
			}
		}
		return index;
	}

	public static State intToState(int s){
		s = s%360;
		int player_hand_int = s/10;
		int dealer_int = s%10;
		String player = "";
		String dealer = "";

		if(player_hand_int<=2){
			if(player_hand_int==0){
				player = "AA";
			}else{
				player = "A" + Integer.toString(player_hand_int+1);
			}
		}else if(player_hand_int<=17){
			player=Integer.toString(player_hand_int+2);
		}else if(player_hand_int<=25){
			player = "A"+Integer.toString(player_hand_int-16);
		}else if(player_hand_int<=33){
			player = Integer.toString(player_hand_int-24) + Integer.toString(player_hand_int-24); 
		}else if(player_hand_int==34){
			player = "XX";
		}else{
			player = "AA";
		}

		if(dealer_int==9){
			dealer = "A";
		}else if(dealer_int==8){
			dealer = "X";
		}else{
			dealer = Integer.toString(dealer_int+2);
		}
		return new State(player, dealer);
	}

	public static double computeHit(int s, State state, double[] vOfStateOld){
		double hit = 0;
		int idx = 2;
		int val = value(state.getPlayerHand(),true);
		int val2 = value2(state.getPlayerHand());
		int bet = 1;
		int dealer_int = 0;
		String dea = state.getDealer();

		if(dea.charAt(0)=='A'){
			dealer_int = 9;
		}else if(dea.equals("X")){
			dealer_int = 8;
		}else{
			dealer_int = dea.charAt(0)-'0'-2;
		}

		if(val==val2){
			for(idx = 2;idx<=11;idx++){
				if(idx==10){
					if(val+idx>21){
						hit-=(p*bet);
					}else if(val+idx==20){
						// hit+=(p*expOfState20[dealer_int]);
						hit+=(p*expOf20(bet,dealer_int));
					}else if(val+idx==21){
						hit+=(p*(exward(state.getPlayerHand()+"X",state.getDealer(),bet)));
					}else{
						hit+=(p*vOfStateOld[360+stateToInt(new State(Integer.toString(val+idx),state.getDealer()))]);
					}
				}else if(idx==11){
					if(val+idx>21){
						if(val<19){
							hit+=(q*vOfStateOld[360+stateToInt(new State(Integer.toString(val+1),state.getDealer()))]);
						}else if(val==19){
							// hit+=(q*expOfState20[dealer_int]);
							hit+=(q*expOf20(bet,dealer_int));
						}else if(val==20){
							hit+=(q*(exward(state.getPlayerHand()+"1",state.getDealer(),bet)));
						}else{
							hit-=(q*bet);
						}
					}else if(val+idx==20){
						hit+=(q*vOfStateOld[360+stateToInt(new State("A9",state.getDealer()))]);
					}else if(val+idx==21){
						hit+=(q*(exward(state.getPlayerHand()+"A",state.getDealer(),bet)));
					}else{
						// possible change of answers
						hit+=(q*Math.max(vOfStateOld[360+stateToInt(new State("A"+Integer.toString(val),state.getDealer()))],
							vOfStateOld[360+stateToInt(new State(Integer.toString(1+val),state.getDealer()))]));
						// hit+=(q*vOfStateOld[360+stateToInt(new State("A"+Integer.toString(val),state.getDealer()))]);
					}
				}else{
					if(val+idx>21){
						hit-=(q*bet);
					}else if(val+idx==21){
						hit+=(q*exward(state.getPlayerHand()+Integer.toString(idx),state.getDealer(),bet));
					}else if(val+idx==20){
						hit+=(q*expOf20(bet,dealer_int));
					}else{
						hit+=(q*vOfStateOld[360+stateToInt(new State(Integer.toString(val+idx),state.getDealer()))]);
					}
				}
			}
		}else{
			for(idx = 2;idx<=11;idx++){
				if(idx==10){
					if(val+idx>21){
						if(val2+idx<=21){
							if(val2+idx==20){
								hit+=(p*vOfStateOld[360+stateToInt(new State("XX",state.getDealer()))]);
							}else{
								hit+=(p*vOfStateOld[360+stateToInt(new State(Integer.toString(val2+idx),state.getDealer()))]);
							}
						}else{
							hit-=(p*bet);
						}
					}else if(val+idx==20){
						// hit+=(p*expOfState20[dealer_int]);
						hit+=(p*expOf20(bet,dealer_int));
					}else if(val+idx==21){
						hit+=(p*(exward(state.getPlayerHand()+"X",state.getDealer(),bet)));
					}else{
						hit+=(p*vOfStateOld[360+stateToInt(new State(Integer.toString(val+idx),state.getDealer()))]);
					}
				}else if(idx==11){
					if(val+idx>21){
						if(val<19){
							if(state.getPlayerHand().charAt(0)=='A' && state.getPlayerHand().charAt(1)=='A'){
								hit+=(q*Math.max(vOfStateOld[360+stateToInt(new State("3",state.getDealer()))],vOfStateOld[360+stateToInt(new State("13",state.getDealer()))]));
							}else if(state.getPlayerHand().charAt(0)=='A'){
								hit+=(q*Math.max(vOfStateOld[360+stateToInt(new State(Integer.toString(1+val2),state.getDealer()))],vOfStateOld[360+stateToInt(new State(Integer.toString(11+val2),state.getDealer()))]));
							}else{
								hit+=(q*vOfStateOld[360+stateToInt(new State(Integer.toString(val+1),state.getDealer()))]);
							}
						}else if(val==19){
							hit+=(q*expOf20(bet,dealer_int));
						}else if(val==20){
							hit+=(q*(exward(state.getPlayerHand()+"X1",state.getDealer(),bet)));
						}else{
							hit-=(q*bet);
						}
					}else if(val+idx==20){
						hit+=(q*Math.max(vOfStateOld[s+10],expOf20(bet,dealer_int)));
					}else if(val+idx==21){
						hit+=(q*(exward(state.getPlayerHand()+"A",state.getDealer(),bet)));
					}else{
						hit+=(q*Math.max(vOfStateOld[360+stateToInt(new State(Integer.toString(val+1),state.getDealer()))],
							vOfStateOld[360+stateToInt(new State(Integer.toString(val+11),state.getDealer()))]));
					}
				}else{
					if(val+idx>21){
						if(val2+idx<=21){
							hit+=(q*vOfStateOld[360+stateToInt(new State(Integer.toString(val2+idx),state.getDealer()))]);
						}else{
							hit-=(q*bet);
						}
					}else if(val+idx==21){
						hit+=(q*exward(state.getPlayerHand()+Integer.toString(idx),state.getDealer(),bet));
					}else if(val+idx==20){
						// System.out.println(state.getDealer());
						// hit+=(q*Math.max(expOfState20[dealer_int],vOfStateOld[360+stateToInt(new State("10",state.getDealer()))]));
						hit+=(q*Math.max(expOf20(bet,dealer_int),vOfStateOld[360+stateToInt(new State("10",state.getDealer()))]));
					}else{
						// if(state.getPlayerHand().charAt(0)=='A' && state.getPlayerHand().charAt(1)=='A'){
							// hit+=(q*Math.max(vOfStateOld[360+stateToInt(new State(Integer.toString(val+idx),state.getDealer()))],vOfStateOld[360+stateToInt(new State(Integer.toString(val2+idx),state.getDealer()))]))
						// }else{
							hit+=(q*Math.max(vOfStateOld[360+stateToInt(new State(Integer.toString(val+idx),state.getDealer()))],vOfStateOld[360+stateToInt(new State(Integer.toString(val2+idx),state.getDealer()))]));
						// }
					}
				}
			}
		}

		return hit;
	}

	public static double computeDouble(int s,State state){
		double down = 0;
		int idx = 2;
		int val = value(state.getPlayerHand(),false);
		int val2 = value2(state.getPlayerHand());
		int bet = 1;

		for(idx=2;idx<=11;idx++){
			if(idx==10){
				if(val+idx>21){
					if(val2+idx<=21){
						if(state.getPlayerHand().charAt(0)=='A' && state.getPlayerHand().charAt(1)=='A'){
							down+=(p*exward("11X",state.getDealer(),2*bet));
						}else if(state.getPlayerHand().charAt(0)=='A'){
							down+=(p*exward("1"+state.getPlayerHand().charAt(1)+"X",state.getDealer(),2*bet));
						}else{
							// do nothing
						}
					}else{
						down-=(p*2*bet);
					}
				}else{
					down+=(p*exward(state.getPlayerHand()+"X",state.getDealer(),2*bet));
				}
			}else if(idx==11){
				if(value(state.getPlayerHand(),false)>10){
					if(value(state.getPlayerHand(),false)<=21){
						if(state.getPlayerHand().charAt(0)=='A' && state.getPlayerHand().charAt(1)=='A'){
							down+=(q*exward("AAA",state.getDealer(),2*bet));
						}else if(state.getPlayerHand().charAt(0)=='A'){
							down+=(q*exward("A"+state.getPlayerHand().charAt(1)+"1",state.getDealer(),2*bet));
						}else{
							down+=(q*exward(state.getPlayerHand()+"1",state.getDealer(),2*bet));
						}
					}else{
						down-=(q*2*bet);
					}
				}else{
					down+=(q*exward(state.getPlayerHand()+"A",state.getDealer(),2*bet));
				}
			}else{
				if(val+idx>21){
					if(val2+idx<=21){
						down+=(q*exward(state.getPlayerHand()+Integer.toString(idx),state.getDealer(),2*bet));
					}else{
						down-=(q*2*bet);
					}
				}else{
					if(val!=val2){
						if(state.getPlayerHand().charAt(0)=='A' && state.getPlayerHand().charAt(1)=='A'){
							down+=(q*exward(state.getPlayerHand()+Integer.toString(idx),state.getDealer(),2*bet));
						}else{
							down+=(q*exward("A"+state.getPlayerHand().charAt(1)+Integer.toString(idx),state.getDealer(),2*bet));
						}
					}else{
						down+=(q*exward(state.getPlayerHand()+Integer.toString(idx),state.getDealer(),2*bet));
					}
				}
			}
		}
		return down;
	}

	public static double computeSplit1(int s,State state,double[] vOfStateOld){
		double split = 0;
		int bet = 1;
		int card1;
		int dealer_int;
		String dea = state.getDealer();

		if(dea.charAt(0)=='A'){
			dealer_int = 9;
		}else if(dea.equals("X")){
			dealer_int = 8;
		}else{
			dealer_int = dea.charAt(0)-'0'-2;
		}

		if(state.getPlayerHand().charAt(0)=='X'){
			card1 = 10;
		}else if(state.getPlayerHand().charAt(0)=='A'){
			card1 = 11;
		}else{
			card1 = state.getPlayerHand().charAt(0)-'0';
		}

		// for A
		if(card1!=11){
			if(card1==10){
				split+=(q*exward("AX",state.getDealer(),bet));
				// System.out.println(card1 + " A " +state.getDealer() + " " + (q*exward("AX",state.getDealer(),bet)) );
			}else{
				split+=(q*vOfStateOld[180+10*(card1-2)+dealer_int]);
			}
		}

		// for same card
		if(card1==10){
			split+=(p*vOfStateOld[s]);
		}
		else{
			split+=(q*vOfStateOld[s]);
			// if(card1==11)
				// System.out.println(card1 + " A " +state.getDealer() + " " +q + " " + (vOfStateOld[350 + dealer_int]) );
		}

		for(int i=2; i<=10; i++){
			if(card1!=i){
				if(i==10){
					if(card1!=11){
						split+=(p*vOfStateOld[10*(card1+i-2) + dealer_int]);
					}else{
						split+=(p*exward("AX",state.getDealer(),bet));
					}
				}else{
					if(card1!=11){
						split+=(q*vOfStateOld[10*(card1+i-2) + dealer_int]);
					}else{
						split+=(q*vOfStateOld[180+10*(i-2)+dealer_int]);
					}
				}
			}
		}
		// if(card1==10 && state.getDealer().charAt(0)=='2'){
							// System.out.println(card1 + " statehehe " +state.getDealer() + " " + split );
		// }

		// split = split * split;
		split*=2;
		// System.out.println(split + " " + state.getPlayer() + " " + state.getPlayerHand() + " " + state.getDealer());

		return split;
	}

	public static double compute(int s, double[] vOfStateOld){
		// double max = -Double.MAX_VALUE;
		double max = -10;
		State state = intToState(s);
		int bet = 1;
		
		// int card1;
		// if(state.getPlayerHand().charAt(0)=='X'){
		// 	card1 = 10;
		// }else if(state.getPlayerHand().charAt(0)=='A'){
		// 	card1 = 11;
		// }else{
		// 	card1 = state.getPlayerHand().charAt(0)-'0';
		// }

		// Hit
		double hitValue = -20;
		hitValue = computeHit(s,state,vOfStateOld);
		max = Math.max(max,hitValue);
		// if(card1==10 && state.getDealer().charAt(0)=='2'){
		// 					System.out.println(card1 + " statehehe hit" +state.getDealer() + " " + hitValue );
		// }

		// if(max>vOfStateOld[s]){
		// System.out.println("hitValue "+hitValue);
		// }

		// Stand
		double standValue = -20;
		standValue = exward(state.getPlayerHand(),state.getDealer(),bet);
		max = Math.max(max,standValue);
		// if(card1==10 && state.getDealer().charAt(0)=='2'){
		// 					System.out.println(card1 + " statehehe stand " +state.getDealer() + " " + standValue );
		// }

		// System.out.println("standValue "+standValue);

		if(s>=360){
			return max;
		}

		// if(max>vOfStateOld[s]){
		// 	System.out.println("Report2");
		// }

		//Double-down
			double doubleValue = -20;
			doubleValue = computeDouble(s,state); 
			max = Math.max(max,doubleValue);

		// if(card1==10 && state.getDealer().charAt(0)=='2'){
		// 					System.out.println(card1 + " statehehe double " +state.getDealer() + " " + doubleValue );
		// }
		// System.out.println("doubleValue "+doubleValue);

		// if(max>vOfStateOld[s]){
		// 	System.out.println("Report3");
		// }

		// sPlit
		double splitValue = -20;
		if(state.getPlayer().length()==2 && state.getPlayer().charAt(0)==state.getPlayer().charAt(1) && state.getPlayer().charAt(0)!='1'){
			splitValue = computeSplit1(s,state,vOfStateOld);
			max = Math.max(max,splitValue);
		}
		
		// if(splitValue != -Double.MAX_VALUE)
		// 	System.out.println("splitValue "+splitValue);

		// if(max>vOfStateOld[s]){
		// 	System.out.println("Report4");
		// }

		return max;
	}

	public static char actionWithMaxValue(int s, double[] vOfStateOld){
		State state = intToState(s);
		double max = -10;
		char action = 'A';
		int bet = 1;

		// Hit
		double hitValue = -20;
		hitValue = computeHit(s,state,vOfStateOld);
		if(hitValue>max){
			action = 'H';
			max = hitValue;
		}

		// Stand
		double standValue = -20;
		standValue = exward(state.getPlayerHand(),state.getDealer(),bet);
		if(standValue>max){
			action = 'S';
			max = standValue;
		}

		if(s>=360){
			return action;
		}

		// Double-down
		// if(s<360){
			double doubleValue = -20;
			doubleValue = computeDouble(s,state); 
			if(doubleValue>max){
				action = 'D';
				max = doubleValue;
			}
		// }

		//sPlit
		double splitValue = -20;
		if(state.getPlayer().length()==2 && state.getPlayer().charAt(0)==state.getPlayer().charAt(1) && state.getPlayer().charAt(0)!='1'){
			splitValue = computeSplit1(s,state,vOfStateOld);
			if(splitValue>max){
				action = 'P';
				max = splitValue;
			}
		}
	
		return action;		
	}

	public static double maxResidual(double[] residual){
		double max = -1;
		for (int i = 0; i < residual.length; i++){
			if (residual[i] > max){
				max = residual[i];
			}
		}
		return max;
	}

	public static char[] greedyPolicy(double[] vOfState){
		char actions[] = new char[720];
		for(int i=0; i<720; i++){
			actions[i] = actionWithMaxValue(i, vOfState);
		}
		return actions;
	}

	public static void randomGen(double[] arr){
		int idx = 0;
		for(idx = 0;idx<720;idx++){
			arr[idx] = Math.random();
			// arr[idx] = 2;
		}
	}

	public static double expOf20(int bet,int dealer){
		String str = "";
		if(dealer==8){
			str = "X";
		}else if(dealer==9){
			str = "A";
		}else{
			str = Integer.toString(dealer+2);
		}
		return exward("5555",str,bet);
	}

	public static char[] valueIteration(){
		int bet = 1;

		//Play with this to get optimal solution
		// double epsilon = 2;
		double epsilon = 0.00000001;

		char actions[] = new char[720];
		// System.out.println(actions[3]);

		double vOfStateOld[] = new double[720];
		double vOfState[] = new double[720];
		double residual[] = new double[720];
		randomGen(vOfStateOld);
		double error = 0.5;
		int n = 0;

		do{
			for(int i=0; i<720; i++){
				// if(i>=340&&i<350){
					// System.out.println("*********** "+ n + " " + vOfStateOld[i]);
				// }
				vOfState[i] = compute(i, vOfStateOld);
				residual[i] = Math.abs(vOfState[i] - vOfStateOld[i]);
			}
			
			// int count = 0;
			// for(int k = 0;k<720;k++){
			// 	if(vOfState[k]==vOfStateOld[k]){
			// 		count++;
					
			// 	}
			// }
			// System.out.println("In iteration " + n + " " + (count) + " states did not changed");

			for(int i=0; i<720; i++){
				vOfStateOld[i] = vOfState[i];
			}
			n+=1;
			// System.out.println(n);
			error = maxResidual(residual);
			// System.out.println("state 340 inside loop " + vOfStateOld[340]);
			// System.out.println(error);
			
			// if(n%100==0){
			// 	output_helper(greedyPolicy(vOfState),Integer.toString(n));
			// }	
		}while(error>epsilon);

		System.out.println("Number of iterations: " +n);


		// for(int i=0;i<720;i++){
			// if(i==340)
				// System.out.println("state"+i+" "+vOfStateOld[i]);
		// }

		return greedyPolicy(vOfState);
	}

	public static void output_helper(char[] actions,String str){
		ArrayList<String> list2 = new ArrayList<String>();
		int i=30;
		while(i<360){
			String s = "";
			for(int j=i; j<i+10; j++){
				s += actions[j];
			}
			list2.add(s);
			i+=10;
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add("HHHHHHHHHH");
		list.add("HHHHHHHHHH");
		list.add("HHHHHHHHHH");
		list.add("HHHHHHHHHH");
		list.add("HDDDDHHHHH");
		list.add("DDDDDDDDHH");
		list.add("DDDDDDDDHH");
		list.add("HHSSSHHHHH");
		list.add("SSSSSHHHHH");
		list.add("SSSSSHHHHH");
		list.add("SSSSSHHHHH");
		list.add("SSSSSHHHHH");
		list.add("SSSSSSSSSS");
		list.add("SSSSSSSSSS");
		list.add("SSSSSSSSSS");
		list.add("HHHDDHHHHH");
		list.add("HHHDDHHHHH");
		list.add("HHDDDHHHHH");
		list.add("HHDDDHHHHH");
		list.add("HDDDDHHHHH");
		list.add("SDDDDSSHHH");
		list.add("SSSSSSSSSS");
		list.add("SSSSSSSSSS");
		list.add("PPPPPPHHHH");
		list.add("PPPPPPHHHH");
		list.add("HHHPPHHHHH");
		list.add("DDDDDDDDHH");
		list.add("PPPPPHHHHH");
		list.add("PPPPPPHHHH");
		list.add("PPPPPPPPHH");
		list.add("PPPPPSPPSS");
		list.add("SSSSSSSSSS");
		list.add("PPPPPPPPPH");

		System.out.println("Number of states which match with the output present on website is " + matcher(list2,list));
		outGen(list2,str);
	}

	public static void main(String[] args){
		p = Float.parseFloat(args[0]);
		q = (1-p)/9;
		char[] actions = new char[720];
		actions = valueIteration();
		output_helper(actions,"");
	}

	public static void outGen(ArrayList<String> list,String str){
		File out = new File("./Policy"+str+".txt");
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			String s = "";
			int i,j,k;
			for(i = 5;i<20;i++){
				bw.write(Integer.toString(i));
				bw.write("\t");
				s = list.get(i-5);
				for(j = 0;j<s.length();j++){
					bw.write(s.charAt(j));
					if(j==s.length()-1){
						bw.write("\n");
					}else{
						bw.write(" ");
					}
				}
			}

			for(i = 2;i<10;i++){
				bw.write("A");
				bw.write(Integer.toString(i));
				bw.write("\t");
				s = list.get(i+13);
				for(j = 0;j<s.length();j++){
					bw.write(s.charAt(j));
					if(j==s.length()-1){
						bw.write("\n");
					}else{
						bw.write(" ");
					}
				}
			}

			for(i = 2;i<11;i++){
				bw.write(Integer.toString(i));
				bw.write(Integer.toString(i));
				bw.write("\t");
				s = list.get(i+21);
				for(j = 0;j<s.length();j++){
					bw.write(s.charAt(j));
					if(j==s.length()-1){
						bw.write("\n");
					}else{
						bw.write(" ");
					}
				}
			}

			bw.write("AA");
			bw.write("\t");
			s = list.get(32);
			for(j = 0;j<s.length();j++){
				bw.write(s.charAt(j));
				if(j!=s.length()-1){
					bw.write(" ");
				}
			}
		bw.close();
		}catch(IOException ioe){

		}
	}

	public static double exward(String pla,String dea,int bet){
		double rval = 0;
		double[] arr = new double[21-value(dea,false)];

		for(int i = 0;i<arr.length;i++){
			arr[i] = -1;
		}

		if(dea.charAt(0)=='A'){
			arr[arr.length-1] = p*reward(pla,"AX",bet);
		}else if(dea.charAt(0)=='X' || dea.charAt(0)=='K' || dea.charAt(0)=='Q' || dea.charAt(0)=='J'){
			arr[arr.length-1] = q*reward(pla,"XA",bet);
		}else{
			arr[arr.length-1] = 0;
		}

		for(int j = arr.length-2;j>=arr.length-6;j--){
			arr[j] = reward2(pla,j+value(dea,true)+2,bet);
		}

		rval = helper(pla,dea,bet,arr);
		return rval;
	}

	public static double helper(String pla,String dea,int bet,double[] arr){
		double rval = 0;
		int idx = 0;
		int val = value(dea,true);
		int val2 = val;
		
		for(int i = arr.length-7;i>=0;i--){
			val = val2+i+2;
			idx = 2;
			while(idx<=11){
				if(val+idx>21){
					if(idx==10){
						rval+=(p*bet);
					}else if(idx==11){
						rval+=(q*Math.min(bet,arr[i+1]));
						// rval+=(q*bet);
					}else{
						rval+=(q*bet);
					}
				}else{
					if(idx==10){
						rval+=(p*arr[i+idx]);
					}else if(idx==11){
						rval+=(q*Math.min(arr[i+1],arr[i+idx]));
					}else{
						rval+=(q*arr[i+idx]);
					}
				}
				idx++;
			}
			arr[i] = rval;
		}

		rval = 0;
		for(idx=0;idx<8;idx++){
			rval+=(q*arr[idx]);
		}
		rval+=(p*(arr[8]));
		rval+=arr[arr.length-1];

		return rval;
	}

	public static double reward(String pla,String dea,int bet){
		double rval = 0;
		int pval = value(pla,true);
		int dval = value(dea,true);

		if(isBJ(pla) && isBJ(dea)){
			rval = 0;
		}else if(isBJ(pla)){
			rval = 1.5*bet;
		}else if(pval>21){
			rval = -1*bet;
		}else if(dval>21){
			rval = bet;
		}else if(dval>pval){
			rval = -1*bet;
		}else if(pval>dval){
			rval = bet;
		}else if(isBJ(dea) && pval==21){
			rval = -1*bet;
		}else if(pval==dval){
			rval = 0;
		}
		return rval;
	}

	public static double reward2(String pla,int dea,int bet){
		double rval = 0;
		int pval = value(pla,true);
		int dval = dea;
		if(isBJ(pla)){
			rval = (1.5*bet);
		}else if(pval>21){
			rval = -1*bet;
		}else if(dval>21){
			rval = bet;
		}else if(dval>pval){
			rval = -1*bet;
		}else if(pval>dval){
			rval = bet;
		}else if(pval==dval){
			rval = 0;
		}
		return rval;
	}

	public static int value(String hand,boolean soft){
		int rv = 0;
		int numA = 0;
		for(int i = 0;i<hand.length();i++){
			if(hand.charAt(i)=='A'){
				numA++;
			}else if(hand.charAt(i)=='K' || hand.charAt(i)=='Q' || hand.charAt(i)=='J' || hand.charAt(i)=='X'){
				rv+=10;
			}else{
				rv+=(hand.charAt(i)-'0');
			}
		}

		if(numA==0){
			return rv;
		}

		if(soft){
			rv+=(11+numA-1);
		}else{
			if(rv>10){
				rv+=numA;
			}else{
				rv+=(numA-1);
				if(rv>10){
					rv+=1;
				}else{
					rv+=11;
				}
			}
		}
		return rv;
	}

	public static int value2(String hand){
		int rv = 0;
		for(int i = 0;i<hand.length();i++){
			if(hand.charAt(i)=='A'){
				rv+=1;
			}else if(hand.charAt(i)=='K' || hand.charAt(i)=='Q' || hand.charAt(i)=='J' || hand.charAt(i)=='X'){
				rv+=10;
			}else{
				rv+=(hand.charAt(i)-'0');
			}
		}
		return rv;
	}

	public static boolean isBJ(String s){
		if(s.length()!=2){
			return false;
		}else{
			if(s.charAt(1)=='A' && s.charAt(0)=='X'){
				return true;
			}else if(s.charAt(0)=='A' && s.charAt(1)=='X'){
				return true;
			}else{
				return false;
			}
		}
	}

	public static int matcher(ArrayList<String> l1,ArrayList<String> l2){
		int rv = 0;
		String s1;
		String s2;
		for(int i = 0;i<33;i++){
			s1 = l1.get(i);
			s2 = l2.get(i);

			for(int j = 0;j<s1.length();j++){
				if(s1.charAt(j)==s2.charAt(j)){
					rv++;
				}
			}
		}
		return rv;
	}
}