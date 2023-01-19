package robots;

import java.util.*;

public class RobotMap {

    public final int n;
    public final int m;
    private final int maxRobotCount;
    private final List<Robot> robots;


    public RobotMap(int n, int m, int maxRobotCount) {
        this.n = n;
        this.m = m;
        this.maxRobotCount = maxRobotCount;
        this.robots = new ArrayList<>();
    }

    public RobotMap(int n, int m){
        this(n, m, 5);
    }

    public Robot createRobot(Point point) throws RobotCreationException {
        if(robots.size() == maxRobotCount){
            throw new RobotCreationException("Превышено колличество роботов");
        }

        final MapPoint robotPosition;
        try {
            validatePoint(point);
            robotPosition = new MapPoint(point.getX(), point.getY());
        } catch (PointValidationException e) {
            throw new RobotCreationException(e.getMessage());
        }

        Robot robot = new Robot(robotPosition);
        robots.add(robot);
        return robot;
    }

    private void validatePoint(Point point) throws PointValidationException {
        validatePointIsFree(point);
    }

    private void validatePointIsFree(Point point) throws PointValidationException {
        for (Robot robot : robots) {
            if (point.equals(robot.getPoint())) {
                throw new PointValidationException("Позиция " + point + " занята другим роботом: " + robot);
            }
        }
    }

    public class Robot {

        public static final Direction DEFAULT_DIRECTION = Direction.TOP;

        private final UUID id;
        private MapPoint point;
        private Direction direction;
        private int robotCount;

        public Robot(MapPoint point) {
            this.id = UUID.randomUUID();
            this.point = point;
            this.direction = DEFAULT_DIRECTION;
            this.robotCount = robots.size() + 1;
        }

        public void move(int steps) throws RobotMoveException {
            final MapPoint newPoint;
            try {
                newPoint = switch (direction) {
                    case TOP -> new MapPoint(point.getX() - steps, point.getY());
                    case RIGHT -> new MapPoint(point.getX(), point.getY() + steps);
                    case BOTTOM -> new MapPoint(point.getX() + steps, point.getY());
                    case LEFT -> new MapPoint(point.getX(), point.getY() - steps);
                };

                validatePoint(newPoint);
            } catch (PointValidationException e) {
                throw new RobotMoveException(e.getMessage(), this);
            }

            this.point = newPoint;
            System.out.println("Robot-" + robotCount + " move " + steps + " steps");
        }

        public void move() throws RobotMoveException{
            move(1);
        }

        public void changeDirection(Direction direction) {
            this.direction = direction;
        }

        public void printMap(){
            for (int i = 0; i < map.n ; i++) {
                System.out.println();
                for (int j = 0; j < map.m; j++) {
                    System.out.print("-");
                }
                
            }
        }

        public MapPoint getPoint() {
            return point;
        }

        @Override
        public String toString() {
            return "Robot-" + robotCount + " location: " + point;
        }
    }

    public class MapPoint extends Point {

        public MapPoint(int x, int y) throws PointValidationException {
            super(x, y);

            if (x < 0 || x > n || y < 0 || y > m) {
                throw new PointValidationException("Недопустимое значение Point: " + this);
            }
        }
    }

}
