package com.ddlan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04 {
    static List<Integer> ints;
    static List<List<List<Integer>>> boards;
    static List<List<List<Boolean>>> markeds;

    static void readInput(boolean practice) {
        try {
            String filepath;
            if (practice) {
                filepath = "src/com/ddlan/Day04Practice.txt";
            } else {
                filepath = "src/com/ddlan/Day04.txt";
            }
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);

            String firstLine = scanner.nextLine();
            Scanner intScanner = new Scanner(firstLine).useDelimiter(",");
            List<Integer> currInts = new ArrayList<>();
            while (intScanner.hasNextInt()) {
                currInts.add(intScanner.nextInt());
            }
            ints = currInts;

            List<List<List<Integer>>> currBoards = new ArrayList<>();
            List<List<List<Boolean>>> currMarkeds = new ArrayList<>();
            List<List<Integer>> currBoard = null;
            List<List<Boolean>> currMarked = null;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    if (currBoard != null) {
                        currBoards.add(currBoard);
                        currMarkeds.add(currMarked);
                    }
                    currBoard =  new ArrayList<>();
                    currMarked =  new ArrayList<>();
                } else {
                    Scanner boardScanner = new Scanner(line);
                    List<Integer> boardLine = new ArrayList<>();
                    List<Boolean> markedLine = new ArrayList<>();
                    while (boardScanner.hasNextInt()) {
                        boardLine.add(boardScanner.nextInt());
                        markedLine.add(false);

                    }
                    currBoard.add(boardLine);
                    currMarked.add(markedLine);
                }
            }

            // add last board
            currBoards.add(currBoard);
            currMarkeds.add(currMarked);

            boards = currBoards;
            markeds = currMarkeds;

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static boolean didWin(List<List<Boolean>> marked, int y, int x) {
        boolean win = true;
        for (int i = 0; i < marked.size(); i++) {
            if (!marked.get(i).get(x)) {
                win = false;
                break;
            }
        }

        if (win) return true;
        win = true;

        for (int j = 0; j < marked.get(0).size(); j++) {
            if (!marked.get(y).get(j)) {
                win = false;
                break;
            }
        }

        return win;
    }

    private static boolean markInt(int boardNum, int bingoInt) {
        List<List<Integer>> board = boards.get(boardNum);
        List<List<Boolean>> marked = markeds.get(boardNum);

        if (board == null) return false;

        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size(); j++) {
                if (board.get(i).get(j) == bingoInt) {
                    marked.get(i).set(j, true);
                    return didWin(marked, i, j);
                }
            }
        }
        return false;
    }

    private static int getScore(int boardNum) {
        List<List<Integer>> board = boards.get(boardNum);
        List<List<Boolean>> marked = markeds.get(boardNum);

        int unmarkedSum = 0;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size(); j++) {
                if (!marked.get(i).get(j)) unmarkedSum += board.get(i).get(j);
            }
        }

        return unmarkedSum;
    }

    static int part1() {
        for (Integer bingoInt: ints) {
            for (int i = 0; i < boards.size(); i++) {
                if (markInt(i, bingoInt)) {
                    return bingoInt * getScore(i);
                }
            }
        }

        return -1;
    }

    static int part2() {
        int winnersLeft = boards.size();
        for (Integer bingoInt: ints) {
            for (int i = 0; i < boards.size(); i++) {
                if (markInt(i, bingoInt)) {
                    if (winnersLeft == 1) {
                        return bingoInt * getScore(i);
                    }

                    winnersLeft--;
                    boards.set(i, null);
                }
            }
        }

        return -1;
    }
}
