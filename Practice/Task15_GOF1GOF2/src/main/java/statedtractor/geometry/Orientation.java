package statedtractor.geometry;

public enum Orientation {
    NORTH {
        public Orientation turnClockwise() {
            return EAST;
        }

        public Orientation turnСounterclockwise() {
            return WEST;
        }

        public Position moveForwards(Position p) {
            return p.changeY(1);
        }

        public Position moveBackwards(Position p) {
            return p.changeY(-1);
        }
    },

    WEST {
        public Orientation turnClockwise() {
            return NORTH;
        }

        public Orientation turnСounterclockwise() {
            return SOUTH;
        }

        public Position moveForwards(Position p) {
            return p.changeX(-1);
        }

        public Position moveBackwards(Position p) {
            return p.changeX(1);
        }
    },

    SOUTH {
        public Orientation turnClockwise() {
            return WEST;
        }

        public Orientation turnСounterclockwise() {
            return EAST;
        }

        public Position moveForwards(Position p) {
            return p.changeY(-1);
        }

        public Position moveBackwards(Position p) {
            return p.changeY(1);
        }
    },

    EAST {
        public Orientation turnClockwise() {
            return SOUTH;
        }

        public Orientation turnСounterclockwise() {
            return NORTH;
        }

        public Position moveForwards(Position p) {
            return p.changeX(1);
        }

        public Position moveBackwards(Position p) {
            return p.changeX(-1);
        }
    };

    abstract public Orientation turnClockwise();

    abstract public Orientation turnСounterclockwise();

    abstract public Position moveForwards(Position p);

    abstract public Position moveBackwards(Position p);
}

