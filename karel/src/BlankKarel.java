/*
 * File: BlankKarel.java
 * ---------------------
 * This class is a blank one that you can change at will.
 */
import stanford.karel.SuperKarel;


public class BlankKarel extends SuperKarel {

    int karelSteps = 0;
    int beepersUsed = 0;
    boolean cleanTheMap = false; // this field decides to clean the map before start dividing or not;

    //This method to collect beepers from the ground and subtract it from the total used beepers;
    public void collectBeeper() {
        if (beepersPresent()) {
            pickBeeper();
            beepersUsed--;
        }
    }

    // method allows karel to move until he hits a wall while using the oneStep method;
    public void moveUntilHit(boolean putBeepers, boolean collectBeepers) {
        while (frontIsClear()) {
            moveOneStep(collectBeepers, putBeepers);
        }
    }

    // oneStep method let karel move one step, and check if there is a beeper on the ground and if he wants to collect it or keep it;
    public void moveOneStep(boolean collectBeepers, boolean putBeepers) {
        move();
        karelSteps++;
        if (beepersPresent() && collectBeepers && putBeepers) {
            return;
        }
        if (collectBeepers) {
            collectBeeper();
        }
        if (noBeepersPresent() && putBeepers) {
            putBeeper();
        }
    }

    // This method lets karel to move a specific number of moves, so I can stop karel anyWhere not only when he hits a wall;
    public void moveSpecific(int numberOfMoves, boolean collectBeepers, boolean putBeepers) {
        for (int i = 0; i < numberOfMoves; i++) {
            moveOneStep(collectBeepers, putBeepers);
        }
    }

    // This method allows karel to move diagonally soo I can divide oddxodd maps later on;
    public void diagonalMove() {
        while (frontIsClear()) {
            moveOneStep(false, false);
            turnLeft();
            moveOneStep(false, false);
            turnRight();
        }
    }

    // this method was made to clean a (oddXodd) OR (evenXeven) Maps;
    public void cleanMaps(int sideDimension) {
        for (int i = 0; i < sideDimension / 2; i++) {
            turnLeft();
            moveOneStep(true, false);
            turnLeft();
            moveUntilHit(false, true);
            turnRight();
            moveOneStep(true, false);
            turnRight();
            moveUntilHit(false, true);
        }
    }

    // this method was made to clean a oddXeven Maps;
    public void cleanMapsDiff(int firstSide, int secondSide) {
        for (int i = 0; i < secondSide / 2; i++) {
            moveUntilHit(false, true);
            turnLeft();
            moveOneStep(true, false);
            turnLeft();
            moveSpecific(firstSide - 1, true, false);
            turnRight();
            moveOneStep(true, false);
            turnRight();
        }
    }

    // This method to divide Even X Even maps;
    public void divideEvenMap(int n) {
        moveSpecific(n - 1, true, true);
        turnRight();
        moveUntilHit(true, false);
        turnLeft();
        moveOneStep(false, true);
        turnLeft();
    }

    // This method job is to return karel to the start point after calculating the dimensions of the map
    // soo I can start dividing all maps from the same point;
    public void returnToStartPoint() {
        moveUntilHit(false, true);
        turnLeft();
        moveUntilHit(false, true);
        turnLeft();
    }

    // And this method is responsible for maps that have one side like 1x8 / 1x5 / 1x6;
    public void oneLineMaps(int side) {
        if (side % 2 == 0) {
            int s = side / 2;
            turnLeft();
            moveSpecific(s, false, false);
            putBeeper();
            moveUntilHit(false, true);
        } else {
            int s = side / 2;
            turnLeft();
            moveSpecific(s, false, false);
            putBeeper();
            moveOneStep(true, true);
            moveUntilHit(false, true);
        }
    }

    public void putBeeper() {
        if (noBeepersPresent()) {
            super.putBeeper();
            beepersUsed++;
        }
    }

    public void run() {
        super.run();

        // these two integers refer to the X and Y axes (the dimensions of the map);
        int sideOne = 0;
        int sideTwo = 0;
        //those two while loops to check the dimensions of the map;
        while (frontIsClear()) {
            moveOneStep(true, false);
            sideOne++;
        }
        turnLeft();
        while (frontIsClear()) {
            moveOneStep(true, false);
            sideTwo++;
        }
        //---------------------------------------

        if (sideOne <= 3 && sideTwo <= 3) {
            return;
            //  We can't divide this type of maps, it will be full of beepers;

        } else if (sideOne == 0) {
            turnLeft();
            oneLineMaps(sideTwo);
        } else if (sideTwo == 0) {
            oneLineMaps(sideOne);
        } else if (sideOne % 2 == sideTwo % 2) {
            // this for oddXodd Or evenXeven;
            int n = 0;
            if (sideOne % 2 == 0) {
                // When the map is odd on both sides;
                turnLeft();
                if (cleanTheMap) { // this cleans the map from beepers if cleanTheMap set to be true;
                    moveUntilHit(false, true);
                    cleanMaps(sideOne);
                    turnRight();
                    turnRight();
                } else returnToStartPoint();
                n = sideOne / 2;
                moveSpecific(n, true, false);
                turnLeft();
                putBeeper();
                moveUntilHit(true, true);
                turnLeft();
                moveUntilHit(false, true);

                turnLeft();
                diagonalMove();
                turnRight();
                moveUntilHit(false, false);
                turnRight();
                moveSpecific(n, true, false);
                turnRight();
                putBeeper();
                moveUntilHit(true, false);
                turnLeft();
                moveUntilHit(false, false);

                turnLeft();
                diagonalMove();
            }
            // when map is even on both sides;
            else {
                if (cleanTheMap) {
                    cleanMaps(sideOne);
                    turnLeft();
                    moveOneStep(true, false);
                    turnLeft();
                    moveUntilHit(false, true);
                    turnLeft();
                } else {
                    turnLeft();
                    returnToStartPoint();
                }
                n = sideOne / 2 + 1;
                moveSpecific(n, true, false);
                putBeeper();
                turnLeft();
                for (int i = 0; i < 3; i++) {
                    divideEvenMap(n);
                }
                moveSpecific(n - 1, false, true);
                turnRight();
                moveUntilHit(true, false);
                turnRight();
                moveUntilHit(false, false);
            }
        } else {
            // When the map has a different dimensions;
            int n = 0;
            if (sideOne % 2 == 0) {
                turnLeft();
                if (cleanTheMap) {
                    cleanMapsDiff(sideOne, sideTwo);
                    moveUntilHit(false, true);
                    turnLeft();
                    turnLeft();
                    moveSpecific(sideOne - 1, true, false);
                    turnRight();
                    moveOneStep(true, false);
                    turnRight();
                    moveUntilHit(false, true);
                    turnRight();
                    turnRight();
                } else {
                    returnToStartPoint();
                }
                n = sideOne / 2;
                moveSpecific(n, true, false);
                putBeeper();
                turnLeft();
                moveUntilHit(true, true);
                turnLeft();
                moveUntilHit(false, true);
                turnLeft();
                n = sideTwo / 2;
                moveSpecific(n, true, false);
                putBeeper();
                turnLeft();
                moveUntilHit(true, true);
                turnRight();
                moveOneStep(false, true);
                turnRight();
                moveUntilHit(true, true);
                turnLeft();
                moveUntilHit(false, true);
            }
        }
        System.out.println("Karel Steps: " + karelSteps);
        System.out.println("Number of Used Beepers: " + beepersUsed);
    }
}

