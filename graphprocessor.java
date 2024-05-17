import java.io.*;
import java.util.*;

public class graphprocessor {
    private Map<String, Map<String, Integer>> graph;
    private Set<String> nodes = new HashSet<>();

    public graphprocessor() {
        this.graph = new HashMap<>();
    }

    public static void main(String[] args) {
        graphprocessor processor = new graphprocessor();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入文本文件路径：");
        String filePath = scanner.nextLine();
        try {
            processor.generateGraphFromFile(filePath);
            System.out.println("有向图生成成功！");
            visualizeGraph(processor.generateGraphText());

            while (true) {
                System.out.println("\n请选择功能：");
                System.out.println("1. 展示有向图");
                System.out.println("2. 查询桥接词");
                System.out.println("3. 根据桥接词生成新文本");
                System.out.println("4. 计算两个单词之间的最短路径");
                System.out.println("5. 随机游走");
                System.out.println("0. 退出程序");
                System.out.print("请输入选项：");
                int option = scanner.nextInt();
                scanner.nextLine(); // 消费掉输入缓冲区中的换行符
                switch (option) {
                    case 1:
                        processor.showDirectedGraph();
                        break;
                    case 2:
                        System.out.print("请输入两个单词以查找桥接词（以空格分隔）：");
                        String[] words = scanner.nextLine().split("\\s+");
                        if (words.length == 2) {
                            processor.queryBridgeWords(words[0], words[1]);
                        } else {
                            System.out.println("输入错误，请输入两个单词！");
                        }
                        break;
                    case 3:
                        System.out.print("请输入一行新文本：");
                        String inputText = scanner.nextLine();
                        processor.generateNewText(inputText);
                        break;
                    case 4:
                        System.out.print("请输入一或两个单词以计算它们之间的最短路径（以空格分隔）：");
                        String[] shortestPathWords = scanner.nextLine().split("\\s+");
                        if (shortestPathWords.length == 2) {
                            processor.calcShortestPath(shortestPathWords[0], shortestPathWords[1]);
                        } else if (shortestPathWords.length == 1){
                            processor.calcShortestPath(shortestPathWords[0], "!");
                        } else {
                            System.out.println("输入错误，请输入一或两个单词！");
                        }
                        break;
                    case 5:
                        processor.randomWalk();
                        break;
                    case 0:
                        System.out.println("程序已退出。");
                        return;
                    default:
                        System.out.println("无效选项，请重新选择。");
                        break;
                }
            }
        }
           
         catch (IOException e) {
            System.out.println("文件读取失败：" + e.getMessage());
        }
    }

