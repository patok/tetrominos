package ar.edu.ips.aus.seminario2.tetrominos.infra.ui.console;

import ar.edu.ips.aus.seminario2.tetrominos.domain.Block;
import ar.edu.ips.aus.seminario2.tetrominos.domain.PlayField;
import ar.edu.ips.aus.seminario2.tetrominos.domain.Tetromino;

public class ConsoleHelper {

    public static String output(Block block) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                if (j == 0) {
                    output.append("|");
                }
                output.append(block.getCells()[i][j] ? "X" : " ");
                if (j == block.getWidth() - 1) {
                    output.append("|");
                }
            }
            output.append("\n");
        }
        return output.toString();
    }

    public static String output(Tetromino tetro) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < tetro.getHeight(); i++) {
            for (int j = 0; j < tetro.getWidth(); j++) {
                if (j == 0) {
                    output.append("|");
                }
                if (tetro.getCells()[i][j]) {
                    output.append(output(tetro.getShape()));
                } else {
                    output.append(" ");
                }
                if (j == tetro.getWidth() - 1) {
                    output.append("|");
                }
            }
            output.append("\n");
        }
        return output.toString();
    }

    public static String output(Tetromino.Shape shape) {
        StringBuilder output = new StringBuilder();
        if (shape == null)
            output.append(ConsoleColor.RESET.toString());
        else {
            switch (shape) {
                case I:
                    output.append(ConsoleColor.MAGENTA_BACKGROUND.toString());
                    break;
                case J:
                    output.append(ConsoleColor.YELLOW_BACKGROUND.toString());
                    break;
                case L:
                    output.append(ConsoleColor.BLUE_BACKGROUND.toString());
                    break;
                case O:
                    output.append(ConsoleColor.GREEN_BACKGROUND.toString());
                    break;
                case S:
                    output.append(ConsoleColor.MAGENTA_BACKGROUND.toString());
                    break;
                case T:
                    output.append(ConsoleColor.RED_BACKGROUND.toString());
                    break;
                case Z:
                    output.append(ConsoleColor.GREEN_BACKGROUND_BRIGHT.toString());
                    break;
                default:
                    output.append(ConsoleColor.RESET.toString());
            }
        }
        output.append("X");
        output.append(ConsoleColor.RESET.toString());
        return output.toString();
    }

    public static String output(PlayField field) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < field.getHeight(); i++) {
            for (int j = 0; j < field.getWidth(); j++) {
                if (j == 0) {
                    output.append("|");
                }
                if (field.getCells()[i][j]) {
                    output.append(output(field.getShapes()[i][j]));
                } else {
                    output.append(" ");
                }
                if (j == field.getWidth() - 1) {
                    output.append("|");
                }
            }
            output.append("\n");
        }
        output.append("=");
        for (int j = 0; j < field.getWidth(); j++) {
            output.append("=");
        }
        output.append("=");
        return output.toString();
    }
}
