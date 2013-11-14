package edu.gatech.constraints.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InfixToPostfix {
	private Map<String, Integer> operators;

	public InfixToPostfix() {
		operators = new HashMap<String, Integer>();
		operators.put("-", 1);
		operators.put("+", 1);
		operators.put("*", 2);
		operators.put("/", 2);
		operators.put("(", 3);
		operators.put(")", 3);
	}

	public List<String> convertInfixToPostfix(String str) {
		List<String> AL = new ArrayList<String>();
		MyStackList<String> operatorStack = new MyStackList<String>();
		String parts[] = str.split(" ");
		String stackTop;
		for (String part : parts) {
			if (contains(operators.keySet(), part)) {
				if (operatorStack.size() == 0) {
					operatorStack.push(part);
					continue;
				}

				stackTop = operatorStack.peep();
				int stackOperatorPrecedence = operators.get(stackTop);
				int currentOperatorPrecedence = operators.get(part);

				if (part.equals(")")) {
					stackTop = operatorStack.pop();
					while (!stackTop.equals("(")) {
						AL.add(stackTop);
						stackTop = operatorStack.pop();
					}
				} else {

					if (stackOperatorPrecedence < currentOperatorPrecedence || stackTop.equals("(")) {
						operatorStack.push(part);
					} else {
						while (currentOperatorPrecedence <= stackOperatorPrecedence && operatorStack.size() != 0) {
							stackTop = operatorStack.pop();
							if (!stackTop.equals(")") && !stackTop.equals("(")) {
								AL.add(stackTop);
							}
							if (operatorStack.size() != 0) {
								stackTop = operatorStack.peep();
								stackOperatorPrecedence = operators.get(stackTop);
							}
						}
						operatorStack.push(part);
					}
				}
			} else {
				AL.add(part);
			}
		}
		while (operatorStack.size() > 0) {
			stackTop = operatorStack.pop();
			if (stackTop.equals("(") || stackTop.equals(")")) {
				continue;
			}
			AL.add(stackTop);
		}

		for (int i = 0; i < AL.size(); i++) {
			System.out.println(AL.get(i));
		}
		return AL;
	}

	private boolean contains(Set<String> haystack, String needle) {
		boolean contains = false;
		for (String item : haystack) {
			if (needle.equalsIgnoreCase(item)) {
				contains = true;
				break; // No need to look further.
			}
		}
		return contains;
	}

}
