import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;


// This is a Singleton Java Binary Operations Utility Class
public final class BinaryNumberUtility {
	private static BinaryNumberUtility instance;

	// State variable for the remainder operations
	private boolean remainderState = false;

	private BinaryNumberUtility() {}

	public static BinaryNumberUtility getInstance() {
		if (instance == null) {
			instance = new BinaryNumberUtility();
		}

		return instance;
	}

	public int addToDecimal(long numOne, long numTwo){
		return convertToDecimal(numOne) + convertToDecimal(numTwo);
	}

	public String addBinaryNumbers(long numOne, long numTwo) {
		System.out.println(String.format("Initial Binary numbers: %d, %d", numOne, numTwo));
		List<String> resultBuffer = new ArrayList<>();

		/* In a loop, iterate through all the values of the number and let the
		 addition rule method "additionRule()" decide each value of the ArrayList resultBuffer */
		while( numOne > 0 || numTwo > 0) {
			// get the lowest order bit values
			long bitOne = numOne % 10;
			long bitTwo = numTwo % 10;
			String result = null;

			try {
				// additionRule() returns appropriate bit considering Binary-Addition rules
				result = additionRule(bitOne, bitTwo);
			} catch(InputMismatchException e) {
				System.out.println("There was an Exception thrown by the Program");
				e.printStackTrace();
			}
				// DEBUG System.out.println(String.format("%d, %d result is: %s", bitOne, bitTwo, result));
				if (!remainderState && result == "10") {
					/* if result is 2 and no current Remainder, set result to 0.
					 	Then we need to activate remainder state */
					remainderState = true;
					result = "0";
				} else if (remainderState) {
					// determineCurrentCaluewithRemainder() will toggle the state according to the current result
					result = determineCurrentValueWithRemainder(result);
				}

				resultBuffer.add(result);


			// reduce long values by one decimal point
			numOne /= 10;
			numTwo /= 10;
		}

		/* TODO "Write and Call" a Function to convert resultBuffer and the values remaining in either numOne or numTwo
		 to a String and then return the String from the Function */
		if(remainderState) {
			// if remainderState is still true after iteration, then add 1 to Buffer
			resultBuffer.add("1");
		}
		return convertResultsToString(resultBuffer);
	}

	private String determineCurrentValueWithRemainder(String result) {
		switch (result) {
		case "10":
			// if with remainder the current addition is 2, then
			// leave remainderState True and return '1'
			return "1";
		case "0":
			// if with remainder, the current addition is 0, then, switch off remainderState
			remainderState = false;
			return "1";
		case "1":
			// if with remainder, the current addition is 1, then value is '10' (2) return '0' and
			// leave state True
			return "0";
		default:
			return null;
		}

	}

	private String convertResultsToString(List<String> resultBuffer) {
		// Reverse the result first
		List<String> reversedResult = new ArrayList<>();
		for (int i = resultBuffer.size() - 1; i >= 0; i--) {
			reversedResult.add(resultBuffer.get(i));
		}

		// Build result String
		StringBuilder strBuilder = new StringBuilder();
		for (String bit : reversedResult) {
			strBuilder.append(bit);
		}

		return strBuilder.toString();
	}

	private String additionRule(long one, long two) throws InputMismatchException {
		boolean oneIsCertainlyZero = (one ^ two) == 1;
		boolean bothAreOne = (one & two) == 1;
		boolean bothAreZero = (one == 0) && (two == 0);
		if (oneIsCertainlyZero) {
			return "1";
		} else if(bothAreOne) {
			return "10";
		} else if(bothAreZero) {
			return "0";
		}

		throw new InputMismatchException("The binary numbers do not meet addition criteria");
	}
	public int convertToDecimal(long binaryValue) {
		return this.convertToDecimal(String.valueOf(binaryValue));
	}

	public int convertToDecimal(String binaryValue) {
		/* (Algorithm flow) - 'Horner Scheme Algorithm'
		 Multiply every value from the left by '2' and add to the rightmost value
		 till the "n-1" value(where n is the rightmost index) and simply add the
		 last value without Multiplying it by '2' */

		// Change the Binary to an array first
		String[] binaryArray = binaryValue.split("");

		int size = binaryArray.length;
		int currentIndex = 0;
		/* if the binary number has more than two bits, calculate the Multipliable index,
		 if not, index will be 0 */
		int multipliableIndex = size > 1 ? size - 2: 0;
		int resultBuffer = 0;

		// While currentIndex is within the Multipliable range, we implement the Horner Scheme Algorithm
		while(currentIndex <= multipliableIndex) {
			/* If this is the first iteration, use the first array value for the currentValue.
			 If not, use the previously calculated value in resultBuffer */
			long currentValue = resultBuffer > 0 ? resultBuffer : Long.parseLong(binaryArray[0]);

			// Perform the operations
			currentValue *= 2;
			long nextValue = Long.parseLong(binaryArray[currentIndex + 1]);
			currentValue += nextValue;

			// Add result of each Multiplication and Addition operation back to to resultBuffer
			resultBuffer = (int)currentValue;
			currentIndex++;
		}
		return resultBuffer;
	}

}
