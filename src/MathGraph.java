import java.io.*;
import java.util.*;

public class MathGraph {
    private final static String folder = "./src/Data/";
    private int vertexes = 0;
    private int ribs = 0;
    private float[][] matrix;
    private float[][] ribsTable;

    public MathGraph() {
    }

    public int getVertexes() {
        return vertexes;
    }

    public int getRibs() {
        return ribs;
    }

    public void load(int typeFile, String filename) {
        File loadFile = new File(folder + filename + ".txt");
        switch (typeFile) {
            case 1:
                parseMatrix(loadFile);
                if ((matrix != null) && vertexes >= 0) setRibsTable(matrix);
                break;
            case 2:
                parseRibsTable(loadFile);
                if ((ribsTable != null) && ribs > 0) setMatrix(ribsTable);
                break;
        }
    }

    private void parseMatrix(File file) {
        try {
            Scanner scanner = new Scanner(file);
            vertexes = Integer.parseInt(scanner.nextLine());
            matrix = new float[vertexes][vertexes];
            int i = 0;
            while (scanner.hasNext()) {
                String[] tmpVal = scanner.nextLine().split("\\s+");
                if (tmpVal.length != 1 && tmpVal[0] != " ") {
                    for (int j = 0; j < tmpVal.length; j++) {
                        try {
                            matrix[i][j] = Float.parseFloat(tmpVal[j]);
                        } catch (NumberFormatException e) {
                            matrix[i][j] = Float.parseFloat(tmpVal[j].replace(',', '.'));
                        }
                    }
                    i++;
                }
            }
            System.out.println("Матрица графа успешно загружена.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        }
    }

    private void parseRibsTable(File file) {
        try {
            Scanner scanner = new Scanner(file);
            ribs = Integer.parseInt(scanner.nextLine());
            ribsTable = new float[ribs][3];
            int i = 0;
            while (scanner.hasNext()) {
                String[] tmpVal = scanner.nextLine().split("\\s+");
                if (tmpVal.length != 1 && tmpVal[0] != " ") {
                    for (int j = 0; j < 3; j++) {
                        try {
                            ribsTable[i][j] = Float.parseFloat(tmpVal[j]);
                        } catch (NumberFormatException e) {
                            ribsTable[i][j] = Float.parseFloat(tmpVal[j].replace(',', '.'));
                        }
                    }
                    i++;
                }
            }
            System.out.println("Список смежности ребер успешно загружен.");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        }
    }

    private void setMatrix(float[][] ribsTable) {
        for (float[] floats : ribsTable) {
            if (floats[0] > vertexes) vertexes = (int) floats[0];
            if (floats[1] > vertexes) vertexes = (int) floats[1];
        }
        matrix = new float[vertexes][vertexes];
        for (int i = 0; i < ribs; i++) {
            int x = (int) ribsTable[i][0] - 1;
            int y = (int) ribsTable[i][1] - 1;
            float way = ribsTable[i][2];
            matrix[x][y] = matrix[y][x] = way;
        }
    }

    private void setRibsTable(float[][] matrix) {
        List<float[]> listRibs = new ArrayList<>();
        for (int i = 0; i < vertexes - 1; i++) {
            for (int k = i + 1; k < vertexes; k++) {
                if (matrix[i][k] > 0) {
                    float[] rib = new float[]{i + 1, k + 1, matrix[i][k]};
                    listRibs.add(rib);
                }
            }
        }
        ribsTable = new float[listRibs.size()][3];
        for (int i = 0; i < listRibs.size(); i++) {
            ribsTable[i] = listRibs.get(i);
        }
        ribs = ribsTable.length;
    }

    public void addVertex() {
        vertexes += 1;
        float[][] newMatrix = new float[vertexes][vertexes];
        for (int i = 0; i < vertexes - 1; i++) {
            for (int j = 0; j < vertexes - 1; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
            newMatrix[i][vertexes - 1] = 0;
        }
        matrix = newMatrix;
        System.out.println("Вершина " + vertexes + " добавлена");
    }

    //вес ребра
    public float weightRib(int i, int j) {
        return matrix[i - 1][j - 1];
    }

    //добавить ребро
    public void addRib(int i, int j, float way) {
        if (way <= 0 || i == j || i > vertexes || j > vertexes) {
            System.out.println("Некорректные значения ребер или веса.");
            return;
        }
        matrix[i - 1][j - 1] = matrix[j - 1][i - 1] = way;
        setRibsTable(matrix);
        System.out.println("Ребро " + i + "-" + j + " с весом " + way + " добавлено.");
    }

    //удаление ребер
    public void deleteRib(int i, int j) {
        matrix[i - 1][j - 1] = matrix[j - 1][i - 1] = 0;
        setRibsTable(matrix);
        System.out.println("Ребро " + i + "-" + j + " удалено.");
    }

    //смежность ребер
    public boolean isCommunicate(int i, int j) {
        if (matrix[i - 1][j - 1] != 0) return true;
        else return false;
    }

    public void save(int typefile, String filename) {
        try {
            File saveFile = new File(folder + filename + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            switch (typefile) {
                case 1:
                    writer.flush();
                    writer.write(String.valueOf(vertexes));
                    for (int i = 0; i < vertexes; i++) {
                        writer.write("\n");
                        for (int j = 0; j < vertexes; j++) {
                            writer.write(matrix[i][j] + " ");
                        }
                    }
                    writer.close();
                    break;
                case 2:
                    writer.flush();
                    writer.write(String.valueOf(ribs));
                    for (int i = 0; i < ribs; i++) {
                        writer.write("\n");
                        for (int j = 0; j < 3; j++) {
                            writer.write(ribsTable[i][j] + " ");
                        }
                    }
                    writer.close();
                    break;
            }
            System.out.println("Граф записан.");
        } catch (IOException e) {
            System.out.println("Ошибка записи.");
        }
    }

    //обход в ширину
    public void goround(int startVer) {
        ArrayList<Integer> beginList = new ArrayList<>();
        ArrayList<Integer> roundList = new ArrayList<>();
        ArrayList<Integer> singleList = new ArrayList<>();
        //pull of unstepping vertexes and single vertexes
        for (int i = 1; i <= vertexes; i++) {
            boolean isconn = false;
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i - 1][j] != 0) {
                    isconn = true;
                    break;
                }
            }
            if (isconn) beginList.add(i);
            else singleList.add(i);

        }
        //print single vertexes
        if (singleList.size() != 0) {
            System.out.println("----------");
            System.out.println("Не связные вершины: ");
            for (int i : singleList) System.out.println(i);
        }
        //if start vertex is single vertex
        if (singleList.contains(startVer)) {
            System.out.println("-----------");
            System.out.println("Вершина " + startVer + " является отдельной не связной вершиной.");
            System.out.println("Обход возможен среди вершин: ");
            for (int i : beginList) System.out.println(i);
            return;
        }
        //first component
        roundList.add(startVer);
        beginList.remove((Integer) startVer);
        for (int i = 0; i < roundList.size(); i++) {
            ArrayList<Integer> forDelete = new ArrayList<>();
            int strtvr = roundList.get(i);
            for (int j : beginList) {
                if(isCommunicate(strtvr,j)){
                    roundList.add(j);
                    forDelete.add(j);
                }
            }
            for(Integer del:forDelete){
                if(beginList.contains(del)) beginList.remove(del);
            }
        }
        //print component
        System.out.println("-----------");
        System.out.println("Обход компоненты связности: ");
        for (int i : roundList) System.out.println(i);
        //last vertexes
        if(!beginList.isEmpty()){
            System.out.println("-----------");
            System.out.println("Так же обход возможен среди вершин: ");
            for (int i : beginList) System.out.println(i);
        }
    }
}
