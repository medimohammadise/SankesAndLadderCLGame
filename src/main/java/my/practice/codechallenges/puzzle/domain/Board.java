package my.practice.codechallenges.puzzle.domain;

import java.util.HashMap;
import java.util.Map;
import static my.practice.codechallenges.puzzle.setting.Constants.MIN_NUMBER_SQUARES;
import my.practice.codechallenges.puzzle.io.Colors;

public class Board {
	private Map<Integer, Square> squares = null;
	private int numSquares;
	private boolean showMap=true;

	public Board(int numSquares, Map ladders, Map snakes) {
		if (numSquares<MIN_NUMBER_SQUARES) throw new RuntimeException("Number  of squares (Board's dimension) should be more than "+MIN_NUMBER_SQUARES);
		this.numSquares = numSquares;
		squares = new HashMap<>();
		makeSquares(numSquares);
		makeLaddersandSnakes(ladders, "L");
		makeLaddersandSnakes(snakes, "S");

	}

	private void makeSquares(int numSquares) {
		int squareCount = 0;
		for (int i = 0; i < numSquares; i++) {
			for (int j = 0; j < numSquares; j++) {
				Square square = new Square(squareCount, this);
				squares.put(squareCount, square);
				squareCount++;
			}
		}

	}

	/*
	 * This method create and config ladders and snakes at the same time
	 */
	private void makeLaddersandSnakes(Map ladders, String squareType) {
		int x = 0;
		int y = 0;
		int ladderCount = 0;
		String ladderName = "";
		SquareType newRole = null;

		for (Object key : ladders.keySet()) {
			{
				ladderName = key.toString();
				Map<String, String> valueList = (Map<String, String>) ladders.get(key);
				for (Map.Entry listEntry : valueList.entrySet()) {

					if (listEntry.getKey().equals("x"))
						x = Integer.valueOf(listEntry.getValue().toString());
					else
						y = Integer.valueOf(listEntry.getValue().toString());
				}

				if (squareType.equals("L"))
					newRole = new Ladder(x, y, ladderCount, ladderName);
				else
					newRole = new Sanke(x, y, ladderCount, ladderName);
                if (x>=this.numSquares*this.numSquares || y>this.numSquares*this.numSquares) throw new RuntimeException("please correct cordinates for  x : "+x+" and y : "+y+" in the configuration file" );
				squares.get(x).setSquareType(newRole);
				squares.get(y).setSquareType(newRole);
				ladderCount++;
			}

		}

	}

	public Square firstSquare() {
		return squares.get(0);
	}

	public Square lastSquare() {
		return squares.get(squares.size() - 1);

	}

	public Square findSquare(int position) {
		assert (position > 0) && (position < squares.size()) : "inexistent square";
		return squares.get(position);
	}

	/*
	 * This method print full map based on currect game status
	 */
	public String mapView() {
		int y = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		while (y < squares.size()) {
			sb.append(String.format("%-4" + "s", " "));
			for (int col = 0; col < numSquares; col++) {
				if (squares.get(y).getPlayer() != null)

					sb.append(String.format("%-5" + "s", Colors.YELLOW_BG.format("P")) + "   ");
				else if (squares.get(y).getSquareType() instanceof Sanke) {

					if (squares.get(y).getFallDownOrGoUpPosition(y) < 0)
						sb.append(String.format("%5" + "s",
								Colors.RED.format("S" + squares.get(y).getSquareType().getId() + "->")) + "   ");
					else
						sb.append(String.format("%-5" + "s",
								Colors.RED.format("-S" + squares.get(y).getSquareType().getId())) + "   ");
				} else if (squares.get(y).getSquareType() instanceof Ladder) {
					if (squares.get(y).getFallDownOrGoUpPosition(y) > 0)
						sb.append(String.format("%5" + "s",
								Colors.GREEN.format("L" + squares.get(y).getSquareType().getId() + "^")) + "   ");
					else
						sb.append(String.format("%-5" + "s",
								Colors.GREEN.format("L" + squares.get(y).getSquareType().getId())) + "   ");
				} else
					sb.append(String.format("%-5" + "d", y));
				y++;
			}
			sb.append("\n");
		}
		if (this.showMap)
			return sb.toString();
		else
			return "Showing map has disabbled by Player";

	}

	public boolean isShowMap() {
		return showMap;
	}

	public void setShowMap(boolean showMap) {
		this.showMap = showMap;
	}
	

}
