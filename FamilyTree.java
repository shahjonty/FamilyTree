import java.io.*;
import java.util.*;

public class FamilyTree {

    /**
     * Declare necessary variables to describe your Tree
     * Each Node in the Tree represents a person
     * You can declare other classes if necessary
     */

    private class Node {
        private String name;
        private String father;
        private String mother;

        private Node(String name, String father, String mother) {
            this.name = name;
            this.mother = mother;
            this.father = father;
        }


    }

    private ArrayList<Node> Gene;
    private ArrayList<String> person;

    public FamilyTree() {
        Gene = new ArrayList<>();
        person = new ArrayList<>();
    }

    private void addNode(String name, String mother, String father) {
        Gene.add(new Node(name, mother, father));
    }

    private ArrayList<String> result;
    private boolean check = false;
    private boolean checkValue = false;

    private boolean bfs(String name, String name2) {
        Set<String> list1 = new HashSet<>();
        Set<String> list2 = new HashSet<>();
        Queue<String> explore1 = new LinkedList<>();
        Queue<String> explore2 = new LinkedList<>();
        explore1.add(name);
        explore2.add(name2);
        list1.add(name);
        list2.add(name2);

        while (!explore1.isEmpty() || !explore2.isEmpty()) {

            if (!explore1.isEmpty()) {
                String next = explore1.remove();
                if (next != null) {
                    if (get(next) != null && get(next).father != null) {
                        String x = get(next).father;
                        if (x.equals(name2)) {
                            checkValue = true;
                            return true;
                        }
                        if (!list1.contains(x)) {
                            explore1.add(x);
                            list1.add(x);
                        }
                    }

                    if (get(next) != null && get(next).mother != null) {
                        String x = get(next).mother;
                        if (x.equals(name2)) {
                            checkValue = true;
                            return true;
                        }
                        if (!list1.contains(x)) {
                            explore1.add(x);
                            list1.add(x);
                        }
                    }
                }
            }

            if (!explore2.isEmpty()) {
                String next2 = explore2.remove();
                if (next2 != null) {
                    if (get(next2).father != null) {
                        String x = get(next2).father;
                        if (x.equals(name)) {
                            checkValue = false;
                            return true;
                        }
                        if (!list2.contains(x)) {
                            explore2.add(x);
                            list2.add(x);
                        }
                    }

                    if (get(next2).mother != null) {
                        String x = get(next2).mother;
                        if (x.equals(name)) {
                            checkValue = false;
                            return true;
                        }
                        if (!list2.contains(x)) {
                            explore2.add(x);
                            list2.add(x);
                        }
                    }
                }
            }
            Set<String> intersection = new HashSet<>(list1);
            intersection.retainAll(list2);
            result = new ArrayList<>();
            if (!intersection.isEmpty()) {
                result.addAll(intersection);
                Collections.sort(result);
                check = true;
                return false;
            }
        }
        return false;
    }


    private Node get(String name) {
        for (Node temp : Gene) {
            if (temp.name.equals(name)) {
                return temp;
            }
        }
        return null;
    }


    /**
     * @input directory or filename of input file. This file contains the information necessary to build the child
     * parent relation. Throws exception if file is not found
     * @param familyFile
     * @throws Exception
     */
    private BufferedReader br;
    private BufferedWriter wr;

    public void buildFamilyTree(String familyFile) throws Exception {
        String line;
        try {
            br = new BufferedReader(new FileReader(familyFile));
            while ((line = br.readLine()) != null) {
                line = line.trim().replaceAll(" +", " ");
                String[] names = line.split(" ");

                for (int i = 0; i < names.length; i++) {
                    if (!person.contains(names[i])) {
                        this.addNode(names[i], null, null);
                        this.person.add(names[i]);
                    }
                }


                for (int i = 2; i < names.length; i++) {
                    for (Node temp : Gene) {
                        if (temp.name.equals(names[i])) {
                            temp.father = names[0];
                            temp.mother = names[1];
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * @param queryFile
     * @param outputFile
     * @throws Exception
     * @input directory or filename of Query and Output.
     * queryFile contains the queries about the tree.
     * The output of this query should be written in file outputfile.
     */

    public void evaluate(String queryFile, String outputFile) throws Exception {
        /*
         * Traverse the tree to answer the queries
         * For information on queries take a look at the handout
         */
        String line;
        try {
            br = new BufferedReader(new FileReader(queryFile));
            wr = new BufferedWriter(new FileWriter(outputFile));
            while ((line = br.readLine()) != null) {
                line = line.trim().replaceAll(" +", " ");
                String[] names = line.split(" ");

                boolean y = bfs(names[0], names[1]);
                ArrayList<String> result2 = result;
                boolean temp2 = check;
                boolean c = checkValue;

                if (y) {
                    if (c) {
                        wr.write(names[0] + " is a descendant of " + names[1] + "\n");
                    } else {
                        wr.write(names[1] + " is a descendant of " + names[0] + "\n");
                    }
                } else if (temp2 && result2.size() > 0) {
                    for (String x : result2) {
                        wr.write(x + " ");
                    }
                    wr.write("\n");
                } else {
                    wr.write("unrelated\n");
                }

            }
            wr.close();
            br.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        FamilyTree tree = new FamilyTree();
        String filename = "input1.txt";
        String query = "query1.txt";
        String out = "output1.txt";
        tree.buildFamilyTree(filename);
        tree.evaluate(query, out);
    }
}

