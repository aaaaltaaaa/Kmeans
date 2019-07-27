import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Kmeans {
    int n_clusters;
    double  threshold;
    double means[][];
    Integer label[];
    double data[][];
    int h;
    int w;

    public Kmeans(int n_clusters, double threshold) {
        this.n_clusters = n_clusters;
        this.threshold = threshold;
    }

    public void load(String dataFile)
    {
        ArrayList<String> temp = new ArrayList<>();
        String T;
        try {
            BufferedReader file=new BufferedReader(new FileReader(dataFile));
            while (true){
                T=file.readLine();
                if (T==null) break;
                temp.add(T);
            }

            h=temp.size();
            w=temp.get(0).split(" ").length;

            double data[][]=new double[h][w];
            for (int i = 0; i < h; i++) {
                int j = 0;
                for (String s : temp.get(i).split(" ")) {
                    data[i][j]=Double.valueOf(s);
                    j++;
                }
            }
            this.data=data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.data = data;
    }

    public void store(String dataFile)
    {
        ArrayList<String> temp = new ArrayList<>();
        String T;
        try {
            BufferedWriter file=new BufferedWriter(new FileWriter(dataFile));
            for (int i = 0; i < h ; i++) {
                file.write(label[i].toString());
                if (i!=h-1)
                    file.write("\n");
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.data = data;
    }

    void init(){
        means=new double[n_clusters][w];
        for (int i = 0; i < n_clusters; i++) {
                means[i]=data[(int)(Math.random()*(h-1))];
        }
        label =new Integer[h];
        for (int i = 0; i < h; i++) {
            label[i]=0;
        }
    }

    double EuclideanDistance(double A[],double B[]){
        double distance=0;
        double sum=0;
        for (int i = 0; i < w; i++) {
            sum+=Math.pow((A[i]-B[i]),2);
        }
        distance= Math.pow(sum,0.5);
        return distance;
    }

    double ManhattanDistance(double A[],double B[]){
        double distance=0;
        for (int i = 0; i < w; i++) {
            distance+=Math.abs(A[i]-B[i]);
        }
        return distance;
    }

    int updateLabel(String method){
        int changeNum=0;
        int lab=0;
        double min=0;
        double distance=0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < n_clusters ; j++) {
                switch (method){
                    case "Manhattan":distance=ManhattanDistance(data[i],means[j]); break;
                    case "Euclidean":distance=EuclideanDistance(data[i],means[j]); break;
                }

                 if (j==0||distance<min){
                     min=distance;
                     lab=j;
                 }
            }
            if (lab!=label[i]){
                changeNum++;
                label[i]=lab;
            }
        }
        return changeNum;
    }

    void updateMeans(){
        double num[]=new double[n_clusters];
        double sum[][]=new double[n_clusters][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                sum[label[i]][j]+=data[i][j];
            }
            num[label[i]]++;
        }
        for (int i = 0; i < n_clusters; i++) {
            for (int j = 0; j < w; j++) {
                means[i][j]=sum[i][j]/num[i];
            }
        }
    }

    public void fit_predict(String method){
        init();
        while(threshold*h*w<updateLabel(method)){
            updateMeans();
        }
    }

    public static void main(String[] args) {
        Kmeans kmeans=new Kmeans(3,0.01);
        kmeans.load("data.txt");
        kmeans.fit_predict("Euclidean");
        kmeans.store("result.txt");
    }
}
