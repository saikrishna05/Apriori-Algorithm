import java.io.*;
import java.util.*;
public class Apriori extends Observable {
    public static void main(String[] args) throws Exception {
        Apriori apriori = new Apriori();
        apriori.loadData();
        apriori.runAlgorithm();
    }

    List<int[]> itemsets ;
    String filename;
    int numberOfItems;
    int numberOfTransactions;
    double minimumSupport;


    private void runAlgorithm() throws Exception {

        itemsets = new ArrayList<int[]>();
        for(int i = 0; i< numberOfItems; i++)
        {
            int[] item = {i};
            itemsets.add(item);
        }

        int currentItemSet=1;

        while (itemsets.size()>0)
        {
            List<int[]> frequentItems = new ArrayList<int[]>();

            boolean flag;
            int count[] = new int[itemsets.size()];



            BufferedReader read_data = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            boolean[] transactions = new boolean[numberOfItems];

            for (int i = 0; i < numberOfTransactions; i++) {

                Arrays.fill(transactions, false);
                StringTokenizer stFile = new StringTokenizer(read_data.readLine(), " ");

                while (stFile.hasMoreTokens())
                {
                    int parsedVal = Integer.parseInt(stFile.nextToken());
                    transactions[parsedVal]=true;
                }

                for (int c = 0; c < itemsets.size(); c++) {
                    flag = true;
                    for (int x : itemsets.get(c)) {
                        if (transactions[x] == false) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        count[c]++;
                    }
                }

            }

            read_data.close();

            for (int i = 0; i < itemsets.size(); i++) {
                if ((count[i] / (double) (numberOfTransactions)) >= minimumSupport) {
                    System.out.println(Arrays.toString(itemsets.get(i)));
                    frequentItems.add(itemsets.get(i));
                }
            }
            itemsets = frequentItems;

            if(itemsets.size()!=0)
            {
               System.out.println("Number of Items :"+itemsets.size()+" frequent item-sets of size "
                       + currentItemSet
                       + "  with support "+minimumSupport);


                int currentSizeOfItemsets = itemsets.get(0).length;
                System.out.println("Creating item-sets of size "+(currentSizeOfItemsets+1));

                HashMap<String, int[]> tempItems = new HashMap<String, int[]>();


                for(int i=0; i<itemsets.size(); i++)
                {
                    for(int j=i+1; j<itemsets.size(); j++)
                    {

                        assert (itemsets.get(i).length==itemsets.get(j).length);

                        int [] newItem = new int[currentSizeOfItemsets+1];

                        for(int s=0; s<newItem.length-1; s++) {
                            newItem[s] = itemsets.get(i)[s];
                        }

                        int ndifferent = 0;
                        for(int s1=0; s1<itemsets.get(j).length; s1++)
                        {
                            boolean found = false;
                            for(int s2=0; s2<itemsets.get(i).length; s2++) {
                                if (itemsets.get(i)[s2]==itemsets.get(j)[s1]) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found){
                                ndifferent++;
                                newItem[newItem.length -1] = itemsets.get(j)[s1];
                            }

                        }
                        if (ndifferent==1) {

                            Arrays.sort(newItem);
                            tempItems.put(Arrays.toString(newItem),newItem);
                        }
                    }
                }
                itemsets = new ArrayList<int[]>(tempItems.values());
                System.out.println("Created "+itemsets.size()+" unique itemsets of size "+(currentSizeOfItemsets+1));

            }

            currentItemSet++;
        }
        System.out.println("maximum limit for items reached");
    }


    // reads .dat file
    private void loadData() throws Exception
    {
        filename = "dataset1.dat";

       System.out.println("enter min support (0-1) : ");
       Scanner in = new Scanner(System.in);
       minimumSupport = in.nextDouble();

        numberOfItems = 0;
        numberOfTransactions =0;
        BufferedReader read_data = new BufferedReader(new FileReader(filename));
        while (read_data.ready()) {
            numberOfTransactions++;
            StringTokenizer t = new StringTokenizer(read_data.readLine()," ");
            while (t.hasMoreTokens()) {
                if ( (Integer.parseInt(t.nextToken() + 1))  > numberOfItems) {
                    numberOfItems =Integer.parseInt(t.nextToken() + 1);
                }
            }
        }
        System.out.println(numberOfItems +" items, "+ numberOfTransactions +" transactions, minsup = " +
                minimumSupport  );
    }



}
