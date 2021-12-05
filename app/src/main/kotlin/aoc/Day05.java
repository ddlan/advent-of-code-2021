package com.ddlan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day05 {
    static class Coord {
        int x;
        int y;
        int hashCode;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
            this.hashCode = Objects.hash(x, y);
        }

        public String toString() {
            return "(" + x + "," + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Coord that = (Coord) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }

    static class LineSegment {
        Coord a;
        Coord b;
        int hashCode;

        public LineSegment(Coord a, Coord b) {
            this.a = a;
            this.b = b;
            this.hashCode = Objects.hash(a, b);
        }

        public String toString() {
            return "(" + a.toString() + "," + b.toString() + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            LineSegment that = (LineSegment) o;
            return a == that.a && b == that.b;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }

    static List<LineSegment> lineSegments;

    static void readInput(boolean practice) {
        try {
            String filepath;
            if (practice) {
                filepath = "src/com/ddlan/Day05Practice.txt";
            } else {
                filepath = "src/com/ddlan/Day05.txt";
            }
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);

            lineSegments = new ArrayList<>();
            while (scanner.hasNextLine()) {
                Scanner lineScanner = new Scanner(scanner.nextLine()).useDelimiter(",| -> ");

                int ax = lineScanner.nextInt();
                int ay = lineScanner.nextInt();
                int bx = lineScanner.nextInt();
                int by = lineScanner.nextInt();

                LineSegment lineSegment = new LineSegment(new Coord(ax, ay), new Coord(bx, by));
                lineSegments.add(lineSegment);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static int drawVertical(LineSegment lineSegment, Map<Coord, Integer> covers) {
        int smaller = Math.min(lineSegment.a.y, lineSegment.b.y);
        int larger = Math.max(lineSegment.a.y, lineSegment.b.y);
        int overlaps = 0;

        for (int i = smaller; i <= larger; i++) {
            Coord coord = new Coord(lineSegment.a.x, i);
            Integer curr = covers.get(coord);

            if (curr == null) curr = 0;
            else if (curr == 1) overlaps++;

            covers.put(coord, curr + 1);
        }

        return overlaps;
    }

    private static int drawHorizontal(LineSegment lineSegment, Map<Coord, Integer> covers) {
        int smaller = Math.min(lineSegment.a.x, lineSegment.b.x);
        int larger = Math.max(lineSegment.a.x, lineSegment.b.x);
        int overlaps = 0;

        for (int i = smaller; i <= larger; i++) {
            Coord coord = new Coord(i, lineSegment.a.y);

            Integer curr = covers.get(coord);
            if (curr == null) curr = 0;
            else if (curr == 1)  overlaps++;

            covers.put(coord, curr+1);
        }

        return overlaps;
    }

    private static int drawUpRightDiagonal(LineSegment lineSegment, Map<Coord, Integer> covers) {
        int smallerX, largerX, smallerY;
        if (lineSegment.a.x < lineSegment.b.x) {
            smallerX = lineSegment.a.x;
            largerX = lineSegment.b.x;
            smallerY = lineSegment.a.y;
        } else {
            smallerX = lineSegment.b.x;
            largerX = lineSegment.a.x;
            smallerY = lineSegment.b.y;
        }
        int overlaps = 0;

        for (int i = smallerX, j = 0; i <= largerX; i++, j++) {
            Coord coord = new Coord(i, smallerY+j);

            Integer curr = covers.get(coord);
            if (curr == null) curr = 0;
            else if (curr == 1)  overlaps++;

            covers.put(coord, curr+1);
        }

        return overlaps;
    }

    private static int drawDownRightDiagonal(LineSegment lineSegment, Map<Coord, Integer> covers) {
        int smallerX, largerX, smallerY;
        if (lineSegment.a.x < lineSegment.b.x) {
            smallerX = lineSegment.a.x;
            largerX = lineSegment.b.x;
            smallerY = lineSegment.a.y;
        } else {
            smallerX = lineSegment.b.x;
            largerX = lineSegment.a.x;
            smallerY = lineSegment.b.y;
        }
        int overlaps = 0;

        for (int i = smallerX, j = 0; i <= largerX; i++, j++) {
            Coord coord = new Coord(i, smallerY-j);

            Integer curr = covers.get(coord);
            if (curr == null) curr = 0;
            else if (curr == 1)  overlaps++;

            covers.put(coord, curr+1);
        }

        return overlaps;
    }

    static int part1() {
        Map<Coord, Integer> covers = new HashMap<>();
        int overlaps = 0;

        for (LineSegment lineSegment: lineSegments) {
            if (lineSegment.a.x == lineSegment.b.x) {
                overlaps += drawVertical(lineSegment, covers);
            } else if (lineSegment.a.y == lineSegment.b.y) {
                overlaps += drawHorizontal(lineSegment, covers);
            }
        }

        return overlaps;
    }

    static int part2() {
        Map<Coord, Integer> covers = new HashMap<>();
        int overlaps = 0;

        for (LineSegment lineSegment: lineSegments) {
            if (lineSegment.a.x == lineSegment.b.x) { // vertical
                overlaps += drawVertical(lineSegment, covers);
            } else if (lineSegment.a.y == lineSegment.b.y) { // horizontal
                overlaps += drawHorizontal(lineSegment, covers);
            } else { // check diagonal
                float slope = (float) (lineSegment.a.x - lineSegment.b.x) / (float) (lineSegment.a.y - lineSegment.b.y);
                if (slope == 1.0) { // up right
                    overlaps += drawUpRightDiagonal(lineSegment, covers);
                } else if (slope == -1.0) { // down right
                    overlaps += drawDownRightDiagonal(lineSegment, covers);
                }
            }
        }


        return overlaps;
    }
}

// 0,9 -> 5,9|8,0 -> 0,8|9,4 -> 3,4|2,2 -> 2,1|7,0 -> 7,4|6,4 -> 2,0|0,9 -> 2,9|3,4 -> 1,4|0,0 -> 8,8|5,5 -> 8,2
//
// 111111.... 111111.... 111111.... 111111.... 111111.... 111111.... 222111.... 222111.... 111111.... 111111....
// .......... 1......... 1......... 1......... 1......... 1......... 1......... 1......... 1.......1. 1.......1.
// .......... .1........ .1........ .1........ .1........ .1........ .1........ .1........ .1.....1.. .1.....1..
// ..........  ..1....... ..1....... ..1....... ..1....... ..1....... ..1...... ..1....... ..1...1... ..1...1...
// .......... ...1...... ...1...... ...1...... ...1...... ...1...... ...1...... ...1...... ...1.1.... ...1.2....
// .......... ....1..... ...1211111 ...1211111 ...1211211 ...1212211 ...1212211 .112211211 .112311211 .112312211
// .......... .....1.... .....1.... .....1.... .....1.1.. .....2.1.. .....2.1.. .....1.... ...1.1.... ...1.1.1..
// .......... ......1... ......1... ..1...1... ..1...11.. ..1.1.11.. ..1.1.11.. ..1...1... ..2...1... ..2...1.1.
// .......... .......1.. .......1.. ..1....1.. ..1....2.. ..11...2.. ..11...2.. ..1....1.. .11....1.. .11....1..
// .......... ........1. ........1. ........1. .......11. ..1....11. ..1....11. ........1. 1.......1. 1.......1.
//
// 0          0          1          1          3          5          8          9          10          12
// 0          0          1          1          3          5          8          9          10          12







