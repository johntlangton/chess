package chess.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import chess.model.ChessPiece;

/**
 * This serves the icons for the pieces.
 * 
 * Example of a singleton pattern. The idea is you only create one instance of
 * this class and only make static references to it. That means every other
 * instance of all objects (even if you launch multiple of these appsin the same
 * jvm) all refer to only one instance of this. The reason you might want to do
 * this is to have one authoritative source for storing some sort of global
 * state. Don't abuse that though. Probably the most common initial mistakes of
 * programmers are to make way too much globally visible to make things
 * "easier". In this case we use a Singleton to control a resource so we don't
 * create too many of them - i.e. to minimize memory footprint. Since they're
 * read only anyway everyone can just grab their icons from this class.
 * 
 * @author psye
 *
 */
public class IconManager {

	private static IconManager iconManager;

	private ImageIcon blackRook, blackKnight, blackBishop, blackKing, blackQueen, blackPawn, whiteRook, whiteKnight,
			whiteBishop, whiteKing, whiteQueen, whitePawn;

	/**
	 * Private constructor. Singletons may look different in other programming
	 * languages. In java it generally means you have a private Constructor that's
	 * accessed through a static method, along with a static local reference to
	 * yourself.
	 * 
	 * Because the constructor is private, it means that it can only be called
	 * internally to this class.
	 */
	private IconManager() {
		createIcons();
	}

	/**
	 * Create all of the image icons that will be used on the buttons. You usually
	 * don't want to hard code stuff like this. Other ways to do this would be to
	 * create a properties file, so that someone would swap in other images if they
	 * wanted to. Since this is such a simple, small app I'll just hard code this
	 * icon creation for now, but before we're done, if you want, we can go ahead
	 * and change this so it's reading them in from a properties file. It's good to
	 * know how to use properties files.
	 */
	private void createIcons() {
		BufferedImage img = null;
		try {
			// The path is relative to the root of the classpath (in this case the build
			// path in the IDE)
			img = ImageIO.read(new File("img/black_rook.png"));
			blackRook = new ImageIcon(img);
			img = ImageIO.read(new File("img/white_rook.png"));
			whiteRook = new ImageIcon(img);
			img = ImageIO.read(new File("img/black_knight.png"));
			blackKnight = new ImageIcon(img);
			img = ImageIO.read(new File("img/white_knight.png"));
			whiteKnight = new ImageIcon(img);
			img = ImageIO.read(new File("img/black_bishop.png"));
			blackBishop = new ImageIcon(img);
			img = ImageIO.read(new File("img/white_bishop.png"));
			whiteBishop = new ImageIcon(img);
			img = ImageIO.read(new File("img/black_queen.png"));
			blackQueen = new ImageIcon(img);
			img = ImageIO.read(new File("img/white_queen.png"));
			whiteQueen = new ImageIcon(img);
			img = ImageIO.read(new File("img/black_king.png"));
			blackKing = new ImageIcon(img);
			img = ImageIO.read(new File("img/white_king.png"));
			whiteKing = new ImageIcon(img);
			img = ImageIO.read(new File("img/black_pawn.png"));
			blackPawn = new ImageIcon(img);
			img = ImageIO.read(new File("img/white_pawn.png"));
			whitePawn = new ImageIcon(img);
		} catch (IOException e) {
			// TODO we can talk about loggers and logging and how you might want to respod
			// to different types of errors, e.g. ones that are bad but your app can keep
			// going, vs ones that are catastrophic.
			System.out.println(e);
		}
	}

	public void updateIcon(ChessButton button, ChessPiece piece) {
		// giant switch for setting the icon of the button
		if (piece.isBlack()) {
			switch (piece.getChessPieceType()) {
			case Rook:
				button.setIcon(blackRook);
				break;
			case Knight:
				button.setIcon(blackKnight);
				break;
			case Bishop:
				button.setIcon(blackBishop);
				break;
			case King:
				button.setIcon(blackKing);
				break;
			case Queen:
				button.setIcon(blackQueen);
				break;
			case Pawn:
				button.setIcon(blackPawn);
				break;
			}
		} else {
			switch (piece.getChessPieceType()) {
			case Rook:
				button.setIcon(whiteRook);
				break;
			case Knight:
				button.setIcon(whiteKnight);
				break;
			case Bishop:
				button.setIcon(whiteBishop);
				break;
			case King:
				button.setIcon(whiteKing);
				break;
			case Queen:
				button.setIcon(whiteQueen);
				break;
			case Pawn:
				button.setIcon(whitePawn);
				break;
			}
		}
	}

	public static IconManager getIconManager() {
		if (iconManager == null) {
			iconManager = new IconManager();
		}
		return iconManager;
	}

}