    public void generateGraphFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line).append(" "); // 将每行文本连接起来，并在行末添加一个空格
        }
        String[] word = content.toString().replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+"); // 使用空格分割单词
        for (int i = 0; i < word.length - 1; i++) {
            String word1 = word[i];
            String word2 = word[i + 1];
            if (!word1.isEmpty() && !word2.isEmpty()) {
                // 更新有向图中节点之间的边和权重
                addEdge(word1, word2);
            }
        }
        Collections.addAll(nodes, word);
        reader.close();
    }

    private void addEdge(String source, String destination) {
        // 更新有向图中节点之间的边和权重
        Map<String, Integer> neighbors = graph.getOrDefault(source, new HashMap<>());
        Integer temp = neighbors.get(destination);
        if (temp == null) {
            temp = 1;
        } else {
            temp++;
        }
        neighbors.put(destination, temp);
        graph.put(source, neighbors);
    }

    private String generateGraphText() {
        StringBuilder graphTextBuilder = new StringBuilder();
        graphTextBuilder.append("digraph G {\n");
        for (String node : graph.keySet()) {
            Map<String, Integer> neighbors = graph.get(node);
            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                graphTextBuilder.append("  ").append(node).append(" -> ").append(neighbor.getKey()).append(" [label=").append(neighbor.getValue()).append("];\n");
            }
        }
        graphTextBuilder.append("}\n");
        return graphTextBuilder.toString();
    }

    @SuppressWarnings("deprecation")
    private static void visualizeGraph(String graphText) {
        try {
            PrintWriter writer = new PrintWriter("graph.dot");
            writer.println(graphText);
            writer.close();
            Process process = Runtime.getRuntime().exec("dot -Tpng -o graph.png graph.dot");
            process.waitFor();
            System.out.println("图形化展示完成。请查看 graph.png 文件。");
        } catch (IOException | InterruptedException e) {
            System.out.println("无法展示图形：" + e.getMessage());
        }
    }

    public void showDirectedGraph() {
        System.out.println("有向图内容如下：");
        for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            String node = entry.getKey();
            Map<String, Integer> neighbors = entry.getValue();
            System.out.print(node + " -> ");
            for (Map.Entry<String, Integer> temp : neighbors.entrySet()) {
                String neighbor = temp.getKey();
                Integer weight = temp.getValue();
                System.out.print(neighbor + "[weight=" + weight + "] " );
            }
            System.out.println();
        }
    }
    
    private List<String> findBridgeWords(String word1, String word2) {
        List<String> bridgeWords = new ArrayList<>();
        for (String bridgeWord : graph.keySet()) {
            if (graph.get(word1).get(bridgeWord) != null && graph.get(bridgeWord).get(word2) != null) {
                bridgeWords.add(bridgeWord);
            }
        }
        return bridgeWords;
    }

    public void queryBridgeWords(String word1, String word2) {
        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            System.out.println("No " + word1 + " or " + word2 + " in the graph!");
            return;
        }

        List<String> bridgeWords = findBridgeWords(word1, word2);
        if (bridgeWords.isEmpty()) {
            System.out.println("No bridge words from " + word1 + " to " + word2 + "!");
        } else {
            System.out.print("The bridge words from " + word1 + " to " + word2 + " are: ");
            for (int i = 0; i < bridgeWords.size(); i++) {
                System.out.print(bridgeWords.get(i));
                if (i < bridgeWords.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(".");
        }
    }

    public void generateNewText(String inputText) {
        StringBuilder newTextBuilder = new StringBuilder();
        String[] words = inputText.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i + 1];
            newTextBuilder.append(currentWord).append(" ");
            if (graph.containsKey(currentWord) && graph.containsKey(nextWord)) {
                List<String> bridgeWords = findBridgeWords(currentWord, nextWord);
                if (!bridgeWords.isEmpty()) {
                    // 如果存在桥接词，则随机选择一个插入
                    Random random = new Random();
                    String selectedBridgeWord = bridgeWords.get(random.nextInt(bridgeWords.size()));
                    newTextBuilder.append(selectedBridgeWord).append(" ");
                }
            }
        }
        newTextBuilder.append(words[words.length - 1]); // 添加最后一个单词
        System.out.println("生成的新文本为：");
        System.out.println(newTextBuilder.toString());
    }

    public void calcShortestPath(String word1, String word2) {
        if (!graph.containsKey(word1)) {
            System.out.println("No " + word1 + " in the graph!");
            return;
        }else if (!graph.containsKey(word2) && !"!".equals(word2)) {
            System.out.println("No " + word2 + " in the graph!");
            return;
        }

        // Queue<String> queue = new LinkedList<>();
        Map<String, Integer> dis = new HashMap<>();
        Map<String, Integer> cho = new HashMap<>();
        Map<String, String> pre = new HashMap<>();
        for (String name : nodes) {
            Integer x = graph.get(word1).get(name);
            if (x == null) {
                x = Integer.MAX_VALUE;
            }
            cho.put(name, x);
            dis.put(name, x);
            pre.put(name, word1);
        }
        while(true){
            Integer min = Integer.MAX_VALUE;
            String minnode = new String();
            for (Map.Entry<String, Integer> temp : cho.entrySet()) {
                if (temp.getValue() < min) {
                    min = temp.getValue();
                    minnode = temp.getKey();
                }
            }
            if (min == Integer.MAX_VALUE) break;
            dis.put(minnode, min);
            cho.remove(minnode);
            for (Map.Entry<String, Integer> temp : cho.entrySet()){
                if (graph.get(minnode) == null) break;
                Integer x = graph.get(minnode).get(temp.getKey());
                if (x != null && temp.getValue() > min + x) {
                    cho.put(temp.getKey(), min + x);
                    pre.put(temp.getKey(), minnode);
                }
            }
        }

        if ("!".equals(word2)) {
            for (String word : nodes) {
                if (dis.get(word) == Integer.MAX_VALUE) {
                    System.out.println("No path found from " + word1 + " to " + word + "!");
                    continue;
                }
                System.out.print("The shortest path from " + word1 + " to " + word + " : length " + dis.get(word) + " " + word);
                String currentWord = word;
                do {
                    currentWord = pre.get(currentWord);
                    System.out.print(" <- " + currentWord);
                }while(!word1.equals(currentWord));
                System.out.println();
            }
        } else {
            if (dis.get(word2) == Integer.MAX_VALUE) {
                System.out.println("No path found from " + word1 + " to " + word2 + "!");
                return ;
            }
            System.out.println("The length of the shortest path is: " + dis.get(word2));
            System.out.print("The shortest path from " + word1 + " to " + word2 + " is: " + word2);
            String currentWord = word2;
            do {
                currentWord = pre.get(currentWord);
                System.out.print(" <- " + currentWord);
            }while(!word1.equals(currentWord));
            System.out.println();
        }
    }

    public void randomWalk() {
        Random random = new Random();
        List<String> nodesVisited = new ArrayList<>();
        List<String> edgesVisited = new ArrayList<>();

        List<String> rnodes = new ArrayList<>(graph.keySet());
        String currentNode = rnodes.get(random.nextInt(rnodes.size()));
        nodesVisited.add(currentNode);
        edgesVisited.add(currentNode);

        while (true) {
            List<String> neighbors = new ArrayList<>(graph.get(currentNode).keySet());
            if (neighbors == null || neighbors.isEmpty()) {
                break;
            }
            String nextNode = neighbors.get(random.nextInt(neighbors.size()));
            edgesVisited.add(" -> " + nextNode);
            if (nodesVisited.contains(nextNode)) {
                nodesVisited.add(nextNode);
                break; // 出现重复节点，结束遍历
            }
            nodesVisited.add(nextNode);
            currentNode = nextNode;
        }

        // 输出遍历的节点和边到文件
        writeRandomWalkToFile(nodesVisited, edgesVisited);
    }  

    private void writeRandomWalkToFile(List<String> nodesVisited, List<String> edgesVisited) {
        try (PrintWriter writer = new PrintWriter("random_walk.txt")) {
            writer.println("Edges visited:");
            for (String edge : edgesVisited) {
                writer.print(edge);
            }
        } catch (IOException e) {
            System.out.println("Failed to write random walk to file: " + e.getMessage());
        }
    }
}