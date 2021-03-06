package chess.controller;

import java.util.ArrayList;

import chess.model.BoardCoordinate;
import chess.model.ChessBoardModel;
import chess.model.ChessPiece;

/**
 * Note that the logic in here assumes 0,0 is the top left of the board and
 * black is at the top. Most of the methods in here are static which means they
 * are common across all instances. In general, the less global variable
 * references you maintain the better (uses less memory, causes less object
 * creation and garbage collection which means less wasted computation).
 * 
 * @author John T. Langton
 *
 */
public class MoveLogic {

	/**
	 * Yes means a piece can move to a blank space. No means a piece cannot move to
	 * a space, blank or occupied. Take means a piece can move to a space to take
	 * another piece.
	 * 
	 * @author John T. Langton
	 *
	 */
	public enum Can {
		Yes, No, Take
	};

	/**
	 * This method and the methods it calls are about finding all of the possible
	 * moves of a piece.
	 * 
	 * @param model
	 * @param row
	 * @param col
	 * @return
	 */
	public static ArrayList<BoardCoordinate> getMoves(ChessBoardModel model, int row, int col) {
		ChessPiece piece = model.getChessPiece(row, col);
		if (piece == null) {
			return new ArrayList<BoardCoordinate>(0);
		}
		switch (piece.getChessPieceType()) {
		case Rook:
			return getRookMoves(model, row, col);
		case Knight:
			return getKnightMoves(model, row, col);
		case Bishop:
			return getBishopMoves(model, row, col);
		case King:
			return getKingMoves(model, row, col);
		case Queen:
			return getQueenMoves(model, row, col);
		case Pawn:
			return getPawnMoves(model, row, col);
		default:
			return new ArrayList<BoardCoordinate>(0);
		}
	}

