import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {
    private static MathGraph graph = new MathGraph();
    private final static int[] actions = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final static int[] typeFiles = new int[]{1, 2};
    private final static Scanner scanner = new Scanner(System.in);

    private Console() {
    }

    public static void start() {
        System.out.println("--------------------\nВыберите действие: ");
        System.out.println("1 - загрузить граф;");
        System.out.println("2 - сохранить граф;");
        System.out.println("3 - добавить вершину;");
        System.out.println("4 - узнать вес ребра;");
        System.out.println("5 - добавить ребро;");
        System.out.println("6 - удалить ребро;");
        System.out.println("7 - проверка смежности вершин;");
        System.out.println("8 - количество вершин;");
        System.out.println("9 - количество ребер;");
        System.out.println("10 - обход в ширину (индивидуальное задание);");
        System.out.println("11 - Очистить граф;");
        setAction(tryScan(scanner, actions));
    }

    private static int tryScan(Scanner scanner, int[] actions) {
        try {
            int action = scanner.nextInt();
            if (!Arrays.stream(actions)
                    .filter(x -> x == action)
                    .findFirst()
                    .isPresent()) {
                throw new InputMismatchException();
            }
            scanner.nextLine();
            return action;
        } catch (InputMismatchException e) {
            System.out.println("Некорректный выбор. Выберите действие:");
            scanner.nextLine();
            return tryScan(scanner, actions);
        }
    }

    private static void setAction(int action) {
        switch (action) {
            case (1):
                System.out.println("Выберите тип файла:\n1 - матрица смежности;\n2 - списки ребер;");
                int numOfAction = tryScan(scanner, typeFiles);
                System.out.println("Введите имя файла: ");
                String filename = scanner.nextLine();
                graph.load(numOfAction, filename);
                start();
            case (2):
                System.out.println("Выберите тип файла:\n1 - матрица смежности;\n2 - списки ребер;");
                numOfAction = tryScan(scanner, typeFiles);
                System.out.println("Введите имя файла: ");
                filename = scanner.nextLine();
                graph.save(numOfAction, filename);
                start();
            case (3):
                graph.addVertex();
                start();
            case (4):
                try {
                    System.out.println("Введите номер первой вершины: ");
                    int i = scanner.nextInt();
                    System.out.println("Введите номер второй вершины: ");
                    int j = scanner.nextInt();
                    System.out.println("Ребро " + i + "-" + j + " равно " + graph.weightRib(i, j));
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    System.out.println("Некорректные значения вершин.");
                }
                start();
            case (5):
                try {
                    System.out.println("Введите номер первой вершины: ");
                    int i = scanner.nextInt();
                    System.out.println("Введите номер второй вершины: ");
                    int j = scanner.nextInt();
                    System.out.println("Введите вес ребра: ");
                    String way = scanner.next();
                    System.out.println(way);
                    graph.addRib(i, j, Float.parseFloat(way));
                } catch (NullPointerException e) {
                    System.out.println("Некорректные значения.");
                }
                start();
            case (6):
                try {
                    System.out.println("Введите номер первой вершины: ");
                    int i = scanner.nextInt();
                    System.out.println("Введите номер второй вершины: ");
                    int j = scanner.nextInt();
                    graph.deleteRib(i, j);
                } catch (NullPointerException e) {
                    System.out.println("Некорректные значения вершин.");
                }
                start();
            case (7):
                try {
                    System.out.println("Введите номер первой вершины: ");
                    int i = scanner.nextInt();
                    System.out.println("Введите номер второй вершины: ");
                    int j = scanner.nextInt();
                    if (graph.isCommunicate(i, j)) System.out.println("Вершины смежны.");
                    else System.out.println("Вершины не смежны.");
                } catch (NullPointerException e) {
                    System.out.println("Некорректные значения вершин.");
                }
                start();
            case (8):
                System.out.println("Количество вершин: " + graph.getVertexes());
                start();
            case (9):
                System.out.println("Количество ребер: " + graph.getRibs());
                start();
            case (10):
                try {
                    System.out.println("Начальная вершина обхода: ");
                    int i = scanner.nextInt();
                    graph.goround(i);
                }catch (NullPointerException e){
                    System.out.println("Некорректное значение вершины.");
                }
                start();
            case (11):
                graph=new MathGraph();
                System.out.println("Граф очищен.");
                start();
        }
    }
}
