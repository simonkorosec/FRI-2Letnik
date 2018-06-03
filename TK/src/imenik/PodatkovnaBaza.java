package imenik;

import java.io.*;

class PodatkovnaBaza
{
    public static void main(String[] args)
    {
        SeznamiUV seznamiUV = new SeznamiUV();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String output;
        
        seznamiUV.addImpl("pv", new PrioritetnaVrsta<>(new PrijateljPrimerjajPoImenu()), new PrioritetnaVrsta<>(new PrijateljPrimerjajPoTelSt()));
        seznamiUV.addImpl("sk", new Sklad<>(new PrijateljPrimerjajPoImenu()), new Sklad<>(new PrijateljPrimerjajPoTelSt()));
        seznamiUV.addImpl("bst", new Bst<>(new PrijateljPrimerjajPoImenu()), new Bst<>(new PrijateljPrimerjajPoTelSt()));

        try
        {
            do
            {
                System.out.print("Enter command: ");
                input = br.readLine();
                output = seznamiUV.processInput(input);
                System.out.println(output);
            }
            while (!input.equalsIgnoreCase("exit"));
        }
        catch (IOException e)
        {
            System.err.println("Failed to retrieve the next command.");
            System.exit(1);
        }
    }
}