	public static boolean onBoard(int row, int col) {
		if (row < 0 || row > 7 || col < 0 || col > 7) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This is a bit different than seeing if a piece can move to a space. This is
	 * only the logic of whether a piece can occupy a space, assuming it could move
	 * there (move logic is controlled elsewhere). This is just a convenience method
	 * since this logic is repetitive in other methods. Any time you have logic that
	 * seems to repeat in a lot of different places, consider making a method for
	 * that specific logic. That way you can reduce the overall amount of code, it's
	 * easier to read and maintain (e.g. when you make changes to that one methods,
	 * every other method that calls it benefits from the changes and is updated -
	 * rather than having to change the same logic spread across multiple methods).
	 * 
	 * @param model
	 * @param sourceRow
	 * @param sourceCol
	 * @param targetRow
	 * @param targetCol
	 * @return
	 */
	public static Can canOccupySpace(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {
		// if it's not on the board then no
		if (!(onBoard(sourceRow, sourceCol) && onBoard(targetRow, targetCol))) {
			return Can.No;
		}

		ChessPiece source = model.getChessPiece(sourceRow, sourceCol);
		// should never really get this, can't move a blank space
		if (source == null) {
			return Can.No;
		}

		ChessPiece target = model.getChessPiece(targetRow, targetCol);
		// if there's no piece in the location then sure
		if (target == null) {
			return Can.Yes;
		}
		// if the piece in the target location belongs to the other player (indicated by
		// the color being different) then yes, you can take it
		else if (source.isBlack() != target.isBlack()) {
			return Can.Take;
		}
		// the only other situation is your own
		// piece is in the location so no you can't
		else {
			return Can.No;
		}
	}

	public static ArrayList<BoardCoordinate> getRookMoves(ChessBoardModel model, int row, int col) {
		// I put a 16 into the constructor because we know it will never get larger
		ArrayList<BoardCoordinate> locations = new ArrayList<BoardCoordinate>(16);

		// go left
		for (int targetCol = col - 1; targetCol > -1; targetCol--) {
			Can result = canOccupySpace(model, row, col, row, targetCol);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(row, targetCol));
			} else if (result == Can.Take) {
				// can't move any further in this direction because we took a piece
				locations.add(new BoardCoordinate(row, targetCol));
				break;
			} else {
				// stop as soon as we reach a space we can't occupy
				// this breaks out of the for loop
				break;
			}
		}

		// go down
		for (int targetRow = row + 1; targetRow < 8; targetRow++) {
			Can result = canOccupySpace(model, row, col, targetRow, col);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(targetRow, col));
			} else if (result == Can.Take) {
				locations.add(new BoardCoordinate(targetRow, col));
				break;
			} else {
				break;
			}
		}

		// go right
		for (int targetCol = col + 1; targetCol < 8; targetCol++) {
			Can result = canOccupySpace(model, row, col, row, targetCol);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(row, targetCol));
			} else if (result == Can.Take) {
				locations.add(new BoardCoordinate(row, targetCol));
				break;
			}

			else {
				break;
			}
		}

		// go up
		for (int targetRow = row - 1; targetRow > -1; targetRow--) {
			Can result = canOccupySpace(model, row, col, targetRow, col);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(targetRow, col));
			} else if (result == Can.Take) {
				locations.add(new BoardCoordinate(targetRow, col));
				break;
			} else {
				break;
			}
		}

		return locations;
	}

	/**
	 * There is a max of 8 possible moves for the knight
	 * 
	 * @param model
	 * @param row
	 * @param col
	 * @return
	 */
	public static ArrayList<BoardCoordinate> getKnightMoves(ChessBoardModel model, int row, int col) {
		ArrayList<BoardCoordinate> locations = new ArrayList<BoardCoordinate>(16);

		// up 2 right 1
		int targetRow = row - 2, targetCol = col + 1;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}
		// up 2 left 1
		targetRow = row - 2;
		targetCol = col - 1;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}
		// down 2 right 1
		targetRow = row + 2;
		targetCol = col + 1;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}

		// down 2 left 1
		targetRow = row + 2;
		targetCol = col - 1;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}

		// left 2 up 1
		targetRow = row - 1;
		targetCol = col - 2;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}

		// left 2 down 1
		targetRow = row + 1;
		targetCol = col - 2;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}

		// right 2 up 1
		targetRow = row - 1;
		targetCol = col + 2;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}

		// right 2 down 1
		targetRow = row + 1;
		targetCol = col + 2;
		if (canOccupySpace(model, row, col, targetRow, targetCol) != Can.No) {
			locations.add(new BoardCoordinate(targetRow, targetCol));
		}
		return locations;
	}

	public static ArrayList<BoardCoordinate> getBishopMoves(ChessBoardModel model, int row, int col) {
		ArrayList<BoardCoordinate> locations = new ArrayList<BoardCoordinate>(16);

		// go left and up
		for (int targetCol = col - 1, targetRow = row - 1; targetCol > -1 && targetRow > -1; targetCol--, targetRow--) {
			Can result = canOccupySpace(model, row, col, targetRow, targetCol);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(targetRow, targetCol));
			} else if (result == Can.Take) {
				// can't move any further in this direction because we took a piece
				locations.add(new BoardCoordinate(targetRow, targetCol));
				break;
			} else {
				// stop as soon as we reach a space we can't occupy
				// this breaks out of the for loop
				break;
			}
		}

		// go right and up (the only logic that changes is the indexes)
		for (int targetCol = col + 1, targetRow = row - 1; targetCol < 8 && targetRow > -1; targetCol++, targetRow--) {
			Can result = canOccupySpace(model, row, col, targetRow, targetCol);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(targetRow, targetCol));
			} else if (result == Can.Take) {
				// can't move any further in this direction because we took a piece
				locations.add(new BoardCoordinate(targetRow, targetCol));
				break;
			} else {
				// stop as soon as we reach a space we can't occupy
				// this breaks out of the for loop
				break;
			}
		}

		// go left and down
		for (int targetCol = col - 1, targetRow = row + 1; targetCol > -1 && targetRow < 8; targetCol--, targetRow++) {
			Can result = canOccupySpace(model, row, col, targetRow, targetCol);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(targetRow, targetCol));
			} else if (result == Can.Take) {
				// can't move any further in this direction because we took a piece
				locations.add(new BoardCoordinate(targetRow, targetCol));
				break;
			} else {
				// stop as soon as we reach a space we can't occupy
				// this breaks out of the for loop
				break;
			}
		}

		// go right and down
		for (int targetCol = col + 1, targetRow = row + 1; targetCol < 8 && targetRow < 8; targetCol++, targetRow++) {
			Can result = canOccupySpace(model, row, col, targetRow, targetCol);
			if (result == Can.Yes) {
				locations.add(new BoardCoordinate(targetRow, targetCol));
			} else if (result == Can.Take) {
				// can't move any further in this direction because we took a piece
				locations.add(new BoardCoordinate(targetRow, targetCol));
				break;
			} else {
				// stop as soon as we reach a space we can't occupy
				// this breaks out of the for loop
				break;
			}
		}

		return locations;
	}

	public static ArrayList<BoardCoordinate> getKingMoves(ChessBoardModel model, int row, int col) {
		// TODO: Leaving for the nephews to complete.
		return new ArrayList<BoardCoordinate>(0);
	}

	public static ArrayList<BoardCoordinate> getQueenMoves(ChessBoardModel model, int row, int col) {
		// A queen can basically move like a rook and bishop combined
		ArrayList<BoardCoordinate> locations = getRookMoves(model, row, col);
		locations.addAll(getBishopMoves(model, row, col));
		return locations;
	}

	public static ArrayList<BoardCoordinate> getPawnMoves(ChessBoardModel model, int sourceRow, int sourceCol) {
		ChessPiece piece = model.getChessPiece(sourceRow, sourceCol);
		if (piece == null) {
			return new ArrayList<BoardCoordinate>(0);
		}

		ArrayList<BoardCoordinate> moves = new ArrayList<BoardCoordinate>();

		// if you're black
		if (piece.isBlack()) {
			// See if you can move 1 down
			// You can move down one if there's no piece there and it's still on the board.
			int targetRow = sourceRow + 1;
			// just including for clarity but unnecessary
			int targetCol = sourceCol;
			if (canOccupySpace(model, sourceRow, sourceCol, targetRow, targetCol) == Can.Yes) {
				moves.add(new BoardCoordinate(targetRow, targetCol));
			}
			targetRow = sourceRow + 2;
			targetCol = sourceCol;
			// see if you can move 2 down, not only sourceCol value changes in this logic
			if (sourceRow == 1 && canOccupySpace(model, sourceRow, sourceCol, targetRow, targetCol) == Can.Yes) {
				moves.add(new BoardCoordinate(targetRow, sourceCol));
			}
			// see if we can move down and to the right to take a piece
			targetRow = sourceRow + 1;
			targetCol = sourceCol + 1;
			if (canOccupySpace(model, sourceRow, sourceCol, targetRow, targetCol) == Can.Take) {
				moves.add(new BoardCoordinate(targetRow, targetCol));
			}
			// see if we can move down and to the left to take a piece
			targetRow = sourceRow + 1;
			targetCol = sourceCol - 1;
			if (canOccupySpace(model, sourceRow, sourceCol, targetRow, targetCol) == Can.Take) {
				moves.add(new BoardCoordinate(targetRow, targetCol));
			}
		}

		// if you're white, just the inverse of the prior logic
		else {
			int targetRow = sourceRow - 1;
			if (canOccupySpace(model, sourceRow, sourceCol, targetRow, sourceCol) == Can.Yes) {
				moves.add(new BoardCoordinate(targetRow, sourceCol));
			}
			targetRow = sourceRow - 2;
			if (sourceRow == 6 && canOccupySpace(model, sourceRow, sourceCol, targetRow, sourceCol) == Can.Yes) {
				moves.add(new BoardCoordinate(targetRow, sourceCol));
			}
			int targetCol = sourceCol + 1;
			targetRow = sourceRow - 1;
			if (canOccupySpace(model, sourceRow, sourceCol, targetRow, targetCol) == Can.Take) {
				moves.add(new BoardCoordinate(targetRow, targetCol));
			}
			targetCol = sourceCol - 1;
			targetRow = sourceRow - 1;
			if (canOccupySpace(model, sourceRow, sourceCol, targetRow, targetCol) == Can.Take) {
				moves.add(new BoardCoordinate(targetRow, targetCol));
			}
		}

		return moves;
	}

	public static boolean canMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow, int targetCol) {
		// A few quick sanity checks in here so we don't have to
		// repeat the logic in all of the methods called form here
		// can't move a blank
		ChessPiece sourcePiece = model.getChessPiece(sourceRow, sourceCol);
		if (sourcePiece == null) {
			return false;
		}
		// both positions must be on the board
		if (!(onBoard(sourceRow, sourceCol) && onBoard(targetRow, targetCol))) {
			return false;
		}
		// if it's not your turn
		if (model.getTurn() != sourcePiece.isBlack()) {
			return false;
		}
		// if you're trying to take a piece of the same color
		ChessPiece targetPiece = model.getChessPiece(targetRow, targetCol);
		if (targetPiece != null && sourcePiece.isBlack() == targetPiece.isBlack()) {
			return false;
		}

		/*
		 * The rest of these methods have logic that focuses on whether a piece can move
		 * where they're trying to move, and if there's another piece in the way before
		 * they get to the intended target (except for the knight who jumps).
		 */
		switch (sourcePiece.getChessPieceType()) {
		case Rook:
			return canRookMove(model, sourceRow, sourceCol, targetRow, targetCol);
		case Knight:
			return canKnightMove(model, sourceRow, sourceCol, targetRow, targetCol);
		case Bishop:
			return canBishopMove(model, sourceRow, sourceCol, targetRow, targetCol);
		case King:
			return canKingMove(model, sourceRow, sourceCol, targetRow, targetCol);
		case Queen:
			return canQueenMove(model, sourceRow, sourceCol, targetRow, targetCol);
		case Pawn:
			return canPawnMove(model, sourceRow, sourceCol, targetRow, targetCol);
		default:
			return false;
		}
	}

	public static boolean canRookMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {
		// how much you're moving up/down
		int yDelta = targetRow - sourceRow;
		// how much you're moving left/right
		int xDelta = targetCol - sourceCol;

		// In this case I decided to look for all conditions
		// where the move was illegal and otherwise allow it

		// you can only move up, down, left, and right,
		// so one of your deltas has to be 0
		if (yDelta != 0 && xDelta != 0) {
			return false;
		}

		// Otherwise let's see if there's a piece between where we are and where we want
		// to be. Note if yDelta is 1 then we're only moving 1 space so don't have to
		// check if there's a piece between, so we start checking at yDelta>1.

		// if we're moving down
		if (yDelta > 1) {
			for (int row = sourceRow + 1; row < targetRow; row++) {
				if (model.getChessPiece(row, targetCol) != null) {
					return false;
				}
			}
		}
		// if we're moving up
		else if (yDelta < 1) {
			for (int row = sourceRow - 1; row > targetRow; row--) {
				if (model.getChessPiece(row, targetCol) != null) {
					return false;
				}
			}
		}
		// if we're moving right
		else if (xDelta > 1) {
			for (int col = sourceCol + 1; col < targetCol; col++) {
				if (model.getChessPiece(col, targetRow) != null) {
					return false;
				}
			}
		}
		// if we're moving left
		else if (xDelta < 1) {
			for (int col = sourceCol - 1; col > targetCol; col--) {
				if (model.getChessPiece(col, targetRow) != null) {
					return false;
				}
			}
		}

		// don't necessarily need an else

		// otherwise this is a legal move
		return true;
	}

	public static boolean canKnightMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {
		// TODO Leaving for the nephews to complete

		// you don't have to check if there's a piece between the source and target
		// location because knights jump. Just have to make sure it's a legal move. You
		// can refer to the getKnightMoves() method for guidance or even call it in your
		// logic or refactor so this method and it share logic.
		return false;
	}

	public static boolean canBishopMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {
		// how much you're moving up/down
		int yDelta = targetRow - sourceRow;
		// how much you're moving left/right
		int xDelta = targetCol - sourceCol;

		// you can only move diagonal (remember that yDelta or xDelta could be negative
		// but their squares should be the same sign and value)
		if (((yDelta * yDelta) != (xDelta * xDelta)) || xDelta == 0) {
			return false;
		}

		// otherwise let's see if there's a piece between
		// where we are and where we want to be

		// if we're moving down and to the right
		if (yDelta > 1 && xDelta > 1) {
			for (int col = sourceCol + 1, row = sourceRow + 1; col < targetCol; col++, row++) {
				if (model.getChessPiece(row, col) != null) {
					return false;
				}
			}
		}

		// if we're moving down and to the left
		else if (yDelta < -1 && xDelta > 1) {
			for (int col = sourceCol - 1, row = sourceRow + 1; col > targetCol; col--, row++) {
				if (model.getChessPiece(row, col) != null) {
					return false;
				}
			}
		}

		// if we're moving up and to the right
		else if (yDelta > 1 && xDelta < -1) {
			for (int col = sourceCol + 1, row = sourceRow - 1; col < targetCol; col++, row--) {
				if (model.getChessPiece(row, col) != null) {
					return false;
				}
			}
		}

		// if we're moving up and to the left
		else if (yDelta < -1 && xDelta < -1) {
			for (int col = sourceCol - 1, row = sourceRow - 1; col > targetCol; col--, row--) {
				if (model.getChessPiece(row, col) != null) {
					return false;
				}
			}
		}

		// don't necessarily need an else

		// otherwise this is a legal move
		return true;
	}

	public static boolean canKingMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {
		// TODO Leaving for the nephews to complete
		// The kind can move in any direction as long
		// as it's only 1 away from where it is.
		return false;
	}

	public static boolean canQueenMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {
		// A queen can basically move like a rook and bishop combined. So if neither the
		// rook nor bishop could make this move then the queen cannot, otherwise one of
		// them can, thus the queen can
		if (!canRookMove(model, sourceRow, sourceCol, targetRow, targetCol)
				&& !canBishopMove(model, sourceRow, sourceCol, targetRow, targetCol)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean canPawnMove(ChessBoardModel model, int sourceRow, int sourceCol, int targetRow,
			int targetCol) {

		ChessPiece sourcePiece = model.getChessPiece(sourceRow, sourceCol);
		ChessPiece targetPiece = model.getChessPiece(targetRow, targetCol);
		boolean sourceIsBlack = sourcePiece.isBlack();

		// how much you're moving up/down
		int yDelta = targetRow - sourceRow;
		// how much you're moving left/right
		int xDelta = targetCol - sourceCol;

		// default to returning false, look for any true condition
		// start with black which can only move down
		if (sourceIsBlack) {
			// if your on the starting row, you can move down 2, if you don't move sideways,
			// and there's no piece in that location
			if (sourceRow == 1 && yDelta == 2 && xDelta == 0 && targetPiece == null) {
				return true;
			}
			// otherwise you can move down 1 if there's no piece in that location
			if (yDelta == 1 && xDelta == 0 && targetPiece == null) {
				return true;
			}

			// otherwise you can move down and sideways 1 if you're taking a piece that
			// belongs to the other player
			if (yDelta == 1 && (xDelta == 1 || xDelta == -1) && targetPiece != null
					&& sourceIsBlack != targetPiece.isBlack()) {
				return true;
			}
		}
		// otherwise if you're white, you kind of invert the logic
		else {
			if (sourceRow == 6 && yDelta == -2 && xDelta == 0 && targetPiece == null) {
				return true;
			}
			if (yDelta == -1 && xDelta == 0 && targetPiece == null) {
				return true;
			}
			if (yDelta == -1 && (xDelta == 1 || xDelta == -1) && targetPiece != null
					&& sourceIsBlack != targetPiece.isBlack()) {
				return true;
			}
		}
		// all other moves are illegal
		return false;
	}
}